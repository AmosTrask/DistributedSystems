/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystemsproject;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class MainPage extends JFrame implements ActionListener{
    
    private String userUsername;
    private int userId;
    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    
    private JButton newGameButton;
    private JButton myScoreButton;
    
    public MainPage(String userUsername, int userId) {
        this.userUsername = userUsername;
        this.userId = userId;
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.createPage();
    }
    
    public void createPage() {
        this.setTitle("Main");
        this.setLayout(new GridLayout(2,1));
        
        this.newGameButton = new JButton("New game");
        this.myScoreButton = new JButton("My score");
        
        this.add(this.myScoreButton);
        this.add(this.newGameButton);
        
        this.myScoreButton.addActionListener(this);
        this.newGameButton.addActionListener(this);
        
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.newGameButton) {

        } else if (source == this.myScoreButton) {

        }
    }
}
