package com.duell.model;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Human extends Player {
    private final char DIRECTION_NOT_CHOSEN = 'a';

    public Human(Board board) {
        super(board);
    }

    @Override
    public boolean play(Coordinates from, Coordinates to, char dir) {
        prevCoordinates = from;
        newCoordinates = to;
        direction = dir;

        boolean[] directions = {true, true};

        if (board.isLegal(prevCoordinates, newCoordinates, false) &&
                board.isPathGood(prevCoordinates, newCoordinates, directions)) {

            if (validateDirection(direction, directions) == false) {
                return false;
            }

            if (board.getDiceAt(newCoordinates) != null && board.getDiceAt(newCoordinates).isPlayerKing()) {
                printMessage = "Ok, you are smarter";
                playerWon = true;
            }

            board.move(prevCoordinates, newCoordinates, direction);
            printMessage = "That's a sleek move";

            return true;
        }

        printMessage = "You can't do that. That move is physically/virtually impossible";
        return false;
    }

    @Override
    public void play() {
        refreshPlayers();
        boolean movePossible = false;

//        if (canWin()) {
//            printMessage = "The Best There Is, The Best There Was and The Best There Ever Will Be - Bret Hart";
//            movePossible = true;
//            playerWon = true;
//        }
        //else {
            TreeNode threat = isKingInThreat();
            if (threat != null) {
                if (canEatThreat(threat)){
                    printMessage = "Move the die indicated to remove the threat. ";
                    movePossible = true;
                }
                else {
                    // Blocks the move if possible.
                    if (blockMove(threat)) {
                        printMessage = "Move the indicated die to block. ";
                        movePossible = true;
                    }
                }
            }
            else {
                // Executes if there is not threat to the king.
                if (canEatOpponent()) {
                    printMessage = "Move the die indicated to eat my players. ";

                    movePossible = true;
                }
            }
        //}

        if (!movePossible) {
            // Just make a general move that gets you closer to the king.
            safeOffenceMove();
            printMessage = "There are no possible preys for you. Just move this safe one. ";
        }



        // Temporarily creates an array of directions to call the main board function.
        boolean[] tempDir = {true, true};

        direction = 'f';

        // Checks if the path is good.
        board.isPathGood(prevCoordinates, newCoordinates, tempDir);



        if (tempDir[0] == false) {
            direction = 'l';
        }

        if (tempDir[0] && tempDir[1]) {
            bothDirectionPossible = true;
        }
        else bothDirectionPossible = false;

        printMessage  = printMessage + " Move your player ";
        if (direction == 'f') {
            printMessage = printMessage + "frontal first if direction is enabled";
        }
        else {
            printMessage = printMessage + "lateral first if direction is enabled";
        }

    }

    @Override
    public void refreshPlayers() {
        // Resets the dices of current and opponent players.
        opponentPlayer.clear();
        currentPlayer.clear();

        // Loops through each square in the board and adds the dices to the vectors of current and
        // opponent players accordingly.
        for (int row = 0; row < board.getTotalRows(); row++) {
            for (int col = 0; col < board.getTotalColumns(); col++) {
                if (board.getDiceAt(row, col) != null) {
                    Dice d = board.getDiceAt(row, col);

                    // Checks if the dice in a square is of same players as of provided player.
                    // If not, it adds the dice to opponent's vector.
                    if (d.isPlayerComputer()) {
                        opponentPlayer.add(new TreeNode(d, row, col));
                    }
                    else {
                        currentPlayer.add(new TreeNode(d, row, col));
                    }
                }
            }
        }
    }


    private boolean validateDirection(char userChoice, boolean[] directions) {
        if (directions[0] == true && directions[1] == true) {
            if (userChoice == DIRECTION_NOT_CHOSEN) {
                printMessage = "You have to choose the direction";
                return false;
            }
            else {
                direction = userChoice;
                return true;
            }
        }

        if (directions[0] == true) {
            direction = 'f';
        }
        else {
            direction = 'l';
        }

        return true;
    }
}
