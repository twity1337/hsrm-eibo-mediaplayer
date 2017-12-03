package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        String testpath = "G:\\HS-RM Downloads\\HS WI\\3. Semester\\Eibo\\hsrm-eibo-mediaplayer\\media\\03. Prelude.mp3";

        try {
            MediaUtil.createMetadata(testpath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }

        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");

        ViewBuilder.getInstance().preparePrimaryStage(primaryStage);
        primaryStage.show();

        MediaController mediaControl = MediaController.getInstance();
    }
}
