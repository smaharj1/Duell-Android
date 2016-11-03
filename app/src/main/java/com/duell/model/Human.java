package com.duell.model;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Human extends Player {

    public Human(Board board) {
        super(board);
    }

    @Override
    public void play(Coordinates from, Coordinates to) {
        prevCoordinates = from;
        newCoordinates = to;
    }

    public boolean checkValidity() {


        return false;
    }
}
