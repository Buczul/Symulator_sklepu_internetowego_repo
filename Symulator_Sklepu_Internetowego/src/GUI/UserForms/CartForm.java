package GUI.UserForms;

import productPCG.BookCategory;
import productPCG.ProductServices.ProductInfo;
import shoppingCartPCG.ShoppingCart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartForm extends JFrame {
    private JPanel mainPanel;
    private JScrollPane cartScrollPane;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton backButton;
    private JPanel cartItemsPanel;

    private ShoppingCart cart;

    public CartForm() {
        super("Twój koszyk");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        cart = ShoppingCart.getInstance();

        // Inicjalizacja panelu produktów
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartScrollPane.setViewportView(cartItemsPanel);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder());

        updateCartDisplay();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cart.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Koszyk jest pusty.",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    CheckoutForm checkoutForm = new CheckoutForm();
                    dispose();
                }
            }
        });
    }

    //Aktualizacja wyświetlanej zawartości koszyka
    private void updateCartDisplay() {
        cartItemsPanel.removeAll();

        for (ProductInfo product : cart.getItems()) {
            cartItemsPanel.add(new CartItemPanel(product));
            cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        totalLabel.setText(String.format("Razem: %.2f zł", cart.getTotalPrice()));
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    //Panel pojedyńczego produktu w koszyku
    private class CartItemPanel extends JPanel {
        public CartItemPanel(ProductInfo product) {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createEtchedBorder()
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            // Panel informacji
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            infoPanel.add(new JLabel("Tytuł:"));
            infoPanel.add(new JLabel(product.getTitle()));
            infoPanel.add(new JLabel("Autor:"));
            infoPanel.add(new JLabel(product.getAuthor()));
            infoPanel.add(new JLabel("Kategoria:"));
            infoPanel.add(new JLabel(BookCategory.valueOf(product.getCategoryName()).toString()));
            infoPanel.add(new JLabel("Cena:"));
            infoPanel.add(new JLabel(String.format("%.2f zł", product.getPrice())));

            // Przycisk "Usuń z koszyka"
            JButton removeButton = new JButton("Usuń z koszyka");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart.removeItem(product);
                    updateCartDisplay();
                    JOptionPane.showMessageDialog(null,
                            "Produkt usunięty z koszyka: " + product.getTitle(),
                            "Sukces",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

            add(infoPanel, BorderLayout.CENTER);
            add(removeButton, BorderLayout.EAST);
        }
    }
}