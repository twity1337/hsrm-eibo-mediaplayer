package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainBorderPane extends BorderPane {

    private final MediaController controller = MediaController.getInstance();

    private MenuBar menuBar = new MenuBar();
    private Label test_playlistPath = new Label("Playlist");
    private Button playPauseButton = new Button(">");
    private Label currentTime = new Label("--:--");

    public MainBorderPane() {
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
                        new FileChooser.ExtensionFilter("Alle Mediendateien", "*.mp3","*.mp4", "*.wav", "*.mkv"),
                        new FileChooser.ExtensionFilter("Audiodateien", "*.mp3", "*.wav"),
                        new FileChooser.ExtensionFilter("Videodateien", "*.mp4","*.mkv")
                );
                fileChooser.setTitle("Medium öffnen ...");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                List<File> choosedFiles = fileChooser.showOpenMultipleDialog(null);
                if(choosedFiles != null)
                {
                    controller.setPlaylist(new Playlist(new String[]{choosedFiles.get(0).getPath()}));
                    resetMediaControls();
                    ths.test_playlistPath.setText("Track: " + choosedFiles.toString());
                    System.out.println(choosedFiles.toString());
                }

            }
        });

        MenuItem openPlaylistItem = new MenuItem("Playlist öffnen...");
        openPlaylistItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Unterstützte Playlisten", "*.m3u")
                );
                fileChooser.setTitle("Playlist öffnen ...");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                File choosedFile = fileChooser.showOpenDialog(null);
                if(choosedFile != null)
                {

                    try {
                        controller.setPlaylist(new Playlist(choosedFile.getPath(), MediaUtil.parseM3u(choosedFile)));
                    }catch (IOException e)
                    {
                        // TODO: Handle error
                        System.out.println("ERROR: '" + choosedFile + "': " + e.getLocalizedMessage());
                        return;
                    }
                    ths.test_playlistPath.setText("Playlist: " + choosedFile.toString());
                    System.out.println(choosedFile.toString());
                }
            }
        });

        MenuItem[] fileSubMenu = {
                openMenuItem,
                openPlaylistItem,
                new SeparatorMenuItem(),
                new MenuItem("Beenden")
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

    private Parent getBottomComponents()
    {
        HBox mediaControls = new HBox();
        controller.getCurrentMediaplayer().setOnEndOfMedia(this::resetMediaControls);
        playPauseButton.setOnAction(event -> {
            if(controller.isPlaying())
            {
                playPauseButton.setText(">");
            }else
            {
                playPauseButton.setText("| |");
            }
            controller.togglePlayPause();
        });


        double mediaStopTime = controller.getCurrentMediaplayer().getStopTime().toSeconds();
        Slider progressSlider = new Slider(0,mediaStopTime, 0);


        // TODO: Fix known bug:
        // Property listener are only created on the currentMediaplayer. If user changes the current Track or Playlist,
        // listeners won't be active anymore... (no display of current time, progress slider, ...)
        // -> register Listeners to MediaController and re-Set them on each MediaPlayer?

        controller.getCurrentMediaplayer().currentTimeProperty().addListener((observable, oldValue, newValue) ->{
            progressSlider.setValue(newValue.toSeconds());

            currentTime.setText(
                    parseToTimeString(newValue.toSeconds()) + " / " +
                    parseToTimeString(mediaStopTime)
            );
        });
        progressSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue.toString());
            }
        });


        mediaControls.getChildren().addAll(playPauseButton, currentTime, progressSlider);
        return mediaControls;
    }

    private void resetMediaControls()
    {
        this.playPauseButton.setText(">");
        this.currentTime.setText("--:--");
    }

    private Parent getTabContent_CurrentPlayback()
    {
        BorderPane coverBorderPane = new BorderPane();
        Button prevButton = new Button("<");
        prevButton.setMinHeight(100);
        Rectangle placeh = new Rectangle(100, 100);
        Button nextButton = new Button(">");
        nextButton.setMinHeight(100);
        nextButton.setOnAction(event -> controller.skipToNext());

        coverBorderPane.setLeft(prevButton);
        coverBorderPane.setCenter(placeh);
        coverBorderPane.setRight(nextButton);
        BorderPane.setAlignment(prevButton, Pos.CENTER);
        BorderPane.setAlignment(nextButton, Pos.CENTER);
        return coverBorderPane;
    }

    private Parent getTabContent_Playlist()
    {
        return this.test_playlistPath;
    }


    private String parseToTimeString(double timeInSeconds)
    {
        if(timeInSeconds < (60 * 60) )
            return String.format("%01d:%02d", (int)timeInSeconds/60, (int)timeInSeconds%60);

        return String.format("%01d:%02d:%02d", (int)timeInSeconds/(60*60), (int)timeInSeconds/60, (int)timeInSeconds%60);
    }
}
