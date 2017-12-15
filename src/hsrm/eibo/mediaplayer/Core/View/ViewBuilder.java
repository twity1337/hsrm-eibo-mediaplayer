package hsrm.eibo.mediaplayer.Core.View;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.Exception.HasAdditionalInformation;
import hsrm.eibo.mediaplayer.Core.View.Components.MainBorderPane;
import hsrm.eibo.mediaplayer.Core.View.Components.NotificationScene;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

public class ViewBuilder implements Observer{

    private static final String WINDOW_TITLE = "QuarkTime-Player";
    private static final int WINDOW_DEFAULT_HEIGHT = 600;
    private static final int WINDOW_DEFAULT_WIDTH = 870;
    private static final int WINDOW_MIN_HEIGHT = 560;
    private static final int WINDOW_MIN_WIDTH = 500;
    public static final String STYLESHEET_MAIN_PATH = "/hsrm/eibo/mediaplayer/Resources/Css/main.css";
    public static final int NOTIFICATION_DIALOG_HEIGHT = 300;
    public static final int NOTIFICATION_DIALOG_WIDTH = 500;

    private static ViewBuilder instance;

    private ViewBuilder() {
        ErrorHandler.getInstance().addObserver(this);
    }

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

    public void showErrorDialog(String message, String exceptionText)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(this.primaryStage);
        dialog.setTitle("Fehler");
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.setHeight(NOTIFICATION_DIALOG_HEIGHT);
        dialog.setWidth(NOTIFICATION_DIALOG_WIDTH);

        NotificationScene scene = new NotificationScene(dialog, NOTIFICATION_DIALOG_WIDTH, NOTIFICATION_DIALOG_HEIGHT);
        scene.setMessage(message);
        if(exceptionText != null)
            scene.setExceptionText(exceptionText);

        dialog.setScene(scene.render());
        dialog.showAndWait();
    }

    public void showErrorDialog(String message)
    {
        showErrorDialog(message, null);
    }

    public void handleShutdown() {
        System.out.println("-- Exiting --");
        System.exit(0);
    }

    @Override
    public void update(Observable o, Object arg) {
        ErrorHandler observable = (ErrorHandler) o;
        List<Exception> collectedErrors = ((ErrorHandler) o).getCollectedErrors();

        ViewBuilder ths = this;
        collectedErrors.forEach(e -> {

            String errorSummary = e.getClass().toString() + ": " + e.getLocalizedMessage();
            if(e instanceof HasAdditionalInformation)
                errorSummary =  e.getClass().toString() + ": " + ((HasAdditionalInformation) e).getAddionalInformationMessage();
            ths.showErrorDialog(observable.getLastErrorSummary(), errorSummary);
        });
    }
}
