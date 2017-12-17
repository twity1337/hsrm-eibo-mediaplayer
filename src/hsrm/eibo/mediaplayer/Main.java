package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Observable;

/**
 * @author Marc Lucas Pflueger
 * @author Tim Wissmann
 * This application is the work of both authors for the "Hochschule Rhein Main"
 * course "Entwicklung interaktiver Benutzeroberflächen" WS 17/18.
 * The task was to create an Mp3 Media player with javafx as the framework.
 * The application should be designed with a design pattern taught in named course.
 * We used the ModelViewController design to make the application and its components
 * both extendable and interchangeable.
 */
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
