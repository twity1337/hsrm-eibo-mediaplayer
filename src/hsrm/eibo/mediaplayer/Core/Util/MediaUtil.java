package hsrm.eibo.mediaplayer.Core.Util;


import hsrm.eibo.mediaplayer.Core.Model.Metadata;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.PlaylistManager;
import javafx.scene.media.MediaPlayer;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.audio.MidiParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;

public class MediaUtil {
    //or MediaUtil wathever
    private static final Parser[] AUTO_DETECT_EXCEPTED_PARSERS = {
            new AudioParser(),
            new MidiParser(),
            new Mp3Parser()
    };
    //need method for list creation out of one URI/list of URIs with no prior list/add to existing list
    public static Metadata createMetadata(String path)
        //TODO: throws IOException, SAXException, TikaException
    {
        Metadata dataToAdd;
        AutoDetectParser parser = new AutoDetectParser(AUTO_DETECT_EXCEPTED_PARSERS);
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

        // TODO: Handle empty (not existing) metadata.
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

    public static void loadPlaylistFromFile(File file) throws IOException
    {
        PlaylistManager.getInstance().add(new Playlist(parseM3u(file)));
    }

    public static ArrayList<MediaPlayer> generateMediaplayerList(Playlist playlist) {
        ArrayList<MediaPlayer> playerList = new ArrayList<>();

        for(int i = 0; i<playlist.size();i++)
        {
            playerList.add(playlist.get(i).getTrackMediaPlayer());
        }

        return playerList;
    }

    /**
     * method to generate list of length n with randomly order numbers 0,...,(n-1)
     * @param length of integer array
     * @return integer array with length n and numbers 0,...,(n-1)
     */
    public static int[] generateShuffelList(int length) {
        int[] randomizedList = new int[length];

        for(int i = 1; i < length; i++)
        {
            int index = ((int)(Math.random()*length));
            while (randomizedList[index]!=0) index=(index+1)%length;
            randomizedList[index] = i;
        }
        return randomizedList;
    }

    //TODO: make extended m3u compatible
    // TODO: Make "private"
    public static String[] parseM3u(File file) throws IOException
    {
        String[] list;
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<String> buffer = new ArrayList<>();

        try
        {
            while((line = reader.readLine())!= null)
                buffer.add(line);
        } finally {
            reader.close();
        }
        list = buffer.toArray(new String[0]);
        return list;
    }
}
