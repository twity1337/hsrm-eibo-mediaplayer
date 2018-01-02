package hsrm.eibo.mediaplayer.Game.View;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class GameOptionPane extends GridPane {

    private Stage parentStage;
    private int lastRowIndex = 0;

    GameOptionPane(Stage parentStage) {
        this.parentStage = parentStage;
        initBasicPaneComponents();
        initAdditionalComponents();
        completeBeforeRendering();
    }

    /**
     * Returns a button object added as default button.
     * @return Button The default action button.
     */
    protected abstract Button getDefaultButton();

    /**
     * Adds new components between basic components and button row.
     * @see #appendNewRow(Node...) for adding another row of components.
     */
    protected abstract void initAdditionalComponents();

    /**
     * Adds a new row of Node objects to the underlying GridPane.
     * Respects the current index of existing rows.
     * @param items the Node objects to add in one row
     */
    protected void appendNewRow(Node... items)
    {
        this.addRow(lastRowIndex++, items);
    }

    /**
     * Getter for parent stage.
     * @return parentStage
     */
    protected Stage getParentStage() {
        return parentStage;
    }

    /**
     * Initializes all basic components displayed on "new game" dialog.
     */
    private void initBasicPaneComponents() {
        Label text0 = new Label("Instrument:");
        ComboBox<String> comboBox0 = new ComboBox<>();
        comboBox0.getItems().addAll("Klavier", "Gitarre", "Test");


        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> this.parentStage.close());

        this.appendNewRow(text0,  comboBox0);
    }

    /**
     * Finalizes the JavaFX element for rendering and adds last components like Buttons and so on...
     */
    private void completeBeforeRendering() {
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(event -> this.parentStage.close());

        Button defaultAction = getDefaultButton();
        defaultAction.setDefaultButton(true);
        this.appendNewRow(cancelButton, defaultAction);
    }
}
