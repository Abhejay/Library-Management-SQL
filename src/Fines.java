import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import java.sql.*;

import javax.swing.border.EmptyBorder;
import javax.swing.*;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Fines extends JFrame{

    private JPanel contentPane;
    private JButton btnUpdate;
    private JButton btnPayment;
    private JButton btnBack;
    private JButton btnDisplay;
    private JTextField loanID;
    private JTextField amount;
    private JLabel paymentLabel;
    private JLabel card_idLabel;
    private JLabel amountLabel;


    public Fines() {

        setTitle("Fines");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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

        card_idLabel = new JLabel("Card ID");
        card_idLabel.setBounds(42, 80, 150, 20);
        contentPane.add(card_idLabel);

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
        btnPayment.setBounds(35, 170, 120, 30);
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
                        JOptionPane.showMessageDialog(null, "Payment processed");
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnUpdate = new JButton("Update Fines");
        btnUpdate.setBounds(35, 200, 120, 30);
        contentPane.add(btnUpdate);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    databaseController db = new databaseController();
                    db.update();
                    JOptionPane.showMessageDialog(null, "Fines updated");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        btnDisplay = new JButton("Display Fines");
		btnDisplay.setBounds(35, 230, 120, 30);
		contentPane.add(btnDisplay);
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					databaseController db = new databaseController();
					db.displayFines();
					
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
    }

}
