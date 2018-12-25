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

public class LoginDB extends JFrame{

	public static JTextField username;
	public static JPasswordField password;
	static JFrame frame;
	static JLabel passwordL;
	static JLabel usernameL;
	static JButton login;

	static JPanel loginMenu, passMenu;

	public static void main(String[] args) {

		frame = new JFrame();
		frame.setSize(325, 150);
		frame.setTitle("Login Menu");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		
		loginMenu = new JPanel();
		passMenu = new JPanel();
		login = new JButton("Login");

		usernameL = new JLabel("Username:");

		passwordL = new JLabel("Password:");
		username = new JTextField(15);
		password = new JPasswordField(15);
		password.setEchoChar('*');

		loginMenu.add(usernameL, BorderLayout.NORTH);
		loginMenu.add(username, BorderLayout.SOUTH);

		passMenu.add(passwordL,BorderLayout.NORTH);
		passMenu.add(password, BorderLayout.SOUTH);

		frame.getContentPane().add(loginMenu,BorderLayout.NORTH);
		frame.getContentPane().add(passMenu,BorderLayout.CENTER);
		frame.getContentPane().add(login,BorderLayout.SOUTH);

		login.addActionListener(new LoginListener());
		password.addActionListener(new LoginListener());
		frame.setVisible(true);
		
	}
	
	public static void closeLogin(){
        frame.setVisible(false);
        frame.dispose();
       
    }

	
	public static class LoginListener implements ActionListener
	{
        public LoginListener()
        {

        }

        public void actionPerformed(ActionEvent e)
        {
            String input_user= "";
            String input_pass= "";

            if(!username.getText().equals(""))
            {
                input_user = username.getText();
            }
            if(!password.getPassword().equals(""))
            {
                input_pass = String.valueOf(password.getPassword());
            }
         

            if(input_user != "" && input_pass != ""){
                try {
                    ConnectDB.connect(input_user, input_pass);
                    
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        }     
	}

	


}

