package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Game.Network.Client.SocketClientManager;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;

import java.net.InetAddress;

public class GameController {

    private static GameController instance = new GameController();
    private GameController(){}
    public static GameController getInstance() {
        return instance;
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

        return this;
    }


    public GameController startNewGame() {
        SocketHostManager.getInstance().startP2pServerThread();
        return this;
    }

    public GameController joinNetworkGame(InetAddress serverAddress) {

        SocketClientManager socketCManager = new SocketClientManager(serverAddress);
        socketCManager.startClient();

        return this;
    }

}
