package com.duell.model;

/**
 * Created by Sujil on 11/3/2016.
 */

public class TreeNode {
    private Dice dice;
    private int row;
    private int col;
    private Coordinates coordinates;

    public TreeNode(Dice d, int r, int c) {
        dice = d;
        row = r;
        col = c;
        coordinates = new Coordinates(row, col);
    }

    public int getRow() { return row;}

    public int getCol() { return col;}
    public Dice getDice() { return dice;}

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
