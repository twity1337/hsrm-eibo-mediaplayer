package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.MediaListElementInterface;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManager;
import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManagerObserver;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class MainBorderPane extends BorderPane {

    private static final boolean DEBUG_MODE_ENABLED = true;



    private final MediaController controller = MediaController.getInstance();
    private final ViewBuilder viewBuilder;

    private MenuBar menuBar = new MenuBar();
    private Button playPauseButton = new Button(">");
    private Label currentTime = new Label("--:--");
    private Slider progressSlider = new Slider(0, 0, 0);

    public MainBorderPane(ViewBuilder vb) {
        this.viewBuilder = vb;
        this.setTop(getMenuItems());
        this.setCenter(getCenterComponents());
        this.setBottom(getBottomComponents());
    }

    private Parent getMenuItems() {
        VBox topVBox = new VBox();
        MainBorderPane ths = this;
        PlaylistManager playlistManager = PlaylistManager.getInstance();

        Menu[] menus = {
                new Menu("Datei"),
                new Menu("Wiedergabe"),
                new Menu("Playlist"),
                new Menu("Ansicht"),
                new Menu("?"),
                null
        };

        MenuItem openMenuItem = new MenuItem("Öffnen...");
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Alle Mediendateien",
                                MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO | MediaUtil.FILETYPE_GID_VIDEO)
                        ),
                        new FileChooser.ExtensionFilter("Audiodateien", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO)),
                        new FileChooser.ExtensionFilter("Videodateien", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_VIDEO))
                );
                fileChooser.setTitle("Medium öffnen ...");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                List<File> chosenFiles = fileChooser.showOpenMultipleDialog(null);
                if(chosenFiles != null)
                {
                    try {
                        playlistManager.createPlaylistFromFile(chosenFiles);
                    }catch (PlaylistIOException e)
                    {
                        // TODO: Make that better... (error modal window..)
                        System.err.println("ERROR: Error loading files: " + e.getLocalizedMessage());
                    }
                    resetMediaControls();
                    System.out.println(chosenFiles.toString());
                }

            }
        });

        MenuItem openPlaylistItem = new MenuItem("Playlist öffnen...");
        openPlaylistItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Unterstützte Playlisten", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_PLAYLIST))
                );
                fileChooser.setTitle("Playlist öffnen ...");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                File chosenFile = fileChooser.showOpenDialog(null);
                if(chosenFile != null)
                {
                    playlistManager.createPlaylistFromFile(chosenFile);
                    playlistManager.isLoadingListProperty().addListener((observable, oldValue, newValue) -> {
                        Playlist playlistToAdd;
                        if (oldValue && !newValue &&
                            (playlistToAdd=PlaylistManager.getInstance().getLastAddedPlaylist())!=null)
                        {
                            controller.setPlaylist(playlistToAdd);
                        }
                    });
                }
            }
        });

        MenuItem exitMenuItem = new MenuItem("Beenden");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ths.viewBuilder.handleShutdown();
            }
        });

        MenuItem[] fileSubMenu = {
                openMenuItem,
                openPlaylistItem,
                new SeparatorMenuItem(),
                exitMenuItem
        };

        menus[0].getItems().addAll(fileSubMenu);

        if(DEBUG_MODE_ENABLED) {
            menus[5] = new Menu("__DEBUG");
            applyOptionalDebugItems(menus[5]);
        }

        this.menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(this.menuBar);

        return topVBox;
    }

    private void applyOptionalDebugItems(Menu root) {
        if(!DEBUG_MODE_ENABLED)
            return;

        MenuItem[] items = {
                new MenuItem("Load Playlist from media/_DEBUG.m3u...")
        };

        items[0].setOnAction(event -> {
            File debugPlaylistFile = new File(System.getProperty("user.dir")+"/media/_DEBUG.m3u");
            if(!debugPlaylistFile.canRead())
            {
                System.err.println("ERROR: Error reading debug playlist file: \"" + debugPlaylistFile.getPath() + "\". Is this file present? ");
                return;
            }

            PlaylistManager.getInstance().createPlaylistFromFile(debugPlaylistFile);
            PlaylistManager.getInstance().isLoadingListProperty().addListener((observable, oldValue, newValue) -> {
                Playlist playlistToAdd;
                if (oldValue && !newValue &&
                        (playlistToAdd=PlaylistManager.getInstance().getLastAddedPlaylist())!=null)
                {
                    controller.setPlaylist(playlistToAdd);
                }
            });

        });
        root.getItems().addAll(items);
    }

    private Parent getCenterComponents() {

        TabPane tabPane = new TabPane();

        Tab tab0 = new Tab();
        tab0.setText("Aktuelle Wiedergabe");
        tab0.setContent(getTabContent_CurrentPlayback());
        tab0.setClosable(false);
        Tab tab1 = new Tab();
        tab1.setText("Playlist");
        tab1.setContent(getTabContent_Playlist());
        tab1.setClosable(false);
        tabPane.getTabs().addAll(tab0, tab1);

        return tabPane;
    }

    private Parent getBottomComponents() //TODO: volume controll
    {
        HBox mediaControls = new HBox();
        controller.endOfMediaProperty().addListener((observable, oldValue, newValue) -> {if(newValue) resetMediaControls();});
        controller.playingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) // is playing
            {
                playPauseButton.setText("||");
            }else
            {
                playPauseButton.setText(">");
            }
        });
        playPauseButton.setOnAction(event -> {
            if(controller.isPlaying())
                controller.pause();
            else
                controller.play();
        });
        // set max Value of Slider
        this.controller.trackDurationProperty().addListener((observable, oldValue, newValue) ->
                progressSlider.setMax(newValue.doubleValue()));
       // bind slider property to time property of Mediacontroller

        controller.currentTimeProperty().addListener((observable, oldValue, newValue) ->{
            if (!progressSlider.isValueChanging())
                this.progressSlider.setValue(newValue.doubleValue());

            currentTime.setText(
                    parseToTimeString(this.progressSlider.getValue()) + " / " +
                            parseToTimeString(this.progressSlider.getMax())
            );
        });

        progressSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (progressSlider.isValueChanging())
                {
                    controller.seek(progressSlider.getValue());
                }
            }
        });

        HBox.setHgrow(progressSlider, Priority.ALWAYS);
        mediaControls.getChildren().addAll(playPauseButton, currentTime, progressSlider);
        return mediaControls;
    }

    private void resetMediaControls()
    {
        this.playPauseButton.setText(">");
        this.currentTime.setText("--:--");
        this.progressSlider.setValue(0);
    }

    private Parent getTabContent_CurrentPlayback()
    {
        BorderPane coverBorderPane = new BorderPane();
        Button prevButton = new Button("<<");
        prevButton.setMinHeight(100);
        prevButton.setOnAction(event -> controller.skipToPrevious());
        Button nextButton = new Button(">>");
        nextButton.setMinHeight(100);
        nextButton.setOnAction(event -> controller.skipToNext());

        ImageView imageview = new ImageView();
        imageview.imageProperty().bind(controller.getCoverProperty());

        coverBorderPane.setLeft(prevButton);
        coverBorderPane.setCenter(imageview);
        coverBorderPane.setRight(nextButton);
        BorderPane.setAlignment(prevButton, Pos.CENTER);
        BorderPane.setAlignment(nextButton, Pos.CENTER);
        return coverBorderPane;
    }

    private Parent getTabContent_Playlist()
    {
        PlaylistTreeView tree = new PlaylistTreeView();

        PlaylistManager.addOnChangeObserver(new PlaylistManagerObserver() {
            @Override
            public void update(PlaylistManager playlistManager) {
                // onChange of Playlist

                Playlist addedPlaylist = playlistManager.getLastAddedPlaylist();
                final TreeItem<MediaListElementInterface> playlistTreeItem = new TreeItem<>(addedPlaylist);
                playlistTreeItem.setExpanded(true);

                addedPlaylist.forEach(new Consumer<Track>() {
                    @Override
                    public void accept(Track track) {
                        // forEach Track in Listview
                        track.metadataReadyProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public synchronized void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                // Metadata ready on single track in TreeView.
                                if(newValue)
                                {
                                    tree.redraw();
                                }
                            }
                        });
                        playlistTreeItem.getChildren().add(new TreeItem<>(track));
                    }
                });
                // add playlist to TreeView
                tree.getRoot().getChildren().add(playlistTreeItem);
            }
        });

        tree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() != 2)
                    return;

                MediaListElementInterface selectedElement = ((TreeItem<MediaListElementInterface>)(((TreeView) event.getSource()).getSelectionModel().getSelectedItem())).getValue();
                if(selectedElement == null) // no selected item
                    return;
                if (selectedElement instanceof Track)
                {
                    controller.setCurrentMediaplayer((Track) selectedElement);
                    controller.play();
                }
            }
        });


        VBox vbox = new VBox();
        vbox.getChildren().addAll(tree);
        return vbox;
    }

    private String parseToTimeString(double timeInSeconds)
    {
        if(timeInSeconds < (60 * 60) )
            return String.format("%01d:%02d", (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));

        return String.format("%01d:%02d:%02d", (int)timeInSeconds/(60*60), (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));
    }
}
