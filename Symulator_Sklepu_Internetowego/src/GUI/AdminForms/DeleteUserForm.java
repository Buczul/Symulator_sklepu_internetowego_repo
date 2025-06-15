package GUI.AdminForms;

import userPCG.User;
import userPCG.UserServices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class DeleteUserForm extends JFrame {
    private JPanel panel1;
    private JButton exitButton;
    private JPanel listPanel;
    private JScrollPane usersScrollPane;

    UserServices userServices = new UserServices();

    public DeleteUserForm() {
        super("Usuwanie użytkowników");
        this.setContentPane(this.panel1);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicjalizacja panelu użytkowników
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        usersScrollPane.setViewportView(listPanel);
        usersScrollPane.setBorder(BorderFactory.createEmptyBorder());

        refreshUserList();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setVisible(true);
    }

    //Odświeżenie listy użytkowników
    private void refreshUserList() {
        listPanel.removeAll();

        try {
            List<User> users = userServices.getAllUsers();

            for (User user : users) {
                listPanel.add(new UserItemPanel(user));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            listPanel.revalidate();
            listPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas ładowania użytkowników: " + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Klasa panelu pojedyńczego użytkownika
    private class UserItemPanel extends JPanel {
        public UserItemPanel(User user) {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createEtchedBorder()
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            // Panel informacji
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            infoPanel.add(new JLabel("Nazwa użytkownika:"));
            infoPanel.add(new JLabel(user.getUsername()));
            infoPanel.add(new JLabel("Email:"));
            infoPanel.add(new JLabel(user.getEmail()));
            infoPanel.add(new JLabel("Rola:"));
            infoPanel.add(new JLabel(user.getRoleId() == 2 ? "Administrator" : "Użytkownik"));

            // Panel przycisków
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

            // Przycisk "Usuń"
            JButton deleteButton = new JButton("Usuń");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            DeleteUserForm.this,
                            "Czy na pewno chcesz usunąć użytkownika: " + user.getUsername() + "?",
                            "Potwierdzenie usunięcia",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            boolean deleted = userServices.deleteUserByUsername(user.getUsername());
                            if (deleted) {
                                JOptionPane.showMessageDialog(DeleteUserForm.this,
                                        "Użytkownik został usunięty.",
                                        "Sukces",
                                        JOptionPane.INFORMATION_MESSAGE);
                                refreshUserList();
                            } else {
                                JOptionPane.showMessageDialog(DeleteUserForm.this,
                                        "Nie udało się usunąć użytkownika.",
                                        "Błąd",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(DeleteUserForm.this,
                                    "Błąd podczas usuwania użytkownika: " + ex.getMessage(),
                                    "Błąd",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            buttonsPanel.add(deleteButton);

            add(infoPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.EAST);
        }
    }
}