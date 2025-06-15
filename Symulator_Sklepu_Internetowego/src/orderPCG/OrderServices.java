package orderPCG;

import database.DBConnection;
import productPCG.ProductServices.ProductInfo;
import shoppingCartPCG.ShoppingCart;
import userPCG.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderServices {
    //Tworzenie nowego zamówienia z koszyka
    public static boolean createOrder(User user, ShoppingCart cart) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            //Dodanie nagłówka do zamówienia
            String orderSql = "INSERT INTO orders (user_id, order_date, total_amount, status) VALUES (?, NOW(), ?, 'NEW')";
            int orderId;

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getId());
                orderStmt.setDouble(2, cart.getTotalPrice());
                orderStmt.executeUpdate();

                // Pobranie wygenerowanego id
                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            // Dodanie pozycji zamówienia
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, 1, ?)";
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (ProductInfo product : cart.getItems()) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setString(2, product.getId());
                    itemStmt.setDouble(3, product.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            //Zatwierdzenie transakcji
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    //Pobranie listy wszystkich zamówień
    public static List<OrderInfo> getAllOrders() throws SQLException {
        List<OrderInfo> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.order_date, o.total_amount, o.status, " +
                "u.first_name, u.last_name, u.street, u.apartment_number, " +
                "u.postal_code, u.city, u.email, u.phone " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderInfo order = new OrderInfo(
                        rs.getInt("id"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("street"),
                        rs.getString("apartment_number"),
                        rs.getString("postal_code"),
                        rs.getString("city"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        getOrderItems(rs.getInt("id"))
                );
                orders.add(order);
            }
        }
        return orders;
    }

    //Pobranie pozycji zamówienia
    private static List<OrderItemInfo> getOrderItems(int orderId) throws SQLException {
        List<OrderItemInfo> items = new ArrayList<>();
        String sql = "SELECT oi.product_id, p.title, p.product_type, oi.price " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItemInfo item = new OrderItemInfo(
                        rs.getString("product_id"),
                        rs.getString("title"),
                        rs.getString("product_type"),
                        rs.getDouble("price")
                );
                items.add(item);
            }
        }
        return items;
    }

    //Aktualizacja statusu zamówienia
    public static boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //Wewnętrzna klasa pomocnicza do przechowywania informacji o zamówieniu
    public static class OrderInfo {
        private int id;
        private Timestamp orderDate;
        private double totalAmount;
        private String status;
        private String firstName;
        private String lastName;
        private String street;
        private String apartmentNumber;
        private String postalCode;
        private String city;
        private String email;
        private String phone;
        private List<OrderItemInfo> items;

        public OrderInfo(int id, Timestamp orderDate, double totalAmount, String status,
                         String firstName, String lastName, String street, String apartmentNumber,
                         String postalCode, String city, String email, String phone,
                         List<OrderItemInfo> items) {
            this.id = id;
            this.orderDate = orderDate;
            this.totalAmount = totalAmount;
            this.status = status;
            this.firstName = firstName;
            this.lastName = lastName;
            this.street = street;
            this.apartmentNumber = apartmentNumber;
            this.postalCode = postalCode;
            this.city = city;
            this.email = email;
            this.phone = phone;
            this.items = items;
        }

        public int getId() { return id; }
        public Timestamp getOrderDate() { return orderDate; }
        public double getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getStreet() { return street; }
        public String getApartmentNumber() { return apartmentNumber; }
        public String getPostalCode() { return postalCode; }
        public String getCity() { return city; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public List<OrderItemInfo> getItems() { return items; }
    }

    //Wewnętrzna klasa pomocnicza do przechowywania informacji o produktach w zamówieniu
    public static class OrderItemInfo {
        private String productId;
        private String title;
        private String productType;
        private double price;

        public OrderItemInfo(String productId, String title, String productType, double price) {
            this.productId = productId;
            this.title = title;
            this.productType = productType;
            this.price = price;
        }

        public String getProductId() { return productId; }
        public String getTitle() { return title; }
        public String getProductType() { return productType; }
        public double getPrice() { return price; }
    }
}