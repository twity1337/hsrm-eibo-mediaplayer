package hsrm.eibo.mediaplayer.Core.View.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A Notification scene for error / notification dialog boxes.
 */
public class NotificationScene extends Scene {

    /**
     * The Stage of the main window.
     */
    private final Stage parentStage;

    /**
     * The main VBox element containing all minor elements.
     */
    private VBox mainBox = new VBox();

    /**
     * The message label for error summary.
     */
    private Label messageLabel = new Label();

    /**
     * The text area containing the exception text.
     */
    private TextArea exceptionText = new TextArea();

    /**
     * A spacer element, which is used if the exceptionText Text-area is not present.
     */
    private Region spacer = new Region();

    /**
     * the constructor with parentStage, height and width value.
     * @param parentStage the parent stage of the main window.
     * @param width the width of the notification stage.
     * @param height the height of the notification stage.
     */
    public NotificationScene(Stage parentStage, double width, double height) {
        super(new VBox(), width, height);
        mainBox = (VBox) super.getRoot();
        this.parentStage = parentStage;
        initBasicElements();
    }

    /**
     * Initializes all basic elements in main VBox on object construction.
     */
    private void initBasicElements() {

        this.mainBox.setPadding(new Insets(10));
        this.mainBox.setSpacing(5);
        this.mainBox.setFillWidth(true);

        HBox buttonLine = new HBox();
        buttonLine.setAlignment(Pos.BOTTOM_CENTER);

        // OK button
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> parentStage.close());
        okButton.setAlignment(Pos.BOTTOM_CENTER);
        okButton.setMinWidth(75);
        okButton.setDefaultButton(true);
        buttonLine.getChildren().add(okButton);

        // Spacer region and exception text
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(exceptionText, Priority.ALWAYS);
        this.exceptionText.setEditable(false);
        this.exceptionText.setWrapText(true);

        this.mainBox.getChildren().addAll(this.messageLabel, buttonLine);
    }

    /**
     * Sets the summary text label
     * @param message the summary / non copy-able text in notification scene.
     */
    public void setMessage(String message)
    {
        if(message != null)
            this.messageLabel.setText(message + ":");
    }

    /**
     * Sets the exception detail message (the copy-able text of the notification)
     * Displayed in an non-editable textArea.
     * @param exceptionText the exception text
     */
    public void setExceptionText(String exceptionText)
    {
        if(exceptionText != null)
            this.exceptionText.setText(exceptionText);
    }

    /**
     * Renders the NotificationScene and return this object.
     * @return the rendered NotificationScene as Scene object.
     */
    public Scene render()
    {
        if(this.exceptionText.getText().isEmpty())
            this.mainBox.getChildren().add(1, this.spacer);
        else
            this.mainBox.getChildren().add(1, this.exceptionText);
        super.setRoot(this.mainBox);
        return this;
    }


}
