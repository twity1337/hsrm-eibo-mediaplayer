package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    // Layout für Klavier: start bei Taste y= Ton c (bass 2. Zwischenraum), Halbtonschritte;
    // Taste a = Ton c (violin Schlüssel 1. Hilfslinie), Halbtonschritte
    public static final Map<Integer,Integer> ORDINAL_LAYOUT = new HashMap<>();
    public static final String[] KEYNAMES_PIANO = new String[]{"C","Cis/Des","D","Dis/Es","E","","F","Fis/Ges","G","Gis/As","A","Ais/b","H","","C","Cis/Des","D","Dis/Es","E","","F","Fis/Ges","G","Gis/As","A","Ais/b","H","","C","Cis/Des","D","Dis/Es","E"};
    public static final String[] KEYBOARD_KEYNAMES = new String[]{"Y","S","X","D","C","V","G","B","H","N","J","M","Q","2","W","3","E","R","5","T","6","Z","7","U","I","9","O","0","P"};
    public static final int NR_OF_WHITE_KEYS = 17;
    public static final int NR_OF_BLACK_KEYS = 12;
    public final static int BASIC_C_NOTE = 60;

    private final static double BLACK_KEY_SIZE_FACTOR = 0.7;
    private final static int DEFAULT_KEY_HIGHT = 200;
    private final static int DEFAULT_KEY_WIDTH = 50;
    public static final int KEY_NOT_ASSIGNED = -1;

    public static Pane createKeyboardPane(){
        StackPane sPane = new StackPane();
        HBox whiteKeyboard = createWhiteKeyboard();
        HBox blackKeyboard = createBlackKeyboard();
        sPane.getChildren().addAll(whiteKeyboard,blackKeyboard);
        return sPane;
    }

    public static int getKeyboardWidth(){return DEFAULT_KEY_WIDTH*NR_OF_WHITE_KEYS;}

    public static int getKeyboardHeight(){return DEFAULT_KEY_HIGHT;}

    public static int getNotePitchFromKeyevent(KeyEvent event){
        String key = event.getCode().getName().toUpperCase();
        int index;
        if((index = ArrayUtils.indexOf(KEYBOARD_KEYNAMES, key))== ArrayUtils.INDEX_NOT_FOUND)
            return KEY_NOT_ASSIGNED;
        return BASIC_C_NOTE + index;
    }

    private static HBox createWhiteKeyboard() {
        HBox wBoard = new HBox();
        wBoard.setMinWidth(DEFAULT_KEY_WIDTH*NR_OF_WHITE_KEYS);
        wBoard.setMinHeight(DEFAULT_KEY_HIGHT);
        int index = 0;
        for (int i = 0; i < KEYNAMES_PIANO.length ; i++)
        {
            if (i%2 != 0)
            {
                if (KEYNAMES_PIANO[i].length() != 0)
                    index++;
                continue; // überspringe Zwischenräume
            }
            Canvas c = createImageWhiteKey(KEYBOARD_KEYNAMES[index]);
            int note = calculateNote(i);
            KeyboardKey whiteK = new KeyboardKey(note);
            whiteK.setMinWidth(DEFAULT_KEY_WIDTH);
            whiteK.setMinHeight(DEFAULT_KEY_HIGHT);
            whiteK.getChildren().add(c);
            wBoard.getChildren().add(whiteK);
            index++;
        }
        return wBoard;
    }

    private static HBox createBlackKeyboard() {
        HBox bBoard = new HBox();

        bBoard.setMinWidth(DEFAULT_KEY_WIDTH*NR_OF_WHITE_KEYS*BLACK_KEY_SIZE_FACTOR);
        bBoard.setMinHeight(DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR);
        bBoard.setSpacing(DEFAULT_KEY_WIDTH-DEFAULT_KEY_WIDTH*BLACK_KEY_SIZE_FACTOR);
        HBox padding = new HBox();
        padding.setMinWidth(DEFAULT_KEY_WIDTH-DEFAULT_KEY_WIDTH*BLACK_KEY_SIZE_FACTOR);
        bBoard.getChildren().add(padding);
        int index = 0;
        for (int i = 0; i < KEYNAMES_PIANO.length ; i++)
        {
            if (i%2 == 0)  // überspringe Ganztöne
            {
                if (KEYNAMES_PIANO[i].length() == 0)
                    continue;
                index++;
                continue;
            }

            Canvas c = createImageBlackKey(KEYBOARD_KEYNAMES[index]);
            int note = calculateNote(i);
            KeyboardKey blackK = new KeyboardKey(note);

            if (KEYNAMES_PIANO[i].length()==0) // überspringe halb Tontasten die nicht da sind
            {
                blackK.setMouseTransparent(true);
                c = new Canvas();
            } else {
                index++;
            }
            blackK.setMinWidth(DEFAULT_KEY_WIDTH*BLACK_KEY_SIZE_FACTOR);
            blackK.setMinHeight(DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR);
            blackK.getChildren().add(c);
            bBoard.getChildren().add(blackK);
        }
        return bBoard;
    }

    private static Canvas createImageWhiteKey(String name) {
        Canvas canvas = new Canvas(DEFAULT_KEY_WIDTH, DEFAULT_KEY_HIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.fillRoundRect(0, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT,10,10);
        gc.strokeRoundRect(0, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT,10,10);
        gc.strokeText(name,DEFAULT_KEY_WIDTH/2-5,DEFAULT_KEY_HIGHT-20);
        return canvas;
    }

    private static Canvas createImageBlackKey(String name) {
        Canvas canvas = new Canvas(DEFAULT_KEY_WIDTH*BLACK_KEY_SIZE_FACTOR, DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.fillRoundRect(0, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR,10,10);
        gc.strokeRoundRect(0, 0,DEFAULT_KEY_WIDTH,DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR,10,10);
        gc.strokeText(name,DEFAULT_KEY_WIDTH*BLACK_KEY_SIZE_FACTOR/2-5,DEFAULT_KEY_HIGHT*BLACK_KEY_SIZE_FACTOR-20);
        return canvas;
    }


    private static class KeyboardKey extends HBox{
        private int note;

        public KeyboardKey(int note){
            this.note = note;
        }

        public int getNote(){
            return this.note;
        }
    }

    private static int calculateNote(int index){
        return BASIC_C_NOTE+index;
    }
}
