import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.text.*;
import java.awt.event.ActionListener;
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

public class ConnectDB {

	static Connection conn1 = null;

	 public static void connect(String user, String pass) throws SQLException {
		try {
			// registers Oracle JDBC driver - though this is no longer required
			// since JDBC 4.0, but added here for backward compatibility
			Class.forName("oracle.jdbc.OracleDriver");


			//String dbURL1 = "jdbc:oracle:thin:" + user + "/" + pass + @oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs

			String dbURL1 = "jdbc:oracle:thin:"+ user + "/" + pass + "@192.168.56.101:1521/orcl";
			/* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation. 
			xe is the sid, change according to what you setup during installation. */

			conn1 = DriverManager.getConnection(dbURL1);
			if (conn1 != null) {
				JOptionPane.showMessageDialog(null,"Connected with the database");
				LoginDB.closeLogin();
				new CarRentalDB(conn1);
			}

			else
			{
				JOptionPane.showMessageDialog(null,"Failed to connect to database");
			}

		}

		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		} 
	}
}
