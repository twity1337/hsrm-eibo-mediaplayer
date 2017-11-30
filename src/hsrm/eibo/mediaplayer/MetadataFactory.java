package hsrm.eibo.mediaplayer;


import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MetadataFactory {
    //or Util wathever

    //need method for list creation out of one URI/list of URIs with no prior list/add to existing list
    public static Metadata createMetadata(String path)
        //throws IOException, SAXException, TikaException
    {
        Metadata dataToAdd;
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata tikaMetadata;

        tikaMetadata = new org.apache.tika.metadata.Metadata();
        File file = new File(path);
        try(InputStream stream = new FileInputStream(file))
        {
            parser.parse(stream, handler, tikaMetadata);
        } catch (Exception e) {
            // TODO: Richtig machen!
            throw new RuntimeException(e);
        }
        dataToAdd = new Metadata(
            tikaMetadata.get("title"),
            tikaMetadata.get("xmpDM:album"),
            tikaMetadata.get("interpret"),
            Integer.parseInt(tikaMetadata.get("xmpDM:releaseDate")),
            tikaMetadata.get("xmpDM:genre"),
            Float.parseFloat(tikaMetadata.get("xmpDM:duration")),
            Float.parseFloat(tikaMetadata.get("xmpDM:audioSampleRate"))
        );
        //TODO: equal empty
        return dataToAdd;
    }
}
