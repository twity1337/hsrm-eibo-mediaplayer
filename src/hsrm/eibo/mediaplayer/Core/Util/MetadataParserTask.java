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
 * Task to provide threaded metadata parsing
 */
public class MetadataParserTask extends Task<Metadata>{

    /**
     * Name of Parser thread
     */
    public static final String METADATA_PARSER_THREAD_NAME = "Metadata Parser Thread";

    /**
     * List of Parsers: determines filetypes which can be parsed.
     */
    private static final Parser[] AUTO_DETECT_EXCEPTED_PARSERS = {
            new AudioParser(),
            new MidiParser(),
            new Mp3Parser()
    };

    /**
     * reference to media file
     */
    private String filePathOfMediaFile;

    /**
     * Constructor creates parser task with file path to file
     * @param filePathOfMediaFile path of file to load
     */
    public MetadataParserTask(String filePathOfMediaFile) {
        this.filePathOfMediaFile = filePathOfMediaFile;
    }

    /**
     * Method to get artist information as String. Differnet tags can beset
     * @param m source of metadata
     * @return name of srtist, null if no information in media file
     */
    private String getArtist(org.apache.tika.metadata.Metadata m)
    {
        String a;
        if ((a=m.get("creator"))!=null
                || (a=m.get("meta:author"))!=null
                || (a=m.get("xmpDM:artist"))!=null)
            ;
        return a;
    }

    /**
     * Method to start thread of Task object, creates an tika.metadata object to read data from
     * @return metadata as Model.Metadata object
     * @throws Exception
     */
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
