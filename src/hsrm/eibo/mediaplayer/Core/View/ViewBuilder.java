package hsrm.eibo.mediaplayer.Core.View;

import hsrm.eibo.mediaplayer.Core.View.Components.MainBorderPane;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ViewBuilder {

    private static ViewBuilder instance;
    public static ViewBuilder getInstance()
    {
        if(instance == null) {
            instance = new ViewBuilder();
        }
        return instance;
    }

    public BorderPane createMainView()
    {
        return new MainBorderPane();
    }

}
