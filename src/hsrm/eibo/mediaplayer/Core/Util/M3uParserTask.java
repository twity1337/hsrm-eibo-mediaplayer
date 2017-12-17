package hsrm.eibo.mediaplayer.Core.Util;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class to create Parser Thread for reading m3u playlist files
 * and convert them to String array
 */
public class M3uParserTask extends Task<String[]>
{
    /**
     * The Thread name for parser thread.
     */
    public static final String M3U_PARSER_THREAD_NAME = "M3U Parser Thread";
    /**
     * reference to file loaded
     */
    private File playlistFile;

    /**
     * Constructor expects file object of playlist to load
     * @param playlistFile File obejct to load
     */
    public M3uParserTask(File playlistFile) {
        this.playlistFile = playlistFile;
    }

    /**
     * Method to start Parser task
     * @return list of file paths to media files
     * @throws Exception
     */
    @Override
    protected String[] call() throws Exception {
        ArrayList<String> buffer = new ArrayList<>();
        String[] list;
        BufferedReader reader = null;
        try
        {
            String line;
            reader = new BufferedReader(new FileReader(playlistFile));
            while((line = reader.readLine())!= null)
                if (line.charAt(0)!='#')
                    buffer.add(line);
        } finally {
            reader.close();
        }
        list = buffer.toArray(new String[0]);
        return list;
    }
}
