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