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
            printMessage = "The Best There Is, The Best There Was and The Best There Ever Will Be - Bret Hart";
            movePossible = true;
            playerWon = true;
        }
        else {
            TreeNode threat = isKingInThreat();
            if (threat != null) {
                if (canEatThreat(threat)){
                    printMessage = "You Cant See Me - John Cena";
                    movePossible = true;
                }
                else {
                    // Blocks the move if possible.
                }
            }
            else {
                // Executes if there is not threat to the king.
                if (canEatOpponent()) {
                    printMessage = "YOU WANT SOME? COME GET SOME! - John Cena";

                    movePossible = true;
                }
            }
        }

        if (!movePossible) {
            // Just make a general move that gets you closer to the king.
            safeOffenceMove();
            printMessage = "Just playing offense";
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

    }


}
