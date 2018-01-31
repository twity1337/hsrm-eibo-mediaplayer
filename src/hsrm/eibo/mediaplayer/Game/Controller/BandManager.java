package hsrm.eibo.mediaplayer.Game.Controller;

import com.sun.javafx.collections.ObservableListWrapper;
import hsrm.eibo.mediaplayer.Game.Model.BandMember;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class BandManager {
    private static BandManager instance = new BandManager();
    private ObservableList<BandMember> band;

    private BandManager()
    {
        band = new ObservableListWrapper<BandMember>(new LinkedList<BandMember>());
    }

    public static BandManager getInstance(){return instance;}

    public void addBandmember(BandMember bandMember){
        band.add(bandMember);
    }

    public void removeBandmember(BandMember bandMember){
        band.remove(bandMember);
    }

    public BandMember getBandmemberByName(String name){
        for (BandMember member : band){
            if (member.getName().equals(name))
                return member;
        }
        return null;
    }

    public ObservableList<BandMember> getBand(){return band;}
}
