package GUI.UserForms;

import GUI.LoginForm;
import shoppingCartPCG.ShoppingCart;
import userPCG.User;
import userPCG.UserSession;
import productPCG.*;
import productPCG.ProductServices.ProductInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogForm extends JFrame {
    private JPanel panel1;
    private JPanel listPanel;
    private JButton cartButton;
    private JComboBox<String> productTypeCombo;
    private JScrollPane productsScrollPane;
    private JButton logoutButton;
    private JButton accountSettingsButton;
    private JPanel productsPanel;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> sortByCombo;
    private JLabel userNameLabel;

    User currentUser = UserSession.getInstance().getCurrentUser();
    int roleId = currentUser.getRoleId();
    String userName = currentUser.getUsername();

    public CatalogForm() {
        super("Katalog BookHaven");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if(roleId == 1)   userNameLabel.setText("Witaj, "+ userName);

        // Inicjalizacja panelu produktów
        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsScrollPane.setViewportView(productsPanel);
        productsScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // ComboBox typów produktów
        productTypeCombo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Wszystkie typy",
                "Książki fizyczne",
                "E-booki",
                "Audiobooki"
        }));

        productTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProductList();
            }
        });

        // ComboBox kategorii
        categoryCombo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Wszystkie kategorie",
                "Biografia",
                "Fantastyka",
                "Science Fiction",
                "Historyczne",
                "Kryminały",
                "Dla dzieci",
                "Nauka języków",
                "Naukowe",
                "Popularnonaukowe",
                "Obyczajowe",
                "Literatura piękna",
                "Inne"
        }));

        categoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProductList();
            }
        });

        // ComboBox sortowania
        sortByCombo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Cena (rosnąco)",
                "Tytuł (od A do Z)",
                "Autor (od A do Z)"
        }));

        sortByCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProductList();
            }
        });

        // Pierwsze ładowanie listy produktów
        refreshProductList();

        accountSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roleId == 3) {
                    JOptionPane.showMessageDialog(null,
                            "Gość ma dostęp jedynie do przeglądania oferty.",
                            "Zalogowano jako gość.",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    AccountSettingsForm accountSettingsForm = new AccountSettingsForm();
                }
            }
        });

        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roleId == 3) {
                    JOptionPane.showMessageDialog(null,
                            "Gość ma dostęp jedynie do przeglądania oferty.",
                            "Zalogowano jako gość.",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    CartForm cartForm = new CartForm();
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserSession.getInstance().logout();
                ShoppingCart.getInstance().clearCart();
                dispose();
                LoginForm loginForm = new LoginForm();

                JOptionPane.showMessageDialog(null,
                        "Zostałeś pomyślnie wylogowany.",
                        "Wylogowano",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void refreshProductList() {
        productsPanel.removeAll();

        try {
            List<ProductInfo> products = ProductServices.getAllProducts();

            products = filterProducts(products);

            sortProducts(products);

            for (ProductInfo product : products) {
                productsPanel.add(new ProductItemPanel(product));
                productsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            productsPanel.revalidate();
            productsPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Filtrowanie produktów w panelu
    private List<ProductInfo> filterProducts(List<ProductInfo> products) {
        String selectedType = (String) productTypeCombo.getSelectedItem();
        String selectedCategory = (String) categoryCombo.getSelectedItem();

        return products.stream()
                .filter(p -> selectedType.equals("Wszystkie typy") ||
                        (selectedType.equals("Książki fizyczne") && p.getProductType().equals("PHYSICAL")) ||
                        (selectedType.equals("E-booki") && p.getProductType().equals("EBOOK")) ||
                        (selectedType.equals("Audiobooki") && p.getProductType().equals("AUDIOBOOK")))
                .filter(p -> selectedCategory.equals("Wszystkie kategorie") ||
                        BookCategory.valueOf(p.getCategoryName()).toString().equals(selectedCategory))
                .collect(Collectors.toList());
    }

    //Sortowanie listy
    private void sortProducts(List<ProductInfo> products) {
        String sortBy = (String) sortByCombo.getSelectedItem();

        List<ProductInfo> mutableList = new ArrayList<>(products);

        mutableList.sort((p1, p2) -> {
            switch (sortBy) {
                case "Cena (rosnąco)":
                    return Double.compare(p1.getPrice(), p2.getPrice());
                case "Tytuł (od A do Z)":
                    return p1.getTitle().compareToIgnoreCase(p2.getTitle());
                case "Autor (od A do Z)":
                    return p1.getAuthor().compareToIgnoreCase(p2.getAuthor());
                default:
                    return 0;
            }
        });
        products.clear();
        products.addAll(mutableList);
    }

    //Panel produktów
    private class ProductItemPanel extends JPanel {
        public ProductItemPanel(ProductInfo product) {
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

            // Przyciski "Dodaj do koszyka"
            JButton addToCartButton = new JButton("Dodaj do koszyka");
            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (roleId == 3) {
                    JOptionPane.showMessageDialog(null,
                            "Gość ma dostęp jedynie do przeglądania oferty.",
                            "Zalogowano jako gość",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        ShoppingCart.getInstance().addItem(product);
                        JOptionPane.showMessageDialog(null,
                                "Produkt dodany do koszyka: " + product.getTitle(),
                                "Sukces",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            add(infoPanel, BorderLayout.CENTER);
            add(addToCartButton, BorderLayout.EAST);
        }
    }
}