package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistException;
import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Exception.TrackUnsupportedFileTypeException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Playlist contains model.Track objects,
 * a given name (or empty), and a file path of origin (or empty).
 */
public class Playlist extends ArrayList<Track> implements MediaListElementInterface {
    /**
     * name of playlist
     */
    private String name;
    /**
     * file path to playlist file (playlist was loaded from)
     */
    private String location;

    /**
     * Constructor to create (load from file) playlist containing Track object(s)
     * @param location file path to playlist file
     * @param trackPaths of media file(s)
     * @throws PlaylistIOException on file IO operation error
     * @throws TrackUnsupportedFileTypeException on opening unsupported file type
     */
    public Playlist(String location, String[] trackPaths)
            throws PlaylistIOException, TrackUnsupportedFileTypeException
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
            }
        }

        if(exception != null)
            throw exception;
    }

    /**
     * Constructor to create new playlist containing Track objects
     * @param trackPaths of Track object(s)
     * @throws PlaylistIOException on file IO operation error
     * @throws TrackUnsupportedFileTypeException on opening unsupported file type
     */
    public Playlist(String... trackPaths) throws PlaylistIOException, TrackUnsupportedFileTypeException
    {
        this(null, trackPaths);
    }

    /**
     * Method to return name of playlist.
     * @return String name of playlist
     */
    public String getName() {
        return name;
    }

    /**
     * Method to name playlist
     * @param name playlist will be named
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to return file path of playlist file.
     * @return String location of playlist file in filesystem
     */
    public String toString()
    {
        if(this.name == null || this.name.isEmpty())
            return this.location;
        return this.name;
    }
}
