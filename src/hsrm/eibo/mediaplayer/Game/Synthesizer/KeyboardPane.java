package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class KeyboardPane extends StackPane {
    private final static int DEFAULT_KEY_HIGHT = 200;
    private final static int DEFAULT_KEY_WIDTH = 40;

    public KeyboardPane(){
        HBox whiteKeys = this.getwhiteKeyBoard();
        HBox blackKeys = this.getBlackKeyBoard();
        this.getChildren().addAll(whiteKeys,blackKeys);
    }

    private HBox getBlackKeyBoard() {
        HBox b = new HBox();
        return b;
    }

    private HBox getwhiteKeyBoard() {
        HBox b = new HBox();
        int nrOfKeys = Keyboard.NR_OF_WHITE_KEYS;

        for (int i= 0 ; i < nrOfKeys ; i++)
        {

        }
        return b;
    }

    public static Canvas getPianoKeyboard(){
        HBox whiteKeyBoard = drawWhiteKeys();

        drawWhiteKeys(nrOfKeys, gc);
        return canvas;
    }

    private static HBox drawWhiteKeys() {
        Pane p = new HBox();
        p.setMinWidth(Keyboard.NR_OF_WHITE_KEYS*DEFAULT_KEY_WIDTH);
        p.setMinHeight(DEFAULT_KEY_HIGHT);
        Canvas canvas = new Canvas(DEFAULT_KEY_WIDTH, DEFAULT_KEY_HIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.fillRoundRect(DEFAULT_KEY_WIDTH, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT,10,10);
        gc.strokeRoundRect(DEFAULT_KEY_WIDTH, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT,10,10);
    }

    private static void drawBlackKeys(int nrOfKeys, GraphicsContext gc) {
        gc.setFill(Color.BLACK);

        for (int i = 0; i < nrOfKeys; i++)
        {
            gc.fillRoundRect(i*DEFAULT_KEY_WIDTH, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT,10,10);
        }
    }
}
