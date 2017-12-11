package hsrm.eibo.mediaplayer.Core.View.Components;

import hsrm.eibo.mediaplayer.Core.Model.MediaListElementInterface;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class PlaylistTreeView extends TreeView<MediaListElementInterface> {

    public PlaylistTreeView() {
        super(new TreeItem<>(null));
        super.setShowRoot(false);
    }

    public void redraw() {
        ObservableList<TreeItem<MediaListElementInterface>> oldItems = super.getRoot().getChildren();
        super.setRoot(new TreeItem<>());
        super.getRoot().getChildren().addAll(oldItems);
    }
}
