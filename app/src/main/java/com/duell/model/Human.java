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
        System.out.println("TO: " + to.getRow());

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
