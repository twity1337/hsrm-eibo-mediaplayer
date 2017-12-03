package hsrm.eibo.mediaplayer.Core.Model;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

public class Playlist extends ArrayList<Track>{
    private String name;
    private String location;


    public Playlist(String location, String[] stringList)
            throws IOException, SAXException, TikaException
    {
        this.location = location;
        for(String trackPath : stringList)
            super.add(new Track(trackPath));
    }

    public Playlist(String[] stringList)
            throws IOException, SAXException, TikaException
    {this(null, stringList);}

    public Playlist(Track...tracksToAdd)
    {
        for (Track trackToAdd : tracksToAdd)
            super.add(trackToAdd);
    }

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
