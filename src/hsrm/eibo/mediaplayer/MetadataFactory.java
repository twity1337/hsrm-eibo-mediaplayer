package hsrm.eibo.mediaplayer;


import hsrm.eibo.mediaplayer.Core.Model.Metadata;
//import org.apache.tika.example.ParsingExample;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MetadataFactory {
    //or Util wathever

    //need method for list creation out of one URI/list of URIs with no prior list/add to existing list
    public static ArrayList<Metadata> createMetadataList(URI[] uris, ArrayList<Metadata> playlist, int index)
        //throws IOException, SAXException, TikaException
    {
        Metadata dataToAdd;
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata tikaMetadata;

        for(URI uri : uris)
        {
            tikaMetadata = new org.apache.tika.metadata.Metadata();
            File file = new File(uri.getPath());
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
                uri.getPath(),
                uri.getPath(),
                Float.parseFloat(tikaMetadata.get("xmpDM:duration")),
                Float.parseFloat(tikaMetadata.get("xmpDM:audioSampleRate"))
            );
            //TODO: equal empty
            if(dataToAdd != null)
            {
                playlist.add(index, dataToAdd);
                index++;
            }
        }
        return playlist;
    }

    public static ArrayList<Metadata> createMetadataList(URI[] uris, ArrayList<Metadata> playlist)
    {
        return createMetadataList(uris, playlist, playlist.size());
    }

    public static ArrayList<Metadata> createMetadataList(URI[] uris)
    {
        ArrayList<Metadata> playlist = new ArrayList<Metadata>();
        return createMetadataList(uris, playlist);
    }

    public static ArrayList<Metadata> createMetadataList(URI uri)
    {
        URI[] uris = new URI[] {uri};
        return createMetadataList(uris);
    }
}
