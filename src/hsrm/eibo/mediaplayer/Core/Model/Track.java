package hsrm.eibo.mediaplayer.Core.Model;

import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Track {

    private Metadata metadata;
    private String path;

    /**
     *
     * @param path
     */
    public Track(String path) throws IOException
    {/*
        this.path = path;
        //TODO: überprüfung heir überflüssig?
        if(path != null)
            this.metadata = MediaUtil.createMetadata(this.path);*/
    }

    public Metadata getMetadata()
    {
        return this.metadata;
    }

    public MediaPlayer getTrackMediaPlayer()
    {
        return new MediaPlayer(new Media(this.getUri()));
    }

    public String getPath() {
        return path;
    }

    private String getUri()
    {
        return ("file:///" + path.replace("\\", "/")).replace(" ", "%20");
    }
}
