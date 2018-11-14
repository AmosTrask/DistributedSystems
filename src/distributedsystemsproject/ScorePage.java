/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributedsystemsproject;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class ScorePage extends JFrame {
    
    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel drawsLabel;
    private final List<Game> games;
    private final List<Game> wonGames;
    private final List<Game> drawGames;
    private final List<Game> lostGames;
    private final int userId;
    private final String userUsername;
    
    public ScorePage(String userUsername, int userId) {
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.games = new ArrayList<>();
        this.wonGames = new ArrayList<>();
        this.drawGames = new ArrayList<>();
        this.lostGames = new ArrayList<>();
        this.userId = userId;
        this.userUsername = userUsername;
        this.createPage();
    }
    
    public void createPage() {
        this.setTitle("Score");
        this.setLayout(new GridLayout(3,1));
        
        this.winsLabel = new JLabel();
        this.lossesLabel = new JLabel();
        this.drawsLabel = new JLabel();
        
        this.add(this.winsLabel);
        this.add(this.lossesLabel);
        this.add(this.drawsLabel);
        
        this.pack();
        this.setVisible(true);
    }
    
    public void showAllUsersGames() throws Exception {
        String answer = proxy.showAllMyGames(this.userId);
        if (answer.contains("Error")) {
            JOptionPane.showMessageDialog(this, answer);
        } else {
            String[] lines = answer.split("/n");
            for (String line: lines) {
                String[] items = line.split(","); 
                Game game = new Game(Integer.parseInt(items[0]), items[1], items[2], new SimpleDateFormat("dd/MM/yyyy").parse(items[3]));
                this.games.add(game);
            }
        }
    }
    
    public void fillGamesArray() {
        for (Game game: this.games) {
            String answer = proxy.getGameState(game.getGameId());
            game.setState(Integer.parseInt(answer));
            if ((this.userUsername.equals(game.getPlayer1()) && game.getState() == 1) || (this.userUsername.equals(game.getPlayer2()) && game.getState() == 2)) {
                this.wonGames.add(game);
            } else if (game.getState() == 3) {
                this.drawGames.add(game);
            } else {
                this.lostGames.add(game);
            }
        }
        this.winsLabel.setText("Wins: " + this.wonGames.size());
        this.lossesLabel.setText("Lost: " + this.lostGames.size());
        this.drawsLabel.setText("Draws: " + this.drawGames.size());
    }
}
