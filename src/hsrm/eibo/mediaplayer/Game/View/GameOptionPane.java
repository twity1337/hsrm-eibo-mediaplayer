package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.View.ViewBuilder;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import hsrm.eibo.mediaplayer.Game.Model.InstrumentSelectionModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * An abstract class for instantiating Option panes with game setting options.
 */
public abstract class GameOptionPane extends GridPane {

    private Stage parentWindow;
    private int lastRowIndex = 0;
    protected TextField nameField;
    protected ComboBox<InstrumentSelectionModel> instrumentComboBox;

    GameOptionPane(Stage parentWindow) {
        this.parentWindow = parentWindow;
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
     * Method which extends the given GameSettings with values from additional components, if necessary.
     * @param gameSettings the game settings which were set through the basic components so far.
     */
    protected void updateGameSettingsByAdditionalValues(GameSettings gameSettings) {}

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
     * @return parentWindow
     */
    protected Stage getParentWindow() {
        return parentWindow;
    }

    /**
     * Getter for the filled game settings of values from option pane.
     * @return The defined game settings.
     */
    protected GameSettings getPreparedGameSettings()
    {
        GameSettings settings = new GameSettings();

        this.setBasicGameSettingValues(settings);
        this.updateGameSettingsByAdditionalValues(settings);
        return settings;
    }

    protected boolean validateUserInput()
    {
        GameSettings settings = this.getPreparedGameSettings();
        if(settings.getPlayerName().isEmpty())
        {
            ViewBuilder.getInstance().showErrorDialog("Bitte geben Sie einen Namen ein.");
            return false;
        }
        return true;
    }

    /**
     * Initializes all basic components displayed on "new game" dialog.
     */
    private void initBasicPaneComponents() {
        Label text0 = new Label("Name:");
        nameField = new TextField();

        Label text1 = new Label("Instrument:");
        instrumentComboBox = new ComboBox<>();
        instrumentComboBox.getItems().addAll(
                new InstrumentSelectionModel("Klavier", 0),
                new InstrumentSelectionModel("Gitarre", 105),
                new InstrumentSelectionModel("Helicopter", 125),
                new InstrumentSelectionModel("Orgel", 17)
        );
        instrumentComboBox.getSelectionModel().select(0);

        this.appendNewRow(text0, nameField);
        this.appendNewRow(text1, instrumentComboBox);
    }

    private void setBasicGameSettingValues(GameSettings settings)
    {
        settings.setPlayerName(nameField.getText());
        InstrumentSelectionModel instrument = instrumentComboBox.getSelectionModel().getSelectedItem();
        settings.setInstrumentTitle(instrument.getTitle());
        settings.setInstrumentId(instrument.getInstrumentId());
    }

    /**
     * Finalizes the JavaFX element for rendering and adds last components like Buttons and so on...
     */
    private void completeBeforeRendering() {
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(event -> this.parentWindow.close());

        Button defaultAction = getDefaultButton();
        defaultAction.setDefaultButton(true);
        this.appendNewRow(cancelButton, defaultAction);
    }
}
