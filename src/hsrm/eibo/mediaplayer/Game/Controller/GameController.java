package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Network.Client.SocketClientManager;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameController {

    /**
     * The Game title displayed in game main window.
     */
    public static final String GAME_WINDOW_TITLE = "DevelopBand";

    /**
     * Entered game settings of the current game.
     */
    private static GameSettings gameSettings = null;

    private static GameController instance = new GameController();
    private Stage mainGameWindow;
    private SocketClientManager socketClientManager;

    private GameController(){}
    public static GameController getInstance() {
        return instance;
    }

    public static void setGameSettings(GameSettings gameSettings) {
        GameController.gameSettings = gameSettings;
    }

    public static GameSettings getGameSettings() {
        return gameSettings;
    }

    /**
     * Sets up all requirements for the game mode extension.
     * @return fluent interface
     */
    public GameController initialize()
    {
        /* Use this method to set up all requirements for the game mode.
         * Examples are:
         * - Initializing keyboard listener
         * - Drawing game interface
         * - Initializing network service
         * - etc...
        */

        if(gameSettings == null)
            throw new RuntimeException("No game settings were set. GameSettings has to be set for successful GameController initialization.");

        this.initGameWindow();

        return this;
    }


    /**
     * Handles all functionality for hosting a new network game.
     * @return fluent interface
     */
    public GameController hostNewGame() {
        SocketHostManager.getInstance().startP2pServerThread();

        try {
            socketClientManager = SocketClientManager.getInstance(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            ErrorHandler.getInstance().addError(e);
            ErrorHandler.getInstance().notifyErrorObserver("Fehler beim Hosten des Spiels");
        }
        socketClientManager.startClient();
        return this;
    }

    /**
     * Handles all functionality for joining a network game.
     * @param serverAddress the InetAdress of the server located in the network.
     * @return fluent interface
     */
    public GameController joinNetworkGame(InetAddress serverAddress) {

        socketClientManager = SocketClientManager.getInstance(serverAddress);
        socketClientManager.startClient();

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
        Scene scene = new Scene(new Group(), 200,200);
        mainGameWindow.setTitle(GAME_WINDOW_TITLE);
        mainGameWindow.setScene(scene);
        mainGameWindow.setOnCloseRequest(this::handleGameCloseRequest);


        mainGameWindow.show();
    }

    /**
     * Calls all functions that should be run on close of game.
     * This method is called by Stage.setOnCloseRequest() in {@link #initGameWindow()}
     * @param windowEvent necessary parameter to act as an EventHandler<WindowEvent>
     */
    private void handleGameCloseRequest(WindowEvent windowEvent)
    {
        if(SocketHostManager.getInstance().isRunning())
            SocketHostManager.getInstance().close();
        socketClientManager.close();
    }

}
