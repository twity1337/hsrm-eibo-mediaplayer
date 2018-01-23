package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;

/**
 * class to generate MIDI sound
 */
public class SynthesizerManager {
    public final static int BASIC_C_NOTE = 60;
    public final static int BASIC_PITCH = 600;

    private static SynthesizerManager instance = new SynthesizerManager();
    private Synthesizer synthesizer;
    private Map<String,MidiChannel> midiChannels;
    private int maxMidiChannels;

    public static SynthesizerManager getInstance() {
        return instance;
    }

    private SynthesizerManager() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        maxMidiChannels = synthesizer.getChannels().length;
        midiChannels = new HashMap<>(maxMidiChannels);
        Keyboard.init();
    }

    public Map<Integer,Instrument> getInstrumentHashMap(){
        Map<Integer,Instrument> map = new HashMap<Integer, Instrument>(){
                @Override
                public String toString() {
                    StringBuilder s = new StringBuilder();
                    for (Integer index: this.keySet()){
                        String key =index.toString();
                        String value = this.get(index).toString();
                        s.append("\n" + key + " " + value);
                    }
                    return s.toString();
                }
            };
        Instrument[] instruments = synthesizer.getAvailableInstruments();
        int index = 0;
        for (Instrument i : instruments)
            map.put(index++, i);
        return map;
    }
    //on BandMemberHashMap change
    public void occupyChannel(String id){
        for (int i=0; i < maxMidiChannels; i++)
        {
            if (midiChannels.containsValue(synthesizer.getChannels()[i])){
                i++;
            } else {
                midiChannels.put(id,synthesizer.getChannels()[i]);
                midiChannels.get(id).programChange(Band.getInstance().getBandMemberByID(id).getInstrumentBankId());
                return;
            }
        }
        System.out.println("Maximale Anzahl Spieler erreicht");
    }

    public void playNote(String id, int o){
        if (Keyboard.ORDINAL_LAYOUT.containsKey(o))
            midiChannels.get(id).noteOn(calculateNote(Keyboard.ORDINAL_LAYOUT.get(o)), BASIC_PITCH);
    }

    public void stopNote(String id, int o){
        if (Keyboard.ORDINAL_LAYOUT.containsKey(o))
            midiChannels.get(id).noteOff(calculateNote(Keyboard.ORDINAL_LAYOUT.get(o)), BASIC_PITCH);
    }

    private int calculateNote(int index){
        return BASIC_C_NOTE+index;
    }
}
