package hsrm.eibo.mediaplayer.Core.Model;


public class Metadata {

    private String title;
    private String album;
    private String interpreteur;
    private int year;
    private String genre;
    private float length;
    private float bitrate;

    public Metadata(String title, String album, String interpreteur,
                    int year, String genre, float length, float bitrate) {
        this.title = title;
        this.album = album;
        this.interpreteur = interpreteur;
        this.year = year;
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
        return interpreteur;
    }

    public int getYear() {
        return year;
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
            ((Metadata) obj).getArtist().equals(this.interpreteur) &&
            ((Metadata) obj).getGenre().equals(this.genre) &&
            ((Metadata) obj).getYear() == this.year &&
            ((Metadata) obj).getLength() == this.length &&
            ((Metadata) obj).getBitrate() == this.bitrate)
        {
            return true;
        }

        return false;
    }

    // TODO: Override HashCode method
}
