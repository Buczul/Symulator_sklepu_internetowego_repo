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