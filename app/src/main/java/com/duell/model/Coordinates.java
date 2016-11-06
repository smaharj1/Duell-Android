package com.duell.model;

/**
 * Created by Sujil on 10/30/2016.
 */

public class Coordinates {
    private int row;
    private int col;


    public Coordinates(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() { return row;}

    public int getCol() { return col;}

    public String getString() {
        return (row+1) + "," + (col+1);
    }
}
