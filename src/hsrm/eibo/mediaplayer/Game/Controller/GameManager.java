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
import hsrm.eibo.mediaplayer.Game.View.HostGamePane;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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

    private static GameManager instance = new GameManager();
    private Stage mainGameWindow, hostWindow;
    private SocketClientManager socketClientManager;
    private P2pClientThread clientThread;
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


//        GameWindow.getInstance().start();
//        MyMusician client = MyMusician.getInstance();
//        client.setId(gameSettings.getPlayerName());
//        client.setInstrumentBankId(2); // TODO:
//        Band b = Band.getInstance();
//        b.addBandMember(client);
//        SynthesizerManager sm = SynthesizerManager.getInstance();
//        sm.occupyChannel(client.getId());



        this.initGameWindow();

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
        mainGameWindow.setTitle(GAME_WINDOW_TITLE);
        mainGameWindow.setScene(scene);
        mainGameWindow.setOnCloseRequest(this::handleGameCloseRequest);

        initKeyboardListener(scene);

        mainGameWindow.show();
    }

    private void createHostWindow() {
        hostWindow = new Stage(StageStyle.UNDECORATED);
        hostWindow.initModality(Modality.NONE);
        hostWindow.initOwner(mainGameWindow);
        Scene scene = new Scene(new HostGamePane());
        hostWindow.setScene(scene);

        hostWindow.show();
    }

    private void initKeyboardListener(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                int pitch;
                if((pitch = Keyboard.getNotePitchFromKeyevent(event)) != Keyboard.KEY_NOT_ASSIGNED && !pressedKeys.contains(pitch)) {
                    NetworkEventPacket p = new NetworkEventPacket(gameSettings.getPlayerName(),
                            NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_PLAY_NOTE,
                            new int[]{pitch});
                    clientThread.pushToProcessingQueue(p);
                    pressedKeys.add(pitch);
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                int pitch;
                if((pitch = Keyboard.getNotePitchFromKeyevent(event)) != Keyboard.KEY_NOT_ASSIGNED) {
                    NetworkEventPacket p = new NetworkEventPacket(gameSettings.getPlayerName(),
                            NetworkEventDispatcher.NetworkEventType.EVENT_CLIENT_STOP_NOTE,
                            new int[]{pitch});
                    clientThread.pushToProcessingQueue(p);
                    pressedKeys.remove(pitch);
                }
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
        if(SocketHostManager.getInstance().isRunning())
            SocketHostManager.getInstance().close();
        socketClientManager.close();
    }

}
