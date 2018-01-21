package hsrm.eibo.mediaplayer.Game.Model;

public class InstrumentSelectionModel {

    /**
     * The instrument title
     */
    private String title;

    /**
     * The midi instrument id.
     */
    private int instrumentId;

    public InstrumentSelectionModel(String title, int instrumentId) {
        this.title = title;
        this.instrumentId = instrumentId;
    }

    public String getTitle() {
        return title;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
