import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.*;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.JButton;
import javax.swing.JList;

public class Fines extends JFrame{

    private JPanel contentPane;
    private JButton btnUpdate;
    private JButton btnPayment;
    private JButton btnBack;
    private JTextField loanID;
    private JTextField amount;
    private JLabel paymentLabel;
    private JLabel loanLabel;
    private JLabel amountLabel;


    public Fines() {

        setTitle("Fines");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 350, 450, 300);
        setVisible(true);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnBack = new JButton("Back");
        btnBack.setBounds(380, 25, 50, 20);
        contentPane.add(btnBack);
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Home h = new Home();
                h.setVisible(true);
            }
        });

        paymentLabel = new JLabel("Enter Fine Payment");
        paymentLabel.setBounds(40, 30, 180, 30);
        contentPane.add(paymentLabel);

        loanLabel = new JLabel("Loan ID");
        loanLabel.setBounds(42, 80, 150, 20);
        contentPane.add(loanLabel);

        loanID = new JTextField();
        loanID.setBounds(190, 80, 150, 25);
        contentPane.add(loanID);

        amountLabel = new JLabel("Payment Amount ($)");
        amountLabel.setBounds(42, 135, 150, 20);
        contentPane.add(amountLabel);

        amount = new JTextField();
        amount.setBounds(190, 135, 150, 25);
        contentPane.add(amount);

        btnPayment = new JButton("Pay Fine");
        btnPayment.setBounds(35, 180, 120, 30);
        contentPane.add(btnPayment);
        btnPayment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!(loanID.getText().isEmpty() || amount.getText().isEmpty())) {
                    String id = loanID.getText();
                    String amountPay = amount.getText();
                    float amountFloat = Float.parseFloat(amountPay);

                    try {
                        databaseController db = new databaseController();
                        db.enterPayment(id, amountFloat);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnUpdate = new JButton("Update Fines");
        btnUpdate.setBounds(35, 230, 120, 30);
        contentPane.add(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    databaseController db = new databaseController();
                    db.update();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}