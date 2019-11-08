package cs430Project;

import java.util.*;
import java.io.*;
import java.sql.*;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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


public class StaffPane extends JPanel {
  
  // these variables are here so I don't have to pass them around
  JTextArea requestArea, results;
  String url = System.getenv("CS430URL"); 
  private String queryString = new String();
  private String startQueryMsg = new String("Input query here.");
  private String startResultMsg = new String("Results will appear here.");
  private String usr;
  private String psw;

  // Constructor
  public StaffPane() {
    super(false);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    JPanel top = createTopPanel();
    JScrollPane bottom = createBottomPanel();

    add(top);
    add(bottom);
  }

  // sets the `usr` variable so it doesn't have to be asked for again
  public void setUserName(String newUsr) {
    if(null != newUsr) {
      usr = newUsr;
    }
  }
  // sets the `psw` variable so it doesn't have to be asked for again
  public void setPassword(String newPsw) {
    if(null != newPsw) {
      psw = newPsw;
    }
  }

  // resets the this object back to the original settings
  public void resetUserPsw() {
    usr = null;
    psw = null;
    requestArea.setText(startQueryMsg);
    results.setText(startResultMsg);
  }

  // Creates the top part of the StaffPane
  protected JPanel createTopPanel() {
    JPanel top = new JPanel(); 
    top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));

    Container left = createInputSelection();
    top.add(left);

    return top;
  }

  // Creates the bottom part of the StaffPane
  protected JScrollPane createBottomPanel() {
    
    results = new JTextArea(startResultMsg);

    JScrollPane bottom = new JScrollPane(results);
    bottom.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    bottom.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
    return bottom;
  }

  // Creates the input selection area
  protected Container createInputSelection() {
    Container top = new Container();
    top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
    requestArea = new JTextArea(startQueryMsg);
    requestArea.setLineWrap(true);
    // when the user clicks on the JTextArea, the text is removed
    requestArea.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(requestArea.getText().equals(startQueryMsg)) {
          requestArea.setText(null);
        }
      }
      @Override
      public void focusLost(FocusEvent e) {}
    });
    
    JScrollPane scrollBox = new JScrollPane(requestArea);
    scrollBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    Container bottom = new Container();
    bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));

    // create clear & submit buttons
    JButton clearBtn = new JButton("Clear");
    clearBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // clear the TextAreas
        requestArea.setText(startQueryMsg);
        results.setText(startResultMsg);
      }
    });
    JButton submitBtn = new JButton("Submit");
    // gether the query and attempt to submit it to the DB server
    submitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        queryString = requestArea.getText().trim();
        results.setText(null);
        results.revalidate();
        if(queryString.equals(startQueryMsg) || queryString.length() == 0) {
          results.setText("No query given.");
          results.revalidate();
          return;
        }
        if(queryString.toUpperCase().startsWith("DROP")) {
          results.setText("Not allowed to drop table.");
          results.revalidate();
        } else if (queryString.toUpperCase().startsWith("ALTER")) {
          results.setText("Not allowed to alter table.");
          results.revalidate();
        } else {
          try {
            // Register the oracle driver with DriverManager
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
          } catch(Exception ex) {
            System.err.println("can't find the driver");
            return;
          }
          try {
            
            Connection con=DriverManager.getConnection(url,usr,psw);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(queryString);
            ResultSetMetaData rsmd=rs.getMetaData();
            int numberofcolumn =rsmd.getColumnCount();
            String result = new String();
            String columnnames=new String("");
            Boolean trip = true;
            // for loop needs to from 1 not 0
            for(int i = 1; i <= numberofcolumn; i++) {
               String name=rsmd.getColumnName(i);
               columnnames=columnnames+name+"\t";
            }
            result+=columnnames;
            result+="\n";
            while (rs.next()) {
              trip = false;
              //Read each field of the row, and the for loop also begin with 1
              for(int i=1;i<=numberofcolumn;i++) {
                  String s=rs.getString(i);
                  result+= s + "\t";
              }
              result+="\n";
            }
            // update the `results` JTextArea text
            if(trip) {
              results.setText("No results found.");
              results.revalidate();
            } else {
              results.setText(result);
              results.revalidate();
            }
            stmt.close();
            con.close();
          } catch(Exception ex) {
            results.setText("SQLException: "+ex);
            System.err.println("SQLException: "+ex);
          }
        }
      }
    });

    bottom.add(clearBtn);
    bottom.add(submitBtn);
    top.add(scrollBox);
    top.add(bottom);
    
    return top;
  }


}

