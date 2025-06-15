package GUI.AdminForms;

import productPCG.*;
import productPCG.ProductServices.ProductInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EditProductForm extends JFrame {
    private JComboBox<String> productTypeCombo;
    private JTextField titleField, authorField, priceField;
    private JComboBox<BookCategory> categoryCombo;
    private JPanel specificFieldsPanel, panel1;
    private JButton exitButton;
    private JButton saveButton;

    // Komponenty dla różnych typów produktów
    private JTextField pageCountField, isbnField, yearField, publisherField;
    private JComboBox<String> coverTypeCombo;
    private JCheckBox availableCheckbox;
    private JTextField fileFormatField, fileSizeField, downloadLinkField;
    private JTextField durationField, narratorField, studioField;
    private JComboBox<String> audioFormatCombo;

    private ProductInfo productToEdit;
    private String productId;

    public EditProductForm(ProductInfo product) {
        super("Edycja produktu: " + product.getTitle());
        this.productToEdit = product;
        this.productId = product.getId();

        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);

        initFields();
        loadProductData();

        this.setVisible(true);

        productTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) productTypeCombo.getSelectedItem();
                updateSpecificFields(selectedType);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagementProductForm deleteProductForm = new ManagementProductForm();
                dispose();
            }
        });
    }

    //Inicjalizacja pól
    private void initFields() {
        // Inicjalizacja ComboBox-ów
        productTypeCombo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Książka fizyczna", "E-book", "Audiobook"
        }));

        categoryCombo.setModel(new DefaultComboBoxModel<>(BookCategory.values()));

        // Inicjalizacja pól specyficznych dla typów produktów
        pageCountField = new JTextField(10);
        isbnField = new JTextField(15);
        yearField = new JTextField(4);
        publisherField = new JTextField(20);
        coverTypeCombo = new JComboBox<>(new String[]{"Twarda", "Miękka", "Inna"});
        availableCheckbox = new JCheckBox("Dostępny");

        fileFormatField = new JTextField(10);
        fileSizeField = new JTextField(10);
        downloadLinkField = new JTextField();

        durationField = new JTextField(10);
        narratorField = new JTextField(20);
        studioField = new JTextField(20);
        audioFormatCombo = new JComboBox<>(new String[]{"MP3", "AAC", "WAV", "FLAC"});

        // Ustawienie odpowiedniego typu produktu
        switch (productToEdit.getProductType()) {
            case "PHYSICAL": productTypeCombo.setSelectedItem("Książka fizyczna"); break;
            case "EBOOK": productTypeCombo.setSelectedItem("E-book"); break;
            case "AUDIOBOOK": productTypeCombo.setSelectedItem("Audiobook"); break;
        }

        updateSpecificFields((String)productTypeCombo.getSelectedItem());
    }
    // Wczytanie podstawowych danych produktu
    private void loadProductData() {

        titleField.setText(productToEdit.getTitle());
        authorField.setText(productToEdit.getAuthor());
        priceField.setText(String.valueOf(productToEdit.getPrice()));

        // Ustawienie kategorii
        for (int i = 0; i < categoryCombo.getItemCount(); i++) {
            if (categoryCombo.getItemAt(i).name().equals(productToEdit.getCategoryName())) {
                categoryCombo.setSelectedIndex(i);
                break;
            }
        }

        // Wczytanie szczegółowych danych w zależności od typu produktu
        try {
            switch (productToEdit.getProductType()) {
                case "PHYSICAL":
                    PhysicalBook physicalBook = ProductServices.getPhysicalBook(productId);
                    if (physicalBook != null) {
                        pageCountField.setText(String.valueOf(physicalBook.getPageCount()));
                        coverTypeCombo.setSelectedItem(physicalBook.getCoverType());
                        isbnField.setText(physicalBook.getIsbn());
                        yearField.setText(physicalBook.getPublicationYear() > 0 ?
                                String.valueOf(physicalBook.getPublicationYear()) : "");
                        publisherField.setText(physicalBook.getPublisher() != null ?
                                physicalBook.getPublisher() : "");
                        availableCheckbox.setSelected(physicalBook.isAvailableInStock());
                    }
                    break;

                case "EBOOK":
                    Ebook ebook = ProductServices.getEbook(productId);
                    if (ebook != null) {
                        fileFormatField.setText(ebook.getFileFormat());
                        fileSizeField.setText(String.valueOf(ebook.getFileSizeMB()));
                        downloadLinkField.setText(ebook.getDownloadLink());
                    }
                    break;

                case "AUDIOBOOK":
                    Audiobook audiobook = ProductServices.getAudiobook(productId);
                    if (audiobook != null) {
                        durationField.setText(String.valueOf(audiobook.getDurationMinutes()));
                        narratorField.setText(audiobook.getNarrator());
                        audioFormatCombo.setSelectedItem(audiobook.getAudioFormat());
                        studioField.setText(audiobook.getStudioName());
                    }
                    break;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas wczytywania szczegółów produktu: " + ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Aktualizacja specyficznych pól dla typów produktów
    private void updateSpecificFields(String productType) {
        specificFieldsPanel.removeAll();
        specificFieldsPanel.setLayout(new GridLayout(0, 2, 5, 5));

        switch (productType) {
            case "Książka fizyczna":
                addPhysicalBookFields();
                break;
            case "E-book":
                addEbookFields();
                break;
            case "Audiobook":
                addAudiobookFields();
                break;
        }

        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }

    //Dodanie pól dla ksiązek fizycznych
    private void addPhysicalBookFields() {
        specificFieldsPanel.add(new JLabel("Liczba stron:"));
        specificFieldsPanel.add(pageCountField);

        specificFieldsPanel.add(new JLabel("Typ okładki:"));
        specificFieldsPanel.add(coverTypeCombo);

        specificFieldsPanel.add(new JLabel("ISBN:"));
        specificFieldsPanel.add(isbnField);

        specificFieldsPanel.add(new JLabel("Rok wydania:"));
        specificFieldsPanel.add(yearField);

        specificFieldsPanel.add(new JLabel("Wydawca:"));
        specificFieldsPanel.add(publisherField);

        specificFieldsPanel.add(new JLabel("Dostępność:"));
        specificFieldsPanel.add(availableCheckbox);
    }

    //Dodanie pól dla ebooków
    private void addEbookFields() {
        specificFieldsPanel.add(new JLabel("Format pliku:"));
        specificFieldsPanel.add(fileFormatField);

        specificFieldsPanel.add(new JLabel("Rozmiar (MB):"));
        specificFieldsPanel.add(fileSizeField);

        specificFieldsPanel.add(new JLabel("Link do pobrania:"));
        specificFieldsPanel.add(downloadLinkField);
    }

    //Dodanie pól dla audiobooków
    private void addAudiobookFields() {
        specificFieldsPanel.add(new JLabel("Czas trwania (min):"));
        specificFieldsPanel.add(durationField);

        specificFieldsPanel.add(new JLabel("Narrator:"));
        specificFieldsPanel.add(narratorField);

        specificFieldsPanel.add(new JLabel("Format audio:"));
        specificFieldsPanel.add(audioFormatCombo);

        specificFieldsPanel.add(new JLabel("Studio:"));
        specificFieldsPanel.add(studioField);
    }

    // Aktualizacja danych produktu
    private void updateProduct() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            BookCategory category = (BookCategory) categoryCombo.getSelectedItem();

            boolean baseUpdated = false;
            boolean detailsUpdated = false;

            //Aktualizacja specyficznych danych
            switch (productToEdit.getProductType()) {
                case "PHYSICAL":
                    PhysicalBook physicalBook = new PhysicalBook(
                            productId, title, author, price, category,
                            Integer.parseInt(pageCountField.getText()),
                            (String) coverTypeCombo.getSelectedItem(),
                            isbnField.getText()
                    );
                    physicalBook.setPublicationYear(yearField.getText().isEmpty() ?
                            0 : Integer.parseInt(yearField.getText()));
                    physicalBook.setPublisher(publisherField.getText());
                    physicalBook.setAvailableInStock(availableCheckbox.isSelected());

                    baseUpdated = ProductServices.updateBaseProduct(physicalBook);
                    detailsUpdated = ProductServices.updatePhysicalBook(physicalBook);
                    break;

                case "EBOOK":
                    Ebook ebook = new Ebook(
                            title, author, price, category,
                            fileFormatField.getText(),
                            Double.parseDouble(fileSizeField.getText()),
                            downloadLinkField.getText()
                    );
                    ebook.setId(productId);

                    baseUpdated = ProductServices.updateBaseProduct(ebook);
                    detailsUpdated = ProductServices.updateEbook(ebook);
                    break;

                case "AUDIOBOOK":
                    Audiobook audiobook = new Audiobook(
                            title, author, price, category,
                            Integer.parseInt(durationField.getText()),
                            narratorField.getText(),
                            (String) audioFormatCombo.getSelectedItem(),
                            studioField.getText()
                    );
                    audiobook.setId(productId);

                    baseUpdated = ProductServices.updateBaseProduct(audiobook);
                    detailsUpdated = ProductServices.updateAudiobook(audiobook);
                    break;
            }

            if (baseUpdated && detailsUpdated) {
                JOptionPane.showMessageDialog(this,
                        "Produkt zaktualizowany pomyślnie.",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                throw new SQLException("Nie udało się zaktualizować produktu.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nieprawidłowy format danych.",
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas aktualizacji produktu: " + ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}