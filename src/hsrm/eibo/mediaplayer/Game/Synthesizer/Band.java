package hsrm.eibo.mediaplayer.Game.Synthesizer;

import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Observable;

/**
 * class to collect active BandMembers, handle the connection to them and notify
 * {@link SynthesizerManager} if change in active members occur
 */
public class Band extends Observable{
    /**
     * HashMap of active Bandmembers with connection ID as Key, or something
     */
    private HashMap<String, BandMember> members = new HashMap<>();
    private ObservableMap<String, BandMember> activeMembers = new ObservableMapWrapper<>(members);
    //Singleton
    private static Band instance = new Band();
    private Band(){}
    public static Band getInstance(){return instance;}

    public void addBandMember(String id,BandMember bm){
        activeMembers.put(id,bm);
        this.setChanged();
    }

    public void removeBandMember(String id){
        activeMembers.remove(id);
        this.setChanged();
    }

    public BandMember getBandMemberByID(String id){
        return this.activeMembers.get(id);
    }
}
