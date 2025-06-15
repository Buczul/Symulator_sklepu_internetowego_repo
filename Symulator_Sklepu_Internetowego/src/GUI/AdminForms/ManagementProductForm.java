package GUI.AdminForms;

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

public class ManagementProductForm extends JFrame {
    private JPanel panel1;
    private JScrollPane productsScrollPane;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> sortByCombo;
    private JComboBox<String> productTypeCombo;
    private JButton exitButton;
    private JPanel productsPanel;

    public ManagementProductForm() {
        super("Zarządzanie produktami");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Inicjalizacja panelu produktów
        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsScrollPane.setViewportView(productsPanel);
        productsScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // ComboBox typu
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

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        refreshProductList();
    }

    //Odświerzenie listy produktów
    private void refreshProductList() {
        productsPanel.removeAll();

        try {
            List<ProductInfo> products = ProductServices.getAllProducts();

            // Filtrowanie
            products = filterProducts(products);

            // Sortowanie
            sortProducts(products);

            // Wyświetlanie
            for (ProductInfo product : products) {
                productsPanel.add(new ProductItemPanel(product));
                productsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            productsPanel.revalidate();
            productsPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas ładowania produktów: " + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Filtowanie produktów według wybranych kryterów
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

    //Sortowanie produktów
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

    //Klasa panelu pojedyńczego produktu
    private class ProductItemPanel extends JPanel {
        public ProductItemPanel(ProductInfo product) {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createEtchedBorder()
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            // Panel informacji o produkcie
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            infoPanel.add(new JLabel("Tytuł:"));
            infoPanel.add(new JLabel(product.getTitle()));
            infoPanel.add(new JLabel("Autor:"));
            infoPanel.add(new JLabel(product.getAuthor()));
            infoPanel.add(new JLabel("Kategoria:"));
            infoPanel.add(new JLabel(BookCategory.valueOf(product.getCategoryName()).toString()));
            infoPanel.add(new JLabel("Cena:"));
            infoPanel.add(new JLabel(String.format("%.2f zł", product.getPrice())));

            // Panel przycisków
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

            // Przycisk "Edytuj"
            JButton editButton = new JButton("Edytuj");
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        EditProductForm editForm = new EditProductForm(product);
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ManagementProductForm.this,
                                "Błąd podczas otwierania formularza edycji: " + ex.getMessage(),
                                "Błąd",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Przycisk "Usuń"

            JButton deleteButton = new JButton("Usuń");

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            ManagementProductForm.this,
                            "Czy na pewno chcesz usunąć produkt: " + product.getTitle() + "?",
                            "Potwierdzenie usunięcia",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            boolean deleted = ProductServices.deleteProduct(product.getId());
                            if (deleted) {
                                JOptionPane.showMessageDialog(ManagementProductForm.this,
                                        "Produkt został usunięty",
                                        "Sukces",
                                        JOptionPane.INFORMATION_MESSAGE);
                                refreshProductList();
                            } else {
                                JOptionPane.showMessageDialog(ManagementProductForm.this,
                                        "Nie udało się usunąć produktu",
                                        "Błąd",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(ManagementProductForm.this,
                                    "Błąd podczas usuwania produktu: " + ex.getMessage(),
                                    "Błąd",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            buttonsPanel.add(editButton);
            buttonsPanel.add(deleteButton);

            add(infoPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.EAST);
        }
    }
}