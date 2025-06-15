package productPCG;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;

public class ProductServices {
    // Dodawanie podstawowych informacji o produkcie
    private static String addBaseProduct(Book book) throws SQLException {
        String sql = "INSERT INTO products (id, title, author, price, category_id, product_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String id = book.getId() != null ? book.getId() : generateUUID();
            book.setId(id);

            stmt.setString(1, id);
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, getCategoryId(book.getCategory()));
            stmt.setString(6, getProductType(book));

            stmt.executeUpdate();
            return id;
        }
    }

    //Dodawanie fizycznej książki
    public static void addPhysicalBook(PhysicalBook book) throws SQLException {
        String productId = addBaseProduct(book);

        String sql = "INSERT INTO physical_books (product_id, page_count, cover_type, isbn, " +
                "publication_year, publisher, available_in_stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            stmt.setInt(2, book.getPageCount());
            stmt.setString(3, book.getCoverType());
            stmt.setString(4, book.getIsbn());
            stmt.setInt(5, book.getPublicationYear());
            stmt.setString(6, book.getPublisher());
            stmt.setBoolean(7, book.isAvailableInStock());

            stmt.executeUpdate();
        }
    }

    //Dodawanie ebooka
    public static void addEbook(Ebook ebook) throws SQLException {
        String productId = addBaseProduct(ebook);

        String sql = "INSERT INTO ebooks (product_id, file_format, file_size_mb, download_link) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            stmt.setString(2, ebook.getFileFormat());
            stmt.setDouble(3, ebook.getFileSizeMB());
            stmt.setString(4, ebook.getDownloadLink());

            stmt.executeUpdate();
        }
    }

    //Dodawanie audiobooka
    public static void addAudiobook(Audiobook audiobook) throws SQLException {
        String productId = addBaseProduct(audiobook);

        String sql = "INSERT INTO audiobooks (product_id, duration_minutes, narrator, audio_format, studio_name) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            stmt.setInt(2, audiobook.getDurationMinutes());
            stmt.setString(3, audiobook.getNarrator());
            stmt.setString(4, audiobook.getAudioFormat());
            stmt.setString(5, audiobook.getStudioName());

            stmt.executeUpdate();
        }
    }
    //Aktualizowanie podstawowych danych
    public static boolean updateBaseProduct(Book book) throws SQLException {
        String sql = "UPDATE products SET title = ?, author = ?, price = ?, category_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setDouble(3, book.getPrice());
            stmt.setInt(4, getCategoryId(book.getCategory()));
            stmt.setString(5, book.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //Aktualizowanie książki fizycznej
    public static boolean updatePhysicalBook(PhysicalBook book) throws SQLException {
        String sql = "UPDATE physical_books SET page_count = ?, cover_type = ?, isbn = ?, " +
                "publication_year = ?, publisher = ?, available_in_stock = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, book.getPageCount());
            stmt.setString(2, book.getCoverType());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getPublicationYear());
            stmt.setString(5, book.getPublisher());
            stmt.setBoolean(6, book.isAvailableInStock());
            stmt.setString(7, book.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //Aktualizowanie ebooka
    public static boolean updateEbook(Ebook ebook) throws SQLException {
        String sql = "UPDATE ebooks SET file_format = ?, file_size_mb = ?, download_link = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ebook.getFileFormat());
            stmt.setDouble(2, ebook.getFileSizeMB());
            stmt.setString(3, ebook.getDownloadLink());
            stmt.setString(4, ebook.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //Aktualizowanie audiobooka
    public static boolean updateAudiobook(Audiobook audiobook) throws SQLException {
        String sql = "UPDATE audiobooks SET duration_minutes = ?, narrator = ?, audio_format = ?, studio_name = ? " +
                "WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, audiobook.getDurationMinutes());
            stmt.setString(2, audiobook.getNarrator());
            stmt.setString(3, audiobook.getAudioFormat());
            stmt.setString(4, audiobook.getStudioName());
            stmt.setString(5, audiobook.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //pobieranie id kategorii
    private static int getCategoryId(BookCategory category) throws SQLException {
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.name());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Category not found: " + category.name());
            }
        }
    }


    //Generowanie UUID
    private static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    //Pobranie wszystkich produktów
    public static List<ProductInfo> getAllProducts() throws SQLException {
        List<ProductInfo> products = new ArrayList<>();

        String sql = "SELECT p.id, p.title, p.author, p.product_type, p.price, c.name as category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProductInfo info = new ProductInfo(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("product_type"),
                        rs.getString("category_name"),
                        rs.getDouble("price")
                );
                products.add(info);
            }
        }
        return products;
    }

    // Usuwanie produktu
    public static boolean deleteProduct(String productId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Usuwanie tabeli szczegółów
                String productType = getProductType(conn, productId);
                if (productType != null) {
                    String detailTable = getDetailTableName(productType);
                    if (detailTable != null) {
                        String deleteDetailSql = "DELETE FROM " + detailTable + " WHERE product_id = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(deleteDetailSql)) {
                            stmt.setString(1, productId);
                            stmt.executeUpdate();
                        }
                    }
                }

                // Usuwanie z tabeli products
                String deleteProductSql = "DELETE FROM products WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteProductSql)) {
                    stmt.setString(1, productId);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    //Pobranie typu Produktu
    private static String getProductType(Connection conn, String productId) throws SQLException {
        String sql = "SELECT product_type FROM products WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("product_type");
            }
        }
        return null;
    }

    //Określenie typu produktu
    private static String getProductType(Book book) {
        if (book instanceof PhysicalBook) return "PHYSICAL";
        if (book instanceof Ebook) return "EBOOK";
        if (book instanceof Audiobook) return "AUDIOBOOK";
        throw new IllegalArgumentException("Unknown book type");
    }

    //Zwaranie nazwy tabeli szczegółów
    private static String getDetailTableName(String productType) {
        switch (productType) {
            case "PHYSICAL": return "physical_books";
            case "EBOOK": return "ebooks";
            case "AUDIOBOOK": return "audiobooks";
            default: return null;
        }
    }

    public static PhysicalBook getPhysicalBook(String productId) throws SQLException {
        String sql = "SELECT * FROM physical_books WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PhysicalBook(
                        productId,
                        null, // title będzie pobrane z tabeli products
                        null, // author będzie pobrane z tabeli products
                        0,    // price będzie pobrane z tabeli products
                        null, // category będzie pobrane z tabeli products
                        rs.getInt("page_count"),
                        rs.getString("cover_type"),
                        rs.getString("isbn")
                );
            }
        }
        return null;
    }

    public static Ebook getEbook(String productId) throws SQLException {
        String sql = "SELECT * FROM ebooks WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Ebook(
                        null, // title będzie pobrane z tabeli products
                        null, // author będzie pobrane z tabeli products
                        0,    // price będzie pobrane z tabeli products
                        null, // category będzie pobrane z tabeli products
                        rs.getString("file_format"),
                        rs.getDouble("file_size_mb"),
                        rs.getString("download_link")
                );
            }
        }
        return null;
    }

    public static Audiobook getAudiobook(String productId) throws SQLException {
        String sql = "SELECT * FROM audiobooks WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Audiobook(
                        null, // title będzie pobrane z tabeli products
                        null, // author będzie pobrane z tabeli products
                        0,    // price będzie pobrane z tabeli products
                        null, // category będzie pobrane z tabeli products
                        rs.getInt("duration_minutes"),
                        rs.getString("narrator"),
                        rs.getString("audio_format"),
                        rs.getString("studio_name")
                );
            }
        }
        return null;
    }

    // Wewnętrzna klasa pomocnicza do przechowywania informacji o produkcie
    public static class ProductInfo {
        private String id;
        private String title;
        private String author;
        private String productType;
        private String categoryName;
        private double price;

        public ProductInfo(String id, String title, String author, String productType,
                           String categoryName, double price) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.productType = productType;
            this.categoryName = categoryName;
            this.price = price;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getProductType() { return productType; }
        public String getCategoryName() { return categoryName; }
        public double getPrice() { return price; }
    }
}