package hsrm.eibo.mediaplayer;

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
        ViewBuilder.getInstance().initPrimaryStage(primaryStage);
        primaryStage.show();

    }
}
