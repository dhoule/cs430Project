package cs430Project;

import java.util.*;
import java.util.Vector;
import java.io.*;
import java.sql.*;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  /*
    What is needed:
    1 JTabbedPane - split in 2
    Bottom:
      TextArea that is scrollable
    Top:
      TextArea, to write SQL, prevent DROP TABLE
      Below TextArea:
        2 buttons: "Clear" & "Submit"
      Right of TextArea:
        2 different lists:
          Top: Tables
          Botom: Selected Table's attributes
  */


  public StaffPane() {
    super(false);
    JPanel top = createTopPanel();

    JPanel bottom = createBottomPanel();

    // create a splitpane 
    JSplitPane sl = new JSplitPane(SwingConstants.HORIZONTAL, top, bottom);

    add(sl, BorderLayout.CENTER);
  }

  protected JPanel createTopPanel() {
    JPanel top = new JPanel(); 
    // top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));

    Container left = createInputSelection();

    Container right = createTableList();

    top.add(left, BorderLayout.LINE_START);
    top.add(right, BorderLayout.LINE_END);

    return top;
  }

  protected JPanel createBottomPanel() {
    JPanel bottom = new JPanel();
    JTextArea results = new JTextArea(10, 20);

    Container right = createAttrList();

    bottom.add(results, BorderLayout.LINE_START);
    bottom.add(right, BorderLayout.LINE_END);
  
    return bottom;
  }

  protected Container createInputSelection() {
    Container top = new Container();
    top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
    
    JTextArea requestArea = new JTextArea(8, 10);
    top.add(requestArea);

    Container bottom = new Container();
    bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));

    // create clear & submit buttons
    JButton clearBtn = new JButton("Clear");
    clearBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // TODO clear the TextArea and deselect the lists
        requestArea.setText(null);
      }
    });
    JButton submitBtn = new JButton("Submit");

    bottom.add(clearBtn);
    bottom.add(submitBtn);
    top.add(bottom);
    
    return top;
  }

  protected Container createTableList(){
    Container contain = new Container();
    contain.setMinimumSize(new Dimension(50, 100)); // width, height
    // JPanel top = new JPanel();
    JLabel lTop = new JLabel("Available Tables");
    JList tables = new JList();
    
    tables.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    tables.setLayoutOrientation(JList.VERTICAL_WRAP);
    contain.add(lTop);
    contain.add(tables);

    // JPanel bottom = new JPanel();
    // JLabel lBottom = new JLabel("Available Attributes");
    // JList attrs = new JList();
    // attrs.setLayoutOrientation(JList.VERTICAL_WRAP);
    // bottom.add(lBottom);
    // bottom.add(attrs);

    return contain;
  }

  protected Container createAttrList(){
    Container contain = new Container();
    contain.setMinimumSize(new Dimension(50, 100)); // width, height

    JLabel lTop = new JLabel("Available Attributes");
    JLabel lBottom = new JLabel("Available Attributes");
    JList attrs = new JList();
    attrs.setLayoutOrientation(JList.VERTICAL_WRAP);
    contain.add(lBottom);
    contain.add(attrs);

    return contain;
  }
}

