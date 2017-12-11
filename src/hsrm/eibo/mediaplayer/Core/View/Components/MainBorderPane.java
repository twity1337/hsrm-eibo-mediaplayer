package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.FileIOUtil;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.PlaylistManager;
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
import javafx.stage.FileChooser;
import sun.misc.resources.Messages_es;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.util.List;

public class MainBorderPane extends BorderPane {

    private final MediaController controller = MediaController.getInstance();
    private final ViewBuilder viewBuilder;


    private MenuBar menuBar = new MenuBar();
    private Label test_playlistPath = new Label("Playlist");
    private Button playPauseButton = new Button(">");
    private Label currentTime = new Label("--:--");
    private Slider progressSlider = new Slider(0, 0, 0);

    public MainBorderPane(ViewBuilder vb) {
        this.viewBuilder = vb;
        this.setTop(getTopComponents());
        this.setCenter(getCenterComponents());
        this.setBottom(getBottomComponents());
    }

    private Parent getTopComponents() {
        VBox topVBox = new VBox();
        MainBorderPane ths = this;

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
                        controller.setPlaylist(new Playlist(FileIOUtil.extractFilePaths(chosenFiles.toArray(new File[chosenFiles.size()]))));
                    }catch (PlaylistIOException e)
                    {
                        // TODO: Make that better... (error modal window..)
                        System.err.println("ERROR: Error loading files: " + e.getLocalizedMessage());
                    }
                    resetMediaControls();
                    ths.test_playlistPath.setText("Track: " + chosenFiles.toString());
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

                    try {
                        PlaylistManager.getInstance().loadPlaylistFromFile(chosenFile);
                        PlaylistManager.getInstance().isLoadingListProperty().addListener((observable, oldValue, newValue) -> {
                            Playlist playlistToAdd;
                            if (oldValue && !newValue &&
                                (playlistToAdd=PlaylistManager.getInstance().getLastAddedPlaylist())!=null)
                            {
                                controller.setPlaylist(playlistToAdd);
                            }
                        });
/*
                        controller.setPlaylist(PlaylistManager.getInstance().getLastAddedPlaylist());
*/
                    }catch (PlaylistIOException e)
                    {
                        // TODO: Handle error
                        System.out.println("ERROR: '" + chosenFile + "': " + e.getLocalizedMessage());
                        return;
                    }
                    ths.test_playlistPath.setText("Playlist: " + chosenFile.toString());
                    System.out.println(chosenFile.toString());
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

        this.menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(this.menuBar);

        return topVBox;
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

        progressSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //TODO: hacky method to determine if change occur "naturly" by passing of time or user interaction
                int delta = (int)(newValue.doubleValue()-oldValue.doubleValue());
                if (delta!=0)
                {
                    controller.seek(newValue.doubleValue());
                }

            }
        });
        controller.currentTimeProperty().addListener((observable, oldValue, newValue) ->{
            double timeInSeconds = newValue.doubleValue();
            this.progressSlider.adjustValue(timeInSeconds);

            currentTime.setText(
                    parseToTimeString(this.progressSlider.getValue()) + " / " +
                    parseToTimeString(this.progressSlider.getMax())
            );
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
        PlaylistManager plManager = PlaylistManager.getInstance();
        ListView<Playlist> list = new ListView<>();
        list.getItems().addAll(plManager.toArray(new Playlist[plManager.size()]));

        // TODO: Delete THIS!!
        //try{list.getItems().add(new Playlist(System.getProperty("user.dir") + "/media/test.mp3"));}catch (Exception e){throw new RuntimeException(e.getMessage());}
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Playlist playlist = (Playlist) ((ListView) event.getSource()).getSelectionModel().getSelectedItem();
                if(playlist == null) // no selected item
                    return;
                controller.setCurrentMediaplayer(playlist.get(0));
                ((ListView) event.getSource()).getItems().addAll(plManager.toArray(new Playlist[plManager.size()]));
                controller.play();
            }
        });


        VBox vbox = new VBox();
        vbox.getChildren().addAll(this.test_playlistPath, list);
        return vbox;
    }

    private String parseToTimeString(double timeInSeconds)
    {
        if(timeInSeconds < (60 * 60) )
            return String.format("%01d:%02d", (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));

        return String.format("%01d:%02d:%02d", (int)timeInSeconds/(60*60), (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));
    }
}
