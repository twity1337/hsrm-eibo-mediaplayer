package hsrm.eibo.mediaplayer.Game.Synthesizer;

import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    // Layout für Klavier: start bei Taste y= Ton c (bass 2. Zwischenraum), Halbtonschritte;
    // Taste a = Ton c (violin Schlüssel 1. Hilfslinie), Halbtonschritte
    public static final Map<Integer,Integer> LAYOUT = new HashMap<>();
    private static final int[] ordinalLayout = new int[]{60,54,59,39,38,57,42,37,43,49,45,48,52,26,58,27,40,53,29,55,30,61,31,56,44,33,50,24,51};//"ysxdcvgbhnjmq2w3er5t6zui9o0p";
    public static void init(){
        int i=0;
        for (int o : ordinalLayout){
            LAYOUT.put(ordinalLayout[i],i);
            i++;
        }
    }
}
