package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {

        int[] testIntarray = MediaUtil.generateShuffelList(20);

        //for testing reasons
        String[] list={};
/*
        String testpath = "C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media\\playlist.m3u";
        try {
            list = MediaUtil.parseM3u(testpath);
        } catch (Exception e){}
        Playlist playlist = new Playlist(list);
*/

        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");

        ViewBuilder.getInstance().preparePrimaryStage(primaryStage);
        primaryStage.show();

        MediaController mediaControl = MediaController.getInstance();
    }
}
