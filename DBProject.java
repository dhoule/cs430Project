/**
 * @author Houle, Daniel B.
 * @version 1.0
 * CS430 - Fall 2019
 * Current Setup: Warmup
**/
package cs430Project;

import java.util.*;
import java.util.Vector;
import java.io.*;
import java.sql.*;

import java.awt.*;            // Using AWT layouts
import java.awt.event.*;      // Using AWT event classes and listener interfaces
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;         // Using Swing components and containers
import javax.swing.UIManager; // I don't like how the default UX looks
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

// custom classes
import cs430Project.LoginPane;
import cs430Project.CustomerPane;
import cs430Project.StaffPane;

public class DBProject extends JFrame {
  // Making these components global variables to be used elsewhere
  private StaffPane panel2;
  /**
   * Class constructor
   **/
  public DBProject() {
    // terminate the program via the window itself
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CustomerPane panel1 = new CustomerPane();
    panel2 = new StaffPane();
    
    JTabbedPane tabbedPane = new JTabbedPane() {
      public void setSelectedIndex(int index) {
        // Determine what tab has been selected; starts with 0
        if (index == 1) {
          // This is the "Staff Options"
          LoginPane loginP = new LoginPane(DBProject.this);
          loginP.setVisible(true);
          // only procede of login was successful
          if(loginP.isSucceeded()){
            // set the username and password given by the "Staff"
            panel2.setUserName(loginP.getUsername());
            panel2.setPassword(loginP.getPassword());
            super.setSelectedIndex(index);
          }
          else {
            super.setSelectedIndex(0);
          }
        }
        else {
          // if the tab changes, reset the StaffPane back to original settings
          panel2.resetUserPsw();
          super.setSelectedIndex(index);
        }
      }
    };
    
    tabbedPane.addTab("Customer Options", panel1);
    tabbedPane.addTab("Staff Options", panel2);

    //Add the tabbed pane to this panel.
    add(tabbedPane);

    // Set options for the app, and make it visible
    setTitle("Notown Records");
    setMinimumSize(new Dimension(500, 350)); // width, height
    setVisible(true);
  }

  public static void main(String[] args){
    try { 
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
    } 
    catch (Exception e) { 
      System.out.println("Look and Feel not set"); 
    } 
    DBProject app = new DBProject();
  }
  
}
