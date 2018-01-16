package hsrm.eibo.mediaplayer.Game.Synthesizer;

import com.sun.javafx.collections.ObservableSetWrapper;
import javafx.collections.ObservableSet;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.sound.midi.Instrument;
import java.util.HashSet;
import java.util.Observable;

public abstract class BandMember{
    private Instrument instrument;
    private int instrumentBankId;
    private HashSet<KeyCode> pressedKeys = new HashSet<>();
    private String id;
    private SynthesizerManager synthInstance;

    public BandMember() {
        synthInstance = SynthesizerManager.getInstance();
    }

 /*   public void addPressedKeys(KeyCode k){
        pressedKeys.add(Keyboard.LAYOUT.indexOf(k));
    }

    public void removePressedKey(KeyCode k){
        pressedKeys.remove(Keyboard.LAYOUT.indexOf(k.getCharacter()));
    }*/

    public void keyPressed(KeyCode k){
        if (pressedKeys.contains(k))
            return;
        pressedKeys.add(k);
        synthInstance.playNote(this.id,k.ordinal());
    }

    public void keyReleased(KeyCode k){
        pressedKeys.remove(k);
        synthInstance.stopNote(this.id,k.ordinal());
    }

    public void setInstrumentBankId(int bankId){
        this.instrumentBankId = bankId;
        this.instrument = this.synthInstance.getInstrumentHashMap().get(instrumentBankId);
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getInstrumentBankId(){return instrumentBankId;}

}
