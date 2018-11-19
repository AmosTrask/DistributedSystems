/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import WebService.TTTWebService;
import WebService.TTTWebService_Service;
import model.Game;
import model.Move;
import model.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class GamePage extends JFrame implements ActionListener, Runnable {

    private User user;
    private Game game;
    private final TTTWebService proxy;
    private final TTTWebService_Service link;
    private Thread thread;
    private boolean player1;
    private boolean isPlaying;

    private JButton[][] cases;
    private JLabel gameIdLabel;
    private JLabel opponentLabel;
    private JLabel turnLabel;

    private List<Move> moves;

    public GamePage(User user, Game game, boolean player1) {
        this.player1 = player1;
        this.user = user;
        this.game = game;
        this.link = new TTTWebService_Service();
        this.proxy = link.getTTTWebServicePort();
        this.moves = new ArrayList<>();

        this.isPlaying = false;

        this.createPage();
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        boolean gameRunning = true;
        while (gameRunning) {

            String gameStateAnswer = this.proxy.getGameState(this.game.getGameId());
            if (gameStateAnswer.contains("ERROR")) {
                JOptionPane.showMessageDialog(this, gameStateAnswer);
            } else {

                if (this.game.getPlayer2() == null) {
                    //Player 2 just joigned the game only accessed by player1
                    this.turnLabel.setText("Your turn");
                    this.opponentLabel.setText(this.game.getPlayer2());
                    this.isPlaying = true;
                }
                String boardAnswer = this.proxy.getBoard(this.game.getGameId());
                System.out.println(boardAnswer);
                if (boardAnswer.contains("ERROR-DB")) {
                    JOptionPane.showMessageDialog(this, boardAnswer);
                } else if (!boardAnswer.equals("ERROR-NOMOVES")) {

                    String moves_str[] = boardAnswer.split("\n");
                    String[] items = moves_str[moves_str.length - 1].split(",");
                    for (String s : items) {
                        System.out.println(s);
                    }
                    Move lastMove = new Move(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]));
                    System.out.println(lastMove);
                    this.moves.add(lastMove);

                    if (moves_str.length == 2 && player1) {
                        this.game.setPlayer2(items[2]);
                    }

                    System.out.println(lastMove.getUserId());

                    if (moves_str.length % 2 == 0 && player1) {
                        this.turnLabel.setText("Your turn");
                        this.game.setNextPlayer("player1");
                        this.cases[lastMove.getX()][lastMove.getY()].setText("O");
                        this.isPlaying = true;
                    } else if (moves_str.length % 2 != 0 && player1) {
                        this.turnLabel.setText("Your opponent turn");
                        this.game.setNextPlayer("player2");
                        this.isPlaying = false;
                    } else if (moves_str.length % 2 != 0 && !player1) {
                        this.turnLabel.setText("Your turn");
                        this.game.setNextPlayer("player2");
                        this.cases[lastMove.getX()][lastMove.getY()].setText("O");
                        this.isPlaying = true;
                    } else {
                        this.turnLabel.setText("Your opponent turn");
                        this.game.setNextPlayer("player1");
                        this.isPlaying = false;
                    }

                    String winAnswer = this.proxy.checkWin(this.game.getGameId());
                    if (winAnswer.contains("ERROR")) {
                        JOptionPane.showMessageDialog(this, winAnswer);
                    } else {
                        int state = Integer.parseInt(winAnswer);
                        switch (state) {
                            case 0:
                                break;
                            case 1:
                                this.proxy.setGameState(this.game.getGameId(), 1);
                                if (player1) {
                                    JOptionPane.showMessageDialog(this, "You won");
                                } else {
                                    JOptionPane.showMessageDialog(this, "You lost");
                                }
                                gameRunning = false;
                                break;
                            case 2:
                                this.proxy.setGameState(this.game.getGameId(), 2);
                                if (!player1) {
                                    JOptionPane.showMessageDialog(this, "You won");
                                } else {
                                    JOptionPane.showMessageDialog(this, "You lost");
                                }
                                gameRunning = false;
                                break;
                            case 3:
                                this.proxy.setGameState(this.game.getGameId(), 3);
                                JOptionPane.showMessageDialog(this, "Draw");
                                gameRunning = false;
                                break;
                        }

                       
                    }

                }

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MainPage mainPage = new MainPage(this.user);
        this.dispose();
    }

    private void createPage() {
        this.setTitle("Tic Tac Toe");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        this.gameIdLabel = new JLabel("Game id:" + this.game.getGameId());
        if (player1) {
            this.opponentLabel = new JLabel("Waiting for an opponent");
        } else {
            this.opponentLabel = new JLabel(this.game.getPlayer1());

        }

        this.turnLabel = new JLabel("Not your turn");
        this.cases = new JButton[3][3];

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.cases[i][j] = new JButton();
                this.cases[i][j].addActionListener(this);
                this.cases[i][j].putClientProperty("INDEX", new Integer[]{i, j});

                boardPanel.add(this.cases[i][j]);
            }
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));

        this.add(mainPanel);

        mainPanel.setPreferredSize(new Dimension(325, 425));
        infoPanel.setPreferredSize(new Dimension(300, 50));
        boardPanel.setPreferredSize(new Dimension(300, 300));

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.SOUTH);

        infoPanel.add(this.gameIdLabel);
        infoPanel.add(this.opponentLabel);
        infoPanel.add(this.turnLabel);

        this.add(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        Integer[] index = (Integer[]) button.getClientProperty("INDEX");
        int i = index[0];
        int j = index[1];

        // If the player clicks on a square
        if (i < 3) {
            if (this.turnLabel.getText().equals("Your turn")) {
                String checkSquareAnswer = this.proxy.checkSquare(i, j, this.game.getGameId());
                if (checkSquareAnswer.contains("ERROR")) {
                    JOptionPane.showMessageDialog(this, checkSquareAnswer);
                } else if (Integer.parseInt(checkSquareAnswer) == 1) {
                    JOptionPane.showMessageDialog(this, "Square taken");
                } else {
                    String takeSquareAnswer = this.proxy.takeSquare(i, j, this.game.getGameId(), this.user.getId());
                    if (takeSquareAnswer.contains("ERROR")) {
                        JOptionPane.showMessageDialog(this, takeSquareAnswer);
                    } else if (Integer.parseInt(takeSquareAnswer) == 0) {
                        JOptionPane.showMessageDialog(this, "Error");
                    } else {
                        button.setText("X");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Not you turn");
            }
        }

    }
}
