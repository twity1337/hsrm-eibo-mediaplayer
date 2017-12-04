package hsrm.eibo.mediaplayer.Core.Model;

import java.util.ArrayList;

public class Playlist extends ArrayList<Track>{
    private String name;

    private String location;


    public Playlist(String location, String[] trackPaths) {
        this.location = location;
        for(String trackPath : trackPaths)
            super.add(new Track(trackPath));
    }

    public Playlist(String[] trackPaths){this(null, trackPaths);}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return location of playlist file in filesystem
     */
    public String getLocation() {
        return location;
    }
}
