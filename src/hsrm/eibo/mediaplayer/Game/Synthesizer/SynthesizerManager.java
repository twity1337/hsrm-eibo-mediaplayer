package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javax.sound.midi.*;
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
            System.out.println(i.toString());
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
                midiChannels.get(id).programChange(Band.getInstance().getBandMemberByID(id).getInstrumentBankId());
                return;
            }
        }
        System.out.println("Maximale Anzahl Spieler erreicht");
    }
    //Testing reasons
    public void playNote(String id, BandMember bm){
            midiChannels.get(id).noteOn(60,600);
            midiChannels.get(id).noteOn(70,600);
    }
}
