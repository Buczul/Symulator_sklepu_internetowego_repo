package productPCG;

public class PhysicalBook extends Book{
    private int pageCount;
    private String coverType;
    private boolean availableInStock;
    private String isbn;
    private int publicationYear;
    private String publisher;

    public PhysicalBook(String id, String title, String author, double price,
                        BookCategory category, int pageCount, String coverType,
                        String isbn) {
        super(title, author, price, category);
        this.pageCount = pageCount;
        this.coverType = coverType;
        this.isbn = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public boolean isAvailableInStock() {
        return availableInStock;
    }

    public void setAvailableInStock(boolean availableInStock) {
        this.availableInStock = availableInStock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
