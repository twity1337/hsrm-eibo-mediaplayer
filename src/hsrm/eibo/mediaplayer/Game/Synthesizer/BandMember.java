package hsrm.eibo.mediaplayer.Game.Synthesizer;

import com.sun.javafx.collections.ObservableSetWrapper;
import javafx.collections.ObservableSet;
import javafx.scene.input.KeyEvent;

import javax.sound.midi.Instrument;
import java.util.HashSet;
import java.util.Observable;

public class BandMember extends Observable{
    private Instrument instrument;
    private int instrumentBankId;
    private HashSet<Integer> set = new HashSet<>();
    private ObservableSet<Integer> pressedKeys = new ObservableSetWrapper<>(set);
    private String id;

    public BandMember(String id, int instrumentBankId){
        this.id = id;
        this.instrumentBankId = instrumentBankId;
        this.instrument = SynthesizerManager.getInstance().getInstrumentHashMap().get(instrumentBankId);
    }

    public BandMember(String id){
        this(id, 0);
    }

    //add Observerpattern
    public void addPressedKeys(KeyEvent k){
        pressedKeys.add(Keyboard.LAYOUT.indexOf(k.getCharacter()));// smth like that
        this.setChanged();
    }

    public void removePressedKey(KeyEvent k){
        pressedKeys.remove(Keyboard.LAYOUT.indexOf(k.getCharacter()));
        this.setChanged();
    }

    public void setInstrument(Instrument i){
        this.instrument = i;
        this.setChanged();
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public String getId() {
        return id;
    }

    public int getInstrumentBankId(){return instrumentBankId;}

}
