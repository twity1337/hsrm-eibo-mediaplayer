package hsrm.eibo.mediaplayer.Core.View.Components;

import javafx.scene.control.*;
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

        Menu[] menus = {
                new Menu("Datei"),
                new Menu("Wiedergabe"),
                new Menu("Playlist"),
                new Menu("Ansicht"),
                new Menu("?")
        };

        MenuItem[] fileSubMenu = {
                new MenuItem("Öffnen..."),
                new MenuItem("Playlist öffnen..."),
                new SeparatorMenuItem(),
                new MenuItem("Beenden")
        };

        menus[0].getItems().addAll(fileSubMenu);

        menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(menuBar);

        return topVBox;
    }

}
