package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Controller.MediaController;
import javafx.event.EventHandler;
import javafx.scene.Group;
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

    private Group getCenterComponents() {
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
        return buttonGroup;
    }

}
