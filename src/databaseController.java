import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.MysqlDataSource;

public class databaseController {


    private static Connection con = null;

    //CONSTRUCTOR
    public databaseController() {
        executeController();
    }

    //CONNECTS TO DATABASE
    public static void executeController() {

        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("admin");
            dataSource.setPassword("groupllama");
            dataSource.setServerName("library-management.cupcvhuzco7w.us-east-2.rds.amazonaws.com");

            con = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void checkOut(String isbn, String cardId) {
    	
    
    	try{
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 14);
			String dateDue = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				        
			Statement statement1= con.createStatement();
			ResultSet rs1 = statement1.executeQuery("select * from sys.BOOKS where Isbn10='"+isbn+"';");
			Statement statement2 = con.createStatement();
			ResultSet rs2 = statement2.executeQuery("select * from sys.BORROWER where Card_id='"+cardId+"';");

			if(rs1.next()&&rs2.next())
			{
				Statement statement3 = con.createStatement();
				ResultSet rs3 = statement3.executeQuery("select * from sys.BOOK_LOANS where Isbn='"+isbn+"' and Date_in is null;");
				

				if(rs3.next())
				{
					JOptionPane.showMessageDialog(null, "This book has already been issued");	
					
				}
				else
				{
					Statement statement4 = con.createStatement();
					ResultSet rs4 = statement4.executeQuery("select * from sys.BOOK_LOANS where Card_id='"+cardId+"' and Date_in is null and Due_date<CAST(CURRENT_TIMESTAMP AS DATE);");
					if(rs4.next())
					{
						JOptionPane.showMessageDialog(null, "Can not issue book since borrower already has a book that is overdue");	
						
					}
					else
					{
						if(maxBookLoans(cardId)){
							JOptionPane.showMessageDialog(null, "Can not issue book since borrower already has 3 book loans");
							
						}
						else
						{
							if(checkFines(cardId)){
								JOptionPane.showMessageDialog(null, "Can not issue book since borrower has pending fines to pay");
							
							}
							else{
								createBookLoan(isbn, cardId, date, dateDue);
								JOptionPane.showMessageDialog(null, "Book has been issued. New Book Loan Created!");
						
							}
						}
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "ISBN or Card ID is invalid");	
				
			}
    	} catch(SQLException ex) {
    		System.out.println("Error in connection 2: " + ex.getMessage());
    	}
	
		}
    
    public static Boolean maxBookLoans(String cardId) throws SQLException {
    	Statement statement5 = con.createStatement();
		ResultSet rs5 = statement5.executeQuery("select count(*) from sys.BOOK_LOANS where Card_id='"+cardId+"' and Date_in is null;");
		rs5.next();
		if(rs5.getInt(1)>=3)
		{
			return true;
			
		}
		
		return false;
    }
    
    public static Boolean checkFines(String cardId) throws SQLException {
    	Statement statement6 = con.createStatement();
		ResultSet rs6 = statement6.executeQuery("select * from sys.BOOK_LOANS AS B, sys.FINES as F where Card_id='"+cardId+"' and B.Loan_id=F.Loan_id and paid=0;");	
		if(rs6.next())
		{
			return true;
		}
		
		return false;
    }
    
    public static void createBookLoan(String isbn, String cardId, String date, String dueDate) throws SQLException {
    	Statement statement7 = con.createStatement();
		ResultSet rs= statement7.executeQuery("select max(Loan_id) from sys.BOOK_LOANS");
		int loan_id=0;
		if(rs.next())
		{				 	
			loan_id=rs.getInt(1)+1;
		}

		String query="INSERT INTO sys.BOOK_LOANS (Loan_id, Isbn, Card_id, Date_out, Due_date) VALUES ("+loan_id+", '"+isbn+"', '"+cardId+"', '"+date+"', '"+dueDate+"');";
		Statement statement8 = con.createStatement();
		statement8.executeUpdate(query);
    }
    
    public static void checkIn(int loan_id, String date) throws SQLException {
		Statement statement9=con.createStatement();
		statement9.executeUpdate("Update sys.BOOK_LOANS set Date_in='"+date+"' where Loan_id="+loan_id+";");
		
		Statement statement10=con.createStatement();
		ResultSet rs= statement10.executeQuery("Select * from sys.BOOK_LOANS where Loan_id="+loan_id+" and Due_date<Date_in;");
		
		if(rs.next()) {
			JOptionPane.showMessageDialog(null, "The Book is overdue, and fines are to be paid");
			//new Fines();
		}
		else {
        	JOptionPane.showMessageDialog(null, "The Book has been checked in");
        	new CheckIn();
        }
    }
    
    public static ResultSet checkInMethods(String isbn, String card, String borrower) throws SQLException {
    	Statement stmt=con.createStatement();
		ResultSet rs;
		
		if(card.equals("") && borrower.equals("")){
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn='"+isbn+"' and Date_in is null;");
		}
		else if(isbn.equals("") && borrower.equals("")){
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id='"+card+"' and Date_in is null;");
		}
		else if(isbn.equals("") && card.equals("")){
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id in (Select Card_id from sys.borrower where Bname like '%"+borrower+"%') and Date_in is null;");
		}
		else if(borrower.equals("")){
			
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn='"+isbn+"' and Card_id='"+card+"' and Date_in is null;");
			System.out.println(rs.getString("Card_id"));
			
		}
		else if(isbn.equals("")){
			rs=stmt.executeQuery("(Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Card_id='"+card+"' and Date_in is null)");
		}
		else if(card.equals("")){
			rs=stmt.executeQuery("(Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn='"+isbn+"' and Card_id in (Select Card_id from sys.borrower where Bname like '%"+borrower+"%') and Date_in is null)");
		}
		else{
			rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from sys.BOOK_LOANS where Isbn='"+isbn+"' and Card_id='"+card+"' and Date_in is null;");
		}
		
		return rs;
    }


    //SEARCH BOOK FUNCTION -- YET TO BE COMPLETED
    public static Vector<String> executeBookSearch(String command) {
        Vector<String> books = new Vector<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Title FROM sys.BOOKS");

            while (rs.next()) {
                System.out.println(rs.getString("Title"));
                books.add(rs.getString("Title"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }


    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY

}

















