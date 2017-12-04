package hsrm.eibo.mediaplayer;

import hsrm.eibo.mediaplayer.Core.Exception.PlaylistIOException;
import hsrm.eibo.mediaplayer.Core.Model.Playlist;
import hsrm.eibo.mediaplayer.Core.Util.MediaUtil;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class PlaylistManager extends ArrayList<Playlist>{
    private static PlaylistManager instance = new PlaylistManager();


    public static PlaylistManager getInstance() {
        return instance;
    }


    private PlaylistManager(){}

    public void loadPlaylistFromFile(File playlistFile) throws PlaylistIOException
    {
        ParseM3U parser = new ParseM3U();
        parser.setFile(playlistFile);
        parser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    PlaylistManager.getInstance().add(
                            new Playlist((String[])event.getSource().getValue()));
                } catch (PlaylistIOException e) {
                    e.printStackTrace();
                    //TODO: notify user that loading failed
                }
            }
        });
    }

    public void savePlaylistToFile(Playlist playlist){
        //TODO: fill me i'm a stub
    }

    public Playlist getPlaylist(String name)
    {
        for (Playlist playlist:this){
            if(playlist.getName().equals(name))
                return playlist;
        }
        return null;
    }

    //TODO: make extended m3u compatible
    private class ParseM3U extends Service<String[]>
    {
        private File _file;
        public final void setFile(File file){this._file = file;}

        @Override
        protected Task<String[]> createTask() {
            ArrayList<String> buffer = new ArrayList<>();
            return new Task<String[]>() {
                @Override
                protected String[] call() throws Exception {
                    String[] list;
                    BufferedReader reader = null;
                    try
                    {
                        String line;
                        reader = new BufferedReader(new FileReader(_file));
                        while((line = reader.readLine())!= null)
                            buffer.add(line);
                    } finally {
                        reader.close();
                    }
                    list = buffer.toArray(new String[0]);
                    return list;
                }
            };
        }
    }
}
