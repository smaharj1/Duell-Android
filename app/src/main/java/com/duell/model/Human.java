/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

/**
 * This class runs the game for Human. It performs all the play the human makes.
 */

public class Human extends Player {
    // Constant that tells if the direction is not chosen.
    private final char DIRECTION_NOT_CHOSEN = 'a';

    /**
     * Default constructor.
     * @param board It holds the Board object.
     */
    public Human(Board board) {
        super(board);
    }

    /**
     * It plays the human's game. It gets the user inputs from the UI of the game.
     * @param from It is the coordinate that the die is to move from.
     * @param to It  is the coordinate that the die is to move to.
     * @param dir It holds the direction to move ot.
     * @return Returns true if the play as the user wished is possible.
     */
    @Override
    public boolean play(Coordinates from, Coordinates to, char dir) {
        prevCoordinates = from;
        newCoordinates = to;
        direction = dir;

        boolean[] directions = {true, true};

        // Checks if the move is legal and the path is good. If yes, then perform the logic of the game.
        if (board.isLegal(prevCoordinates, newCoordinates, false) &&
                board.isPathGood(prevCoordinates, newCoordinates, directions)) {

            // Validates the direction.
            if (validateDirection(direction, directions) == false) {
                return false;
            }

            // Determines if the game ends after the move.
            if (board.getDiceAt(newCoordinates) != null && board.getDiceAt(newCoordinates).isPlayerKing()) {
                printMessage = "Ok, you are smarter";
                playerWon = true;
            }

            // Moves the die on the board.
            board.move(prevCoordinates, newCoordinates, direction);
            printMessage = "That's a sleek move";

            if (direction == 'f') {
                printMessage+= "\n It moved frontally first.";
            }
            else {
                printMessage += "\n It moved laterally first.";
            }

            return true;
        }

        printMessage = "You can't do that. That move is physically/virtually impossible";
        return false;
    }

    /**
     * Plays the game if the human asks for help.
     */
    @Override
    public void play() {
        // Resets the board according to the movements.
        refreshPlayers();
        boolean movePossible = false;

        // Checks if the player can win. If yes, suggest that move.
        if (canWin()) {
            printMessage = "The Best There Is, The Best There Was and The Best There Ever Will Be - Bret Hart";
            movePossible = true;
            playerWon = true;
        }
        else {
            // Check if the king is in threat. If it it, eat the threat. Or block the move.
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
        }

        // If none of the moves are possible, then, just make offensive move.
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

    /**
     * Resets the player dices by reading the board.
     */
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

    /**
     * Validates the direction. If the user has not chosen the direction, then tells the user to choose
     * the direction.
     * @param userChoice It holds the choice of the user.
     * @param directions It holds the directions that the user can move first.
     * @return  Returns true if the direction user chose is valid.
     */
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
