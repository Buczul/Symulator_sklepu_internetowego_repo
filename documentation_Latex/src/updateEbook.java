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