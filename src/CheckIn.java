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


public class CheckIn extends JFrame {

	private JFrame mainFrame;
	private JPanel contentPane;
	private Object Color;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckIn frame = new CheckIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CheckIn() {
		
		JTextField isbnText;
		JTextField tfText1;
		
		setTitle("Check In");
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
		
		//Borrower's name Label
		JLabel borrowerLabel=new JLabel("Borrower's Name. :",JLabel.LEFT);
		borrowerLabel.setFont(new Font("Times New Roman",Font.BOLD,16));
		GridBagConstraints borrowerLabel_gbc=new GridBagConstraints();
		borrowerLabel_gbc.gridx=0;
		borrowerLabel_gbc.gridy=4;
		contentPane.add(borrowerLabel, borrowerLabel_gbc);
					
		//Borrower's name Text Field
		JTextField borrowerText = new JTextField();
		borrowerText.setFont(new Font("Ariel",Font.PLAIN,16));
		GridBagConstraints gbc_borrowerText =new GridBagConstraints();
		gbc_borrowerText.fill=GridBagConstraints.HORIZONTAL;
		gbc_borrowerText.insets=new Insets(0,0,5,0);
		gbc_borrowerText.gridx=1;
		gbc_borrowerText.gridy=4;
		contentPane.add(borrowerText, gbc_borrowerText);
		borrowerText.setColumns(15);
				

		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setContentPane( contentPane );
		setVisible( true );
		
		JButton checkIn=new JButton("Check In");

		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setContentPane( contentPane );
		setVisible( true );
		   
		checkIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(isbnText.getText().equals("") && cardText.getText().equals("") && borrowerText.getText().equals(""))
				{ 
					JOptionPane.showMessageDialog(null, "Please enter all values to proceed!");
				}
				else{
					
					try{
							
						new ViewCheckedInBooks(isbnText.getText(), cardText.getText(), borrowerText.getText());	
						isbnText.setText("");
						cardText.setText("");
						borrowerText.setText("");
					}
					catch(Exception ex) {
						System.out.println("Error in connection 1: " + ex.getMessage());
					}
				
				}
			}
		});
		   
		//Constraints for check out button
		GridBagConstraints gbc_btnCheckIn=new GridBagConstraints();
		gbc_btnCheckIn.fill=GridBagConstraints.HORIZONTAL;
		gbc_btnCheckIn.gridx = 0;
		gbc_btnCheckIn.gridy = 6;
		gbc_btnCheckIn.gridwidth=2;
		contentPane.add(checkIn,gbc_btnCheckIn);
				
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
		gbc_btnClose.gridy = 8;
		gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
		gbc_btnClose.gridwidth=2;
		contentPane.add(back, gbc_btnClose);	
	}
}
	


