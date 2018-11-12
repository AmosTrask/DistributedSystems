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
public class LoginPage extends JFrame implements ActionListener{
    
    private JTextField usernameLogField;
    private JTextField passwordLogField;
    private JButton goToRegistrationButton;
    private JButton logButton;
    private TTTWebService proxy;
    private TTTWebService_Service link;
    
    public LoginPage () {
        this.createPage();
        link = new TTTWebService_Service();
        proxy = link.getTTTWebServicePort();
    }
    
    private void createPage(){
        this.setTitle("Login");
        this.setLayout(new GridLayout(3,2));
        
        this.usernameLogField = new JTextField();
        this.passwordLogField = new JTextField();
        this.goToRegistrationButton = new JButton("register");
        this.logButton = new JButton("Sign in");
        
        this.add(new JLabel("username"));
        this.add(usernameLogField);
        this.add(new JLabel("password"));
        this.add(passwordLogField);
        this.add(goToRegistrationButton);
        this.add(logButton);
        
        this.goToRegistrationButton.addActionListener(this);
        this.logButton.addActionListener(this);
        
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
        if (source == goToRegistrationButton) {
            this.dispose();
            RegistrationPage registrationPage = new RegistrationPage();
        } else if (source == logButton) {
        
        }
    }
}
