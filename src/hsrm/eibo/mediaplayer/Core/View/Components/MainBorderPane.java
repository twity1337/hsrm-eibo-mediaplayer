package hsrm.eibo.mediaplayer.Core.View.Components;

import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainBorderPane extends BorderPane {

    MenuBar menuBar = new MenuBar();

    public MainBorderPane()
    {
        this.setTop(getTopComponents());
        this.setCenter(new Button("Test"));
    }

    private Pane getTopComponents()
    {
        VBox topVBox = new VBox();
        topVBox.getChildren().addAll(menuBar);

        return topVBox;
    }

}
