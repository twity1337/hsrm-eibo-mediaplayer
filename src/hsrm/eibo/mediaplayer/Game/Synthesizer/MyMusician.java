package hsrm.eibo.mediaplayer.Game.Synthesizer;

public class MyMusician extends BandMember{
    private static MyMusician ourInstance = new MyMusician();

    public static MyMusician getInstance() {
        return ourInstance;
    }

    private MyMusician() {
        super();
    }
}
