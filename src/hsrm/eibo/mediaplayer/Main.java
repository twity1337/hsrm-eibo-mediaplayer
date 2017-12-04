package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Core.Util.MetadataService;
import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String testpath = "C:\\Users\\lucas\\IdeaProjects\\eibo-project\\hsrm-eibo-mediaplayer\\media\\03. Prelude.mp3";
        final Metadata[] md = new Metadata[1];
        MetadataService service;


            service = new MetadataService();
            service.setPath(testpath);
            service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    md[0] = (Metadata) event.getSource().getValue();
                    System.out.println(md[0].toString());
                }
            });
            service.start();

        String testFilePath = ("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/media/03. Prelude.mp3").replace(" ", "%20");

        ViewBuilder.getInstance().preparePrimaryStage(primaryStage);
        primaryStage.show();

        MediaController mediaControl = MediaController.getInstance();
    }
}
