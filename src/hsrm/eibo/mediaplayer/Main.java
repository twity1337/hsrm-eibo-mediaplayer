package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String testpath = "C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media\\03. Prelude.mp3";

/*        PlaylistManager manager = PlaylistManager.getInstance();
        try {
            manager.loadPlaylistFromFile(new File("C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media\\playlist.m3u"));
            manager.loadPlaylistFromFile(new File("C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media\\playlist.m3u"));

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        ViewBuilder.getInstance().preparePrimaryStage(primaryStage);
        primaryStage.show();

    }
}
