package hsrm.eibo.mediaplayer;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Dies ist ein Test!");
        Track Track_to_test_metadata_reading = new Track("C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media");
        System.exit(0);
    }
}
