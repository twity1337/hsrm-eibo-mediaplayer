package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Controller.PlaylistManager;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.MediaListElementInterface;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.Util.TreeViewTranslator;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main view for mp3 player
 */
public class MainBorderPane extends BorderPane {

    /**
     * Resource path for button images and icons.
     */
    private static final String ICON_RESOURCE_PATH = "/hsrm/eibo/mediaplayer/Resources/Icons/";
    /**
     * A Boolean which indicates, if the application runs in debug mode.
     * In Debug mode additional MenuItems (for loading default m3u playlists, reloading css, ...) will be displayed.
     */
    private boolean debugModeEnabled = false;

    /**
     * Singleton instance of MediaController
     */
    private final MediaController controller = MediaController.getInstance();
    /**
     * Singleton instance of ViewBuilder
     */
    private final ViewBuilder viewBuilder;


    /**
     * Default constructor
     * @param vb the ViewBuilder instance
     * @param debugModeEnabled a boolean indicating if the application runs in debug mode
     */
    public MainBorderPane(ViewBuilder vb, boolean debugModeEnabled) {
        this.viewBuilder = vb;
        this.debugModeEnabled = debugModeEnabled;
        this.setTop(getMenuItems());
        this.setBottom(getBottomComponents());
        this.setCenter(getCenterComponents());

        this.getStyleClass().add("main-border-pane");
    }

    /**
     * The generator for Menu Items in top area.
     * @return javafx.scene.Parent A VBox containing all MenuItem elements.
     */
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
        openMenuItem.setOnAction(event -> {
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

        });

        MenuItem openPlaylistItem = new MenuItem("Playlist öffnen...");
        openPlaylistItem.setOnAction(event -> {
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
        });

        MenuItem exitMenuItem = new MenuItem("Beenden");
        exitMenuItem.setOnAction(event -> ths.viewBuilder.handleShutdown());

        MenuItem[] fileSubMenu = {
                openMenuItem,
                openPlaylistItem,
                new SeparatorMenuItem(),
                exitMenuItem
        };

        menus[0].getItems().addAll(fileSubMenu);

        MenuItem clearPlaylistItem = new MenuItem("Playlist leeren");
        clearPlaylistItem.setOnAction(event -> {
            PlaylistManager.getInstance().clear();
        });
        menus[2].getItems().addAll(clearPlaylistItem);

        if(debugModeEnabled) {
            applyOptionalDebugItems(menus[0]);
        }

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(menuBar);

