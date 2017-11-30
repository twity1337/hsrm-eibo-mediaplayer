package hsrm.eibo.mediaplayer.Core.View;

import hsrm.eibo.mediaplayer.Core.View.Components.MainBorderPane;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewBuilder {

    private static final String WINDOW_TITLE = "QuarkTime-Player";
    private static final int WINDOW_DEFAULT_HEIGHT = 600;
    private static final int WINDOW_DEFAULT_WIDTH = 870;
    private static final int WINDOW_MIN_HEIGHT = 300;
    private static final int WINDOW_MIN_WIDTH = 400;

    private static ViewBuilder instance;
    public static ViewBuilder getInstance()
    {
        if(instance == null) {
            instance = new ViewBuilder();
        }
        return instance;
    }

    public void preparePrimaryStage(Stage primaryStage)
    {
        Scene root = new Scene(this.createMainView(), WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT);
        primaryStage.setScene(root);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setMinHeight(WINDOW_MIN_HEIGHT);
        primaryStage.setMinWidth(WINDOW_MIN_WIDTH);
    }

    public BorderPane createMainView()
    {
        return new MainBorderPane();
    }

}
