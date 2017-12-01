package hsrm.eibo.mediaplayer.Core.Model;

import java.util.ArrayList;

public class Playlist extends ArrayList<Track>{
    private String name;
    private String location;


    public Playlist(String location, String[] stringList) {
        this.location = location;
        for(String trackPath : stringList)
            super.add(new Track(trackPath));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
}
