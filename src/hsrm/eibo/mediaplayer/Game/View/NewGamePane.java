package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class NewGamePane extends GameOptionPane {

    public NewGamePane(Stage parentWindow) {
        super(parentWindow);
    }

    @Override
    protected Button getDefaultButton() {
        Button defaultButton = new Button("Spiel erstellen");
        defaultButton.setOnAction(event -> System.out.println("Do stuff..."));
        return defaultButton;
    }

    @Override
    protected void initAdditionalComponents() {
        Label text1 = new Label("Hintergrund-Song:");
        TextField pathField = new TextField();
        Button fileChooserButton = new Button("..");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mediadateien", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO)));
        fileChooser.setTitle("Hintergrund-Datei auswählen...");
        fileChooserButton.setOnAction(event -> {
            String selectedPath = fileChooser.showOpenDialog(this.getParentStage()).getAbsolutePath();
            pathField.setText(selectedPath);
        });


        this.appendNewRow(text1, pathField, fileChooserButton);
    }
}
