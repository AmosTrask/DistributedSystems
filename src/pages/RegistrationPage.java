/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class RegistrationPage extends JFrame implements ActionListener {
    
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    private JButton registrationButton;
    private JButton clearButton;
    
    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    
    public RegistrationPage() {
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.createPage();
    }
    
    private void createPage() {
        
        this.setTitle("Register");
        this.setLayout(new GridLayout(5,2));
        
        this.nameField = new JTextField();
        this.surnameField = new JTextField();
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.registrationButton = new JButton("register");
        this.clearButton = new JButton("clear");
        
        this.add(this.nameField);
        this.add(new JLabel("name"));
        this.add(this.surnameField);
        this.add(new JLabel("surname"));
        this.add(this.usernameField);
        this.add(new JLabel("username"));
        this.add(this.passwordField);
        this.add(new JLabel("password"));
        this.add(this.registrationButton);
        this.add(this.clearButton);
        
        this.registrationButton.addActionListener(this);
        this.clearButton.addActionListener(this);
        
        WindowListener l;
        l = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        this.addWindowListener(l);
       
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.registrationButton) {
            String answer = proxy.register(usernameField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText());
            if (answer.contains("ERROR")) {
                JOptionPane.showMessageDialog(this, answer);
            } else {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Registered");
                LoginPage loginPage = new LoginPage();
            }
        } else if (source == this.clearButton) {
            this.surnameField.setText("");
            this.passwordField.setText("");
            this.nameField.setText("");
            this.surnameField.setText("");
        }
    }

}

