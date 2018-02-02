package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Observable object to track keyboard events and notify visual keyboard for color highlighting
 */
public class KeyboardChangeEvent extends Observable {

    public enum EventCode {KEY_PRESSED, KEY_RELEASED}

    public static final int KEY_EVENT_TYPE = 0;
    public static final int PRESSED_KEY_INDEX = 1;
    public static final int PRESSED_KEY_COLOR = 2;

    private static KeyboardChangeEvent instance = new KeyboardChangeEvent();
    private KeyboardChangeEvent() {}
    public static KeyboardChangeEvent getInstance() {
        return instance;
    }

    public void change(EventCode eventCode, int pressedKeyIndex, Color pressedKeyColor) {
        this.setChanged();
        this.notifyObservers(new Object[]{eventCode, pressedKeyIndex, pressedKeyColor});
    }



}
