package GUI.AdminForms;

import productPCG.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductForm extends JFrame {
    private JComboBox<String> productTypeCombo;
    private JTextField titleField, authorField, priceField;
    private JComboBox<BookCategory> categoryCombo;
    private JPanel specificFieldsPanel, panel1;
    private JButton exitButton;
    private JButton addButton;

    // Dodatkowe komponenty dla różnych typów produktów
    private JTextField pageCountField, isbnField, yearField, publisherField;
    private JComboBox<String> coverTypeCombo;
    private JCheckBox availableCheckbox;

    private JTextField fileFormatField, fileSizeField, downloadLinkField, asinField;

    private JTextField durationField, narratorField, studioField;
    private JComboBox<String> audioFormatCombo;

    public AddProductForm() {
        super("Dodawanie nowego produktu");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        categoryCombo.setModel(new DefaultComboBoxModel<>(BookCategory.values()));
        initSpecificFields();
        this.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct(e);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        productTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) productTypeCombo.getSelectedItem();
                updateSpecificFields(selectedType);
            }
        });
    }

    private void initSpecificFields() {
        // Inicjalizacja komponentów dla książek fizycznych
        pageCountField = new JTextField(10);
        isbnField = new JTextField(15);
        yearField = new JTextField(4);
        publisherField = new JTextField(20);
        coverTypeCombo = new JComboBox<>(new String[]{"Twarda", "Miękka", "Specjalna"});
        availableCheckbox = new JCheckBox("Dostępny", true);

        // Inicjalizacja komponentów dla ebooków
        fileFormatField = new JTextField(10);
        fileSizeField = new JTextField(10);
        downloadLinkField = new JTextField();
        asinField = new JTextField(15);

        // Inicjalizacja komponentów dla audiobooków
        durationField = new JTextField(10);
        narratorField = new JTextField(20);
        studioField = new JTextField(20);
        audioFormatCombo = new JComboBox<>(new String[]{"MP3", "AAC", "WAV", "FLAC"});

        // Ustawienie początkowego panel
        updateSpecificFields((String)productTypeCombo.getSelectedItem());
    }

    //Aktualizacja pól specyficznych dla typów produktów
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

    //Dodawanie pól dla książek fizycznych
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

    //Dodawanie pól dla ebooków
    private void addEbookFields() {
        specificFieldsPanel.add(new JLabel("Format pliku:"));
        specificFieldsPanel.add(fileFormatField);

        specificFieldsPanel.add(new JLabel("Rozmiar (MB):"));
        specificFieldsPanel.add(fileSizeField);

        specificFieldsPanel.add(new JLabel("Link do pobrania:"));
        specificFieldsPanel.add(downloadLinkField);

    }

    //Dodawanie pól dla audiobooków
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

    //Dodawanie produktu
    private void addProduct(ActionEvent e) {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            BookCategory category = (BookCategory) categoryCombo.getSelectedItem();

            String productType = (String) productTypeCombo.getSelectedItem();

            //Pola specyficzne dla typów produktu
            switch (productType) {
                case "Książka fizyczna":
                    PhysicalBook physicalBook = new PhysicalBook(
                            null, // ID zostanie wygenerowane w ProductServices
                            title,
                            author,
                            price,
                            category,
                            Integer.parseInt(pageCountField.getText()),
                            (String) coverTypeCombo.getSelectedItem(),
                            isbnField.getText()
                    );
                    physicalBook.setPublicationYear(yearField.getText().isEmpty() ? 0 : Integer.parseInt(yearField.getText()));
                    physicalBook.setPublisher(publisherField.getText());
                    physicalBook.setAvailableInStock(availableCheckbox.isSelected());

                    ProductServices.addPhysicalBook(physicalBook);
                    break;

                case "E-book":
                    Ebook ebook = new Ebook(
                            title,
                            author,
                            price,
                            category,
                            fileFormatField.getText(),
                            Double.parseDouble(fileSizeField.getText()),
                            downloadLinkField.getText()
                    );

                    ProductServices.addEbook(ebook);
                    break;

                case "Audiobook":
                    Audiobook audiobook = new Audiobook(
                            title,
                            author,
                            price,
                            category,
                            Integer.parseInt(durationField.getText()),
                            narratorField.getText(),
                            (String) audioFormatCombo.getSelectedItem(),
                            studioField.getText()
                    );

                    ProductServices.addAudiobook(audiobook);
                    break;
            }

            JOptionPane.showMessageDialog(this, "Produkt dodany pomyślnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy format.", "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Czyszczenie formularza
    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        priceField.setText("");
        categoryCombo.setSelectedIndex(0);

        pageCountField.setText("");
        isbnField.setText("");
        yearField.setText("");
        publisherField.setText("");
        fileFormatField.setText("");
        fileSizeField.setText("");
        downloadLinkField.setText("");
        durationField.setText("");
        narratorField.setText("");
        studioField.setText("");
    }
}