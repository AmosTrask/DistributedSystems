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
    private User user;
    
    public ScorePage(User user) {
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.games = new ArrayList<>();
        this.user = user;
        this.createPage();
    }
    
    private void createPage() {
        this.setTitle("Score");
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.winsLabel = new JLabel(" ");
        this.lossesLabel = new JLabel(" ");
        this.drawsLabel = new JLabel(" ");
        
        this.add(this.winsLabel);
        this.add(this.lossesLabel);
        this.add(this.drawsLabel);
        
        this.pack();
        this.setVisible(true);
        this.showAllUsersGames();
    }
    
    public void showAllUsersGames() {
        String answer = proxy.showAllMyGames(this.user.getId());
        if (answer.contains("Error")) {
            JOptionPane.showMessageDialog(this, answer);
        } else {
            String[] lines = answer.split("/n");
            for (String line: lines) {
                String[] items = line.split(","); 
                Game game = new Game(Integer.parseInt(items[0]), items[1], items[2]);
                this.games.add(game);
            }
        }
        this.gamesAnalysis();
    }
    
    public void gamesAnalysis() {
        for (Game game: this.games) {
            String answer = proxy.getGameState(game.getGameId());
            game.setState(Integer.parseInt(answer));
            if ((this.user.getUsername().equals(game.getPlayer1()) && game.getState() == 1) || (this.user.getUsername().equals(game.getPlayer2()) && game.getState() == 2)) {
                this.user.addWin(game);
            } else if (game.getState() == 3) {
                this.user.addDraw(game);
            } else {
                this.user.addLost(game);
            }
        }
        this.winsLabel.setText("Wins: " + this.user.getWonNb());
        this.lossesLabel.setText("Lost: " + this.user.getLostNb());
        this.drawsLabel.setText("Draws: " + this.user.getDrawNb());
    }
}
