/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class makes the movement for computer. It plays the move and algo performs the algorithmic movement.
 */

public class Computer extends Player {

    /**
     * Default constructor that just calls the super class's constructor.
     * @param board It holds the Board object.
     */
    public Computer(Board board) {
        super(board);
    }

    /**
     * Plays the computer's move and records the old and new coordinates.
     */
    @Override
    public void play() {
        // Reset the players because there are changes in the board every time.
        refreshPlayers();
        boolean movePossible = false;

        // Checks if the computer can win. If yes, then win.
        if (canWin()) {
            printMessage = "The Best There Is, The Best There Was and The Best There Ever Will Be - Bret Hart \n";
            printMessage += "Moved " + board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location highlighted to win the game.";
            movePossible = true;
            playerWon = true;
        }
        else {
            // Checks if the king is in threat. If yes, try to eat the threat. If no, block the threat's
            // path.
            TreeNode threat = isKingInThreat();
            if (threat != null) {
                if (canEatThreat(threat)){
                    printMessage = "You Cant See Me - John Cena\n";
                    printMessage += "Moved "+ board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location to indicated to eat the threat.";
                    movePossible = true;
                }
                else {
                    // Blocks the move if possible.
                    if (blockMove(threat)) {
                        printMessage = "Threat has been blocked\n";
                        printMessage += "Moved "+ board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location to indicated to block from eating the king.";
                        movePossible = true;
                    }
                }
            }
            else {
                // Executes if there is not threat to the king.
                if (canEatOpponent()) {
                    printMessage = "YOU WANT SOME? COME GET SOME! - John Cena\n ";
                    printMessage += "Moved "+ board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location to eat the opponent's player.";
                    movePossible = true;
                }
            }
        }

        // If any attacking moves are not possible, just make an offensive move.
        if (!movePossible) {
            // Just make a general move that gets you closer to the king.
            safeOffenceMove();
            printMessage += "Moved "+ board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location to play offense.";
        }

        // Temporarily creates an array of directions to call the main board function.
        boolean[] tempDir = {true, true};
        char direction = 'f';

        // Checks if the path is good.
        board.isPathGood(prevCoordinates, newCoordinates, tempDir);

        if (tempDir[0] == false) {
            direction = 'l';
        }

        // Moves the die on the board.
        board.move(prevCoordinates, newCoordinates,direction);
        if (direction == 'f'){
            printMessage += "\n It moved frontally first.";
        }
        else {
            printMessage += "\n It moved laterally first.";
        }

    }


}
