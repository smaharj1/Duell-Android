package com.duell.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Computer extends Player {



    public Computer(Board board) {
        super(board);

    }

    @Override
    public void play() {
        refreshPlayers();
        boolean movePossible = false;

        if (canWin()) {
            printMessage = "The Best There Is, The Best There Was and The Best There Ever Will Be - Bret Hart \n";
            printMessage += "Moved " + board.getDiceAt(prevCoordinates.getRow(), prevCoordinates.getCol()).getValue() + " to the location highlighted to win the game.";
            movePossible = true;
            playerWon = true;
        }
        else {
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

        board.move(prevCoordinates, newCoordinates,direction);
        if (direction == 'f'){
            printMessage += "\n It moved frontally first.";
        }
        else {
            printMessage += "\n It moved laterally first.";
        }

    }


}
