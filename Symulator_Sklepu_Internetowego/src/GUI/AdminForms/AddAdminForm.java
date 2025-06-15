package GUI.AdminForms;

import userPCG.User;
import userPCG.UserServices;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddAdminForm extends JFrame {
    private JPanel panel1;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JLabel errorLabel;
    private JButton exitButton;
    private JButton addButton;

    public AddAdminForm() {
        super("Dodawanie administratora");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAdmin();
            }
        });
    }

    //Rejestracja użytkownika o roli admin
    private void registerAdmin() {
        String username = loginField.getText().trim();
        String email = emailField.getText().trim();
        String password1 = new String(passwordField.getPassword());
        String password2 = new String(confirmPasswordField.getPassword());

        // Walidacja pól
        if (username.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            showError("Wypełnij wszystkie pola.");
            return;
        }

        if (!password1.equals(password2)) {
            showError("Hasła nie są identyczne.");
            return;
        }

        UserServices userService = new UserServices();
        User newUser = new User(username, email, password1);

        try {
            userService.addAdmin(newUser);
            JOptionPane.showMessageDialog(this, "Administrator został dodany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (SQLException ex) {
            showError("Błąd bazy danych: " + ex.getMessage());
        }
    }

    //Wyświetlenie wiadomości o błędzie
    private void showError(String message) {
        if (errorLabel == null) {
            errorLabel = new JLabel();
            panel1.add(errorLabel);
        }
        errorLabel.setText(message);
    }
}
