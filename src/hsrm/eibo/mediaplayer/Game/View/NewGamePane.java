package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import hsrm.eibo.mediaplayer.Game.Controller.GameController;
import hsrm.eibo.mediaplayer.Game.Model.GameSettings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class NewGamePane extends GameOptionPane {

    private TextField backgroundSongPathField = null;

    public NewGamePane(Stage parentWindow) {
        super(parentWindow);
    }

    @Override
    protected Button getDefaultButton() {
        Button defaultButton = new Button("Spiel erstellen");
        defaultButton.setOnAction(event -> {
            GameController.setGameSettings(this.getPreparedGameSettings());
            GameController.getInstance().initialize().hostNewGame();
            this.getParentWindow().close();
        });
        return defaultButton;
    }

    @Override
    protected void initAdditionalComponents() {
        Label text1 = new Label("Hintergrund-Song:");
        TextField backgroundSongPathField = new TextField();
        this.backgroundSongPathField = backgroundSongPathField;
        Button fileChooserButton = new Button("..");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mediadateien", MediaUtil.getSupportedFileTypes(MediaUtil.FILETYPE_GID_AUDIO)));
        fileChooser.setTitle("Hintergrund-Datei auswählen...");
        fileChooserButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(this.getParentWindow());
            if (selectedFile == null)
                return;
            backgroundSongPathField.setText(selectedFile.getAbsolutePath());
            this.backgroundSongPathField = backgroundSongPathField;
        });


        this.appendNewRow(text1, backgroundSongPathField, fileChooserButton);
    }

    @Override
    protected void updateGameSettingsByAdditionalValues(GameSettings gameSettings) {
        if(this.backgroundSongPathField != null)
            gameSettings.setBackgroundSongPath(this.backgroundSongPathField.getText());
    }
}
