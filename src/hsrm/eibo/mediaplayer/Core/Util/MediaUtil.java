package hsrm.eibo.mediaplayer.Core.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utility class containing methods for media file handling and MediaController
 */
public class MediaUtil {

    public static int FILETYPE_GID_AUDIO = 1;
    public static int FILETYPE_GID_VIDEO = 1<<1;
    public static int FILETYPE_GID_PLAYLIST = 1<<2;


    private static String[][] supportedFiletype_managingArray = {
        {}, // GID 0 : not used
        {"*.mp3","*.mp4", "*.wav"}, // GID 1 : Audio
        {"*.mp4","*.mkv"}, // GID 2 : Video
        {}, // GID 3 : not used
        {"*.m3u"}, // GID 4 : Playlist
    };


    public static String[] getSupportedFileTypes(int gid)
    {
        ArrayList<String> retVal = new ArrayList<>();
        if((gid & FILETYPE_GID_AUDIO) != 0)
            retVal.addAll(Arrays.asList(supportedFiletype_managingArray[FILETYPE_GID_AUDIO]));
        if((gid & FILETYPE_GID_VIDEO) != 0)
            retVal.addAll(Arrays.asList(supportedFiletype_managingArray[FILETYPE_GID_VIDEO]));
        if((gid & FILETYPE_GID_PLAYLIST) != 0)
            retVal.addAll(Arrays.asList(supportedFiletype_managingArray[FILETYPE_GID_PLAYLIST]));
        return retVal.toArray(new String[] {});
    }

    /**
     * Method to generate list with length n in random order: 0,...,(n-1)
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

