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
    public static final String STYLESHEET_MAIN_PATH = "/hsrm/eibo/mediaplayer/Resources/Css/main.css";

    private static ViewBuilder instance;
    public static ViewBuilder getInstance()
    {
        if(instance == null) {
            instance = new ViewBuilder();
        }
        return instance;
    }

    private Stage primaryStage = null;

    public void initPrimaryStage(Stage primaryStage)
    {
        if(this.primaryStage != null)
            throw new RuntimeException("ViewBuilder.initPrimaryStage() can only be called once. It was already called before.");
        Scene root = new Scene(this.createMainView(), WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT);
        root.getStylesheets().add(this.getClass().getResource(STYLESHEET_MAIN_PATH).toString());

        primaryStage.setScene(root);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setMinHeight(WINDOW_MIN_HEIGHT);
        primaryStage.setMinWidth(WINDOW_MIN_WIDTH);
        primaryStage.setOnCloseRequest(event -> handleShutdown());
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane createMainView()
    {
        return new MainBorderPane(this);
    }

    public void handleShutdown() {
        System.out.println("-- Exiting --");
        System.exit(0);
    }
}
