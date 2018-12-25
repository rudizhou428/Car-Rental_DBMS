import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.text.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * This program demonstrates how to make database connection with Oracle

 *
 */
public class CarRentalDB extends JFrame implements ActionListener {

	static Connection conn1 = null;
	Container frame = getContentPane ();
	private JPanel title,mid,bottom;
	private JLabel CarRentalDB = new JLabel ("Car Rental DBMS", SwingConstants.CENTER);
	private JButton btnDrop, btnQuery, btnAdd, btnInsert, btnExit, btnClear, btnRemove;
	private static JTable output;
	private static String table;
	private static JPopupMenu popupMenu;
	private static JMenuItem deleteRow;
	private static JMenuItem insertRow;
	//private JCheckBox car, caris, booking, cartype, customer, delivers, getbookinginfo, rentalrates, shop, visits ;

	public CarRentalDB(Connection conn)
	{
		super ("CarRentalDB");   // Set the frame's name
		conn1 = conn;

		CarRentalDB.setFont (new Font ("Comic Sans", Font.BOLD, 36));
		CarRentalDB.setForeground (Color.RED);

		btnDrop = new JButton ("Drop Tables");
		btnQuery = new JButton ("Query Tables");
		btnAdd = new JButton ("Create Tables");
		btnInsert = new JButton ("Insert Data Into Tables");
		btnExit= new JButton ("Close DBMS");
		btnRemove = new JButton ("Remove Record From Table");
		btnClear = new JButton ("Clear Output");

		title = new JPanel ();
		mid = new JPanel ();
		bottom = new JPanel();

		popupMenu = new JPopupMenu();
		insertRow = new JMenuItem("Insert Record");
		deleteRow = new JMenuItem("Delete Current Record");
		popupMenu.add(insertRow);
		popupMenu.add(deleteRow);

		output = new JTable();
		output.setFont(new Font("Serif", Font.PLAIN, 20));
		output.setComponentPopupMenu(popupMenu);
		output.setRowHeight(60);

		/*
		car = new JCheckBox("Table CAR"); 
		caris = new JCheckBox("Table CARIS"); 
		booking = new JCheckBox("Table BOOKING"); 
		cartype = new JCheckBox("Table CARTYPE"); 
		customer = new JCheckBox("Table CUSTOMER"); 
		delivers = new JCheckBox("Table DELIVERS"); 
		getbookinginfo = new JCheckBox("Table GETBOOKINGINFO"); 
		rentalrates = new JCheckBox("Table RENTALRATES"); 
		shop = new JCheckBox("Table SHOP"); 
		visits = new JCheckBox("Table VISITS"); 
		 */


		title.setBackground (Color.lightGray);
		title.setLayout (new BorderLayout ());
		title.add (CarRentalDB, BorderLayout.NORTH);


		bottom.setBackground (Color.BLUE);
		bottom.setLayout (new FlowLayout ());
		bottom.add (btnDrop);
		bottom.add (btnQuery);
		bottom.add (btnAdd);
		bottom.add (btnInsert);
		bottom.add(btnRemove);
		bottom.add(btnClear);
		bottom.add(btnExit);


		mid.add(output);

		/*
		mid.add(car);
		mid.add(caris);
		mid.add(booking);
		mid.add(cartype);
		mid.add(customer);
		mid.add(delivers);
		mid.add(getbookinginfo);
		mid.add(rentalrates);
		mid.add(shop);
		mid.add(visits);
		 */

		frame.setLayout (new BorderLayout ());
		frame.add (title, BorderLayout.NORTH);
		frame.add (mid, BorderLayout.CENTER);
		frame.add (bottom, BorderLayout.SOUTH);
		//add stuff to the frame

		deleteRow.addActionListener(new actionDeleteRow());
		insertRow.addActionListener(new actionInsertRecord());

		output.addMouseListener(new TableMouseListener(output));


		btnExit.addActionListener(this);
		btnInsert.addActionListener(new actionInsertable());
		btnAdd.addActionListener(new actionAddTable());
		btnDrop.addActionListener(new actionDropTable());
		btnQuery.addActionListener(new actionViewQuery()); 
		btnClear.addActionListener(new actionClearOutput()); 
		//add some action listeners


		setResizable (false);
		setSize (1300, 850);     // Set the frame's size
		setVisible (true);              // Show the frame
		setLocationRelativeTo (null);
		//set size, make it visible, make it so the size is unchangeable, center the frame



	}
	public static void dropTables()
	{
		String query1 = "DROP TABLE VISITS";

		String query2 = "DROP TABLE GETBOOKINGINFO";

		String query3 = "DROP TABLE DELIVERS";

		String query4 = "DROP TABLE CARIS";

		String query5 = "DROP TABLE CARTYPE";

		String query6 = "DROP TABLE RENTALRATES";

		String query7 = "DROP TABLE BOOKING";

		String query8 = "DROP TABLE SHOP";

		String query9 = "DROP TABLE CAR";

		String query10 = "DROP TABLE CUSTOMER";




		try (Statement stmt = conn1.createStatement()) {
			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3); 
			stmt.executeUpdate(query4);
			stmt.executeUpdate(query5); 	  
			stmt.executeUpdate(query6);
			stmt.executeUpdate(query7); 
			stmt.executeUpdate(query8);
			stmt.executeUpdate(query9);
			stmt.executeUpdate(query10); 

			DefaultTableModel model = (DefaultTableModel) output.getModel();
			model.setRowCount(0);

			JOptionPane.showMessageDialog(null, "Tables sucesfully dropped!");
		}

		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error dropping tables");
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		}


	}

	public static void createTables()
	{
		String query1 = "CREATE TABLE car (VIN varchar2(50) NOT NULL, Brand varchar2(20), Condition varchar2(10), CarYear varchar2(4), Colour varchar2(10),PRIMARY KEY (VIN))";

		String query2 = "CREATE TABLE booking (BookingID int, ReturnCondition varchar2(20), ReturnLocation varchar2(20), PickupLocation varchar2(20), PRIMARY KEY (BookingID))";

		String query3 = "CREATE TABLE customer (CustomerID int, FName varchar2(20), LName varchar2(20),PhoneNumber varchar2(12), Email varchar2(30), Country varchar(20),PRIMARY KEY (CustomerID))";

		String query4 = "CREATE TABLE delivers (VIN varchar2(50) REFERENCES car(VIN),bookingid int REFERENCES booking(bookingid),delivered varchar2(1),PRIMARY KEY (VIN, bookingid))";

		String query5 = "CREATE TABLE shop (StoreID int, PhoneNumber varchar2(12), Country varchar2(20), City varchar2(20), Streeet varchar2(20), PRIMARY KEY (StoreID))";

		String query6 = "CREATE TABLE cartype (ModelName varchar(5), CategoryType varchar2(12), Description varchar2(50),PRIMARY KEY (ModelName))";

		String query7 = "CREATE TABLE rentalrates (BookingID int REFERENCES booking(BookingID),WeeklyRates int, LateFees int, DailyRates int, Discount int, Penalty int,Primary KEY(BookingID))"; 

		String query8 = "CREATE TABLE visits (CustomerID int REFERENCES customer(CustomerID), StoreID int REFERENCES shop(StoreID), PRIMARY KEY (customerID, StoreID))";

		String query9 = "CREATE TABLE getbookinginfo (CustomerID int REFERENCES customer(CustomerID), BookingID int REFERENCES booking(BookingID),PRIMARY KEY (customerID, BookingID))";

		String query10 = "CREATE TABLE caris (VIN varchar2(50) REFERENCES car(VIN),ModelName varchar2(50), PRIMARY KEY (VIN))";


		try (Statement stmt = conn1.createStatement()) {
			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3); 
			stmt.executeUpdate(query4);
			stmt.executeUpdate(query5); 	  
			stmt.executeUpdate(query6);
			stmt.executeUpdate(query7); 
			stmt.executeUpdate(query8); 
			stmt.executeUpdate(query9); 
			stmt.executeUpdate(query10); 
			JOptionPane.showMessageDialog(null,"Tables successfully created!");



		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error creating the tables");
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		} 



	}

	public static void insertRecord(String table)
	{
		if (table.equalsIgnoreCase(""))
		{
			JOptionPane.showMessageDialog(null, "Please select a table before adding a new record");
		}




		DefaultTableModel aModel = (DefaultTableModel) output.getModel();
		Object rowDataObj = aModel.getDataVector().elementAt(output.getSelectedRow());
		String[] rowDataArray = rowDataObj.toString().split(",");
		String[] colArr = new String[rowDataArray.length];
		String colNames = "";
		String queryNames = "";

		for (int i = 0 ; i < colArr.length ; i++)
		{
			colArr[i] =  output.getModel().getValueAt(0, i).toString() + ", ";
			colNames = colNames + colArr[i];
		}

		colNames = colNames.replaceAll(", $", "");


		try (Statement stmt = conn1.createStatement()) {

			String[] queryParts = new String[rowDataArray.length];

			ResultSet rs = stmt.executeQuery("SELECT " +  colNames + " from " + table);
			java.sql.ResultSetMetaData rsmd;
			rsmd = rs.getMetaData();
			int colNo = rsmd.getColumnCount();
			int colType[] = new int[colNo];


			for (int i = 0 ; i < colNo ; i++)
			{
				colType[i] = rsmd.getColumnType(i+1);
			}

			for (int i = 0 ; i < rowDataArray.length ; i++)
			{
				queryParts[i] =  JOptionPane.showInputDialog("Enter value for column: " + colArr[i].replaceAll(", $", ""));
				if (colType[i] == 5 || colType[i] == 2 || colType[i] == 4)
				{
					queryNames = queryNames + queryParts[i] + ", ";
				}
				else
				{
					queryNames = queryNames + '\'' + queryParts[i] + '\'' + ", ";
				}
			}
			queryNames = queryNames.replaceAll(", $", "");

			String query = "insert into " + table + "(" + colNames + ")" + " values(" + queryNames + ")";
			stmt.executeUpdate(query);
			JOptionPane.showMessageDialog(null,"Record sucessfully inserted into table " + table);

		} 

		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error inserting data record into " + table);
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		} 



	}

	public static void deleteRow(String table)
	{
		if (table.equalsIgnoreCase(""))
		{
			JOptionPane.showMessageDialog(null, "Please select a table before deleting row");
		}

		int row = output.getSelectedRow();
		String keyName = output.getModel().getValueAt(0, 0).toString();
		String query;
		String keyValue = output.getModel().getValueAt(row, 0).toString();





		try (Statement stmt = conn1.createStatement()) {

			ResultSet rs = stmt.executeQuery("SELECT " +  keyName + " from " + table);
			java.sql.ResultSetMetaData rsmd;
			rsmd = rs.getMetaData();
			int colType = rsmd.getColumnType(1);

			if (colType == 5 || colType == 2 || colType == 4)
			{
				 query = "delete from " + table + " where " + keyName + " = " + keyValue;
			}
			else
			{
				 query = "delete from " + table + " where " + keyName + " = " + "\'" + keyValue + "\'";
			}

			stmt.executeUpdate(query);
			JOptionPane.showMessageDialog(null,"Record sucessfully deleted from table " + table);

		} 

		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error deleting row data from table " + table);
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		} 
	}



	public static void populateTables()
	{
		String[] query = new String[42];

		query[0] = "insert into booking(bookingid, returncondition, returnlocation, pickuplocation) values(4283280768, 'good', 'Canada', 'USA')";

		query[1] = "insert into booking(bookingid, returncondition, returnlocation, pickuplocation) values(8902389804, 'poor', 'Africa', 'USA')";

		query[2] = "insert into booking(bookingid, returncondition, returnlocation, pickuplocation) values(9540023222, 'normal', 'Australia', 'USA')";

		query[3] = "insert into booking(bookingid, returncondition, returnlocation, pickuplocation) values (1924934845, 'poor', 'Canada', 'USA')";

		query[4] = "insert into car(vin, brand, condition, caryear, colour) values('WDBWK73F99F204995', 'Mazda', 'New', 1997, 'Red')";


		query[5] = "insert into car(vin, brand, condition, caryear, colour) values('1GTJP32LXM3573484', 'Honda', 'New', 2008, 'Black')";

		query[6] = "insert into car(vin, brand, condition, caryear, colour) values('1GTHK33R8XF066893', 'BMW', 'New', 2015, 'White')";

		query[7] = "insert into car(vin, brand, condition, caryear, colour) values('4V1WDBRG7PN659007', 'KIA', 'New', 2014, 'Blue')";

		query[8] = "insert into car(vin, brand, condition, caryear, colour) values('1GCCT19X2VK101207', 'Mazda', 'New', 2018, 'Black')";

		query[9] = "insert into caris(vin, modelname) values('1GCCT19X2VK101207', 'ae5')";

		query[10] = "insert into caris(vin, modelname) values('4V1WDBRG7PN659007', 'ai3')";

		query[11] = "insert into caris(vin, modelname) values('1GTJP32LXM3573484', 'se5')";

		query[12] = "insert into caris(vin, modelname) values('1GTHK33R8XF066893', 'tn4')";

		query[13] = "insert into cartype(modelname, categorytype, description) values('se5', 'SUV', 'blah blah aaa')";

		query[14] = "insert into cartype(modelname, categorytype, description) values('tn4', 'Sedan', 'good car pls rent')";

		query[15] = "insert into cartype(modelname, categorytype, description) values('ai3', 'Sport', 'cool sports car')";

		query[16] = "insert into cartype(modelname, categorytype, description) values ('ae5', 'SUV', 'giant SUV')";

		query[17] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(0123456789,'Ted','James','416-254-7895', 'tedjames@aa.com', 'Canada')";

		query[18] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(1234125665,'Blag','Yeete','421-222-3214', 'BlargYeete@aa.com', 'USA')";

		query[19] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(5277103976,'Ted','Talks','416-254-7895', 'tedtalk@fml.com', 'Canada')";

		query[20] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(8095130599,'John','Doe','254-233-7845', 'johndoe@aye.com', 'USA')";

		query[21] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(4902591536,'Mary','Su','874-789-4561', 'marysu@sad.com', 'Africa')";

		query[22] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(6499314616,'Sarah','Jo','879-123-3333', 'sarahjo@pop.com', 'Europe')";

		query[23] = "insert into customer(customerid, fname, lname, phonenumber, email, country) values(5346567318,'Paul','Jon','416-234-1234', 'pauljon@fml.com', 'Canada')";


		query[24] = "insert into delivers(vin, bookingid, delivered) values('WDBWK73F99F204995', 4283280768, 'Y')";

		query[25] = "insert into delivers(vin, bookingid, delivered) values('1GTJP32LXM3573484', 8902389804, 'Y')";

		query[26] = "insert into delivers(vin, bookingid, delivered) values('1GTHK33R8XF066893', 9540023222, 'Y')";

		query[27] = "insert into delivers(vin, bookingid, delivered) values('1GTHK33R8XF066893', 1924934845,'Y')";


		query[28] = "insert into getbookinginfo(bookingid, customerid) values (8902389804, 4902591536)";

		query[29] = "insert into getbookinginfo(bookingid, customerid) values (4283280768, 5277103976)";

		query[30] = "insert into getbookinginfo(bookingid, customerid) values(1924934845, 6499314616)";


		query[31] = "insert into rentalrates(bookingid, weeklyrates, latefees, dailyrates, discount, penalty) values(4283280768, 50, 10, 5, 5, 10)";

		query[32] = "insert into rentalrates(bookingid, weeklyrates, latefees, dailyrates, discount, penalty) values(8902389804, 25, 3, 5, 5, 10)";

		query[33] = "insert into rentalrates(bookingid, weeklyrates, latefees, dailyrates, discount, penalty) values(9540023222, 23, 4, 3, 5, 10)";



		query[34] = "insert into shop(storeID, phonenumber, country, city, streeet) values (59348,'9052134456','Canada','Toronto','34 young street')";

		query[35] = "insert into shop(storeID, phonenumber, country, city, streeet) values(12180,'4158974563','Canada', 'Markham', '23 china street')";

		query[36] = "insert into shop(storeID, phonenumber, country, city, streeet) values(61953,'2568974563','USA', 'California', '12 cali street')";

		query[37] = "insert into shop(storeID, phonenumber, country, city, streeet) values(3574,'2365478952','Africa', 'Nigeria', '34 front street')";

		query[38] = "insert into shop(storeID, phonenumber, country, city, streeet) values(38252,'3256587412','Australia', 'Melbourne', '34 young street')";



		query[39] = "insert into visits(customerid, storeid) values(4902591536, 59348)";

		query[40] = "insert into visits(customerid, storeid) values(5277103976, 59348)";

		query[41] = "insert into visits(customerid, storeid) values(6499314616, 38252)";

		try (Statement stmt = conn1.createStatement()) {

			for(int i = 0 ; i < query.length ; i ++)
			{
				stmt.executeUpdate(query[i]);
			}
			JOptionPane.showMessageDialog(null,"Data successfully inserted into tables!");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error inserting data into tables");
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		} 




	}


	public static void selectTables()
	{
		String query1 = "select VIN,BRAND,CONDITION,CARYEAR,COLOUR from CAR";

		String query2 = "select BOOKINGID,RETURNCONDITION,RETURNLOCATION,PICKUPLOCATION from BOOKING";

		String query3 = "select VIN,MODELNAME from CARIS";

		String query4 = "select MODELNAME,CATEGORYTYPE,DESCRIPTION from CARTYPE";

		String query5 = "select BOOKINGID, WEEKLYRATES,LATEFEES, DAILYRATES, DISCOUNT , PENALTY from RENTALRATES";

		String query6 = "select CUSTOMERID, FNAME, LNAME,PHONENUMBER,EMAIL,COUNTRY from CUSTOMER";

		String query7 = "select VIN,BOOKINGID,DELIVERED from DELIVERS";

		String query8 = "select CUSTOMERID,BOOKINGID from GETBOOKINGINFO";

		String query9 = "select STOREID, PHONENUMBER, COUNTRY, CITY, STREEET from SHOP";

		String query10 = "select CUSTOMERID, STOREID from VISITS"; 

		try (Statement stmt = conn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);) {



			table = JOptionPane.showInputDialog("Enter table to select");
			DefaultTableModel aModel = (DefaultTableModel) output.getModel();
			DefaultTableModel model = (DefaultTableModel) output.getModel();
			output.setDefaultRenderer(Object.class, new cellFormat());
			java.sql.ResultSetMetaData rsmd;

			if (table.equalsIgnoreCase("car"))
			{
				ResultSet rs1 = stmt.executeQuery(query1);

				model.setRowCount(0);

				String[] tableColumnsName = {"VIN", "BRAND","CONDITION","CARYEAR","COLOUR"}; 

				aModel.setColumnIdentifiers(tableColumnsName);
				Vector row = new Vector();
				row.add("VIN");
				row.add("BRAND");
				row.add("CONDITION");
				row.add("CARYEAR");
				row.add("COLOUR");

				aModel.addRow(row);
				for(int i = 0 ; i < row.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}

				rsmd = rs1.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs1.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs1.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}
				output.setModel(aModel);
			}



			else if (table.equalsIgnoreCase("booking"))
			{
				ResultSet rs2 = stmt.executeQuery(query2);

				model.setRowCount(0);

				String[] tableColumnsName2 = {"BOOKINGID", "RETURNCONDITION","RETURNLOCATION","PICKUPLOCATION"}; 

				aModel.setColumnIdentifiers(tableColumnsName2);
				Vector row2 = new Vector();
				row2.add("BOOKINGID");
				row2.add("RETURNCONDITION");
				row2.add("RETURNLOCATION");
				row2.add("PICKUPLOCATION");

				aModel.addRow(row2);

				for(int i = 0 ; i < row2.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);

				}
				rsmd = rs2.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs2.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs2.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}


			else if (table.equalsIgnoreCase("caris"))
			{
				ResultSet rs3 = stmt.executeQuery(query3);

				model.setRowCount(0);

				String[] tableColumnsName3 = {"VIN", "MODELNAME"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName3);
				Vector row3 = new Vector();
				row3.add("VIN");
				row3.add("MODELNAME");

				aModel.addRow(row3);
				for(int i = 0 ; i < row3.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(200);
				}
				rsmd = rs3.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs3.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs3.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}

			else if (table.equalsIgnoreCase("cartype"))
			{
				ResultSet rs4 = stmt.executeQuery(query4);

				model.setRowCount(0);

				String[] tableColumnsName4 = {"MODELNAME", "CATEGORYTYPE","RETURNLOCATION"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName4);
				Vector row4 = new Vector();
				row4.add("MODELNAME");
				row4.add("CATEGORYTYPE");
				row4.add("RETURNLOCATION");

				aModel.addRow(row4);
				for(int i = 0 ; i < row4.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}
				rsmd = rs4.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs4.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs4.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}
				output.setModel(aModel);
			}

			else if (table.equalsIgnoreCase("rentalrates"))
			{
				ResultSet rs5 = stmt.executeQuery(query5);

				model.setRowCount(0);

				String[] tableColumnsName5 = {"BOOKINGID", "WEEKLYRATES", "LATEFEES", "DAILYRATES", "DISCOUNT", "PENALTY"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName5);
				Vector row5 = new Vector();
				row5.add("BOOKINGID");
				row5.add("WEEKLYRATES");
				row5.add("LATEFEES");
				row5.add("DAILYRATES");
				row5.add("DISCOUNT");
				row5.add("PENALTY");

				aModel.addRow(row5);
				for(int i = 0 ; i < row5.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(200);
				}
				rsmd = rs5.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs5.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs5.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(200);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}


			else if (table.equalsIgnoreCase("customer"))
			{
				ResultSet rs6 = stmt.executeQuery(query6);
				model = (DefaultTableModel) output.getModel();
				model.setRowCount(0);

				String[] tableColumnsName6 = {"CUSTOMERID", "FNAME", "LNAME", "PHONENUMBER", "EMAIL", "COUNTRY"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName6);
				Vector row6 = new Vector();
				row6.add("CUSTOMERID");
				row6.add("FNAME");
				row6.add("LNAME");
				row6.add("PHONENUMBER");
				row6.add("EMAIL");
				row6.add("COUNTRY");

				aModel.addRow(row6);
				for(int i = 0 ; i < row6.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(200);
				}
				rsmd = rs6.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs6.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs6.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(200);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}

			else if (table.equalsIgnoreCase("delivers"))
			{
				ResultSet rs7 = stmt.executeQuery(query7);

				model = (DefaultTableModel) output.getModel();
				model.setRowCount(0);

				String[] tableColumnsName7 = {"VIN", "BOOKINGID", "DELIVERED"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName7);
				Vector row7 = new Vector();
				row7.add("VIN");
				row7.add("BOOKINGID");
				row7.add("DELIVERED");


				aModel.addRow(row7);
				for(int i = 0 ; i < row7.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}
				rsmd = rs7.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs7.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs7.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}


			else if (table.equalsIgnoreCase("getbookinginfo"))
			{
				ResultSet rs8 = stmt.executeQuery(query8);

				model = (DefaultTableModel) output.getModel();
				model.setRowCount(0);

				String[] tableColumnsName8 = {"CUSTOMERID", "BOOKINGID"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName8);
				Vector row8 = new Vector();
				row8.add("CUSTOMERID");
				row8.add("BOOKINGID");


				aModel.addRow(row8);
				for(int i = 0 ; i < row8.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}
				rsmd = rs8.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs8.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs8.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}

			else if (table.equalsIgnoreCase("shop"))
			{
				ResultSet rs9 = stmt.executeQuery(query9);

				model = (DefaultTableModel) output.getModel();
				model.setRowCount(0);

				String[] tableColumnsName9 = {"STOREID", "PHONENUMBER", "COUNTRY", "CITY", "STREEET"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName9);
				Vector row9 = new Vector();
				row9.add("STOREID");
				row9.add("PHONENUMBER");
				row9.add("COUNTRY");
				row9.add("CITY");
				row9.add("STREEET");


				aModel.addRow(row9);
				for(int i = 0 ; i < row9.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}

				rsmd = rs9.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs9.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs9.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);
			}

			else if (table.equalsIgnoreCase("visits"))
			{
				ResultSet rs10 = stmt.executeQuery(query10);

				model = (DefaultTableModel) output.getModel();
				model.setRowCount(0);

				String[] tableColumnsName10 = {"CUSTOMERID", "STOREID"}; 
				aModel = (DefaultTableModel) output.getModel();
				aModel.setColumnIdentifiers(tableColumnsName10);
				Vector row10 = new Vector();
				row10.add("CUSTOMERID");
				row10.add("STOREID");



				aModel.addRow(row10);
				for(int i = 0 ; i < row10.size() ; i ++)
				{
					output.getColumnModel().getColumn(i).setPreferredWidth(250);
				}
				rsmd = rs10.getMetaData();
				int colNo = rsmd.getColumnCount();

				while(rs10.next()){
					Object[] objects = new Object[colNo];

					for(int i=0;i<colNo;i++){
						objects[i]=rs10.getObject(i+1);
						output.getColumnModel().getColumn(i).setPreferredWidth(250);
					}
					aModel.addRow(objects);
				}

				output.setModel(aModel);

			}


			else
			{
				JOptionPane.showMessageDialog(null, "Error table specified does not exist, please pick valid table");
			}


		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error retrieving data from tables");
			JOptionPane.showMessageDialog(null, "Received error code: " + e.getErrorCode());
		}

	}






	public class actionViewQuery implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			selectTables();
		}
	}

	public class actionDropTable implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			dropTables();
		}
	}

	public class actionAddTable implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			createTables();
		}
	}

	public class actionInsertable implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			populateTables();
		}
	}

	public class actionClearOutput implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			DefaultTableModel model = (DefaultTableModel) output.getModel();
			model.setRowCount(0);
		}
	}



	public void actionPerformed(ActionEvent e) {
		System.exit(0);

	}


	public class actionInsertRecord implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			insertRecord(table);
		}
	}


	public class actionDeleteRow implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			deleteRow(table);
		}
	}



}
