package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Game.Network.SocketHostManager;
import hsrm.eibo.mediaplayer.Game.Synthesizer.GameWindow;
import hsrm.eibo.mediaplayer.Game.Synthesizer.Keyboard;

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

        Keyboard.init();
        GameWindow.getInstance().start();

        return this;
    }


    public GameController startNewGame() {

        SocketHostManager socketHManager = new SocketHostManager();
        socketHManager.startServerThread();

        return this;
    }

}
