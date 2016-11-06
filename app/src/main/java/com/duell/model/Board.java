package com.duell.model;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sujil on 10/30/2016.
 */

public class Board {
    private final int TOTAL_ROWS = 8;
    private final int TOTAL_COLUMNS = 9;

    private boolean isGodMode = false;

    private Dice board[][] = new Dice[TOTAL_ROWS][TOTAL_COLUMNS];

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

    public Dice computeDice(int top, int index, boolean isComputer) {
        int right = 3;
        int front = Dice.computeFrontFace(top,right);

        if (index == TOTAL_COLUMNS/2) {
            return new Dice(isComputer,1,1,1);
        }
        else {
            return new Dice(isComputer, top, right, 7-front);
        }
    }

    public Board(Dice[] humanInitDices, Dice[] botInitDices) {
        // Goes through the whole board and initializes the new board according to the dices given.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (i == 0) {
                    board[i][j] = botInitDices[j];
                }
                else if (i == TOTAL_ROWS - 1) {
                    board[i][j] = humanInitDices[j];
                }
                else {
                    board[i][j] = null;
                }
            }
        }

        //isGodMode = false;
    }

    public int getTotalRows() { return TOTAL_ROWS;}
    public int getTotalColumns() { return TOTAL_COLUMNS;}

    /**
        Sets the board.
     */
    public boolean setBoard(String[][] givenBoard) {
        // Loops through each cell and stores the dices.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (givenBoard[i][j].contains("0") || givenBoard[i][j].isEmpty()) {
                    board[i][j] = null;
                }
                else {
                    System.out.println("Board: " + givenBoard[i][j]);
                    board[i][j] = new Dice(givenBoard[i][j]);
                }
            }
        }

        return true;
    }

    // Checks if the move is legal in terms of user's turn and who the user is replacing
    public boolean isLegal(Coordinates oldPos, Coordinates newPos, boolean isPlayerComputer) {
        //Log.v("OLA-BOARD: ", "Checking for " + getDiceAt(oldPos).getValue());


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

    public ArrayList<Coordinates> getPathCoordinates(Coordinates from, Coordinates to) {
        boolean[] directions = {true, true};
        ArrayList<Coordinates> pathCoordinates = new ArrayList<>();

        int row1 = from.getRow();
        int col1 = from.getCol();
        int row2 = to.getRow();
        int col2 = to.getCol();

        if (isPathGood(from, to, directions)) {
            if (directions[0] == true) {
                // This is when frontal is first
                if (row1 < row2) {
                    for (int i = row2-1; i >= row1; i--) {
                        if (col1 == col2 && i == row1) continue;
                        pathCoordinates.add(new Coordinates(i,col2));
                    }
                }
                else if (row1 > row2){
                    for (int i = row1-1; i >= row2; i--) {
                        if (col1 == col2 && i == row1) continue;
                        pathCoordinates.add((new Coordinates(i, col1)));
                    }
                }

                if (col1 < col2) {
                    for (int i =col2-1; i>col1; i--) {
                        pathCoordinates.add(new Coordinates(row1, i));
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
                    for (int i =col2-1; i>=col1; i--) {
                        if (row1 == row2 && i == col1) continue;
                        pathCoordinates.add(new Coordinates(row2, i));
                    }
                }
                else if (col1 > col2) {
                    for (int i =col1-1; i>=col2; i--) {
                        if (row1 == row2 && i == col2) continue;
                        pathCoordinates.add(new Coordinates(row1, i));
                    }
                }

                if (row1 < row2) {
                    for (int i = row2-1; i > row1; i--) {
                        pathCoordinates.add(new Coordinates(i,col1));
                    }
                }
                else if (row1 > row2){
                    for (int i = row1-1; i > row2; i--) {
                        pathCoordinates.add((new Coordinates(i, col2)));
                    }
                }
            }

        }

        return pathCoordinates;


    }

    public boolean bothPathPossible(Coordinates oldPos, Coordinates newPos) {
        int oldRow = oldPos.getRow();
        int oldCol = oldPos.getCol();
        int newRow = newPos.getRow();
        int newCol = newPos.getCol();

        int rowDiff = Math.abs(oldRow-newRow);
        int colDiff = Math.abs(oldCol - newCol);

        if (rowDiff == 0 || colDiff == 0) return false;
        return true;
    }


    //To check if the path is good. It checks if there are any distractions on the way.
    public boolean isPathGood(Coordinates oldPos, Coordinates newPos, boolean[] correctPaths) {
        int oldRow = oldPos.getRow();
        int oldCol = oldPos.getCol();
        int newRow = newPos.getRow();
        int newCol = newPos.getCol();

        int frontal = newRow - oldRow;
        int side = newCol - oldCol;

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

    public Dice getDiceAt(Coordinates given) {
        if (board[given.getRow()][given.getCol()] == null) return null;

        return board[given.getRow()][given.getCol()];
    }

    public Dice getDiceAt(int row, int col) {
        if (board[row][col] == null) return null;

        return board[row][col];
    }

}
