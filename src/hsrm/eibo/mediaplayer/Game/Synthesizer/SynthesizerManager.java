package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;

/**
 * class to generate MIDI sound
 */
public class SynthesizerManager {
    private final static int BASIC_C_NOTE = 60;
    private final static int BASIC_PITCH = 600;

    private static SynthesizerManager instance = new SynthesizerManager();
    private Synthesizer synthesizer;
    private Map<String,MidiChannel> midiChannelKeyMap;
    private int maxMidiChannels;
    private final MidiChannel[] synthesizerChannels;

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
        midiChannelKeyMap = new HashMap<>(maxMidiChannels);
        synthesizerChannels = synthesizer.getChannels();
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

    /**
     * each syntheziser channel is used by only one player
     * @param channelKey player name as id
     * @param instrumentId Integer representing index of instrument in {@link Synthesizer.getAvailableInstruments()}
     */
    public void occupyChannel(String channelKey, int instrumentId){
        for (int i=0; i < maxMidiChannels; i++)
        {
            if (midiChannelKeyMap.containsValue(synthesizerChannels[i])){
                i++;
            } else {
                synthesizerChannels[i].programChange(instrumentId);
                midiChannelKeyMap.put(channelKey,synthesizerChannels[i]);
                return;
            }
        }
        System.out.println("Maximale Anzahl Spieler erreicht");
    }

    /**
     *
     * @param channelKey name of player as String
     * @param ordinalNote is a integer representation of pressed key on a (hardware) keyboard
     */
    public void playNote(String channelKey, int ordinalNote){
        midiChannelKeyMap.get(channelKey).noteOn(ordinalNote, BASIC_PITCH);
    }

    /**
     *
     * @param channelKey name of player as String
     * @param ordinalNote is a integer representation of pressed key on a (hardware) keyboard
     */
    public void stopNote(String channelKey, int ordinalNote){
        midiChannelKeyMap.get(channelKey).noteOff(ordinalNote, BASIC_PITCH);
    }
}
