package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Observable;


public class Main extends Application {

    private static final boolean DEBUG_MODE_ENABLED = true;

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewBuilder.getInstance()
                .setDebugModeEnabled(DEBUG_MODE_ENABLED)
                .initPrimaryStage(primaryStage);
        ErrorHandler.initExceptionHandling();
        primaryStage.show();
    }
}
