package hsrm.eibo.mediaplayer.Core.Util;

import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import javafx.concurrent.Task;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.audio.MidiParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * service to provide threaded metadata parsing
 */

//TODO: rework to TrackService, on failure return file filePathOfMediaFile
public class MetadataParserTask extends Task<Metadata>{

    public static final String METADATA_PARSER_THREAD_NAME = "Metadata Parser Thread";

    private static final Parser[] AUTO_DETECT_EXCEPTED_PARSERS = {
            new AudioParser(),
            new MidiParser(),
            new Mp3Parser()
    };

    private String filePathOfMediaFile;

    public MetadataParserTask(String filePathOfMediaFile) {
        this.filePathOfMediaFile = filePathOfMediaFile;
    }

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
    protected Metadata call() throws Exception {
        Metadata dataToAdd;
        AutoDetectParser parser = new AutoDetectParser(AUTO_DETECT_EXCEPTED_PARSERS);
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata tikaMetadata;
        tikaMetadata = new org.apache.tika.metadata.Metadata();
        File file = new File(filePathOfMediaFile);
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

        return dataToAdd;    }
}
