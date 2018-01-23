package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.beans.property.SimpleBooleanProperty;

public class KeyboardListener {
    private SimpleBooleanProperty active;
    private static KeyboardListener ourInstance = new KeyboardListener();

    public static KeyboardListener getInstance() {
        return ourInstance;
    }

    private KeyboardListener() {
        active = new SimpleBooleanProperty(false);
    }

    public boolean isActive() {
        return active.get();
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
}
