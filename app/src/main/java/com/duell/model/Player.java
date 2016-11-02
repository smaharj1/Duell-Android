package com.duell.model;

import java.util.ArrayList;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Player {
    private ArrayList<Dice> playerDices;
    private String printMessage;
    private Board board;

    public Player(Board b) {
        playerDices = new ArrayList<>();
        board = b;
    }

    public Dice play() {

        return null;
    }

}
