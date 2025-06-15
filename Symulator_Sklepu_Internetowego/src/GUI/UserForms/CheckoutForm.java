package GUI.UserForms;

import orderPCG.OrderServices;
import shoppingCartPCG.ShoppingCart;
import userPCG.User;
import javax.swing.*;
import java.sql.SQLException;
import userPCG.UserSession;
import userPCG.UserServices;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CheckoutForm extends JFrame {
    private JPanel panel1;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField cityField;
    private JTextField postalCodeField;
    private JTextField AddressField;
    private JTextField apartmentNumberField;
    private JTextField phoneNumberField;
    private JButton checkoutButton;
    private JButton backButton;
    private JTextField emailField;
    private JLabel errorLabel;
    private JComboBox comboBox1;

    private User currentUser = UserSession.getInstance().getCurrentUser();
    private UserServices userServices = new UserServices();

    public CheckoutForm() {
        super("Składanie zamówienia");
        this.setContentPane(this.panel1);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        fillUserData();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CartForm cartForm = new CartForm();
                dispose();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });
    }

    //Pobranie pełnych danych użytkownika z bazy danych
    private void fillUserData() {
        try {
            User fullUserData = userServices.getUserById(currentUser.getId());
            if (fullUserData != null) {
                firstNameField.setText(fullUserData.getFirstName() != null ? fullUserData.getFirstName() : "");
                lastNameField.setText(fullUserData.getLastName() != null ? fullUserData.getLastName() : "");
                cityField.setText(fullUserData.getCity() != null ? fullUserData.getCity() : "");
                postalCodeField.setText(fullUserData.getPostalCode() != null ? fullUserData.getPostalCode() : "");
                AddressField.setText(fullUserData.getStreet() != null ? fullUserData.getStreet() : "");
                apartmentNumberField.setText(fullUserData.getApartmentNumber() != null ? fullUserData.getApartmentNumber() : "");
                emailField.setText(fullUserData.getEmail() != null ? fullUserData.getEmail() : "");
                phoneNumberField.setText(fullUserData.getPhone() != null ? fullUserData.getPhone() : "");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych użytkownika: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Składanie zamówienia
    private void checkout() {
        // Walidacja danych
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() ||
                AddressField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
            errorLabel.setText("Proszę wypełnić wszystkie wymagane pola.");
            return;
        }

        // Aktualizacja danych użytkownika
        try {
            User updatedUser = new User();
            updatedUser.setId(currentUser.getId());
            updatedUser.setFirstName(firstNameField.getText());
            updatedUser.setLastName(lastNameField.getText());
            updatedUser.setCity(cityField.getText());
            updatedUser.setPostalCode(postalCodeField.getText());
            updatedUser.setStreet(AddressField.getText());
            updatedUser.setApartmentNumber(apartmentNumberField.getText());
            updatedUser.setPhone(phoneNumberField.getText());

            userServices.updateUser(updatedUser);

            // Tworzenie zamówienia
            ShoppingCart cart = ShoppingCart.getInstance();
            boolean orderCreated = OrderServices.createOrder(currentUser, cart);

            if (orderCreated) {
                JOptionPane.showMessageDialog(this,
                        "Zamówienie zostało złożone pomyślnie. Informacje o zamówieniu zostaną wysłane na adres podany w formularzu.",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
                cart.clearCart();
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas składania zamówienia: " + ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}


