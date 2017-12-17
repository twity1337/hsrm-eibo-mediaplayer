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

public class NotificationScene extends Scene {

    final Stage parentStage;

    VBox mainBox = new VBox();
    Label messageLabel = new Label();
    TextArea exceptionText = new TextArea();
    Region spacer = new Region();

    public NotificationScene(Stage parentStage, double width, double height) {
        super(new VBox(), width, height);
        mainBox = (VBox) super.getRoot();
        this.parentStage = parentStage;
        initBasicElements();
    }

    private void initBasicElements() {

        this.mainBox.setPadding(new Insets(10));
        this.mainBox.setSpacing(5);
        this.mainBox.setFillWidth(true);

        HBox buttonLine = new HBox();
        buttonLine.setAlignment(Pos.BOTTOM_CENTER);


        Button okButton = new Button("OK");
        okButton.setOnAction(event -> parentStage.close());
        okButton.setAlignment(Pos.BOTTOM_CENTER);
        okButton.setMinWidth(75);
        okButton.setDefaultButton(true);
        buttonLine.getChildren().add(okButton);

        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(exceptionText, Priority.ALWAYS);
        this.exceptionText.setEditable(false);
        this.exceptionText.setWrapText(true);

        this.mainBox.getChildren().addAll(this.messageLabel, buttonLine);
    }


    public void setMessage(String message)
    {
        if(message != null)
            this.messageLabel.setText(message + ":");
    }

    public void setExceptionText(String exceptionText)
    {
        if(exceptionText != null)
            this.exceptionText.setText(exceptionText);
    }



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
