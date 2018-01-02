package hsrm.eibo.mediaplayer.Game.Service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class KeyboardListenerService extends Service {
    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("Test");
                return null;
            }
        };
    }
}
