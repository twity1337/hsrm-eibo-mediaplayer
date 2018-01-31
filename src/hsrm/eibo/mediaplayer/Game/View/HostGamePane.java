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

    ArrayList<BandMember> connectedClients;
    private VBox listBox;

    public HostGamePane() {
        SocketHostManager.getInstance().getConnectedClientList().addObserver(this);
        this.setCenter(createPlayerListView());
    }

    private Pane createPlayerListView(){
        listBox = new VBox(createListHeader());
        return listBox;
    }

    private Pane createListHeader(){
        HBox header = new HBox();
        header.getChildren().addAll(new Label("Name"), new Label("Instrument"));
        return header;
    }

    private void updateLayout() {
        listBox.getChildren().clear();
        listBox.getChildren().add(createListHeader());
        for (BandMember bandMember : connectedClients) {
            listBox.getChildren().add(new HBox(new Label(bandMember.getName()), new Label(Integer.toString(bandMember.getInstrument()))));
        }
        requestLayout();
    }

    @Override
    public void update(Observable o, Object arg) {
        connectedClients = (ArrayList<BandMember>) arg;
        Platform.runLater(this::updateLayout);
    }
}
