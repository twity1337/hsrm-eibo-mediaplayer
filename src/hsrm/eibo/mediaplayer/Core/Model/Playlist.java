package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistException;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Exception.TrackUnsupportedFileTypeException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

public class Playlist extends ArrayList<Track> implements MediaListElementInterface {
    private String name;
    private String location;


    public Playlist(String location, String[] trackPaths)
            throws PlaylistIOException
    {
        this.location = location;
        PlaylistIOException exception = null;
        for(String trackPath : trackPaths)
        {
            try {
                super.add(new Track(trackPath));
            } catch (IOException e){
                if (exception == null)
                    exception = new PlaylistIOException();
                exception.addFailedFilePath(trackPath);
            } catch (TrackUnsupportedFileTypeException e)
            {
                System.err.println("ERROR: Error loading file: " + e.getMessage());
            }
        }
        if(exception != null)
            throw exception;
    }

    public Playlist(String... trackPaths) throws PlaylistIOException
    {
        this(null, trackPaths);
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


    public String toString()
    {
        if(this.name == null || this.name.isEmpty())
            return this.location;
        return this.name;
    }
}
