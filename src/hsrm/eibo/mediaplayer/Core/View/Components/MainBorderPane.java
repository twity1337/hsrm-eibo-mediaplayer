package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainBorderPane extends BorderPane {

    private final MediaController controller = MediaController.getInstance();

    private MenuBar menuBar = new MenuBar();
    private Button playPauseButton = new Button();

    public MainBorderPane() {
        this.setTop(getTopComponents());
        this.setCenter(getCenterComponents());
    }

    private Pane getTopComponents() {
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

        this.menuBar.getMenus().addAll(menus);
        topVBox.getChildren().addAll(this.menuBar);

        return topVBox;
    }

    private Parent getCenterComponents() {

        TabPane tabPane = new TabPane();

        Group buttonGroup = new Group();
        this.playPauseButton.setText("Play / Pause");
        this.playPauseButton.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println(event.getClickCount());
                controller.togglePlayPause();
            }
        });
        buttonGroup.getChildren().add(this.playPauseButton);

        Tab tab0 = new Tab();
        tab0.setText("Aktuelle Wiedergabe");
        tab0.setContent(buttonGroup);
        tab0.setClosable(false);
        Tab tab1 = new Tab();
        tab1.setText("Playlist");
        tab1.setContent(null);
        tab1.setClosable(false);
        tabPane.getTabs().addAll(tab0, tab1);

        return tabPane;
    }

}
