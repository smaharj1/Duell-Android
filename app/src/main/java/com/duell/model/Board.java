/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class holds the board of the game. It performs various funcitonalities regarding modifications
 * to the board.
 */

public class Board {
    // Defines the constants for total row and columns.
    private final int TOTAL_ROWS = 8;
    private final int TOTAL_COLUMNS = 9;

    // Determines if the player mode is god mode.
    private boolean isGodMode = false;

    // Holds the board for the game.
    private Dice board[][] = new Dice[TOTAL_ROWS][TOTAL_COLUMNS];

    /**
     * Constructor that initiates the values of the board.
     * @param keys Arrays that consists of key dices of the player.
     */
    public Board(int[] keys) {
        // Goes through the whole board of cell and initializes the board.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (i == 0) {
                    Dice tempDice = computeDice(keys[j],j, true);
                    board[i][j] = tempDice;
                }
                else if (i == TOTAL_ROWS - 1) {
                    Dice tempDice = computeDice(keys[j],j, false);
                    board[i][j] = tempDice;
                }
                else {
                    board[i][j] = null;
                }
            }
        }

        // Initializes god mode as false.
        //isGodMode = false;
    }

    /**
     * Default constructor that sets the board to default values.
     */
    public Board() {
        // Goes through the whole board of cell and initializes the board.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (i == 0) {
                    board[i][j] = new Dice("C21");
                }
                else if (i == TOTAL_ROWS - 1) {
                    board[i][j] = new Dice("H21");
                }
                else {
                    board[i][j] = null;
                }
            }
        }

        // Initializes god mode as false.
        //isGodMode = false;
    }

    /**
     * Computes and returns a Dice object when the top, index and if computer is provided.
     * @param top It holds the top face of the die.
     * @param index It holds the index of the die of a player.
     * @param isComputer It holds if the die is a computer's die or human's.
     * @return Returns Dice object after the computation.
     */
    public Dice computeDice(int top, int index, boolean isComputer) {
        // Sets the right die to 3. It is constant in the beginning.
        int right = 3;
        int front = Dice.computeFrontFace(top,right);

        // Checks if the die is the mid point. If it is, set it as king.
        if (index == TOTAL_COLUMNS/2) {
            return new Dice(isComputer,1,1,1);
        }
        else {
            return new Dice(isComputer, top, right, 7-front);
        }
    }

    /**
     * Returns the total number of rows in the board.
     * @return Returns the total number of rows.
     */
    public int getTotalRows() { return TOTAL_ROWS;}

    /**
     * Returns the total number of columns in the board.
     * @return Returns the total number of columns.
     */
    public int getTotalColumns() { return TOTAL_COLUMNS;}

    /**
     * Sets the board according to the given values of the string representation of the board.
     * @param givenBoard It holds the 2D array that holds the values of the board.
     * @return Returns true if the board is successfully set.
     */
    public boolean setBoard(String[][] givenBoard) {
        // Loops through each cell and stores the dices.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (givenBoard[i][j].contains("0") || givenBoard[i][j].isEmpty()) {
                    board[i][j] = null;
                }
                else {
                    //System.out.println("Board: " + givenBoard[i][j]);
                    board[i][j] = new Dice(givenBoard[i][j]);
                }
            }
        }

        return true;
    }


    /**
     * Checks if the move is legal in terms of user's turn and who the user is replacing.
     * @param oldPos It holds the coordinates of old position.
     * @param newPos It holds the new coordinates that the die has to move to.
     * @param isPlayerComputer It holds if the player is computer.
     * @return Returns true if the move indicated is legal.
     */
    public boolean isLegal(Coordinates oldPos, Coordinates newPos, boolean isPlayerComputer) {
        // Holds the row and column of old and new coordinates.
        int oldRow = oldPos.getRow();
        int oldCol = oldPos.getCol();
        int newRow = newPos.getRow();
        int newCol = newPos.getCol();

        // If the cell in the board is empty, return that it is empty.
        if (board[oldRow][oldCol] == null) {
            isGodMode = false;
            return false;
        }

        // Holds if the player is computer or human.
        boolean isComputer = board[oldRow][oldCol].isPlayerComputer();

        // Check if the the user is trying to move other player's dice.
        if (isPlayerComputer != isComputer) {
            isGodMode = false;
            return false;
        }

        // Checks if the user is trying to replace their own player by the movement.
        if (board[newRow][newCol] != null) {
            if ((board[oldRow][oldCol].isPlayerComputer() && board[newRow][newCol].isPlayerComputer()) ||
            !board[oldRow][oldCol].isPlayerComputer() && !board[newRow][newCol].isPlayerComputer()) {
                isGodMode = false;
                return false;
            }
        }

        return true;
    }

    /**
     * Returns all the coordinates of the path from one coordinate to another if there are no hindrances.
     * @param from It holds the starting coordinates.
     * @param to It holds the ending coordinates.
     * @return Returns the arraylist of coordinates that the die moves through to reach the destination.
     */
    public ArrayList<Coordinates> getPathCoordinates(Coordinates from, Coordinates to) {
        // Initialization of directions and the answer arraylist.
        boolean[] directions = {true, true};
        ArrayList<Coordinates> pathCoordinates = new ArrayList<>();

        int row1 = from.getRow();
        int col1 = from.getCol();
        int row2 = to.getRow();
        int col2 = to.getCol();

        // Checks if the path is good. If it is, then go through each path and record the location.
        if (isPathGood(from, to, directions)) {

            if (directions[0] == true) {
                // This is when frontal is first
                if (row1 < row2) {
                    for (int i = row1+1; i <=row2; i++) {
                        if (col1 == col2 && i == row2) continue;
                        pathCoordinates.add(new Coordinates(i,col1));
                    }
                }
                else if (row1 > row2){
                    for (int i = row1-1; i >= row2; i--) {
                        if (col1 == col2 && i == row1) continue;
                        pathCoordinates.add((new Coordinates(i, col1)));
                    }
                }

                if (col1 < col2) {
                    for (int i =col1+1; i<col2; i++) {
                        pathCoordinates.add(new Coordinates(row2, i));
                    }
                }
                else if (col1 > col2) {
                    for (int i =col1-1; i>col2; i--) {
                        pathCoordinates.add(new Coordinates(row2, i));
                    }
                }
            }
            else if (directions[1] == true) {
                // This is when lateral is first
                if (col1 < col2) {
                    for (int i =col1+1; i<=col2; i++) {
                        if (row1 == row2 && i == col1) continue;
                        pathCoordinates.add(new Coordinates(row1, i));
                    }

                }
                else if (col1 > col2) {
                    for (int i =col1-1; i>=col2; i--) {
                        if (row1 == row2 && i == col2) continue;
                        pathCoordinates.add(new Coordinates(row1, i));
                    }
                }

                if (row1 < row2) {

                    for (int i = row1+1; i < row2; i++) {
                        pathCoordinates.add(new Coordinates(i,col2));
                    }
                }
                else if (row1 > row2){
                    for (int i = row1-1; i > row2; i--) {
                        pathCoordinates.add((new Coordinates(i, col2)));
                    }
                }
            }

        }

        // Returns the path coordinates.
        return pathCoordinates;


    }


    /**
     * Checks if the path is good. It checks if there are any hindrances on the way.
     * @param oldPos It holds the old position of the die.
     * @param newPos It holds the new position of the die.
     * @param correctPaths It holds the possibilities of paths if frontal first and lateral first.
     * @return Returns true if the path is legal and allowed.
     */
    public boolean isPathGood(Coordinates oldPos, Coordinates newPos, boolean[] correctPaths) {
        // Initializes the row and columns.
        int oldRow = oldPos.getRow();
        int oldCol = oldPos.getCol();
        int newRow = newPos.getRow();
        int newCol = newPos.getCol();

        // Computes the total frontal and side movement needed.
        int frontal = newRow - oldRow;
        int side = newCol - oldCol;

        // Checks if the board's old position is empty. If it is empty, then return false.
        if (board[oldRow][oldCol] == null) {
            return false;
        }

        // At this point, it is obvious that the frontal and side are going to be good.
        if (board[oldRow][oldCol].getTop() != Math.abs(frontal) + Math.abs(side)) {
            isGodMode = false;
            return false;
        }

        int index = 0;

        // If it is the computer, then do the increments accordingly and check in each location
        // for the cells to be empty.
        int tempRow = oldRow;
        int tempCol = oldCol;

        // If frontal or side movement is not needed remove it as correct paths.
        if (Math.abs(frontal) == 0) correctPaths[0] = false;
        if (Math.abs(side) == 0) correctPaths[1] = false;

        // Loops frontal first and checks in each and every location if there are any dices.
        for (index = 1; index <= Math.abs(frontal); index++) {
            int j = frontal < 0 ? -index : index;
            tempRow = oldRow + j;

            // This is if its the main location, just ignore this step.
            if (tempRow == newRow && tempCol == newCol) continue;

            // If there is a die on the way, indicate that path as not possible.
            if (board[tempRow][tempCol] != null) correctPaths[0] = false;
        }

        // Make a 90 degree turn if needed and if frontal path is going correct until now.
        if (correctPaths[0] == true) {
            for (index = 1; index < Math.abs(side); index++) {
                int j = side < 0 ? -index : index;
                tempCol = oldCol+ j;
                if (board[tempRow][tempCol] != null) correctPaths[0] = false;
            }
        }

        // Reset temp row and columns.
        tempRow = oldRow;
        tempCol = oldCol;

        // Loops through lateral movement first and checks if there are any dices in each location.
        for (index = 1; index <= Math.abs(side); index++) {
            int j = side < 0 ? -index : index;
            tempCol = oldCol + j;

            // If its the main location, just ignore this step.
            if (tempRow == newRow && tempCol == newCol) continue;

            // If there is a dice on the way, indicate it as incorrect path.
            if (board[tempRow][tempCol] != null) correctPaths[1] = false;
        }

        // Make a 90 degree turn if needed and if frontal path is going correct until now.
        if (correctPaths[1] == true) {
            for (index = 1; index < Math.abs(frontal); index++) {
                int j = frontal < 0 ? -index : index;
                tempRow = oldRow + j;
                if (board[tempRow][tempCol] != null) correctPaths[1] = false;
            }
        }

        // Checks if both direction paths are incorrect.
        if (!correctPaths[0] && !correctPaths[1]) {
            isGodMode = false;
        }

        isGodMode = false;

        // Returns true if either direction is true.
        return correctPaths[0] || correctPaths[1];
    }

    /**
     * Moves the die from one coordinate to another.
     * @param oldPos It holds the old coordinates of the die.
     * @param newPos It holds the new coordinates of the die.
     * @param direction It holds the direction that we want to move to.
     * @return Returns a dice if it eats any die during the movement. Returns null if nothing is eaten.
     */
    public Dice move(Coordinates oldPos, Coordinates newPos, char direction) {
        int oldRow = oldPos.getRow();
        int oldCol = oldPos.getCol();
        int newRow = newPos.getRow();
        int newCol = newPos.getCol();

        Dice diceAte = null;

        // Holds if the player is computer or human.
        boolean isComputer = board[oldRow][oldCol].isPlayerComputer();

        // If the cell in the board is empty, return that it is empty.
        int frontal = newRow - oldRow;
        int side = newCol - oldCol;

        if (!isComputer) {
            if (direction == 'f') {
                // Checks if the dice is rolled forward or backward and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(frontal); i++) {
                    if (frontal < 0) {
                        board[oldRow][oldCol].moveForward();

                    }else {
                        board[oldRow][oldCol].moveBackward();
                    }

                }

                // Checks if the dice is rolled right or left and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(side); i++) {
                    if (side > 0) {
                        board[oldRow][oldCol].moveRight();
                    }
                    else {
                        board[oldRow][oldCol].moveLeft();
                    }
                }
            }
            else {
                // Checks if the dice is rolled right or left and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(side); i++) {
                    if (side > 0) {
                        board[oldRow][oldCol].moveRight();
                    }
                    else {
                        board[oldRow][oldCol].moveLeft();
                    }
                }

                // Checks if the dice is rolled forward or backward and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(frontal); i++) {
                    if (frontal < 0) {
                        board[oldRow][oldCol].moveForward();
                    }
                    else {
                        board[oldRow][oldCol].moveBackward();
                    }
                }
            }
        }
        else {
            if (direction == 'f') {
                // Checks if the dice is rolled forward or backward and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(frontal); i++) {
                    if (frontal > 0) {
                        board[oldRow][oldCol].moveForward();
                    }
                    else {
                        board[oldRow][oldCol].moveBackward();
                    }
                }

                // Checks if the dice is rolled right or left and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(side); i++) {
                    if (side < 0) {
                        board[oldRow][oldCol].moveRight();
                    }
                    else {
                        board[oldRow][oldCol].moveLeft();
                    }
                }
            }
            else {
                // Checks if the dice is rolled right or left and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(side); i++) {
                    if (side < 0) {
                        board[oldRow][oldCol].moveRight();
                    }
                    else {
                        board[oldRow][oldCol].moveLeft();
                    }

                }

                // Checks if the dice is rolled forward or backward and assigns the rolling accordingly.
                for (int i = 0; i < Math.abs(frontal); i++) {
                    if (frontal >0) {
                        board[oldRow][oldCol].moveForward();
                    }
                    else {
                        board[oldRow][oldCol].moveBackward();
                    }
                }
            }
        }

        // Adds the dice to the Cell and removes the dice from previous location.
        if (board[newRow][newCol] != null) {
            //cout << "Dice is eaten!" << endl << endl;
            diceAte = board[newRow][newCol];
            board[newRow][newCol] = null;
        }

        board[newRow][newCol] = board[oldRow][oldCol];
        board[oldRow][oldCol]= null;
        //Log.v("OLA-BOARD", "new row and col is: " + newRow + " " +newCol);

        return diceAte;
    }

    /**
     * Returns the dice at the position specified.
     * @param given It holds the coordinates of the board.
     * @return Returns the dice at the coordinates specified.
     */
    public Dice getDiceAt(Coordinates given) {
        if (board[given.getRow()][given.getCol()] == null) return null;

        return board[given.getRow()][given.getCol()];
    }

    /**
     * Returns the dice at the position specified.
     * @param row It holds the row number.
     * @param col It holds the column number.
     * @return Returns the dice at the coordinates specified.
     */
    public Dice getDiceAt(int row, int col) {
        if (board[row][col] == null) return null;

        return board[row][col];
    }

}
