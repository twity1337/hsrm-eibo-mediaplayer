package hsrm.eibo.mediaplayer;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");
        Media mediaToTestMetaData = new Media(testFilePath);
        System.exit(0);
    }
}
