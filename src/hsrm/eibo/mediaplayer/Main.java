package hsrm.eibo.mediaplayer;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import java.net.URI;
import java.util.ArrayList;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");
        Media mediaToTestMetaData = new Media(testFilePath);
        Track test = new Track(testFilePath);
        System.out.println(test.getMetadata().getAlbum());

        primaryStage.setTitle("bla");
        Group root = new Group();
        Scene scene = new Scene(root);

        MediaPlayer player = test.getTrackMediaPlayer();
        MediaController mediaControl = MediaController.getInstance();
        scene.setRoot(mediaControl);

        primaryStage.setScene(scene);
        primaryStage.show();

        mediaControl.setTrack(player);
        mediaControl.play();
    }
}
