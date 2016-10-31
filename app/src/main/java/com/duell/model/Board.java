package com.duell.model;

/**
 * Created by Sujil on 10/30/2016.
 */

public class Board {
    private final int TOTAL_ROWS = 8;
    private final int TOTAL_COLUMNS = 9;

    private boolean isGodMode = false;


    private Dice board[][] = new Dice[TOTAL_ROWS][TOTAL_COLUMNS];

    public Board() {
        // Goes through the whole board of cell and initializes the board.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                Dice d;
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

    /**
        Sets the board.
     */
    public boolean setBoard(String[][] givenBoard) {
        // Loops through each cell and stores the dices.
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                if (givenBoard[i][j] == "0") {
                    board[i][j] = null;
                }
                else {
                    board[i][j] = new Dice(givenBoard[i][j]);
                }
            }
        }

        return true;
    }

    // Checks if the move is legal in terms of user's turn and who the user is replacing
    public boolean isLegal(Coordinates oldPos, Coordinates newPos, boolean isPlayerComputer) {
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

    //TODO: not checked.
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
            if (tempRow + 1 == newRow && tempCol + 1 == newCol) continue;

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
            if (tempRow + 1 == newRow && tempCol + 1 == newCol) continue;

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

    

    public Dice getDiceAt(int row, int col) {
        if (board[row][col] == null) return null;

        return board[row][col];
    }

}
