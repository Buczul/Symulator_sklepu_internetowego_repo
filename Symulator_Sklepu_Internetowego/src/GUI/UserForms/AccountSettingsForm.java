package GUI.UserForms;

import userPCG.User;
import userPCG.UserSession;
import userPCG.UserServices;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AccountSettingsForm extends JFrame {
    private JPanel panel1;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField cityField;
    private JTextField postalCodeField;
    private JTextField AddressField;
    private JTextField apartmentNumberField;
    private JTextField phoneNumberField;
    private JButton UpdateButton;
    private JButton exitButton;

    private User currentUser = UserSession.getInstance().getCurrentUser();
    private UserServices userServices = new UserServices();

    public AccountSettingsForm() {
        super("Ustawienia konta");
        this.setContentPane(this.panel1);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        fillUserData();

        usernameLabel.setText("Nazwa użytkownika: " + currentUser.getUsername());
        emailLabel.setText("Email: " + currentUser.getEmail());

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        UpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserData();
            }
        });
    }

    //Wypełnianie pól danymi użytkownika
    private void fillUserData() {
        // Pobierz pełne dane użytkownika z bazy danych
        try {
            User fullUserData = userServices.getUserById(currentUser.getId());
            if (fullUserData != null) {
                firstNameField.setText(fullUserData.getFirstName() != null ? fullUserData.getFirstName() : "");
                lastNameField.setText(fullUserData.getLastName() != null ? fullUserData.getLastName() : "");
                cityField.setText(fullUserData.getCity() != null ? fullUserData.getCity() : "");
                postalCodeField.setText(fullUserData.getPostalCode() != null ? fullUserData.getPostalCode() : "");
                AddressField.setText(fullUserData.getStreet() != null ? fullUserData.getStreet() : "");
                apartmentNumberField.setText(fullUserData.getApartmentNumber() != null ? fullUserData.getApartmentNumber() : "");
                phoneNumberField.setText(fullUserData.getPhone() != null ? fullUserData.getPhone() : "");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych użytkownika: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Aktualizacja danych w obiekcie użytkownika
    private void updateUserData() {
        try {
            currentUser.setFirstName(firstNameField.getText());
            currentUser.setLastName(lastNameField.getText());
            currentUser.setCity(cityField.getText());
            currentUser.setPostalCode(postalCodeField.getText());
            currentUser.setStreet(AddressField.getText());
            currentUser.setApartmentNumber(apartmentNumberField.getText());
            currentUser.setPhone(phoneNumberField.getText());

            boolean success = userServices.updateUser(currentUser);

            if (success) {
                JOptionPane.showMessageDialog(this, "Dane zostały zaktualizowane pomyślnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nie udało się zaktualizować danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas aktualizacji danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}