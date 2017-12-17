package hsrm.eibo.mediaplayer.Core.Util;

import java.io.File;

/**
 * Utility class for File IO purposes
 */
public class FileIOUtil {

    /**
     * Method to get file path(s) of given file(s)
     * @param files to get paths to
     * @return path(s) to given file(s)
     */
    public static String[] extractFilePaths(File... files)
    {
        String[] paths = new String[files.length];
        for (int i = 0; i<paths.length; i++) {
            paths[i] = files[i].getPath();
        }
        return paths;
    }
}
