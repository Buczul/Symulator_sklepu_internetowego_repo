    //Rejestracja nowego użytkownika
    public void registerUser(User user) throws SQLException {
        // Walidacja przed rejestracją
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Nieprawidłowy format email");
        }

        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Nazwa użytkownika jest już zajęta");
        }

        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Ten email jest już zarejestrowany");
        }

        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
    }