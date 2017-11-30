package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.net.URI;
import java.util.ArrayList;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        ViewBuilder.getInstance().preparePrimaryStage(primaryStage);
        primaryStage.show();


        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");
        Media mediaToTestMetaData = new Media(testFilePath);
        Track test = new Track(testFilePath);
        System.out.println(test.getMetadata().getAlbum());
    }
}
