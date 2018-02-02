package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Network.Client.SocketClientManager;
import hsrm.eibo.mediaplayer.Game.Network.Client.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.Keyboard;
import hsrm.eibo.mediaplayer.Game.View.HostGamePane;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class GameManager {

    /**
     * The Game title displayed in game main window.
     */
    public static final String GAME_WINDOW_TITLE = "DevelopBand";

    /**
     * Entered game settings of the current game.
     */
    private static GameSettings gameSettings = null;

    /**
     * Singleton instance
     */
    private static GameManager instance = new GameManager();

    /**
     * Main window stages
     */
    private Stage mainGameWindow, hostWindow;

    /**
     * Socket client manager for client and host.
     */
    private SocketClientManager socketClientManager;

    /**
     * ClientThread for client and host instances.
     */
    private P2pClientThread clientThread;
    /**
     * Background playback Media
     */
    private Track playbackMedia = null;

    /**
     * Mouse offset for movable windows.
     */
    private static double yOffset, xOffset;

    private GameManager(){}
    private Set<Integer> pressedKeys = new HashSet<>();
    public static GameManager getInstance() {
        return instance;
    }

    public static void setGameSettings(GameSettings gameSettings) {
        GameManager.gameSettings = gameSettings;
    }

    public static GameSettings getGameSettings() {
        return gameSettings;
    }

    /**
     * Sets up all requirements for the game mode extension.
     * @return fluent interface
     */
    public GameManager initialize()
    {
        /* Use this method to set up all requirements for the game mode.
         * Examples are:
         * - Initializing keyboard listener
         * - Drawing game interface
         * - Initializing network service
         * - etc...
        */

        if(gameSettings == null)
            throw new RuntimeException("No game settings were set. GameSettings has to be set for successful GameManager initialization.");

        this.initGameWindow();
        this.initBackgroundSong();

        return this;
    }

    /**
     * Initializes playback of the background song, if one is set.
     */
    private void initBackgroundSong() {
        String backgroundSongPath = gameSettings.getBackgroundSongPath();
        if (backgroundSongPath == null)
            return;

        try {
            playbackMedia = new Track(backgroundSongPath);
        } catch (Exception e) {
            ErrorHandler err = ErrorHandler.getInstance();
            err.addError(e);
            err.notifyErrorObserver("Fehler bei der Initialisierung des Spiels");
        }
    }


    /**
     * Handles all functionality for hosting a new network game.
     * @return fluent interface
     */
    public GameManager hostNewGame() {
        SocketHostManager.getInstance().startP2pServerThread();

        try {
            this.joinNetworkGame(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            ErrorHandler err = ErrorHandler.getInstance();
            err.addError(e);
            err.notifyErrorObserver("Fehler beim Hosten des Spiels");
        }

        this.createHostWindow();
        return this;
    }

    /**
     * Handles all functionality for joining a network game.
     * @param serverAddress the InetAdress of the server located in the network.
     * @return fluent interface
     */
    public GameManager joinNetworkGame(InetAddress serverAddress) {

        socketClientManager = SocketClientManager.getInstance(serverAddress);
        socketClientManager.startClient();
        clientThread = this.socketClientManager.getClientThread();
        return this;
    }

    /**
     * Initializes the content of the main game window.
     */
    private void initGameWindow()
    {
        mainGameWindow = new Stage(StageStyle.UTILITY);
        mainGameWindow.initModality(Modality.WINDOW_MODAL);
        mainGameWindow.initOwner(ViewBuilder.getInstance().getPrimaryStage());
        Scene scene = new Scene(Keyboard.createKeyboardPane(), Keyboard.getKeyboardWidth()+20,Keyboard.getKeyboardHeight()+20);
        initKeyboardListener(scene);

        mainGameWindow.setTitle(GAME_WINDOW_TITLE);
        mainGameWindow.setResizable(false);
        mainGameWindow.setScene(scene);
        mainGameWindow.setOnCloseRequest(this::handleGameCloseRequest);


        mainGameWindow.show();
    }

    /**
     * Create a tool window on host side, which displays all connected players with different colors.
     */
    private void createHostWindow() {
        hostWindow = new Stage(StageStyle.UNDECORATED);
        hostWindow.initModality(Modality.NONE);
        hostWindow.initOwner(mainGameWindow);
        hostWindow.centerOnScreen();
        hostWindow.setX(HostGamePane.WINDOW_X_POSITION);
        hostWindow.setHeight(HostGamePane.WINDOW_HEIGHT);
        hostWindow.setWidth(HostGamePane.WINDOW_WIDTH);
        Scene scene = new Scene(new HostGamePane(this.playbackMedia));
        hostWindow.setScene(scene);
        hostWindow.setOnCloseRequest(this::handleGameCloseRequest);

        // make window dragable/movable...
        scene.setOnMousePressed(event -> {
            xOffset = hostWindow.getX() - event.getScreenX();
            yOffset = hostWindow.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            hostWindow.setX(event.getScreenX() + xOffset);
            hostWindow.setY(event.getScreenY() + yOffset);
        });

        hostWindow.show();
        Platform.runLater(mainGameWindow::requestFocus);
    }

    private void initKeyboardListener(Scene scene) {
        scene.setOnKeyPressed(event -> {
            int pitch;
            if((pitch = Keyboard.getNotePitchFromKeyevent(event)) != Keyboard.KEY_NOT_ASSIGNED && !pressedKeys.contains(pitch)) {
                NetworkEventPacket p = new NetworkEventPacket(gameSettings.getPlayerName(),
                        NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_PLAY_NOTE,
                        new int[]{pitch});
                clientThread.pushToProcessingQueue(p);
                pressedKeys.add(pitch);
            }
        });

        scene.setOnKeyReleased(event -> {
            int pitch;
            if((pitch = Keyboard.getNotePitchFromKeyevent(event)) != Keyboard.KEY_NOT_ASSIGNED) {
                NetworkEventPacket p = new NetworkEventPacket(gameSettings.getPlayerName(),
                        NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_STOP_NOTE,
                        new int[]{pitch});
                clientThread.pushToProcessingQueue(p);
                pressedKeys.remove(pitch);
            }
        });
    }

    /**
     * Calls all functions that should be run on close of game.
     * This method is called by Stage.setOnCloseRequest() in {@link #initGameWindow()}
     * @param windowEvent necessary parameter to act as an EventHandler<WindowEvent>
     */
    private void handleGameCloseRequest(WindowEvent windowEvent)
    {
        if(hostWindow != null)
            ((HostGamePane)hostWindow.getScene().getRoot()).stopPlayback();
        if(SocketHostManager.getInstance().isRunning())
            SocketHostManager.getInstance().close();
        socketClientManager.close();
    }

}
