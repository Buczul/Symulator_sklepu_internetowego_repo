package productPCG;

public class Ebook extends Book{
        private String fileFormat;
        private double fileSizeMB;
        private String downloadLink;

    public Ebook(String title, String author, double price,
                 BookCategory category, String fileFormat,
                 double fileSizeMB, String downloadLink) {
        super(title, author, price, category);
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
        this.downloadLink = downloadLink;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
