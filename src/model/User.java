package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 18093396 Vincent Redouté
 */
public class User implements Comparable{
    
    private int id;
    private String username;
    private List<Game> wonGames;
    private List<Game> drawGames;
    private List<Game> lostGames;
    
    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.wonGames = new ArrayList<>();
        this.drawGames = new ArrayList<>();
        this.lostGames = new ArrayList<>();
    }
    
     public User(String username) {
        this.username = username;
        this.wonGames = new ArrayList<>();
        this.drawGames = new ArrayList<>();
        this.lostGames = new ArrayList<>();
    }
     
    @Override
    public int compareTo(Object comparestu) {
        int compareWon =((User)comparestu).getWonNb();
        /* For Ascending order*/
        return  compareWon - this.wonGames.size();

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
    
    public void addWin(Game game) {
        this.wonGames.add(game);
    }
    
    public void addLost(Game game) {
        this.lostGames.add(game);
    }
    
    public void addDraw(Game game) {
        this.drawGames.add(game);
    }
    
    public int getWonNb() {
        return this.wonGames.size();
    }
    
    public int getLostNb() {
        return this.lostGames.size();
    }
    
    public int getDrawNb() {
        return this.drawGames.size();
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getUsername() {
        return this.username;
    }
    
}
