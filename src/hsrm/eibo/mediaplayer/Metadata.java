package hsrm.eibo.mediaplayer;


public class Metadata {

    private String title;
    private String album;
    private String interpreteur;
    private int year;
    private String genre;
    private String fileName;
    private String filePath;
    private float length;
    private float bitrate;

    public Metadata(String title, String album, String interpreteur,
                    int year, String genre, String fileName,
                    String filePath, float length, float bitrate) {
        this.title = title;
        this.album = album;
        this.interpreteur = interpreteur;
        this.year = year;
        this.genre = genre;
        this.fileName = fileName;
        this.filePath = filePath;
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

    public String getInterpreteur() {
        return interpreteur;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public float getLength() {
        return length;
    }

    public float getBitrate() {
        return bitrate;
    }

}
