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
  private JMenuBar top; // The menu bar
  private JTextArea display; // The display area

  /* 
   * TODO These need to be changed to actually do something.
   */
  String queryString="Select * from emp";    /*Create query string*/
  // TODO credentials moved to tempStash
  String result=new String();

  /**
   * Class constructor
   **/
  public DBProject() {
    // terminate the program via the window itself
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JTabbedPane tabbedPane = new JTabbedPane() {
      public void setSelectedIndex(int index) {
        // Determine what tab has been selected; starts with 0
        if (index == 1) {
          // TODO uncomment this
          // This is the "Staff Options"
          // LoginPane loginP = new LoginPane(DBProject.this);
          // loginP.setVisible(true);
          
          // if(loginP.isSucceeded()){
            super.setSelectedIndex(index);
          // }
          // else {
          //   super.setSelectedIndex(0);
          // }
        }
        else {
          super.setSelectedIndex(index);
        }
      }
    };
    
    CustomerPane panel1 = new CustomerPane();
    tabbedPane.addTab("Customer Options", panel1);

    StaffPane panel2 = new StaffPane();
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

  protected JComponent makeTextPanel(String text) {
    JPanel panel = new JPanel(false);
    // TODO get rid of this and fill in as needed
    JLabel filler = new JLabel(text);
    filler.setHorizontalAlignment(JLabel.CENTER);
    panel.setLayout(new GridLayout(1, 1));
    panel.add(filler);
    return panel;
  }
  
}
