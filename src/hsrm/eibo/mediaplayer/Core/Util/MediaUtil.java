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
    public static ArrayList<MediaPlayer> generateMediaplayerList(Playlist playlist) {
        ArrayList<MediaPlayer> playerList = new ArrayList<>();

        for (int i = 0; i < playlist.size(); i++) {
            playerList.add(playlist.get(i).getTrackMediaPlayer());
        }

        return playerList;
    }

    /**
     * method to generate list of length n with randomly order numbers 0,...,(n-1)
     *
     * @param length of integer array
     * @return integer array with length n and numbers 0,...,(n-1)
     */
    public static int[] generateShuffelList(int length) {
        int[] randomizedList = new int[length];

        for (int i = 1; i < length; i++) {
            int index = ((int) (Math.random() * length));
            while (randomizedList[index] != 0) index = (index + 1) % length;
            randomizedList[index] = i;
        }
        return randomizedList;
    }
}

