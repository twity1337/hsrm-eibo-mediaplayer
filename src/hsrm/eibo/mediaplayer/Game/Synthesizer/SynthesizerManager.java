package hsrm.eibo.mediaplayer.Game.Synthesizer;

public class SynthesizerManager {
    private static SynthesizerManager ourInstance = new SynthesizerManager();

    public static SynthesizerManager getInstance() {
        return ourInstance;
    }

    private SynthesizerManager() {
    }
}
