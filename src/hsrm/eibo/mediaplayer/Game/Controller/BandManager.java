package hsrm.eibo.mediaplayer.Game.Controller;

import com.sun.javafx.collections.ObservableListWrapper;
import hsrm.eibo.mediaplayer.Game.Model.BandmemberModel;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class BandManager {
    private static BandManager instance = new BandManager();
    private ObservableList<BandmemberModel> band;

    private BandManager()
    {
        band = new ObservableListWrapper<BandmemberModel>(new LinkedList<BandmemberModel>());
    }

    public static BandManager getInstance(){return instance;}

    public void addBandmember(BandmemberModel bandmember){
        band.add(bandmember);
    }

    public void removeBandmember(BandmemberModel bandmember){
        band.remove(bandmember);
    }

    public BandmemberModel getBandmemberByName(String name){
        for (BandmemberModel member : band){
            if (member.getName().equals(name))
                return member;
        }
        return null;
    }

    public ObservableList<BandmemberModel> getBand(){return band;}
}
