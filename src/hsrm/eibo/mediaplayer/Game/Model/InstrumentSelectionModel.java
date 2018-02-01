package hsrm.eibo.mediaplayer.Game.Model;

import java.util.HashMap;
import java.util.Map;

public class InstrumentSelectionModel {

    private static Map<Integer, String> collectedInstrumentList = new HashMap<>();

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
        collectedInstrumentList.put(instrumentId, title);
    }

    public static String getInstrumentTitleById(int instrumentId)
    {
        return collectedInstrumentList.get(instrumentId);
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
