package hsrm.eibo.mediaplayer.Core.Util;

import java.io.File;

public class FileIOUtil {

    public static String[] extractFilePaths(File[] files)
    {
        String[] paths = new String[files.length];
        for (int i = 0; i<paths.length; i++) {
            paths[i] = files[i].getPath();
        }
        return paths;
    }
}
