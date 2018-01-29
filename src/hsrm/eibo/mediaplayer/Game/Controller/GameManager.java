package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Network.Client.SocketClientManager;
import hsrm.eibo.mediaplayer.Game.Network.Client.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.Event.NetworkEventDispatcher;
import hsrm.eibo.mediaplayer.Game.Network.General.Model.NetworkEventPacket;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.*;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameManager {

    /**
     * The Game title displayed in game main window.
     */
    public static final String GAME_WINDOW_TITLE = "DevelopBand";

    /**
     * Entered game settings of the current game.
     */
    private static GameSettings gameSettings = null;

    private static GameManager instance = new GameManager();
    private Stage mainGameWindow;
    private SocketClientManager socketClientManager;

    private GameManager(){}
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


//        GameWindow.getInstance().start();
//        MyMusician client = MyMusician.getInstance();
//        client.setId(gameSettings.getPlayerName());
//        client.setInstrumentBankId(2); // TODO:
//        Band b = Band.getInstance();
//        b.addBandMember(client);
//        SynthesizerManager sm = SynthesizerManager.getInstance();
//        sm.occupyChannel(client.getId());



        this.initGameWindow();
        this.initMidiPiano();

        return this;
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
            ErrorHandler.getInstance().addError(e);
            ErrorHandler.getInstance().notifyErrorObserver("Fehler beim Hosten des Spiels");
        }
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


        P2pClientThread clientThread = this.socketClientManager.getClientThread();
        clientThread.pushToProcessingQueue(new NetworkEventPacket(gameSettings.getPlayerName(), NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_NOTE, new int[]{60}));

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
        Scene scene = new Scene(Keyboard.createKeyboardPane(), 200,200);
        mainGameWindow.setTitle(GAME_WINDOW_TITLE);
        mainGameWindow.setScene(scene);
        mainGameWindow.setOnCloseRequest(this::handleGameCloseRequest);


        mainGameWindow.show();
    }

    /**
     * Initializes the content of the Keyboard and synthesizers classes
     */
    private void initMidiPiano() {
        Keyboard.init();
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
