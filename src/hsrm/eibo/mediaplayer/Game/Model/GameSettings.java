package hsrm.eibo.mediaplayer.Game.Model;

/**
 * Class for keeping settings of a currently running game.
 * Usually set by a "New game"- or "Join Game"-option form.
 */
public class GameSettings {


    /**
     * The player name of the current player.
     */
    private String playerName;

    /**
     * The players selected instrument.
     */
    private String instrumentTitle;

    /**
     * The midi instrument id of the selected instrument.
     */
    private int instrumentId;

    /**
     * The mp3 path for the track to play along.
     */
    private String backgroundSongPath;


    public GameSettings() {
    }

    public GameSettings(String playerName, String instrumentTitle, int instrumentId, String backgroundSongPath) {
        this.playerName = playerName;
        this.instrumentTitle = instrumentTitle;
        this.instrumentId = instrumentId;
        this.backgroundSongPath = backgroundSongPath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getInstrumentTitle() {
        return instrumentTitle;
    }

    public void setInstrumentTitle(String instrumentTitle) {
        this.instrumentTitle = instrumentTitle;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getBackgroundSongPath() {
        return backgroundSongPath;
    }

    public void setBackgroundSongPath(String backgroundSongPath) {
        this.backgroundSongPath = backgroundSongPath;
    }
}
