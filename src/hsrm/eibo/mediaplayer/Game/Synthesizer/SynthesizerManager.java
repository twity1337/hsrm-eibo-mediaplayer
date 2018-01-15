package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.HashMap;

/**
 * class to observe {@link BandMember}'s and play instruments accordingly
 */
public class SynthesizerManager {
    private static SynthesizerManager instance = new SynthesizerManager();
    private Synthesizer synthesizer;
    private HashMap <String,MidiChannel> midiChannels;
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
    }

    public HashMap<Integer,Instrument> getInstrumentHashMap(){
        HashMap<Integer,Instrument> map = new HashMap<>();
        Instrument[] instruments = synthesizer.getAvailableInstruments();
        int index = 0;
        for (Instrument i : instruments)
        {
            map.put(index++,i);
        }
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
                return;
            }
        }
        System.out.println("Maximale Anzahl Spieler erreicht");
    }
    //Testing reasons
    public void playNote(String id){
        midiChannels.get(id).noteOn(60,600);
    }
}
