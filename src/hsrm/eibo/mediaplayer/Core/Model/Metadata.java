package hsrm.eibo.mediaplayer.Core.Model;


import org.apache.commons.collections4.map.UnmodifiableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Objects of class Metadata contain metadata information (as String) of media file.
 * E.g.: title, name of album, artist, length and many more.
 */
public class Metadata {

    /**
     * All information is saved in a HashMap
     */
    private Map<String, String> metadataMap = new HashMap<>();

    public Metadata(String title, String album, String artist,
                    String year, String genre, double length, double bitrate) {
        this.metadataMap.put("title", title);
        this.metadataMap.put("album", album);
        this.metadataMap.put("artist", artist);
        this.metadataMap.put("year", year);
        this.metadataMap.put("genre", genre);
        this.metadataMap.put("length", String.valueOf(length));
        this.metadataMap.put("bitrate", String.valueOf(bitrate));
        this.metadataMap = Collections.unmodifiableMap(this.metadataMap);
    }

    public Map<String, String> getMetadataMap()
    {
        return this.metadataMap;
    }

    public String getTitle() {
        return this.metadataMap.get("title");
    }

    public String getAlbum() {
        return this.metadataMap.get("title");
    }

    public String getArtist() {
        return this.metadataMap.get("artist");
    }

    public String getDate() {
        return this.metadataMap.get("date");
    }

    public String getGenre() {
        return this.metadataMap.get("genre");
    }

    public double getLength() {
        return Double.parseDouble(this.metadataMap.get("length"));
    }

    public double getBitrate() {
        return Double.parseDouble(this.metadataMap.get("bitrate"));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Metadata ))
            return false;

        if(obj == this)
            return true;

        return this.metadataMap.equals(obj);
    }

    @Override
    public String toString()
    {
        return this.metadataMap.toString();
    }

    @Override
    public int hashCode() {
        return this.metadataMap.hashCode();
    }
}
