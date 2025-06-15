package GUI;

import GUI.AdminForms.AdminForm;
import GUI.UserForms.CatalogForm;
import userPCG.User;
import userPCG.UserServices;
import userPCG.UserSession;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JPanel panel1;
    private JPasswordField passwordField;
    private JTextField loginField;
    private JButton guestButton;
    private JButton registerButton;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel statusLabel;

    public LoginForm() {
        super("Logowanie");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegisterForm registerForm = new RegisterForm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserSession.getInstance().loginAsGuest();
                CatalogForm catalogForm = new CatalogForm();
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    //Logowanie użytkownika
    private void performLogin() {
        String username = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Wprowadź login/email i hasło.", true);
            return;
        }

        try {
            if (UserServices.loginAuthenticateUser(username, password)) {
                User currentUser = UserSession.getInstance().getCurrentUser();
                int roleId = currentUser.getRoleId();

                if (roleId == 2) { // Administrator
                    AdminForm adminForm = new AdminForm();
                } else { // Użytkownik (1) lub Gość (3)
                    CatalogForm catalogForm = new CatalogForm();
                }
                dispose();
            } else {
                showStatus("Nieprawidłowy login/email lub hasło.", true);
            }
        } catch (SQLException ex) {
            showDatabaseError();
            ex.printStackTrace();
        }
    }

    //Wyświetlenie komunikatu o statusie
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
    }

    //Wyświetlenie błedu bazy danych
    private void showDatabaseError() {
        JOptionPane.showMessageDialog(this,
                "Błąd połączenia z bazą danych.",
                "Błąd systemu.",
                JOptionPane.ERROR_MESSAGE);
    }
}
