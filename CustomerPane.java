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

  public CustomerPane() {
    super(false);
    
    JPanel top = createTopPanel();

    JPanel bottom = new JPanel();
    JTextArea results = new JTextArea(10, 20);
    bottom.add(results);

    // create a splitpane 
    JSplitPane sl = new JSplitPane(SwingConstants.HORIZONTAL, top, bottom);

    add(sl);
  }

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

  protected Container createInputForm() {
    // Input areas
    Container form = new Container();
    JLabel lblMName = new JLabel("Musician Name:");
    JTextField tfMName = new JTextField(20);
    tfMName.setEnabled(false);
    lblMName.setLabelFor(tfMName);
    musicCh = new JCheckBox("Include");
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
    JTextField tfATitle = new JTextField(20);
    tfATitle.setEnabled(false);
    lblATitle.setLabelFor(tfATitle);
    albumCh = new JCheckBox("Include");
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
    JTextField tfSName = new JTextField(20);
    tfSName.setEnabled(false);
    lblSName.setLabelFor(tfSName);
    songCH = new JCheckBox("Include");
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

    // Create radio buttons
    JRadioButton andBtn = new JRadioButton("AND");
    andBtn.setSelected(true);
    JRadioButton orBtn = new JRadioButton("OR");
    // Grouping the radio button together
    ButtonGroup group = new ButtonGroup();
    group.add(andBtn);
    group.add(orBtn);

    // create clear & submit buttons
    JButton clearBtn = new JButton("Clear");
    clearBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // disable the check boxes to set off their events
        songCH.setSelected(false);
        albumCh.setSelected(false);
        musicCh.setSelected(false);
      }
    });
    JButton submitBtn = new JButton("Submit");

    select.add(andBtn);
    select.add(orBtn);
    select.add(clearBtn);
    select.add(submitBtn);

    return select;
  }

}