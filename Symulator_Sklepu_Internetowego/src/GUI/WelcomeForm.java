package GUI;

import javax.swing.*;

public class WelcomeForm extends JFrame{
    private JPanel panel1;
    private JProgressBar progressBar1;

    public WelcomeForm() {
        super("Witaj");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        progression();

        dispose();
        LoginForm menuForm = new LoginForm();
    }

    //Progresja paska ładowania
    private void progression() {
        int count = 0;
        while (count < 100) {
            this.progressBar1.setValue(count);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count+=5;
        }
    }
}
