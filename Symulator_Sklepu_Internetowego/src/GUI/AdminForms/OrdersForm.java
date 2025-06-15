package GUI.AdminForms;

import orderPCG.OrderServices;
import orderPCG.OrderServices.OrderInfo;
import orderPCG.OrderServices.OrderItemInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;  // Używamy java.util.List zamiast java.awt.List

public class OrdersForm extends JFrame {
    private JScrollPane ordersScrollPane;
    private JButton exitButton;
    private JPanel panel1;
    private JPanel ordersPanel;

    public OrdersForm() {
        super("Zarządzanie zamówieniami");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);

        // Inicjalizacja panelu zamówień
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersScrollPane.setViewportView(ordersPanel);
        ordersScrollPane.setBorder(BorderFactory.createEmptyBorder());

        refreshOrdersList();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setVisible(true);
    }

    //Odświerzenie listy zamówień
    private void refreshOrdersList() {
        ordersPanel.removeAll();

        try {
            List<OrderInfo> orders = OrderServices.getAllOrders();

            for (OrderInfo order : orders) {
                ordersPanel.add(new OrderItemPanel(order));
                ordersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            ordersPanel.revalidate();
            ordersPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas ładowania zamówień: " + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Klasa panelu pojedyńczego zamówienia
    private class OrderItemPanel extends JPanel {
        public OrderItemPanel(OrderInfo order) {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createEtchedBorder()
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            // Panel informacji o zamówieniu
            JPanel orderInfoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            orderInfoPanel.add(new JLabel("ID zamówienia:"));
            orderInfoPanel.add(new JLabel(String.valueOf(order.getId())));
            orderInfoPanel.add(new JLabel("Data zamówienia:"));
            orderInfoPanel.add(new JLabel(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getOrderDate())));
            orderInfoPanel.add(new JLabel("Status:"));
            orderInfoPanel.add(new JLabel(getStatusName(order.getStatus())));
            orderInfoPanel.add(new JLabel("Klient:"));
            orderInfoPanel.add(new JLabel(order.getFirstName() + " " + order.getLastName()));
            orderInfoPanel.add(new JLabel("Adres:"));
            orderInfoPanel.add(new JLabel(order.getStreet() + "/" + order.getApartmentNumber() +
                    ", " + order.getPostalCode() + " " + order.getCity()));
            orderInfoPanel.add(new JLabel("Email:"));
            orderInfoPanel.add(new JLabel(order.getEmail()));
            orderInfoPanel.add(new JLabel("Telefon:"));
            orderInfoPanel.add(new JLabel(order.getPhone()));
            orderInfoPanel.add(new JLabel("Suma:"));
            orderInfoPanel.add(new JLabel(String.format("%.2f zł", order.getTotalAmount())));

            // Panel informacji o produktach z zamówienia
            JPanel productsPanel = new JPanel();
            productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

            for (OrderItemInfo item : order.getItems()) {
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                itemPanel.add(new JLabel(item.getTitle() + " (" + getProductTypeName(item.getProductType()) +
                        ") - " + String.format("%.2f zł", item.getPrice())));
                productsPanel.add(itemPanel);
            }

            // Panel przycisków
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

            //Status zamówienia
            if (!order.getStatus().equals("SHIPPED")) {
                JButton shipButton = new JButton("Zrealizuj");
                shipButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateOrderStatus(order.getId(), "SHIPPED");
                    }
                });
                buttonsPanel.add(shipButton);
            }

            if (!order.getStatus().equals("CANCELLED")) {
                JButton cancelButton = new JButton("Anuluj");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateOrderStatus(order.getId(), "CANCELLED");
                    }
                });
                buttonsPanel.add(cancelButton);
            }

            // Panel główny
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(orderInfoPanel);
            mainPanel.add(productsPanel);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.SOUTH);
        }

        //Aktualizacja statusu zamówienia
        private void updateOrderStatus(int orderId, String newStatus) {
            try {
                boolean updated = OrderServices.updateOrderStatus(orderId, newStatus);
                if (updated) {
                    JOptionPane.showMessageDialog(OrdersForm.this,
                            "Status zamówienia został zaktualizowany.",
                            "Sukces",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshOrdersList();
                } else {
                    JOptionPane.showMessageDialog(OrdersForm.this,
                            "Nie udało się zaktualizować statusu zamówienia.",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(OrdersForm.this,
                        "Błąd podczas aktualizacji statusu zamówienia: " + ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        //Tłumaczenie statusu produktu na polski
        private String getStatusName(String status) {
            switch (status) {
                case "NEW": return "Nowe";
                case "SHIPPED": return "Zrealizowane";
                case "CANCELLED": return "Anulowane";
                default: return status;
            }
        }

        //Tłumaczenie typu produktu na Polski
        private String getProductTypeName(String productType) {
            switch (productType) {
                case "PHYSICAL": return "Książka fizyczna";
                case "EBOOK": return "E-book";
                case "AUDIOBOOK": return "Audiobook";
                default: return productType;
            }
        }
    }
}