package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class HostGamePane extends BorderPane implements Observer{

    ArrayList<BandMember> connectedClient;

    public HostGamePane() {
        SocketHostManager.getInstance().getConnectedClientList().addObserver(this);
        this.setCenter(getPlayerListView());
    }

    private Pane getPlayerListView(){
        VBox listBox = new VBox(createListHeader());
        return listBox;
    }

    private Pane createListHeader(){
        HBox header = new HBox();
        header.getChildren().addAll(new Label("Name"), new Label("Instrument"));
        return header;
    }

    private void updateLayout() {

        this.requestLayout();
    }

    @Override
    public void update(Observable o, Object arg) {
        connectedClient = (ArrayList<BandMember>) arg;
        Platform.runLater(this::updateLayout);
    }
}
