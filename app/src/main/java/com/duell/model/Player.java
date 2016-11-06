package com.duell.model;

import java.util.ArrayList;

/**
 * Created by Sujil on 10/31/2016.
 */

public class Player {
    protected String printMessage;
    protected Board board;
    protected Coordinates prevCoordinates;
    protected Coordinates newCoordinates;
    protected boolean playerWon = false;
    protected char direction;

    protected ArrayList<TreeNode> opponentPlayer;
    protected ArrayList<TreeNode> currentPlayer;

    public Player(Board b) {
        board = b;
        prevCoordinates = null;
        newCoordinates = null;
        opponentPlayer = new ArrayList<>();
        currentPlayer = new ArrayList<>();
        direction = 'f';
    }

    public void play() {

    }

    public boolean play(Coordinates from, Coordinates to, char dir) {


        return false;
    }

    public char getMovementDirection() { return direction;}
    public Coordinates getPrevCoordinates() { return prevCoordinates;}
    public Coordinates getNewCoordinates() { return newCoordinates;}

    public boolean playerWins() {
        return playerWon;
    }

    public String getPrintMessage() { return printMessage;}



    protected void safeOffenceMove() {
        nullifySuggestions();

        for (int row = board.getTotalRows() - 1; row >= 0; row--) {
            for (int col = 0; col < board.getTotalColumns(); col++) {
                // Check if any opponent player nodes can reach this location.
                // If yes, then move on. If no, then check if current player can move their dice to this location.
                if (!canReachLocation(opponentPlayer, row, col)) {
                    if (canReachLocation(currentPlayer, row, col)) {
                        // At this point, the suggested move and location will already have been set.
                        return;
                    }
                }
            }
        }
    }

    protected boolean canReachLocation(ArrayList<TreeNode> playerNodes, int row, int col) {
        nullifySuggestions();

        // Checks if the location can be reached by the given tree nodes.
        for (int index = 0; index < playerNodes.size(); index++) {
            TreeNode tempNode = playerNodes.get(index);

            boolean[] tempDirection = {true, true};
            if (board.isPathGood(tempNode.getCoordinates(), new Coordinates(row, col), tempDirection)) {
                prevCoordinates = tempNode.getCoordinates();
                newCoordinates = new Coordinates(row, col);
                return true;
            }
        }

        // Returns false if the location cannot be reached.
        return false;
    }

    protected boolean canEatOpponent() {
        nullifySuggestions();

        // Goes to each opponent and check if any current player dices can eat the opponent.
        // Return true if it can eat.
        for (int oppIndex = 0; oppIndex < opponentPlayer.size(); oppIndex++) {
            TreeNode tmpNode = opponentPlayer.get(oppIndex);

            if (canEat(currentPlayer, tmpNode)) {
                return true;
            }
        }

        return false;
    }
    protected boolean canEatThreat(TreeNode threateningNode) {
        // Checks if the there is a threat just to make sure.
        if (threateningNode == null) {
            return false;
        }

        // Resets the suggestions.
        nullifySuggestions();

        // At this point, threatening node is not null.
        // Loop through each dice of players and see if you can eat threatening node.
        if (canEat(currentPlayer, threateningNode)) {
            return true;
        }

        // Returns false if the threat cannot be eaten.
        return false;
    }

    protected boolean canEat(ArrayList<TreeNode> current, TreeNode diceToEat) {
        // Loops through all the current node dices and checks if it can eat the dice mentioned.
        for (int index = 0; index < current.size(); index++) {
            TreeNode tempNode = current.get(index);

            boolean[] tempDirection = {true, true};
            if (board.isPathGood(tempNode.getCoordinates(), diceToEat.getCoordinates(), tempDirection)) {
                //System.out.println("Path is good for " + tempNode.getDice().getValue() + " to eat " + diceToEat.getDice().getValue());
                prevCoordinates = tempNode.getCoordinates();
                newCoordinates = diceToEat.getCoordinates();
                return true;
            }
        }

        // Returns false if it cannot eat the dice.
        return false;
    }

