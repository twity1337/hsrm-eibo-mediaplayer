package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Observable;

public class KeyboardChangeEvent extends Observable {


    public static final int PRESSED_KEY_INDEX = 0;
    public static final int PRESSED_KEY_COLOR = 1;

    private static KeyboardChangeEvent instance = new KeyboardChangeEvent();
    private KeyboardChangeEvent() {}
    public static KeyboardChangeEvent getInstance() {
        return instance;
    }

    public void change(int pressedKeyIndex, Color pressedKeyColor) {
        this.setChanged();
        this.notifyObservers(new Object[]{pressedKeyIndex, pressedKeyColor});
    }



}
