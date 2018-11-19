/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Game;
import model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class LeaderboardPage extends JFrame implements ActionListener{

    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    private List<User> players;
    private Map<String, User> userMap;
    private User user;
    private JButton backButton;
    private JPanel mainPanel;

    public LeaderboardPage(User user) {
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.players = new ArrayList();
        this.userMap = new HashMap<>();
        this.user = user;
        this.createPage();
    }

    private void createPage() {
        this.setTitle("Score");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.backButton = new JButton("Back");
        this.backButton.addActionListener(this);
        
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.backButton, BorderLayout.NORTH);
        
        this.getLeagueTable();

        this.add(this.mainPanel);
        
        this.pack();
        this.setVisible(true);
    }

    private void getLeagueTable() {
        String answer = proxy.leagueTable();
        if (answer.contains("Error")) {
            JOptionPane.showMessageDialog(this, answer);
        } else {
            String[] lines = answer.split("\n");
            for (String line : lines) {
                String[] items = line.split(",");
                Game game = new Game(Integer.parseInt(items[0]), items[1], items[2]);
                this.userMap.putIfAbsent(items[1], new User(items[1]));
                this.userMap.putIfAbsent(items[2], new User(items[2]));
                if (items[3].equals("1")) {
                    this.userMap.get(items[1]).addWin(game);
                    this.userMap.get(items[2]).addLost(game);
                } else if (items[3].equals("2")) {
                    this.userMap.get(items[2]).addWin(game);
                    this.userMap.get(items[1]).addLost(game);
                } else if (items[3].equals("3")) {
                    this.userMap.get(items[2]).addDraw(game);
                    this.userMap.get(items[1]).addDraw(game);
                }
            }
            for (Map.Entry<String, User> entry : this.userMap.entrySet()) {
                this.players.add(entry.getValue());
            }
            
            Collections.sort(this.players);

            String[] columnNames = {"Username",
                "Wins",
                "Losses",
                "Draws"};
            Object[][] data = new Object[this.players.size()][4];

            for (int i = 0; i < this.players.size(); i++) {
                data[i][0] = this.players.get(i).getUsername();
                data[i][1] = this.players.get(i).getWonNb();
                data[i][2] = this.players.get(i).getLostNb();
                data[i][3] = this.players.get(i).getDrawNb();
            }
            
            
            JPanel tablePanel = new JPanel(new BorderLayout());
            JTable table = new JTable(data, columnNames);
            tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
            tablePanel.add(table, BorderLayout.SOUTH);
            this.mainPanel.add(tablePanel, BorderLayout.SOUTH);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.backButton) {
            this.dispose();
            MainPage mainPage = new MainPage(this.user);
        }
    }
}
