package com.duell.model;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Computer extends Player {

    public Computer(Board board) {
        super(board);
    }

    @Override
    public Dice play() {

        return null;
    }
}
