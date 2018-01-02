package hsrm.eibo.mediaplayer.Game.View;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class JoinGamePane extends GameOptionPane {

    public JoinGamePane(Stage parentWindow) {
        super(parentWindow);
    }

    @Override
    protected Button getDefaultButton() {
        Button defaultButton = new Button("Spiel beitreten");
        defaultButton.setOnAction(event -> System.out.println("Do stuff..."));
        return defaultButton;
    }

    @Override
    protected void initAdditionalComponents() {

    }
}
