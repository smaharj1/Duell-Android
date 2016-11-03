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

    public Player(Board b) {

        board = b;
    }

    public void play() {

    }

    public Coordinates getPrevCoordinates() { return prevCoordinates;}
    public Coordinates getNewCoordinates() { return newCoordinates;}

}
