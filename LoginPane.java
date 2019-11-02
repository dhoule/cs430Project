package cs430Project;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginPane extends JDialog {
	private JTextField tfUsername;
  private JPasswordField pfPassword;
  private JLabel lbUsername;
  private JLabel lbPassword;
  private JButton btnLogin;
  private JButton btnCancel;
  private boolean succeeded;

  private String usr;
  private String psw;


  public LoginPane(Frame parent) {
  	super(parent, "Login", true);

  	JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    lbUsername = new JLabel("Username: ");
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(lbUsername, cs);

    tfUsername = new JTextField(20);
    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(tfUsername, cs);

    lbPassword = new JLabel("Password: ");
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(lbPassword, cs);

    pfPassword = new JPasswordField(20);
    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    panel.add(pfPassword, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    btnLogin = new JButton("Login");

    btnLogin.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (login(getUsername(), getPassword())) {
          JOptionPane.showMessageDialog(LoginPane.this, "Hi " + getUsername() + "! You have successfully logged in.\nChanging tabs will require another login.", "Login", JOptionPane.INFORMATION_MESSAGE);
          succeeded = true;
          dispose();
        } else {
          JOptionPane.showMessageDialog(LoginPane.this, "Invalid username or password", "Login", JOptionPane.ERROR_MESSAGE);
          // reset username and password
          tfUsername.setText("");
          pfPassword.setText("");
          succeeded = false;
        }
      }
    });
    btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    JPanel bp = new JPanel();
    bp.add(btnLogin);
    bp.add(btnCancel);

    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(bp, BorderLayout.PAGE_END);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }

  public String getUsername() {
    if(null == usr)
      usr = tfUsername.getText().trim();
    return usr;
  }

  public String getPassword() {
    if(null == psw)
      psw = new String(pfPassword.getPassword());
    return psw;
  }

  public boolean isSucceeded() {
    return succeeded;
  }

  private boolean login(String user, String passwd){
    //Create connection with oracle server
    // TODO Make this real
    // Connection con=DriverManager.getConnection(url,user,passwd);
    // System.out.println("Connection sucessful");
    String result=new String();
    String url="jdbc:oracle:thin:@131.230.133.11:1521:cs";    /*Using oracle driver and CS oracle server*/
    //String user="scott";     /*user id which is a demo account offered by oracle*/
    //String passwd="tiger";    /*password*/
    
    //Must put into try and catch because Exception will be thrown when the driver can not be found
    try {
      //Register the oracle driver with DriverManager
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      System.out.println("Done with driver registrations!");
    }catch(Exception ex){
      System.err.println("can't find the driver");
    }
    //Must put into try and catch because connecting to SQL server and execute SQL query will throw SQLExeption in Java
    try {
      System.out.println("Trying to connect...");

      //Create connection with oracle server
      Connection con=DriverManager.getConnection(url,user,passwd);
      System.out.println("Connection sucessful");
      
      con.close();
      return true;
    }catch(SQLException ex){
      System.out.println("SQLException: "+ex);
      return false;
    }
      
    // if (user.equals("scott") && passwd.equals("hobbes")) {
    //   return true;
    // }
    // return false;
  }
}