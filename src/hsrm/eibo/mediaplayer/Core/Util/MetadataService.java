package hsrm.eibo.mediaplayer.Core.Util;

import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.audio.MidiParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * service to provide threaded metadata parsing
 */
public class MetadataService extends Service<Metadata>{

    private static final Parser[] AUTO_DETECT_EXCEPTED_PARSERS = {
            new AudioParser(),
            new MidiParser(),
            new Mp3Parser()
    };

    private String path;

    public final void setPath(String path) {this.path=path;}

    public final String getPath(){return this.path;}

    private final String getArtist(org.apache.tika.metadata.Metadata m)
    {
        String a;
        if ((a=m.get("creator"))!=null
                || (a=m.get("meta:author"))!=null
                || (a=m.get("xmpDM:artist"))!=null)
            ;
        return a;
    }

    @Override
    protected Task createTask() {
        final String _path = getPath();
        if (_path == null)
            return null;
        return new Task() {
            @Override
            protected Object call()
                    throws IOException, SAXException, TikaException {
                Metadata dataToAdd;
                AutoDetectParser parser = new AutoDetectParser(AUTO_DETECT_EXCEPTED_PARSERS);
                BodyContentHandler handler = new BodyContentHandler();
                org.apache.tika.metadata.Metadata tikaMetadata;
                tikaMetadata = new org.apache.tika.metadata.Metadata();
                File file = new File(_path);
                InputStream stream = new FileInputStream(file);
                parser.parse(stream, handler, tikaMetadata);
                stream.close();
                dataToAdd = new Metadata(
                        tikaMetadata.get("title"),
                        tikaMetadata.get("xmpDM:album"),
                        getArtist(tikaMetadata),
                        tikaMetadata.get("xmpDM:releaseDate"),
                        tikaMetadata.get("xmpDM:genre"),
                        Float.parseFloat(tikaMetadata.get("xmpDM:duration")),
                        Float.parseFloat(tikaMetadata.get("xmpDM:audioSampleRate"))
                );

                return null;
            }
        };
    }
}
