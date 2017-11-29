package hsrm.eibo.mediaplayer;


import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import org.apache.tika.example.ParsingExample;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

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
        org.apache.tika.metadata.Metadata tikaMetadata;

        for(String uri : uris)
        {
            tikaMetadata = new org.apache.tika.metadata.Metadata();
            try(InputStream stream = ParsingExample.class.getResourceAsStream(uri))
            {
                parser.parse(stream, handler, tikaMetadata);
            } catch (Exception e) {
                //TODO: HANDLE THIS !!
            }
            dataToAdd = new Metadata(
                tikaMetadata.get("title"),
                tikaMetadata.get("album"),
                tikaMetadata.get("interpret"),
                Integer.parseInt(tikaMetadata.get("year")),
                tikaMetadata.get("genre"),
                tikaMetadata.get("filename"),
                tikaMetadata.get("filepath"),
                Float.parseFloat(tikaMetadata.get("length")),
                Float.parseFloat(tikaMetadata.get("bitrate"))
            );
            if(dataToAdd. != null)
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
