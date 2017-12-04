package hsrm.eibo.mediaplayer.Core.Model;


public class Metadata {

    private String title;
    private String album;
    private String artist;
    private String date;
    private String genre;
    private float length;
    private float bitrate;

    public Metadata(String title, String album, String artist,
                    String year, String genre, float length, float bitrate) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.date = year;
        this.genre = genre;
        this.length = length;
        this.bitrate = bitrate;
    }

    public Metadata() {
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getDate() {
        return date;
    }

    public String getGenre() {
        return genre;
    }

    public float getLength() {
        return length;
    }

    public float getBitrate() {
        return bitrate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Metadata ))
            return false;

        if(obj == this)
            return true;

        if(((Metadata) obj).getTitle().equals(this.title) &&
                ((Metadata) obj).getAlbum().equals(this.album) &&
                ((Metadata) obj).getArtist().equals(this.artist) &&
                ((Metadata) obj).getGenre().equals(this.genre) &&
                (((Metadata) obj).getDate() == this.date) &&
                (((Metadata) obj).getLength() == this.length) &&
                (((Metadata) obj).getBitrate() == this.bitrate))
        {
            return true;
        }

        return false;
    }

    public String toString()
    {
        String t = ", ";
        return (title+t+album+t+artist+t+date+t+genre);
    }

    // TODO: Override HashCode method
}
