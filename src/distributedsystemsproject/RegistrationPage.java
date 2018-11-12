/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystemsproject;

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

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class RegistrationPage extends JFrame implements ActionListener {
    
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    
    private JButton registrationButton;
    private JButton clearButton;
    
    private TTTWebService proxy;
    private TTTWebService_Service link;
    
    public RegistrationPage() {
        this.createPage();
        link = new TTTWebService_Service();
        proxy = link.getTTTWebServicePort();
    }
    
    private void createPage() {
        
        this.setTitle("Register");
        this.setLayout(new GridLayout(6,2));
        
        this.nameField = new JTextField();
        this.surnameField = new JTextField();
        this.usernameField = new JTextField();
        this.passwordField = new JTextField();
        this.emailField = new JTextField();
        this.registrationButton = new JButton("register");
        this.clearButton = new JButton("clear");
        
        this.add(nameField);
        this.add(new JLabel("name"));
        this.add(surnameField);
        this.add(new JLabel("surname"));
        this.add(usernameField);
        this.add(new JLabel("username"));
        this.add(passwordField);
        this.add(new JLabel("password"));
        this.add(emailField);
        this.add(new JLabel("email"));
        this.add(registrationButton);
        this.add(clearButton);
        
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
    }

}

