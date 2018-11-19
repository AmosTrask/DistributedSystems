/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import model.Game;
import model.User;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class JoinGamePage extends JFrame implements ActionListener {

    private final User user;
    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    private List<Game> games;
    private JButton backButton;

    public JoinGamePage(User user) {
        this.user = user;
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.games = new ArrayList();
        this.createPage();
    }

    private void createPage() {

        this.setTitle("Join game");
        this.setSize(900, 900);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel myLabel = new JLabel("Click on a row to join a game");
        this.backButton = new JButton("Back");
        this.backButton.addActionListener(this);

        String[] columnNames = {"Game id",
            "Player username",
            "Started"};

        String openGamesAnswer = this.proxy.showOpenGames();
        if (openGamesAnswer.contains("Error")) {
            JOptionPane.showMessageDialog(this, openGamesAnswer);
        } else {
            String[] games_str = openGamesAnswer.split("\n");
            Object[][] data = new Object[games_str.length][3];
            System.out.println(games_str.length);
            for (int i = 0; i < games_str.length; i++) {
                String[] items = games_str[i].split(",");
                data[i][0] = items[0];
                data[i][1] = items[1];
                data[i][2] = items[2];
                this.games.add(new Game(Integer.parseInt(items[0]), items[1]));
            }
            JTable table = new JTable(data, columnNames);

            table.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
                if (!event.getValueIsAdjusting()) {
                    System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
                    int gameId = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                    String joinGameAnswer = this.proxy.joinGame(this.user.getId(), gameId);
                    if (joinGameAnswer.contains("Error") || joinGameAnswer.equals("0")) {
                        JOptionPane.showMessageDialog(this, joinGameAnswer);
                    } else {
                        String answerSetGameState = this.proxy.setGameState(gameId, 0);
                        if (answerSetGameState.contains("Error")) {
                            JOptionPane.showMessageDialog(this, answerSetGameState);
                        } else {
                            this.dispose();
                            Game game = new Game(gameId, table.getValueAt(table.getSelectedRow(), 1).toString(), this.user.getUsername());
                            GamePage gamePage = new GamePage(this.user, game, false);
                        }
                    }
                }
            });
            
            JPanel southPanel = new JPanel(new BorderLayout());
            southPanel.add(myLabel, BorderLayout.NORTH);
            southPanel.add(table, BorderLayout.SOUTH);
            
            mainPanel.add(this.backButton, BorderLayout.NORTH);
            mainPanel.add(southPanel, BorderLayout.SOUTH);
            
            this.add(mainPanel);
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
