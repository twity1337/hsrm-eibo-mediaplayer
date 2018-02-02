package hsrm.eibo.mediaplayer.Game.View;

import hsrm.eibo.mediaplayer.Core.Controller.ErrorHandler;
import hsrm.eibo.mediaplayer.Core.Model.Track;
import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import hsrm.eibo.mediaplayer.Game.Network.Host.SocketHostManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static hsrm.eibo.mediaplayer.Game.Model.InstrumentSelectionModel.getInstrumentTitleById;

/**
 * Main pane for server view.
 */
public class HostGamePane extends BorderPane implements Observer {

    public static final int WINDOW_HEIGHT = 400;
    public static final int WINDOW_WIDTH = 300;
    public static final int WINDOW_X_POSITION = 100;
    private static final int COLUMN_WIDTH = WINDOW_WIDTH/2;

    private MediaPlayer playbackTrackMediaPlayer = null;
    private ArrayList<BandMember> connectedClients;
    private VBox listBox;

    public HostGamePane(Track backgroundPlaybackMedia) {
        SocketHostManager.getInstance().getConnectedClientList().addObserver(this);
        this.setCenter(createPlayerListView());

        if (backgroundPlaybackMedia == null)
            return;
        try {
            playbackTrackMediaPlayer = backgroundPlaybackMedia.getTrackMediaPlayer();
            playbackTrackMediaPlayer.play();
        } catch (Exception e) {
            ErrorHandler err = ErrorHandler.getInstance();
            err.addError(e);
            err.notifyErrorObserver("Fehler bei der Initialisierung des Spiels");
        }
    }

    /**
     * Stops playback of background song
     */
    public void stopPlayback()
    {
        if(playbackTrackMediaPlayer != null)
            playbackTrackMediaPlayer.dispose();
    }

    private Pane createPlayerListView() {
        listBox = new VBox(createHeaderText(), createListHeader());
        listBox.setPadding(new Insets(5));
        listBox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2))));
        return listBox;
    }

    @NotNull
    private Text createHeaderText() {
        Text header = new Text("Verbundene Spieler");
        header.setFont(new Font(24));
        header.setTextAlignment(TextAlignment.CENTER);
        return header;
    }

    private Pane createListHeader() {
        HBox header = new HBox();
        Label label0 = new Label("Name");
        Label label1 = new Label("Instrument");
        label0.setPrefWidth(COLUMN_WIDTH);
        label0.setStyle("-fx-font-weight: bold");
        label1.setPrefWidth(COLUMN_WIDTH);
        label1.setStyle("-fx-font-weight: bold");
        header.getChildren().addAll(label0, label1);
        return header;
    }

    private void updateLayout() {
        listBox.getChildren().clear();
        listBox.getChildren().addAll(createHeaderText(), createListHeader());
        for (BandMember bandMember : connectedClients) {
            Label label0 = new Label(bandMember.getName());
            label0.setPrefWidth(COLUMN_WIDTH);
            Label label1 = new Label(getInstrumentTitleById(bandMember.getInstrument()));
            label1.setPrefWidth(COLUMN_WIDTH);
            label0.setTextFill(bandMember.getPlayerColor());
            label1.setTextFill(bandMember.getPlayerColor());
            listBox.getChildren().add(new HBox(label0, label1));
        }
        requestLayout();
    }

    /**
     * Updates the current connected player list on Observer update.
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        connectedClients = (ArrayList<BandMember>) arg;
        Platform.runLater(this::updateLayout);
    }
}
