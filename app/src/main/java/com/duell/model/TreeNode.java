/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

/**
 * This class holds the dice and the position of the dice on the board.
 */

public class TreeNode {
    // Private variables indicating the dice's information.
    private Dice dice;
    private int row;
    private int col;
    private Coordinates coordinates;

    /**
     * Default constructors.
     * @param d It holds the Dice object.
     * @param r It holds the row.
     * @param c It holds the column.
     */
    public TreeNode(Dice d, int r, int c) {
        dice = d;
        row = r;
        col = c;
        coordinates = new Coordinates(row, col);
    }

    /**
     *
     * @return Returns the row number.
     */
    public int getRow() { return row;}

    /**
     *
     * @return Returns the column number.
     */
    public int getCol() { return col;}

    /**
     *
     * @return Returns the dice number.
     */
    public Dice getDice() { return dice;}

    /**
     *
     * @return Returns the coordinates.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
