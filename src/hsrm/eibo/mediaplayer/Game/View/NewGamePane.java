package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class NewGamePane extends GridPane {

    private Window parentWindow;

    public NewGamePane(Window parentWindow) {
        this.parentWindow = parentWindow;
        initPaneComponents();
    }

    /**
     * Initializes all basic components displayed on "new game" dialog.
     */
    private void initPaneComponents() {
        Label text0 = new Label("Instrument:");
        ComboBox<String> comboBox0 = new ComboBox<>();
        comboBox0.getItems().addAll("Klavier", "Gitarre", "Test");

        Label text1 = new Label("Hintergrund-Song:");
        TextField path = new TextField();
        Button fileChooserButton = new Button("..");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mediadateien", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO)));
        fileChooser.setTitle("Hintergrund-Datei auswählen...");
        fileChooserButton.setOnAction(event -> {
            String selectedPath = fileChooser.showOpenDialog(this.parentWindow).getAbsolutePath();
            path.setText(selectedPath);
        });

        this.add(text0, 0, 0);
        this.add(comboBox0, 1, 0);
        this.add(text1, 0, 1);
        this.add(path, 1, 1);
        this.add(fileChooserButton, 2, 1);
    }
}