    protected TreeNode isKingInThreat() {
        // Holds the node of king's dice.
        TreeNode playerKing = getCurrentPlayersKing();

        // If it is null, return false.
        if (playerKing == null) {
            return null;
        }

        // Check if each of opponent players can legally move to king's location
        for (int i = 0; i < opponentPlayer.size(); i++) {
            TreeNode tempNode = opponentPlayer.get(i);
            boolean[] tempDirection = {true, true};

            if (board.isPathGood(tempNode.getCoordinates(), playerKing.getCoordinates(), tempDirection)) {
                return opponentPlayer.get(i);
            }
        }

        return null;
    }

    protected TreeNode getCurrentPlayersKing() {
        // Loops through the vector of dices of current player and returns the king node.
        for (int i = 0; i < currentPlayer.size(); i++) {
            if (currentPlayer.get(i).getDice() != null && currentPlayer.get(i).getDice().isPlayerKing()) {
                return currentPlayer.get(i);
            }
        }

        // If there is no king, it returns NULL.
        return null;
    }

    protected boolean blockMove(TreeNode threat) {
        nullifySuggestions();

        ArrayList<Coordinates> pathCoordinates = new ArrayList<>();
        TreeNode currentKing = getCurrentPlayersKing();

        pathCoordinates = board.getPathCoordinates(threat.getCoordinates(), currentKing.getCoordinates());



        for (int i=0; i < currentPlayer.size(); i++) {
            TreeNode currentNode = currentPlayer.get(i);
            if (currentNode.getDice().isPlayerKing()) continue;
            for (int j = 0; j < pathCoordinates.size(); j++) {
                boolean[] dir = {true, true};
                if (board.isPathGood(currentNode.getCoordinates(), pathCoordinates.get(j), dir)) {
                    prevCoordinates = currentNode.getCoordinates();
                    newCoordinates = pathCoordinates.get(j);
                    return true;
                }
            }
        }

        return false;
    }

    protected TreeNode getOpponentsKing() {
        // Loops throught the vector of dices of opponent player and returns the king node.
        for (int i = 0; i < opponentPlayer.size(); i++) {
            if (opponentPlayer.get(i) != null) {
                if (opponentPlayer.get(i).getDice().isPlayerKing())
                    return opponentPlayer.get(i);
            }
        }

        // Returns null if king is not found.
        return null;
    }

    protected boolean canWin() {
        // Holds the values of opponent's king location and winning location case.
        TreeNode opponentKing = getOpponentsKing();
        Coordinates winLocation = new Coordinates(7, 4);

        // Resets the suggestions before computation.
        nullifySuggestions();

        // Loops through current players dices and checks if any dice can make the player win the game.
        for (int index = 0; index < currentPlayer.size(); index++) {
            TreeNode tempNode = currentPlayer.get(index);

            // Checks if either winning location can be reached or king can be eaten.
            boolean[] tempDirections = {true, true};

            if (board.isPathGood(tempNode.getCoordinates(),opponentKing.getCoordinates(),tempDirections)) {
                prevCoordinates = tempNode.getCoordinates();
                newCoordinates = opponentKing.getCoordinates();
                return true;
            }
            else if (board.isPathGood(tempNode.getCoordinates(), winLocation, tempDirections)) {
                prevCoordinates = tempNode.getCoordinates();
                newCoordinates = winLocation;
                return true;
            }
        }

        // Returns false if this is not the winning case.
        return false;
    }

    protected void nullifySuggestions() {
        prevCoordinates = null;
        newCoordinates = null;
    }

    protected void refreshPlayers() {
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
                    if (!d.isPlayerComputer()) {
                        opponentPlayer.add(new TreeNode(d, row, col));
                    }
                    else {
                        currentPlayer.add(new TreeNode(d, row, col));
                    }
                }
            }
        }
    }


}
