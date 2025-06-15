package productPCG;

public class Audiobook extends Book{
    private int durationMinutes;
    private String narrator;
    private String audioFormat;
    private String studioName;

    public Audiobook(String title, String author, double price,
                     BookCategory category, int durationMinutes,
                     String narrator, String audioFormat, String studioName) {
        super(title, author, price, category);
        this.durationMinutes = durationMinutes;
        this.narrator = narrator;
        this.audioFormat = audioFormat;
        this.studioName = studioName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }
}