        return topVBox;
    }

    /**
     * Adds additional MenuItems to the given menu, if debug mode is enabled.
     * @param root the Menu item to extend.
     */
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

    /**
     * Creates the elements for center area.
     * @return A parent object containing the center elements.
     */
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

    /**
     * Creates the elements for bottom area.
     * @return A Parent object containing the bottom elements.
     */
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

    /**
     * Creates an non visible spacer element with a given width.
     * @param width the width in pixel
     * @return Region the spacer region.
     */
    private Region createSpacerRegion(int width) {
        Region spacer = new Region();
        spacer.setPrefWidth(width);
        return spacer;

    }

    /**
     * Creates the metadata label elements for bottom area of view.
     * @return A Pane object containing the elements.
     */
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

    /**
     * Creates the Play/Pause button and binds all necessary listeners to it.
     * @return A Button representing the Play/Pause button.
     */
    private Button createPlayPauseButton()
    {
        Button b = new Button("play");
        applyIconToLabeledElement(b, "play"); //initial setting

        // Change button content on different playing states.
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
        // toggle "isPlaying" on click
        b.setOnAction(event -> {
            if(controller.isPlaying())
                controller.pause();
            else
                controller.play();
        });
        return b;
    }

    /**
     * Creates the "skip to previous" button and adds all bindings.
     * @return An Button representing the Previous-Button.
     */
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

    /**
     * Creates the "skip to next" button and adds all bindings.
     * @return An Button representing the Previous-Button.
     */
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

    /**
     * Creates a button with mute/unmkute functionality.
     * @return Button the volume button
     */
    private Button createVolumeButton()
    {
        Button b = new Button("volume: ");
        applyIconToLabeledElement(b, "speaker"); //initial setting
        // Change button content on change of mute property.
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

    /**
     * Creates a Slider for volume in- and decreasing.
     * @return the volume slider
     */
    private Slider createVolumeSlider()
    {
        Slider s = new Slider(0, 1, 0.5);
        controller.volumeProperty().bindBidirectional(s.valueProperty());
        s.setMinWidth(40);
        return s;
    }

    /**
     * Creates the Time label with automatic reload.
     * @return An Label element representing the current track time.
     */
    private Label createTimeLabel()
    {
        Label l = new Label("--:--");

        // reset time label on change of track / track duration...
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

    /**
     * Creates the Progress slider and binds it to the current track time.
     * @return The Slider element.
     */
    private Slider createProgressSlider()
    {
        Slider s = new Slider(0,0.1,0);
        s.setDisable(true);
        // set max Value of Slider on change of current Track
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

    /**
     * Creates a shuffle button with Toggle-Functionality.
     * @return A ToggleButton with Shuffle-Functionality
     */
    private ToggleButton createShuffleButton()
    {
        ToggleButton b = new ToggleButton("zufällige Wiedergabe aus");
        applyIconToLabeledElement(b, "unshuffle");
        // Change Shuffle button content on change of shuffle property ...
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
        // Toggle shuffle property on click of button
        b.setOnMouseClicked(event -> controller.setInShuffleMode(!controller.isInShuffleMode()));
        return b;
    }

    /**
     * Creates a Repeat button with Toggle-Functionality
     * ToggleButton with Triple-Toggle (None, Single, All)
     * @return A ToggleButton with Repeat-Functionality
     */
    private ToggleButton createRepeatButton()
    {
        ToggleButton b = new ToggleButton("Keine Wiederholung");
        applyIconToLabeledElement(b, "repeat-none");

        // Change button content on change of repeat mode
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

        // Toggle repeat mode on click on button
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

    /**
     * Creates the tab content for current playback.
     * @return A Parent object containing all current playback elements.
     */
    private Parent getTabContent_CurrentPlayback()
    {
        BorderPane coverBorderPane = new BorderPane();
        ImageView imageview = new ImageView();
        imageview.imageProperty().bind(controller.coverProperty());

        coverBorderPane.getStyleClass().add("inner-spacing");
        coverBorderPane.setCenter(imageview);
        return coverBorderPane;
    }

    /**
     * Creates the Playlist-tab content.
     * @return A Parent object containing containing all elements for Playlist view.
     */
    private Parent getTabContent_Playlist()
    {
        PlaylistTreeView tree = new PlaylistTreeView();

        // Redraw TreeView on change of playlists...
        PlaylistManager.addOnChangeObserver(playlistManager -> {
            // onChange of Playlist

            Playlist addedPlaylist = playlistManager.getLastAddedPlaylist();
            final TreeItem<MediaListElementInterface> playlistTreeItem = new TreeItem<>(addedPlaylist);
            playlistTreeItem.setExpanded(true);

            addedPlaylist.forEach(track -> {
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
            });
            // add playlist to TreeView
            tree.getRoot().getChildren().add(playlistTreeItem);
        });

        // Change current media on double click on TreeView item
        tree.setOnMouseClicked(event -> {
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
        });

        // Update current TreeView selection on new track
        controller.currentPlaybackIndexProperty().addListener(((observable, oldValue, newValue) ->
            tree.getSelectionModel().select(
                PlaylistManager.getInstance().getAbsoluteIndexForAllPlaylists(
                        controller.getPlaylist(), controller.getCurrentPlaybackIndex()
                )
            )
        ));
        VBox vbox = new VBox();
        VBox.setVgrow(tree, Priority.ALWAYS);
        vbox.getStyleClass().add("inner-spacing");
        vbox.getChildren().addAll(tree);
        return vbox;
    }

    /**
     * Adds an Icon/Image to a given Labeled element. Lookup path is defined in ICON_RESOURCE_PATH.
     * @param element The labeled element to extend
     * @param name The file name key for icon file.
     */
    private void applyIconToLabeledElement(Labeled element, String name) {
        ImageView image = new ImageView(this.getClass().getResource(ICON_RESOURCE_PATH + name + ".png").toString());
        image.setFitHeight(32);
        image.setFitWidth(32);
        element.setGraphic(image);
        element.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Returns a human-readable time string for display in labeled elements.
     * @param timeInSeconds the time in seconds to format.
     * @return the formatted Time String
     */
    private String parseToTimeString(double timeInSeconds)
    {
        if(timeInSeconds < (60 * 60) )
            return String.format("%01d:%02d", (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));

        return String.format("%01d:%02d:%02d", (int)timeInSeconds/(60*60), (int)timeInSeconds/60, (int)Math.round(timeInSeconds%60));
    }
}
