package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Observable;


public class Main extends Application {

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        try {
            ViewBuilder.getInstance().initPrimaryStage(primaryStage);
            primaryStage.show();
//        } catch (Exception e) {
//            ErrorHandler err = ErrorHandler.getInstance();
//            err.addError(e);
//            err.notifyErrorObserver(e, "Es ist ein unerwarteter Fehler aufgetreten:");
//        }
    }
}
