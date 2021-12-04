import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.sql.Connection;


public class CheckOut extends JFrame {

	private JPanel contentPane;
	private Object Color;
	static Connection conn;

	// Launch the Check Out window
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckOut frame = new CheckOut();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Run Check Out
	public CheckOut() {
		
		JTextField isbnText;
		JTextField tfText1;
		
		setTitle("Check Out");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Content Panel
		contentPane = new JPanel();
		GridBagLayout gbPanel0 = new GridBagLayout();
		contentPane.setLayout( gbPanel0 );
		
		//Isbn text field
		isbnText = new JTextField( );
		isbnText.setFont(new Font("Ariel",Font.PLAIN,16));
		GridBagConstraints gbc_isbnText=new GridBagConstraints();
		gbc_isbnText.fill=GridBagConstraints.HORIZONTAL;
		gbc_isbnText.gridx=1;
		gbc_isbnText.gridy=2;
		contentPane.add(isbnText, gbc_isbnText);
		isbnText.setColumns(15);

		tfText1 = new JTextField( );
		isbnText.setFont(new Font("Ariel",Font.PLAIN,16));
		GridBagConstraints gbc_CardNo=new GridBagConstraints();
		gbc_CardNo.fill=GridBagConstraints.HORIZONTAL;
		gbc_CardNo.gridx=1;
		gbc_CardNo.gridy=2;
		contentPane.add(isbnText, gbc_CardNo);
		isbnText.setColumns(15);

		//Isbn Label
		JLabel IsbnLabel=new JLabel("     ISBN :",JLabel.LEFT);
		IsbnLabel.setFont(new Font("Times New Roman",Font.BOLD,16));
		GridBagConstraints IsbnLabel_gbc=new GridBagConstraints();
		IsbnLabel_gbc.gridx=0;
		IsbnLabel_gbc.gridy=2;
		contentPane.add(IsbnLabel, IsbnLabel_gbc);
			
		//Card Label
		JLabel cardLabel=new JLabel("Card No. :",JLabel.LEFT);
		cardLabel.setFont(new Font("Times New Roman",Font.BOLD,16));
		GridBagConstraints cardLabel_gbc=new GridBagConstraints();
		cardLabel_gbc.gridx=0;
		cardLabel_gbc.gridy=3;
		contentPane.add(cardLabel, cardLabel_gbc);
			
		//Card Text Field
		JTextField cardText = new JTextField();
		cardText.setFont(new Font("Ariel",Font.PLAIN,16));
		GridBagConstraints gbc_cardText =new GridBagConstraints();
		gbc_cardText.fill=GridBagConstraints.HORIZONTAL;
		gbc_cardText.insets=new Insets(0,0,5,0);
		gbc_cardText.gridx=1;
		gbc_cardText.gridy=3;
		contentPane.add(cardText, gbc_cardText);
		cardText.setColumns(15);
		
		//Check Out button
		JButton checkOut=new JButton("Check Out");

		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setContentPane( contentPane );
		setVisible( true );
		   
		//Actions to perform when the Check Out button is clicked
		checkOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(isbnText.getText().equals("") || cardText.getText().equals("") )
				{
					JOptionPane.showMessageDialog(null, "Please enter all values to proceed!");
				}
				else{
					try{
						String isbn=isbnText.getText();
						String cardId=cardText.getText();
						
						//Create an object of databaseController to establish connection with MySQL database
						databaseController controller = new databaseController();
						
						//Call the checkout method to perform the action
						controller.checkOut(isbn, cardId);
						isbnText.setText("");
						cardText.setText("");
					
						}
						catch(Exception ex) {
							System.out.println("Error in connection 1: " + ex.getMessage());
					}
				
				}
			}
		});
		
		//Constraints for check out button
		GridBagConstraints gbc_btnCheckOut=new GridBagConstraints();
		gbc_btnCheckOut.fill=GridBagConstraints.HORIZONTAL;
		gbc_btnCheckOut.gridx = 0;
		gbc_btnCheckOut.gridy = 4;
		gbc_btnCheckOut.gridwidth=2;
		contentPane.add(checkOut,gbc_btnCheckOut);
		
		//Back button
		JButton back = new JButton("Back");
		back.setBounds(400, 144, 131, 23);
		contentPane.add(back);
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				new GUI();
			}
		});
			
		JLabel space2=new JLabel("  ",JLabel.CENTER);
		GridBagConstraints gbc_space2=new GridBagConstraints();
		gbc_space2.gridx=0;
		gbc_space2.gridy=5;
		gbc_space2.gridwidth=2;
		contentPane.add(space2, gbc_space2);
		
		GridBagConstraints gbc_btnClose=new GridBagConstraints();
		gbc_btnClose.fill=GridBagConstraints.HORIZONTAL;
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 6;
		gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
		gbc_btnClose.gridwidth=2;
		contentPane.add(back, gbc_btnClose);		

	}
}


