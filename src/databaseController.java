//SQL JAR import [Located in lib directory
import com.mysql.cj.jdbc.MysqlDataSource;

//Imports
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class databaseController {

    //Global Connection
    private static Connection con = null;

    //Frame for search
    public static JFrame frame_two = new JFrame("Results");
    public static JTable table;
    public static DefaultTableModel defaultTableModel;

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


    //SEARCH BOOK FUNCTION -- COMPLETED
    public static void executeBookSearch(String bookEntry) {
        frame_two.setLayout(new FlowLayout());
        frame_two.setSize(1000, 1000);

        //setting the properties of jtable and defaulttablemodel
        defaultTableModel = new DefaultTableModel();
        table = new JTable(defaultTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 100));
        table.setFillsViewportHeight(true);
        frame_two.add(new JScrollPane(table));
        defaultTableModel.addColumn("ISBN10");
        defaultTableModel.addColumn("TITLE");
        defaultTableModel.addColumn("AUTHOR(S)");
        defaultTableModel.addColumn("Availability");


        JButton btnHome = new JButton("Home");
        btnHome.setBounds(10, 11, 89, 23);
        frame_two.add(btnHome);
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame_two.dispose();
                frame_two.getContentPane().removeAll();
                frame_two.repaint();
                Home h = new Home();
                h.setVisible(true);
            }
        });

        try {
            int searchOptions = 0;

            String sql = "SELECT * FROM sys.BOOKS WHERE (Title LIKE '%" + bookEntry + " %') OR ISBN10 LIKE '%"+ bookEntry + "%' OR Author LIKE '%" + bookEntry + "%'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();


            Vector<String> checkIsbn = new Vector<String>();
            String sqlTwo = "SELECT Isbn FROM sys.BOOK_LOANS";
            PreparedStatement pstmtTwo  = con.prepareStatement(sqlTwo);
            ResultSet rsTwo = pstmtTwo.executeQuery();
            while (rsTwo.next()) {
                checkIsbn.add(rsTwo.getString(1));
            }

            Vector<String> checkAvail = new Vector<String>();
            String sqlThree = "SELECT avail FROM sys.BOOK_LOANS";
            PreparedStatement pstmtThree = con.prepareStatement(sqlThree);
            ResultSet rsThree = pstmtThree.executeQuery();
            while (rsThree.next()) {
                checkAvail.add(rsThree.getString(1));
            }

            for (int i = 0; i < checkIsbn.size(); i++) {
                System.out.println(checkAvail.get(i) + " ");
            }

            while (rs.next()) {

                String availTrue = "true";

                searchOptions = 1;
                int availCheck = 0;
                String isbn10 = rs.getString("ISBN10");
                String title = rs.getString("Title");
                String authors = rs.getString("Author");

                for (int i = 0; i < checkIsbn.size(); i++) {
                    if (isbn10.equals(checkIsbn.get(i)) && !availTrue.equals(checkAvail.get(i))) {
                        availCheck = 1;
                        defaultTableModel.addRow(new Object[]{isbn10, title, authors, "Not Available"});
                    }
                }

                if (availCheck == 0)
                    defaultTableModel.addRow(new Object[]{isbn10, title, authors, "Available"});

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment( JLabel.CENTER );
                table.setDefaultRenderer(String.class, centerRenderer);
                frame_two.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame_two.setVisible(true);
                frame_two.validate();
            }

            if (searchOptions == 0) {
                JOptionPane.showMessageDialog(null, "No Search Results");
                Home home = new Home();
                home.setVisible(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    //ADD BORROWER FUNCTION -- COMPLETED
    public static void addBorrower(String ssn, String firstName, String lastName, String email, String address, String city, String state, String phone) {

        String currentID = "";
        String newID = "";

        try {
            Statement stmt  = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM sys.BORROWER WHERE Ssn in ('"+ssn+"')");

            if (rs1.next()) {
                JOptionPane.showMessageDialog(null, "The borrower already exists in the system");
            }
            else {
                Statement stmt2  = con.createStatement();
                ResultSet rs = stmt2.executeQuery("SELECT MAX(Card_id) FROM sys.BORROWER"); //RETURNS ID001000
                if (rs.next()) {
                    currentID = rs.getString(1);
                }

                String temp = currentID.substring(4, currentID.length());
                int tempInt = Integer.parseInt(temp);
                tempInt++;
                newID = "ID00" + tempInt;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Statement stmt  = con.createStatement();
            stmt.executeUpdate("INSERT INTO sys.BORROWER (Card_id, Ssn, firstName, lastName, email, address, city, state, phone) VALUE ('"+newID+"','"+ssn+"','"+firstName+"','"+lastName+"','"+email+"','"+address+"','"+city+"','"+state+"','"+phone+"')");
            JOptionPane.showMessageDialog(null, "New Borrower Created");
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
                                createBookLoan(isbn, cardId, date, dateDue, "false");
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

    public static void createBookLoan(String isbn, String cardId, String date, String dueDate, String availability) throws SQLException {
        Statement statement7 = con.createStatement();
        ResultSet rs= statement7.executeQuery("select max(Loan_id) from sys.BOOK_LOANS");
        int loan_id=0;
        if(rs.next())
        {
            loan_id=rs.getInt(1)+1;
        }

        String query="INSERT INTO sys.BOOK_LOANS (Loan_id, Isbn, Card_id, Date_out, Due_date, avail) VALUES ("+loan_id+", '"+isbn+"', '"+cardId+"', '"+date+"', '"+dueDate+"', '"+availability+"');";
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
            new Fines();
        }
        else {
            JOptionPane.showMessageDialog(null, "The Book has been checked in");
            Statement statement11=con.createStatement();
            //Indicate that book is available
            statement11.executeUpdate("Update sys.BOOK_LOANS set avail='true' where Loan_id="+loan_id+";");
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


    public static void update() {
        try {
            Statement stmt = con.createStatement();

            // Increment existing fines by $0.25 if Paid == false
            String sql1 = "UPDATE sys.FINES "
                    + "SET Fine_amt = Fine_amt + 0.25 "
                    + "WHERE Paid = false; ";
            stmt.executeUpdate(sql1);

            // If late book has been returned, insert into FINES table with Fine_amt value based on date in - due date
            String sql2 = "INSERT IGNORE INTO sys.FINES "
                    + "SELECT bl.Loan_id, DATEDIFF(bl.Date_in, bl.Due_date)*0.25, false "
                    + "FROM sys.BOOK_LOANS AS bl "
                    + "WHERE Date_in IS NOT NULL AND (SELECT DATEDIFF(Date_in, Due_date)) > 0; ";
            stmt.executeUpdate(sql2);

            // If late book is still out, insert into FINES table with Fine_amt value based on current date - due date
            String sql3 = "INSERT IGNORE INTO sys.FINES "
                    + "SELECT bl.Loan_id, DATEDIFF(CURDATE(), bl.Due_date)*0.25, false "
                    + "FROM sys.BOOK_LOANS AS bl "
                    + "WHERE Date_in IS NULL AND (SELECT DATEDIFF(CURDATE(), Due_date)) > 0; ";
            stmt.executeUpdate(sql3);

            // After each update, filter out previously paid fines
            String sql4 = "DELETE FROM sys.FINES WHERE Paid = true; ";
            stmt.executeUpdate(sql4);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterPayment(String Loan_id, float amount) {
        try {
            Statement stmt = con.createStatement();

            // Update Fine_amt of loan_id entered by user by amount entered by user only if the book has been returned
            String sql1 = "UPDATE sys.FINES AS fi SET Fine_amt = Fine_amt - '"+amount+"' "
                    + "WHERE fi.Loan_id = '"+Loan_id+"' AND fi.Loan_id = ( "
                    + "SELECT Loan_id "
                    + "FROM sys.BOOK_LOANS AS bl "
                    + "WHERE Date_in IS NOT NULL AND bl.Loan_id = fi.Loan_id);";
            stmt.executeUpdate(sql1);

            // Update Paid to true if book loan has been paid off
            String sql2 = "UPDATE sys.FINES "
                    + "SET Paid = true "
                    + "WHERE Fine_amt <= 0;";
            stmt.executeUpdate(sql2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayFines() {
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        JFrame tableFrame = new JFrame();
        JTable table = new JTable(defaultTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(300, 600));
        table.setFillsViewportHeight(true);

        tableFrame.setSize(300, 600);
        tableFrame.add(new JScrollPane(table));
        tableFrame.setVisible(true);



        defaultTableModel.addColumn("Card_id");
        defaultTableModel.addColumn("Total_fines");

        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT bl.Card_id, SUM(fi.Fine_amt) AS Total_fines "
                    + "FROM sys.FINES AS fi INNER JOIN sys.BOOK_LOANS as bl "
                    + "ON fi.Loan_id = bl.Loan_id "
                    + "GROUP BY bl.Card_id; ";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String card_id = rs.getString("Card_id");
                float total_fines = rs.getFloat("Total_fines");

                defaultTableModel.addRow(new Object[] {card_id, total_fines});
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                table.setDefaultRenderer(String.class, centerRenderer);
                tableFrame.setVisible(true);
                tableFrame.validate();

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    //ADD YOU FUNCTIONS AND MAKE A CALL OF THIS IN THE CLASS YOU ARE IN
    //DOING THIS MAKES THE CODE LESS MESSY
}