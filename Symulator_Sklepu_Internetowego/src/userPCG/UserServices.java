package userPCG;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserServices {

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

    //Dodawanie nowego użytkownika z rolą admin
    public void addAdmin(User user) throws SQLException {
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

        String sql = "INSERT INTO users (username, email, password, role_id) VALUES (?, ?, ?, 2)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
    }

    //Aktualizacja dancyh użytkownika
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, city = ?, postal_code = ?, " +
                "street = ?, apartment_number = ?, phone = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getCity());
            statement.setString(4, user.getPostalCode());
            statement.setString(5, user.getStreet());
            statement.setString(6, user.getApartmentNumber());
            statement.setString(7, user.getPhone());
            statement.setInt(8, user.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Logowanie użytkownika po uwierzytelnieniu
    public static boolean loginAuthenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && password.equals(rs.getString("password"))) {
                    UserSession.getInstance().loginUser(extractUserFromResultSet(rs));
                    return true;
                }
            }
        }
        return false;
    }

    // Usuwanie użytkownika po nazwie
    public boolean deleteUserByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Sprawdzanie czy nazwa użytkownika istnieje
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Sprawdzanie czy email istnieje
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT email FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Walidacja formatu emaila
    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    // Tworzenie obiekt User z ResultSet
    private static User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        return user;
    }

    // Pobieranie listy wszystkich użytkowników
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, role_id, email, created_at FROM users ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setRoleId(resultSet.getInt("role_id"));
                user.setEmail(resultSet.getString("email"));
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                users.add(user);
            }
        }
        return users;
    }

    // Pobieranie użytkownika po ID
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setCity(resultSet.getString("city"));
                    user.setPostalCode(resultSet.getString("postal_code"));
                    user.setStreet(resultSet.getString("street"));
                    user.setApartmentNumber(resultSet.getString("apartment_number"));
                    user.setPhone(resultSet.getString("phone"));
                    return user;
                }
            }
        }
        return null;
    }
}
