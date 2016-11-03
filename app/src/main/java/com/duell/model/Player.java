package com.duell.model;

import java.util.ArrayList;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Player {
    protected String printMessage;
    protected Board board;
    protected Coordinates prevCoordinates;
    protected Coordinates newCoordinates;
    protected boolean playerWon = false;

    public Player(Board b) {

        board = b;
    }

    public void play() {

    }

    public void play(Coordinates from, Coordinates to) {
        prevCoordinates = from;
        newCoordinates = to;
    }

    public Coordinates getPrevCoordinates() { return prevCoordinates;}
    public Coordinates getNewCoordinates() { return newCoordinates;}

    public boolean playerWins() {
        return playerWon;
    }

    public String getPrintMessage() { return printMessage;}

}
