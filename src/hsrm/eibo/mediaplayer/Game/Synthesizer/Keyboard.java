package hsrm.eibo.mediaplayer.Game.Synthesizer;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    // Layout für Klavier: start bei Taste y= Ton c (bass 2. Zwischenraum), Halbtonschritte;
    // Taste a = Ton c (violin Schlüssel 1. Hilfslinie), Halbtonschritte
    public static final Map<Integer,Integer> ORDINAL_LAYOUT = new HashMap<>();
    public static final String[] KEYNAMES_PIANO = new String[]{"C","Cis/Des","D","Dis/Es","E","F","Fis/Ges","G","Gis/As","A","Ais/b","H","C","Cis/Des","D","Dis/Es","E","F","Fis/Ges","G","Gis/As","A","Ais/b","H","C","Cis/Des","D","Dis/Es","E"};
    public static final String[] KEYBOARD_KEYNAMES = new String[]{"Y","S","X","D","C","V","G","B","H","N","J","M","Q","2","W","3","E","R","5","T","6","Z","7","U","I","9","O","0","P"};
    public static final int NR_OF_WHITE_KEYS = 17;
    public static final int NR_OF_BLACK_KEYS = 12;
    private static int[] ordinalLayout = new int[]{60,54,59,39,38,57,42,37,43,49,45,48,52,26,58,27,40,53,29,55,30,61,31,56,44,33,50,24,51};//"ysxdcvgbhnjmq2w3er5t6zui9o0p";

    public static void init(){
        int i=0;
        for (int o : ordinalLayout){
            ORDINAL_LAYOUT.put(ordinalLayout[i],i);
            i++;
        }
    }
}
