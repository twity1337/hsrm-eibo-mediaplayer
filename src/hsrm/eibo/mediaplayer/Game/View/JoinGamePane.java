package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Game.Controller.GameManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class JoinGamePane extends GameOptionPane {

    private TextField serverIp;

    public JoinGamePane(Stage parentWindow) {
        super(parentWindow);
    }

    @Override
    protected Button getDefaultButton() {
        Button defaultButton = new Button("Spiel beitreten");
        defaultButton.setOnAction(event -> {
            if(!this.validateUserInput())
            {
                event.consume();
                return;
            }

            GameManager.setGameSettings(this.getPreparedGameSettings());
            GameManager.getInstance().initialize().joinNetworkGame(extractServerIpAdress());
            this.getParentWindow().close();
        });

        return defaultButton;
    }

    @Override
    protected void initAdditionalComponents() {
        Label text1 = new Label("Server-IP:");
        serverIp = new TextField();

        this.appendNewRow(text1, serverIp);
    }

    private InetAddress extractServerIpAdress() {
        InetAddress serverAdress = null;
        try {
            serverAdress = InetAddress.getByName(this.serverIp.getText());
        } catch (UnknownHostException e) {
            ErrorHandler errHandler = ErrorHandler.getInstance();
            errHandler.addError(e);
            errHandler.notifyErrorObserver("Fehler beim Beitreten des Spiels");
        }
        return serverAdress;
    }


}
