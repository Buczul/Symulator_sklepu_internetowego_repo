package GUI.AdminForms;

import GUI.LoginForm;
import userPCG.UserSession;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminForm extends JFrame {
    private JPanel panel1;
    private JButton logoutButton;
    private JButton addAdminButton;
    private JButton deleteUserButton;
    private JButton addProductButton;
    private JButton manageProductButton;
    private JTable productsTable;
    private JButton orderManagementButton;
    private DefaultTableModel tableModel;

    public AdminForm() {
        super("Panel administratora");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserSession.getInstance().logout();
                dispose();
                LoginForm loginForm = new LoginForm();

                JOptionPane.showMessageDialog(null,
                        "Zostałeś pomyślnie wylogowany.",
                        "Wylogowano",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteUserForm deleteUserForm = new DeleteUserForm();
            }
        });

        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddAdminForm addAdminForm = new AddAdminForm();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProductForm addProductForm = new AddProductForm();
            }
        });

        manageProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagementProductForm deleteProductForm = new ManagementProductForm();
            }
        });
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersForm ordersForm = new OrdersForm();
            }
        });
    }
}
