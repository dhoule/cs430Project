package cs430Project;

import java.util.*;
import java.io.*;
import java.sql.*;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cs430Project.SpringUtilities;

public class StaffPane extends JPanel {
  

  JTextArea requestArea, results;
  String url="jdbc:oracle:thin:@131.230.133.11:1521:cs";
  private String result = new String();
  private String queryString = new String();
  private String usr;
  private String psw;


  public StaffPane() {
    super(false);
    JPanel top = createTopPanel();

    JPanel bottom = createBottomPanel();

    // create a splitpane 
    JSplitPane sl = new JSplitPane(SwingConstants.HORIZONTAL, top, bottom);

    add(sl, BorderLayout.CENTER);
    addComponentListener( new ComponentAdapter() {
      public void componentShown ( ComponentEvent e ){
        try {
          DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch(Exception ex) {
          System.out.println("can't find the driver");
        }
        try {
          // Connection con=DriverManager.getConnection(url,"dhoule","7fPr6p7P");
          Connection con=DriverManager.getConnection(url,usr,psw);
          Statement stmt=con.createStatement();
          ResultSet rs=stmt.executeQuery("SELECT table_name,column_name FROM all_tab_cols where owner='" + usr.toUpperCase() + "'");
          // TODO subsequent queries: "SELECT table_name,column_name FROM all_tab_cols where owner='" + usr.toUpperCase() +"'"
          /*Get the meta data of the result set*/
          ResultSetMetaData rsmd=rs.getMetaData();
          
          
          /*Close the statment and connection to release the resource*/
          stmt.close();
          con.close();

        } catch(Exception ex) {
          System.out.println("SQLException: "+ex);
        }
      }

      public void componentHidden( ComponentEvent e ) {
        resetUserPsw();
      }
    });
  }

  public void setUserName(String newUsr) {
    if(null != newUsr) {
      usr = newUsr;
    }
  }

  public void setPassword(String newPsw) {
    if(null != newPsw) {
      psw = newPsw;
    }
  }

  public void resetUserPsw() {
    usr = null;
    psw = null;
  }

  protected JPanel createTopPanel() {
    JPanel top = new JPanel(); 
    // top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));

    Container left = createInputSelection();


    top.add(left, BorderLayout.LINE_START);

    return top;
  }

  protected JPanel createBottomPanel() {
    JPanel bottom = new JPanel();
    results = new JTextArea(10, 20);

    bottom.add(results, BorderLayout.LINE_START);
  
    return bottom;
  }

  protected Container createInputSelection() {
    Container top = new Container();
    top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
    
    requestArea = new JTextArea(8, 10);
    top.add(requestArea);

    Container bottom = new Container();
    bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));

    // create clear & submit buttons
    JButton clearBtn = new JButton("Clear");
    clearBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // clear the TextArea and deselect the lists
        requestArea.setText(null);
      }
    });
    JButton submitBtn = new JButton("Submit");
    submitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        queryString = requestArea.getText();
        System.out.println(queryString);
        if(queryString.toUpperCase().startsWith("DROP")) {
          System.out.println("Not allowed to drop table.");
        } else {
          try {
            // Register the oracle driver with DriverManager
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
          } catch(Exception ex) {
            System.out.println("can't find the driver");
          }
          try {
            results.setText(null);
            Connection con=DriverManager.getConnection(url,usr,psw);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(queryString);
            ResultSetMetaData rsmd=rs.getMetaData();
            int numberofcolumn =rsmd.getColumnCount();
            String columnnames=new String("");
            // for loop needs to from 1 not 0
            for(int i = 1; i <= numberofcolumn; i++) {
               String name=rsmd.getColumnName(i);
               columnnames=columnnames+"\t"+name;
            }
            result+=columnnames;
            result+="\n";
            while (rs.next()) {
            //Read each field of the row, and the for loop also begin with 1
              for(int i=1;i<=numberofcolumn;i++) {
                  String s=rs.getString(i);
                  result+="\t"+s;
              }
              result+="\n";
            }
            results.setText(result);
          } catch(Exception ex) {
            results.setText("SQLException: "+ex);
            System.err.println("SQLException: "+ex);
          }
        }

      }
    });

    bottom.add(clearBtn);
    bottom.add(submitBtn);
    top.add(bottom);
    
    return top;
  }


}

