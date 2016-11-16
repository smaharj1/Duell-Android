/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

/**
 * This class holds the row and column of any sort of board.
 */

public class Coordinates {
    // Private variables that hold row and column numbers.
    private int row;
    private int col;

    /**
     * Default constructor.
     * @param r It holds the row number.
     * @param c It holds the column number.
     */
    public Coordinates(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * Returns the row number.
     * @return Returns the row number.
     */
    public int getRow() { return row;}

    /**
     * Returns the column number.
     * @return Returns the column number.
     */
    public int getCol() { return col;}

    /**
     *  Returns the string representation of the coordinates.
     * @return Returns the string representation of the coordintes.
     */
    public String getString() {
        return (row+1) + "," + (col+1);
    }
}
