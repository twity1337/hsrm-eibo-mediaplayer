package hsrm.eibo.mediaplayer.Game.View;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HostGamePane extends BorderPane{

    public HostGamePane() {
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
}
