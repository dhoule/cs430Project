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

public class CustomerPane extends JPanel {

  // Just putting these here to make it easier/quicker to use
  JCheckBox songCH, albumCh, musicCh;
  JTextField tfMName, tfATitle, tfSName;
  JTextArea resultArea;
  String user = System.getenv("CS430USR"); 
  String passwd = System.getenv("CS430PSW"); 
  String url = System.getenv("CS430URL"); 

  // constructor
  public CustomerPane() {
    super(false);
    
    JPanel top = createTopPanel();
    
    resultArea = new JTextArea(10,15);
    JScrollPane bottom = new JScrollPane(resultArea);
    bottom.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // create a splitpane 
    JSplitPane sl = new JSplitPane(SwingConstants.HORIZONTAL, top, bottom);

    add(sl);
  }

  // Creates the top JPanel
  protected JPanel createTopPanel() {
    JPanel top = new JPanel(); 
    top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));

    Container form = createInputForm();
    Container select = createInputSelection();

    top.add(form);
    top.add(select);

    SpringUtilities.makeCompactGrid(form,
                3, 3,  //rows, cols
                6, 6,  //initX, initY
                6, 6); //xPad, yPad

    return top;
  }

  // Creates the input areas
  protected Container createInputForm() {
    // Input areas
    Container form = new Container();
    JLabel lblMName = new JLabel("Musician Name:");
    tfMName = new JTextField(20);
    tfMName.setEnabled(false);
    lblMName.setLabelFor(tfMName);
    musicCh = new JCheckBox("Include");
    // listener enables/disables the corresponding JTextField
    musicCh.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        AbstractButton abstractButton = (AbstractButton)e.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        tfMName.setEnabled(buttonModel.isSelected());
        if(!tfMName.isEnabled()) { tfMName.setText(null); }
      }
    });
    
    JLabel lblATitle = new JLabel("Album Title:");
    tfATitle = new JTextField(20);
    tfATitle.setEnabled(false);
    lblATitle.setLabelFor(tfATitle);
    albumCh = new JCheckBox("Include");
    // listener enables/disables the corresponding JTextField
    albumCh.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        AbstractButton abstractButton = (AbstractButton)e.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        tfATitle.setEnabled(buttonModel.isSelected());
        if(!tfATitle.isEnabled()) { tfATitle.setText(null); }
      }
    });

    JLabel lblSName = new JLabel("Song Name:");
    tfSName = new JTextField(20);
    tfSName.setEnabled(false);
    lblSName.setLabelFor(tfSName);
    songCH = new JCheckBox("Include");
    // listener enables/disables the corresponding JTextField
    songCH.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        AbstractButton abstractButton = (AbstractButton)e.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        tfSName.setEnabled(buttonModel.isSelected());
        if(!tfSName.isEnabled()) { tfSName.setText(null); }
      }
    });

    form.setLayout(new SpringLayout());


    form.add(lblMName);
    form.add(tfMName);
    form.add(musicCh);
    form.add(lblATitle);
    form.add(tfATitle);
    form.add(albumCh);
    form.add(lblSName);
    form.add(tfSName);
    form.add(songCH);
    form.setSize(500,60);

    return form;
  }

  protected Container createInputSelection() {
    Container select = new Container();
    select.setLayout(new BoxLayout(select, BoxLayout.LINE_AXIS));

    // create clear & submit buttons
    JButton clearBtn = new JButton("Clear");
    clearBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // disable the check boxes to set off their events
        songCH.setSelected(false);
        albumCh.setSelected(false);
        musicCh.setSelected(false);
        resultArea.setText(null);
      }
    });
    JButton submitBtn = new JButton("Submit");
    submitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resultArea.setText(null);
        // Determine the selected input
        int which = 0;
        int song = 1;
        int album = 2;
        int music = 4;
        if(songCH.isSelected()) { which = (which | song); }
        if(albumCh.isSelected()) { which = (which | album); }
        if(musicCh.isSelected()) { which = (which | music); }

        String query = getQuery(which);
        String result=new String();
        Connection con;
        PreparedStatement ps;
        try {
          con = DriverManager.getConnection(url,user,passwd);
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\n Could not connect to database." );
          return;
        }
        try {
          ps = con.prepareStatement(query);
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\n Could not prepare statement." );
          return;
        }
        try {
          
          switch(which) {
            case 1: // Song Name
              ps.setString(1,tfSName.getText());
              break;
            case 2: // Album Title
              ps.setString(1,tfATitle.getText());
              break;
            case 3: // Album Title + Song Name
              ps.setString(1,tfATitle.getText());
              ps.setString(2,tfSName.getText());
              break;
            case 4: // Musician Name
              ps.setString(1,tfMName.getText());
              break;
            case 5: // Musician Name + Song Name
              ps.setString(1,tfMName.getText());
              ps.setString(2,tfSName.getText());
              break;
            case 6: // Musician Name + Album Title
              ps.setString(1,tfMName.getText());
              ps.setString(2,tfATitle.getText());
              break;
            case 7: // Musician Name + Album Title + Song Name
              ps.setString(1,tfATitle.getText());
              ps.setString(2,tfSName.getText());
              ps.setString(3,tfMName.getText());
              break;
          }
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not give value to corresponding input.");
          return;
        }
        ResultSet rs;
        ResultSetMetaData rsmd;
        
        try {
          rs = ps.executeQuery();
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not execute query.");
          return;
        }
        try {
          rsmd = ps.getMetaData();
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not aquire metadata.");
          return;
        }
        int numberofcolumn;
        try {
          numberofcolumn =rsmd.getColumnCount();
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not aquire column headers.");
          return;
        }
        String columnnames=new String("");
        for(int i=1;i<=numberofcolumn;i++) { /*for loop needs to from 1 not 0*/
          String name;
          try {
            name=rsmd.getColumnName(i);
          } catch(SQLException sqlException) {
            resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not aquire column header.");
            return;
          }
          columnnames=columnnames+"\t"+name;
        }
        result+=columnnames;
        result+="\n";
        try {
          while (rs.next()) {
            // Read each field of the row, and the for loop also begin with 1
            for(int i = 1; i <= numberofcolumn; i++) {
              String s;
              try {
                s=rs.getString(i);
              } catch(SQLException sqlException) {
                resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not aquire record attribute.");
                return;
              }
              result+="\t"+s;
            }
            result+="\n";
          }
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not aquire record attribute.");
          return;
        }

        try {
          ps.close();
          con.close();
        } catch(SQLException sqlException) {
          resultArea.setText("Error: " + String.valueOf(sqlException.getErrorCode()) + "\nCould not close connection.");
          return;
        }
        
        resultArea.setText(result);
        resultArea.revalidate(); // update `resultArea` and its scrollbars
      }
    });

    select.add(clearBtn);
    select.add(submitBtn);

    return select;
  }

  // return the premade query, depending on the inputs selected
  private String getQuery(int which) {
    String query;
    switch(which) {
      case 1: // Song Name
        query = new String("SELECT SONGS_APPEARS.title AS title, SONGS_APPEARS.author AS author, ALBUM_PRODUCER.title AS album, ALBUM_PRODUCER.copyrightDate AS CRDate " +
          "FROM SONGS_APPEARS, ALBUM_PRODUCER, PERFORM, MUSICIANS " +
          "WHERE SONGS_APPEARS.title = ? AND SONGS_APPEARS.albumIdentifier = SONGS_APPEARS.albumIdentifier AND SONGS_APPEARS.songId=PERFORM.songId AND PERFORM.ssn= MUSICIANS.ssn");
        break;
      case 2: // Album Title
        query = new String("SELECT ALBUM_PRODUCER.title AS album, ALBUM_PRODUCER.copyrightDate AS CRDate, SONGS_APPEARS.title AS title, SONGS_APPEARS.author AS author, MUSICIANS.name AS name " +
          "FROM MUSICIANS, PERFORM, SONGS_APPEARS, ALBUM_PRODUCER " +
          "WHERE ALBUM_PRODUCER.title = ? AND ALBUM_PRODUCER.albumIdentifier = SONGS_APPEARS.albumIdentifier AND SONGS_APPEARS.songId = PERFORM.songId AND PERFORM.ssn = MUSICIANS.ssn");
        break;
      case 3: // Album Title + Song Name
        query = new String("SELECT ALBUM_PRODUCER.title AS album, SONGS_APPEARS.title AS title, MUSICIANS.name AS name " +
          "FROM MUSICIANS, SONGS_APPEARS, ALBUM_PRODUCER, PERFORM " +
          "WHERE ALBUM_PRODUCER.title = ? AND SONGS_APPEARS.title = ? AND MUSICIANS.ssn = PERFORM.ssn AND SONGS_APPEARS.songId = PERFORM.songId AND SONGS_APPEARS.albumIdentifier = ALBUM_PRODUCER.albumIdentifier");
        break;
      case 4: // Musician Name
        query = new String("SELECT MUSICIANS.name AS name, SONGS_APPEARS.author AS author, SONGS_APPEARS.title AS title, ALBUM_PRODUCER.copyrightDate AS CRDate, ALBUM_PRODUCER.title AS album " +
          "FROM MUSICIANS, PERFORM, SONGS_APPEARS, ALBUM_PRODUCER " +
          "WHERE MUSICIANS.name = ? AND PERFORM.ssn = MUSICIANS.ssn AND PERFORM.songId = SONGS_APPEARS.songId AND SONGS_APPEARS.albumIdentifier = ALBUM_PRODUCER.albumIdentifier" );
        break;
      case 5: // Musician Name + Song Name
        query = new String("SELECT MUSICIANS.name AS name, SONGS_APPEARS.title AS title, ALBUM_PRODUCER.title AS album " +
          "FROM MUSICIANS, SONGS_APPEARS, ALBUM_PRODUCER, PERFORM " +
          "WHERE MUSICIANS.name = ? AND SONGS_APPEARS.title = ? AND MUSICIANS.ssn = PERFORM.ssn AND SONGS_APPEARS.songId = PERFORM.songId AND SONGS_APPEARS.albumIdentifier = ALBUM_PRODUCER.albumIdentifier");
        break;
      case 6: // Musician Name + Album Title
        query = new String("SELECT MUSICIANS.name AS name, ALBUM_PRODUCER.copyrightDate AS CRDate, ALBUM_PRODUCER.title AS album, SONGS_APPEARS.author AS author " +
          "FROM MUSICIANS, ALBUM_PRODUCER, PERFORM, SONGS_APPEARS " +
          "WHERE MUSICIANS.name = ? AND ALBUM_PRODUCER.title = ? AND MUSICIANS.ssn = PERFORM.ssn AND SONGS_APPEARS.songId = PERFORM.songId AND MUSICIANS.ssn = ALBUM_PRODUCER.ssn");
        break;
      case 7: // Musician Name + Album Title + Song Name
        query = new String("SELECT ALBUM_PRODUCER.title AS album, SONGS_APPEARS.title AS title, MUSICIANS.name AS name " +
          "FROM MUSICIANS, SONGS_APPEARS, ALBUM_PRODUCER, PERFORM " +
          "WHERE ALBUM_PRODUCER.title = ? AND SONGS_APPEARS.title = ? AND MUSICIANS.name = ? AND MUSICIANS.ssn = PERFORM.ssn AND SONGS_APPEARS.songId = PERFORM.songId AND SONGS_APPEARS.albumIdentifier = ALBUM_PRODUCER.albumIdentifier");
        break;
      default:
        query = new String("1=0");
    }
    return query;
  }

}