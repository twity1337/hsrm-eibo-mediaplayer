package hsrm.eibo.mediaplayer.Game.Controller;

import hsrm.eibo.mediaplayer.Game.Service.KeyboardListenerService;
import javafx.concurrent.Service;

public class GameController {

    public void initialize()
    {
        /* Use this method to set up all requirements for the game mode.
         * Examples are:
         * - Initializing keyboard listener
         * - Drawing game interface
         * - Initializing network service
         * - etc...
        */

        Service keyboardService = new KeyboardListenerService();
        keyboardService.start();
    }
}
