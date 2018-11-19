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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class MainPage extends JFrame implements ActionListener {

    private User user;
    private final TTTWebService proxy;
    private final TTTWebService_Service link;

    private JButton newGameButton;
    private JButton myScoreButton;
    private JButton joinGameButton;
    private JButton leagueButton;

    public MainPage(User user) {
        this.user = user;
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.createPage();
    }

    private void createPage() {
        this.setTitle("Main");
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.newGameButton = new JButton("New game");
        this.myScoreButton = new JButton("My score");
        this.joinGameButton = new JButton("Join game");
        this.leagueButton = new JButton("LeaderBoard");

        this.add(this.myScoreButton);
        this.add(this.newGameButton);
        this.add(this.joinGameButton);
        this.add(this.leagueButton);

        this.myScoreButton.addActionListener(this);
        this.newGameButton.addActionListener(this);
        this.joinGameButton.addActionListener(this);
        this.leagueButton.addActionListener(this);

        this.pack();
        this.setVisible(true);
    }

    private void createGame() {
        String newGameAnswer = this.proxy.newGame(this.user.getId());
        if (newGameAnswer.contains("ERROR")) {
            JOptionPane.showMessageDialog(this, newGameAnswer);
        } else {
            Game game = new Game(Integer.parseInt(newGameAnswer), this.user.getUsername());
            String setStateAnswer = this.proxy.setGameState(game.getGameId(), -1);
            if (newGameAnswer.contains("ERROR")) {
                JOptionPane.showMessageDialog(this, newGameAnswer);
            } else {
                this.dispose();
                GamePage gamePage = new GamePage(this.user, game, true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.newGameButton) {
            this.createGame();
        } else if (source == this.myScoreButton) {
            this.dispose();
            ScorePage scorePage = new ScorePage(this.user);
        } else if (source == this.joinGameButton) {
            this.dispose();
            JoinGamePage joinGamePage = new JoinGamePage(this.user);
        } else if (source == this.leagueButton) {
            this.dispose();
            LeaderboardPage leaderboardPage = new LeaderboardPage(this.user);
        }
    }
}
