package productPCG;

public abstract class Book implements Product{
    protected String id;
    protected String title;
    protected String author;
    protected double price;
    protected BookCategory category;

    public Book(String title, String author, double price, BookCategory category) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }
}
