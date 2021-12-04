import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Home extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Home() {
		setTitle("Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(99, 46, 310, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(10, 45, 89, 23);
		contentPane.add(btnSearch);
		btnSearch.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().isEmpty()) {
					try {
						dispose();
						databaseController db = new databaseController();
						db.executeBookSearch(textField.getText());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} );



		JButton btnBorrower = new JButton("New Borrower");
		btnBorrower.setBounds(78, 144, 131, 23);
		contentPane.add(btnBorrower);
		btnBorrower.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				CreateBorrower b = new CreateBorrower();
				b.setVisible(true);
			}
		});

		JButton btnCheckIn = new JButton("Check-In");
		btnCheckIn.setBounds(232, 144, 131, 23);
		contentPane.add(btnCheckIn);
		btnCheckIn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				CheckIn c = new CheckIn();
				c.setVisible(true);
			}
		});

		JButton btnCheckOut = new JButton("Check-Out");
		btnCheckOut.setBounds(78, 178, 131, 23);
		contentPane.add(btnCheckOut);
		btnCheckOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				CheckOut c = new CheckOut();
				c.setVisible(true);
			}
		});

		JButton btnFines = new JButton("Fines");
		btnFines.setBounds(232, 178, 131, 23);
		contentPane.add(btnFines);
		btnFines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Fines f = new Fines();
				f.setVisible(true);
			}
		});
	}

}