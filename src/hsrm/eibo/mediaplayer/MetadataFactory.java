package hsrm.eibo.mediaplayer;


import jdk.internal.org.xml.sax.SAXException;
import org.apache.tika.example.ParsingExample;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MetadataFactory {
    //or Util wathever

    //need method for list creation out of one string/list of strings with no prior list/add to existing list
    public static List<Metadata> createMetadataList(String[] uris, List<Metadata> playlist, int index)
        //throws IOException, SAXException, TikaException
    {
        Metadata dataToAdd;
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata tikiMetadata;

        for(String uri : uris)
        {
            tikiMetadata = new org.apache.tika.metadata.Metadata();
            try(InputStream stream = ParsingExample.class.getResourceAsStream(uri))
            {
                parser.parse(stream, handler, tikiMetadata);
            } catch (Exception e) {
                //HANDLE THIS !!
            }
            dataToAdd = new Metadata(
                tikiMetadata.get("title"),
                tikiMetadata.get("album"),
                tikiMetadata.get("interpret"),
                Integer.parseInt(tikiMetadata.get("year")),
                tikiMetadata.get("genre"),
                tikiMetadata.get("filename"),
                tikiMetadata.get("filepath"),
                Float.parseFloat(tikiMetadata.get("length")),
                Float.parseFloat(tikiMetadata.get("bitrate"))
            );
            if(dataToAdd != null)
            {
                playlist.add(index, dataToAdd);
                index++;
            }
        }
        return playlist;
    }

    public static List<Metadata> createMetadataList(String[] uris, List<Metadata> playlist)
    {
        return createMetadataList(uris, playlist, playlist.size());
    }

    public static List<Metadata> createMetadataList(String[] uris)
    {
        List<Metadata> playlist = new ArrayList<Metadata>();
        return createMetadataList(uris, playlist);
    }

    public static List<Metadata> createMetadataList(String uri)
    {
        String[] uris = new String[] {uri};
        return createMetadataList(uris);
    }
}
