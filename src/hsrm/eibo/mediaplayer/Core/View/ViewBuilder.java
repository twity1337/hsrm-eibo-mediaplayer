package hsrm.eibo.mediaplayer.Core.View;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.Exception.HasAdditionalInformation;
import hsrm.eibo.mediaplayer.Core.View.Components.MainBorderPane;
import hsrm.eibo.mediaplayer.Core.View.Components.NotificationScene;
import hsrm.eibo.mediaplayer.Game.View.NewGamePane;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A singleton for View initialization and ErrorHandler Observer for ErrorHandler class.
 */
public class ViewBuilder implements Observer{

    /**
     * The main Window title
     */
    private static final String WINDOW_TITLE = "QuarkTime-Player";
    /**
     * The default height of the main window.
     */
    private static final int WINDOW_DEFAULT_HEIGHT = 600;
    /**
     * The default width of the main window.
     */
    private static final int WINDOW_DEFAULT_WIDTH = 870;
    /**
     * The minimum height of the main window.
     */
    private static final int WINDOW_MIN_HEIGHT = 560;
    /**
     * The minimum width of the main window.
     */
    private static final int WINDOW_MIN_WIDTH = 500;
    /**
     * The default stylesheet path.
     */
    public static final String STYLESHEET_MAIN_PATH = "/hsrm/eibo/mediaplayer/Resources/Css/main.css";
    /**
     * The (fixed) height of the notification / error dialog box.
     */
    public static final int NOTIFICATION_DIALOG_HEIGHT = 300;
    /**
     * The (fixed) width of the notification / error dialog box.
     */
    public static final int NOTIFICATION_DIALOG_WIDTH = 500;

    /**
     * The singleton instance
     */
    private static ViewBuilder instance;
    /**
     * A boolean indicating if the application runs in Debug mode.
     */
    private boolean debugModeEnabled;

    /**
     * Private default constructor (singleton)
     * @see #getInstance()
     */
    private ViewBuilder() {
        ErrorHandler.getInstance().addObserver(this);
    }

    /**
     * Creates the singleton instance.
     * @return the singleton instance.
     */
    public static ViewBuilder getInstance()
    {
        if(instance == null) {
            instance = new ViewBuilder();
        }
        return instance;
    }

    /**
     * Contains the primaryStage of the root window.
     */
    private Stage primaryStage = null;

    /**
     * Initializes the given Stage as primaryStage.
     * This includes setting the Window measurements, stylesheet binding and root Scene creation.
     * @param primaryStage The stage to set as primaryStage
     */
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

    /**
     * Enables the global debugMode setting.
     * @param debugModeEnabled A boolean indicating if debug mode should be enabled.
     * @return fluent interface
     */
    public ViewBuilder setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
        return this;
    }

    /**
     * Returns the set Primary Stage
     * @return a Stage, which has been set as Primary Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Instantiates a new MainBorderPane with the given DebugMode setting.
     * @return A BorderPane containing the main elements.
     */
    public Pane createMainView()
    {
        return new MainBorderPane(this, debugModeEnabled);
    }

    /**
     * Opens a "new game" dialog with options to set up a new game.
     */
    public void showNewGameDialog()
    {
        Stage dialog = this.createDialogStage(300, 500, "Neues Spiel erstellen ...");
        Scene rootScene = new Scene(new NewGamePane(dialog), 300, 500);
        dialog.setScene(rootScene);
        dialog.show();
    }

    /**
     * Opens a "join game" dialog with options to join an exisiting game.
     */
    public void showJoinGameDialog()
    {
        Stage dialog = createDialogStage(300, 500, "Spiel beitreten...");
//        Scene rootScene = new Scene(new JoinGameDialog(), 300, 500);
//        dialog.setScene(rootScene);
        dialog.show();

    }

    /**
     * Opens an error dialog with the given error mesasage and exception text.
     * @param message a summary of all occurred errors, listed in this dialog.
     * @param exceptionText A detail message of any occured problem.
     */
    public void showErrorDialog(String message, String exceptionText)
    {
        Stage dialog = createDialogStage(NOTIFICATION_DIALOG_HEIGHT, NOTIFICATION_DIALOG_WIDTH, "Fehler");
        NotificationScene scene = new NotificationScene(dialog, NOTIFICATION_DIALOG_WIDTH, NOTIFICATION_DIALOG_HEIGHT);
        scene.setMessage(message);
        if(exceptionText != null)
            scene.setExceptionText(exceptionText);

        dialog.setScene(scene.render());
        dialog.showAndWait();
    }

    /**
     * Overloaded method without exceptionText.
     * @see #showErrorDialog(String, String)
     * @param message
     */
    public void showErrorDialog(String message)
    {
        showErrorDialog(message, null);
    }

    /**
     * A method handler, which is called on application shutdown.
     */
    public void handleShutdown() {
        System.out.println("-- Exiting --");
        System.exit(0);
    }

    /**
     * The observer implementation which is called on every time an error occurs.
     * @see ErrorHandler
     * @param o The current instance of {@link ErrorHandler}
     * @param arg is always null
     */
    @Override
    public void update(Observable o, Object arg) {
        ErrorHandler observable = (ErrorHandler) o;
        List<Throwable> collectedErrors = ((ErrorHandler) o).getCollectedErrors();

        ViewBuilder ths = this;
        collectedErrors.forEach(e -> {

            String errorSummary = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage();
            if(e instanceof HasAdditionalInformation)
                errorSummary =  e.getClass().getSimpleName() + ": " + ((HasAdditionalInformation) e).getAddionalInformationMessage();
            if(debugModeEnabled)
            {
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                errorSummary += "\n" + writer.toString();
            }
            ths.showErrorDialog(observable.getLastErrorSummary(), errorSummary);
        });
    }

    @NotNull
    private Stage createDialogStage(int stageHeight, int stageWidth, String stageTitle) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(this.primaryStage);
        dialog.setTitle(stageTitle);
        dialog.setResizable(false);
        dialog.setHeight(stageHeight);
        dialog.setWidth(stageWidth);
        return dialog;
    }
}
