package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameWindow {
    private static GameWindow ourInstance = new GameWindow();
    private Stage gameStage;
    private KeyboardListener listenerInstance;

    public static GameWindow getInstance() {
        return ourInstance;
    }

    private GameWindow() {
        gameStage = new Stage();
        listenerInstance = KeyboardListener.getInstance();
    }

    public void start(){
        Group game = new Group();
        game.autosize();
        Scene scene = new Scene(game);
        gameStage.setTitle("Game");
        gameStage.setScene(scene);
        gameStage.show();
        listenerInstance.setActive(true);

        gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    listenerInstance.setActive(false);}
            });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
               // myMusician.keyPressed(event.getCode());
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // myMusician.keyReleased(event.getCode());
            }
        });
    }
}
