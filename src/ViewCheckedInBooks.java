import java.awt.*;
import java.util.Date;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.sql.Connection;

public class ViewCheckedInBooks extends JFrame
{
    JFrame frame;
    JPanel contentPane;
    static Connection conn=null;
    JTable table;

    ViewCheckedInBooks(String isbn, String card, String borrower) throws SQLException
    {
        frame=new JFrame("Library Management System");
        frame.setSize(500,500);
        frame.setLocation(20, 50);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(contentPane);

        GridBagLayout gbc_contentPane=new GridBagLayout();
        gbc_contentPane.columnWidths=new int[]{0,0};
        gbc_contentPane.rowHeights = new int[]{0, 0, 0};
        gbc_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbc_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbc_contentPane);


        JLayeredPane layer=new JLayeredPane();
        GridBagConstraints gbc_layer = new GridBagConstraints();
        gbc_layer.insets = new Insets(0, 0, 5, 0);
        gbc_layer.fill = GridBagConstraints.BOTH;
        gbc_layer.gridx = 0;
        gbc_layer.gridy = 0;
        contentPane.add(layer, gbc_layer);

        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setBounds(6,6,1200,600);
        layer.add(scrollPane);

        table=new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        scrollPane.setViewportView(table);
        databaseController controller = new databaseController();

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent evnt)
            {
                if(evnt.getClickCount() ==1 || evnt.getClickCount()==2)
                {
                    //System.out.println(loan_id);
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    int loan_id=(int)table.getModel().getValueAt(table.rowAtPoint(evnt.getPoint()), 0);
                    try
                    {
                        controller.checkIn(loan_id, date);
                        frame.setVisible(false);
                    }
                    catch(SQLException e)
                    {
                        System.out.println("ERROR: "+e.getMessage());
                    }
                }
            }
        });

        //Close Button
        JButton close= new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Home h = new Home();
                h.setVisible(true);
            }
        });

        GridBagConstraints gbc_btnClose = new GridBagConstraints();
        gbc_btnClose.fill=GridBagConstraints.HORIZONTAL;
        gbc_btnClose.insets = new Insets(0, 0, 5, 0);
        gbc_btnClose.gridx = 0;
        gbc_btnClose.gridy = 2;
        gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
        contentPane.add(close, gbc_btnClose);
        try{

            String[] columnNames = {"Loan_id", "Card_id", "Isbn"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            ResultSet result = controller.checkInMethods(isbn, card, borrower);
            while(result.next()) {
                int loan_id = result.getInt("Loan_id");
                String card_id = result.getString("Card_id");
                String Isbn = result.getString("Isbn");

                Object[] data = {loan_id, card_id, Isbn};

                tableModel.addRow(data);
            }
            table.setModel(tableModel);
            table.setEnabled(false);

            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        catch(SQLException e)
        {
            System.out.println("ERROR: "+ e.getMessage());
        }

        frame.add(contentPane);
        frame.setVisible(true);
    }
}
