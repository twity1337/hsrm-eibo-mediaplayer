package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.MediaListElementInterface;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.Util.TreeViewTranslator;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManager;
import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManagerObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainBorderPane extends BorderPane {

    private static final String ICON_RESOURCE_PATH = "/hsrm/eibo/mediaplayer/Resources/Icons/";
    private boolean debugModeEnabled = false;

    private final MediaController controller = MediaController.getInstance();
    private final ViewBuilder viewBuilder;

    private MenuBar menuBar = new MenuBar();

    public MainBorderPane(ViewBuilder vb, boolean debugModeEnabled) {
        this.viewBuilder = vb;
        this.debugModeEnabled = debugModeEnabled;
        this.setTop(getMenuItems());
        this.setBottom(getBottomComponents());
        this.setCenter(getCenterComponents());

        this.getStyleClass().add("main-border-pane");
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
                new Menu("?")
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

        if(debugModeEnabled) {
            applyOptionalDebugItems(menus[0]);
        }

        this.menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(this.menuBar);

        return topVBox;
    }

    private void applyOptionalDebugItems(Menu root) {
        if(!debugModeEnabled)
            return;

        MenuItem[] items = {
                new SeparatorMenuItem(),
                new MenuItem("Load Playlist from media/_DEBUG.m3u..."),
                new MenuItem("Reload CSS..."),
                new MenuItem("Show Error-Dialog..."),
        };

        items[1].setOnAction(event -> {
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
        items[2].setOnAction(event -> {
            this.applyCss();
            viewBuilder.getPrimaryStage().getScene().getStylesheets().clear();
            viewBuilder.getPrimaryStage().getScene().getStylesheets().add(this.getClass().getResource(ViewBuilder.STYLESHEET_MAIN_PATH).toString());
        });
        items[3].setOnAction(event -> {
            viewBuilder.showErrorDialog("Es ist ein Test-Fehler aufgetreten!");
            viewBuilder.showErrorDialog("Es ist ein Test-Fehler aufgetreten!", "Ich bin ein Test...");
        });
        root.getItems().addAll(items);
    }

    private Parent getCenterComponents() {

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tabpane");

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

    private Parent getBottomComponents()
    {
        VBox controllBox = new VBox();
        HBox bottomBox = new HBox();
        HBox topBox = new HBox();

        topBox.setAlignment(Pos.CENTER);
        controllBox.getStyleClass().addAll("media-control-elements", "inner-spacing");

        Slider progressSlider = this.createProgressSlider();

        bottomBox.setHgrow(progressSlider, Priority.ALWAYS);
        topBox.setSpacing(5);
        topBox.getChildren().addAll(
                this.createPreviousTrackButton(),
                this.createPlayPauseButton(),
                this.createNextTrackButton(),
                this.createSpacerRegion(10),
                this.createShuffleButton(),
                this.createRepeatButton(),
                this.createSpacerRegion(10),
                this.createVolumeButton(),
                this.createVolumeSlider()
        );
        bottomBox.getChildren().addAll(
                this.createTimeLabel(),
                progressSlider
        );

        controllBox.getChildren().addAll(this.createMetadataLabels(), topBox, bottomBox);

        return controllBox;
    }

    private Region createSpacerRegion(int width) {
        Region spacer = new Region();
        spacer.setPrefWidth(width);
        return spacer;

    }

    private Pane createMetadataLabels() {
        HBox box = new HBox();
        ArrayList<Node> l = new ArrayList<>();
        HBox labelGroup0 = new HBox(), labelGroup1 = new HBox();
        Label labelText0 = new Label(), labelText1 = new Label();
        Region spacer = new Region();
        Label labelTitle0 = new Label("Titel:"), labelTitle1 = new Label("Interpret:");
        labelGroup0.getChildren().addAll(labelTitle0, labelText0);
        labelGroup1.getChildren().addAll(labelTitle1, labelText1);

        l.add(labelGroup0);
        l.add(spacer);
        l.add(labelGroup1);

        labelTitle0.setVisible(false);
        labelTitle1.setVisible(false);
        labelTitle0.getStyleClass().addAll("metadata", "metadata-label", "left");
        labelTitle1.getStyleClass().addAll("metadata", "metadata-label", "right");
        labelText0.getStyleClass().addAll("metadata", "metadata-value", "left");
        labelText1.getStyleClass().addAll("metadata", "metadata-value", "right");

        controller.currentTrackMetadataProperty().addListener(((observable, oldValue, newValue) -> {
            Boolean isVisible = controller.getTrackDuration() > 0;
            labelTitle0.setVisible(isVisible);
            labelTitle1.setVisible(isVisible);
            labelText0.setText(newValue.getMetadataMap().get("title"));
            labelText1.setText(newValue.getMetadataMap().get("artist"));
        }));
        HBox.setHgrow(spacer, Priority.ALWAYS); // use Region in l[2]  as spacer.
        box.getStyleClass().add("metadata-row");
        box.getChildren().addAll(l);
        return box;
    }

    private Button createPlayPauseButton()
    {
        Button b = new Button("play");
        applyIconToLabeledElement(b, "play"); //initial setting
        controller.endOfMediaProperty().addListener((observable, oldValue, newValue) -> {
            controller.setCurrentTime(0);
        });
        controller.playingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) // is playing
            {
                b.setText("Wiedergabe pausieren");
                applyIconToLabeledElement(b, "pause");
            }else
            {
                b.setText("Wiedergabe starten");
                applyIconToLabeledElement(b, "play");
            }
        });
        b.setOnAction(event -> {
            if(controller.isPlaying())
                controller.pause();
            else
                controller.play();
        });
        return b;
    }

    private Button createPreviousTrackButton()
    {
        Button b = new Button("Vorheriger Titel");
        b.setId("previous-track-button");
        b.getStyleClass().add("previous-track");
        b.setOnAction(event -> {
            controller.skipToPrevious();
        });
        applyIconToLabeledElement(b, "rewind");
        return b;
    }

    private Button createNextTrackButton()
    {
        Button b = new Button("Nächster Titel");
        b.setId("next-track-button");
        b.getStyleClass().add("next-track");
        b.setOnAction((ActionEvent event) -> {
            controller.skipToNext();
        });
        applyIconToLabeledElement(b, "forward");
        return b;
    }

    private Button createVolumeButton()
    {
        Button b = new Button("volume: ");
        applyIconToLabeledElement(b, "speaker"); //initial setting
        controller.mutedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue)
            {
                b.setText("volume: not muted");
                applyIconToLabeledElement(b, "speaker");
                if (controller.getVolume() < 0.1)
                    controller.setVolume(0.1);
            } else if (!oldValue && newValue)
            {
                b.setText("volume: muted");
                applyIconToLabeledElement(b,"mute");
            }
        });
        //when player is muted and volume is adjusted, unmute; mute if volume is zero
        controller.volumeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != 0) {
                controller.setMuted(false);
            } else {
                controller.setMuted(true);
            }
        });
        // on mouseclick toggle mute on/off
        b.setOnMouseClicked(event -> {
            controller.setMuted(!controller.isMuted());
        });
        return b;
    }

    private Slider createVolumeSlider()
    {
        Slider s = new Slider(0, 1, 0.5);
        controller.volumeProperty().bindBidirectional(s.valueProperty());
        s.setMinWidth(40);
        return s;
    }

    private Label createTimeLabel()
    {
        Label l = new Label("--:--");
        controller.trackDurationProperty().addListener((observable, oldValue, newValue) -> {
            l.setText("--:--"); //reset text on mediaplayer stop
        });
        controller.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            l.setText(parseToTimeString(newValue.doubleValue())
                    + " / "
                    + parseToTimeString(controller.getTrackDuration()));
        });
        l.getStyleClass().add("currenttime");
        return l;
    }

    private Slider createProgressSlider()
    {
        Slider s = new Slider(0,0.1,0);
        s.setDisable(true);
        // set max Value of Slider
        this.controller.trackDurationProperty().addListener((observable, oldValue, newValue) -> {
            s.setMax(newValue.doubleValue());

            // prevent invlid slider initialization, due to invalid max value
            s.setDisable(Double.isNaN(newValue.doubleValue()));
            s.setValue(0);
        });
        // bind slider property to time property of Mediacontroller
        controller.currentTimeProperty().addListener((observable, oldValue, newValue) ->{
            if (!s.isValueChanging())
            {
                s.setValue(newValue.doubleValue());
            } else {
                controller.seek(s.getValue());
            }
        });
        s.setOnMousePressed(event -> {
            s.setValueChanging(true);
            controller.seek(s.getValue());
        });
        s.setOnMouseReleased(event -> {
            s.setValueChanging(false);
        });
        return s;
    }

    private ToggleButton createShuffleButton()
    {
        ToggleButton b = new ToggleButton("zufällige Wiedergabe aus");
        applyIconToLabeledElement(b, "shuffle");
        controller.inShuffleModeProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue)
            {
                b.setText("zufällige Wiedergabe aus");
                b.setSelected(false);
                applyIconToLabeledElement(b, "unshuffle");
            } else if (!oldValue && newValue)
            {
                b.setText("zufällige Wiedergabe ein");
                b.setSelected(true);
                applyIconToLabeledElement(b, "shuffle");
            }
        });
        b.setOnMouseClicked(event -> {
            controller.setInShuffleMode(!controller.isInShuffleMode());
        });
        return b;
    }

    private ToggleButton createRepeatButton()
    {
        ToggleButton b = new ToggleButton("Keine Wiederholung");
        applyIconToLabeledElement(b, "repeat-none");
        controller.repeatModeProperty().addListener((observable, oldValue,  newValue) -> {
            if (newValue == MediaController.RepeatMode.NONE)
            {
                applyIconToLabeledElement(b, "repeat-none");
                b.setSelected(false);
                b.setText("keine Wiederholung");
            } else if(newValue == MediaController.RepeatMode.ALL)
            {
                applyIconToLabeledElement(b, "repeat-all");
                b.setSelected(true);
                b.setText("playlist wiederholen");
            } else if (newValue == MediaController.RepeatMode.SINGLE)
            {
                applyIconToLabeledElement(b, "repeat-single");
                b.setSelected(true);
                b.setText("track wiederholen");
            }
        });
        b.setOnMouseClicked(event -> {
            if (controller.getRepeatMode() == MediaController.RepeatMode.NONE)
            {
                controller.setRepeatMode(MediaController.RepeatMode.ALL);
            } else if (controller.getRepeatMode() == MediaController.RepeatMode.ALL)
            {
                controller.setRepeatMode(MediaController.RepeatMode.SINGLE);
            } else
            {
                controller.setRepeatMode(MediaController.RepeatMode.NONE);
            }
        });
        return b;
    }

    private Parent getTabContent_CurrentPlayback()
    {
        BorderPane coverBorderPane = new BorderPane();
        ImageView imageview = new ImageView();
        imageview.imageProperty().bind(controller.coverProperty());

        coverBorderPane.getStyleClass().add("inner-spacing");
        coverBorderPane.setCenter(imageview);
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
                if(event.getClickCount() != 2 || tree.getSelectionModel().isEmpty())
                    return;

                MediaListElementInterface selectedElement = tree.getSelectionModel().getSelectedItem().getValue();
                if(selectedElement == null) // no selected item
                    return;
                if (selectedElement instanceof Track)
                {
                    TreeViewTranslator translator = new TreeViewTranslator();
                    translator.setSelectedIndex(tree.getSelectionModel().getSelectedIndex());
                    controller.setPlaylist(translator.lookupPlaylistForTreeIndex());
                    controller.skipToIndex(translator.getRelativePlaylistIndex());

                    controller.play();
                }
            }
        });

        controller.currentPlaybackIndexProperty().addListener(((observable, oldValue, newValue) -> {
            tree.getSelectionModel().select(
                    PlaylistManager.getInstance().getAbsoluteIndexForAllPlaylists(
                            controller.getPlaylist(), newValue.intValue()
                    )
            );
        }));
        VBox vbox = new VBox();
        VBox.setVgrow(tree, Priority.ALWAYS);
        vbox.getStyleClass().add("inner-spacing");
        vbox.getChildren().addAll(tree);
        return vbox;
    }

    private void applyIconToLabeledElement(Labeled element, String name) {
        ImageView image = new ImageView(this.getClass().getResource(ICON_RESOURCE_PATH + name + ".png").toString());
        image.setFitHeight(32);
        image.setFitWidth(32);
        element.setGraphic(image);
        element.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    private String parseToTimeString(double timeInSeconds)
    {
        if(timeInSeconds < (60 * 60) )
            return String.format("%01d:%02d", (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));

        return String.format("%01d:%02d:%02d", (int)timeInSeconds/(60*60), (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));
    }
}
