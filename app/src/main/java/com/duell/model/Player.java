/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

import java.util.ArrayList;

/**
 * This is an player class that holds the basic information and algorithm needed for the game.
 */

public class Player {
    // Protected variables needed for movement computation of the game.
    protected String printMessage;
    protected Board board;
    protected Coordinates prevCoordinates;
    protected Coordinates newCoordinates;
    protected boolean playerWon = false;
    protected char direction;
    protected boolean bothDirectionPossible;

    // These hold the opponent and current players dices.
    protected ArrayList<TreeNode> opponentPlayer;
    protected ArrayList<TreeNode> currentPlayer;

    /**
     * Default constructor.
     * @param b It holds the board info.
     */
    public Player(Board b) {
        board = b;
        prevCoordinates = null;
        newCoordinates = null;
        opponentPlayer = new ArrayList<>();
        currentPlayer = new ArrayList<>();
        direction = 'f';
        bothDirectionPossible = true;
    }

    /**
     * Play funciton.
     */
    public void play() {

    }

    /**
     * It holds the info to play.
     * @param from It is the coordinate to move from.
     * @param to It is the coordinate to move to.
     * @param dir It holds the direction to move first.
     * @return Returns true if movement is successful.
     */
    public boolean play(Coordinates from, Coordinates to, char dir) {


        return false;
    }

    /**
     * Returns the coordinate to move the dice from.
     * @return Returns the coordinate to move the dice from.
     */
    public Coordinates getPrevCoordinates() { return prevCoordinates;}

    /**
     * Returns the coordinate to move the dice to.
     * @return Returns the coordinate to move the dice to.
     */
    public Coordinates getNewCoordinates() { return newCoordinates;}

    /**
     * Returns if the player won.
     * @return Returns if the player won.
     */
    public boolean playerWins() {
        return playerWon;
    }

    /**
     * Returns the print message.
     * @return Returns the print message.
     */
    public String getPrintMessage() { return printMessage;}

    /**
     * It makes an offensive move.
     */
    protected void safeOffenceMove() {
        nullifySuggestions();

        // Goes through each and every coordinates of the board from opponent's side and looks for the position
        // where opponent cannot reach but current player can reach.
        for (int row = board.getTotalRows() - 1; row >= 0; row--) {
            for (int col = 0; col < board.getTotalColumns(); col++) {
                // Check if any opponent player nodes can reach this location.
                // If yes, then move on. If no, then check if current player can move their dice to this location.
                if (canReachLocation(opponentPlayer, row, col) == null) {
                    TreeNode temp = canReachLocation(currentPlayer, row, col);
                    if (temp != null ) {
                        prevCoordinates = temp.getCoordinates();
                        newCoordinates = new Coordinates(row, col);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Checks if the player dices can reach the designated location.
     * @param playerNodes It holds all the dices of a player.
     * @param row It holds the row of the board.
     * @param col It holds the column of the board.
     * @return Returns the dice that can reach the location specified.
     */
    protected TreeNode canReachLocation(ArrayList<TreeNode> playerNodes, int row, int col) {
        nullifySuggestions();

        boolean isComputer = playerNodes.get(0).getDice().isPlayerComputer();

        // Checks if the location can be reached by the given tree nodes.
        for (int index = 0; index < playerNodes.size(); index++) {
            TreeNode tempNode = playerNodes.get(index);
            boolean[] tempDirection = {true, true};
            if (board.isPathGood(tempNode.getCoordinates(), new Coordinates(row, col), tempDirection) && board.isLegal(tempNode.getCoordinates(),new Coordinates(row, col),isComputer)) {
                return tempNode;
            }
        }

        // Returns false if the location cannot be reached.
        return null;
    }

    /**
     * Checks if the opponent can be eaten.
     * @return Returns true if the opponent can be eaten.
     */
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

    /**
     * Checks if the threat to the king can be eaten.
     * @param threateningNode It is the dice that is threatening the king.
     * @return Returns if the threat can be eaten.
     */
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

    /**
     * Checks if the dice can be eaten.
     * @param current It holds all the dices of the current player.
     * @param diceToEat It holds the dice to eat.
     * @return Returns true if the dice can be eaten.
     */
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

    /**
     * Returns if both directions are possible.
     * @return Returns if both directions are possible.
     */
    public boolean isBothDirectionPossible() {
        return bothDirectionPossible;
    }

    /**
     * Returns if the king is in threat.
     * @return Returns the die that is threatening the king if any.
     */
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

    /**
     * Returns the king of current player.
     * @return Returns the king and its location of current player.
     */
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

    /**
     * Checks if blocking a move is possible.
     * @param threat It holds the TreeNode object of the threat.
     * @return Returns true if blocking is possible.
     */
    protected boolean blockMove(TreeNode threat) {
        // Nullifies the suggesting variables.
        nullifySuggestions();

        ArrayList<Coordinates> pathCoordinates;
        TreeNode currentKing = getCurrentPlayersKing();

        pathCoordinates = board.getPathCoordinates(threat.getCoordinates(), currentKing.getCoordinates());

        // Goes through each of the current player and checks if it can reach to any of the path's location
        // so that it can be a hindrance.
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

    /**
     * Returns the opponent's king and its position.
     * @return Returns the opponent's king and its position.
     */
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

    /**
     * Checks if current player can win the game.
     * @return Returns true if the game can be won.
     */
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

    /**
     * Nullifies the suggestions.
     */
    protected void nullifySuggestions() {
        prevCoordinates = null;
        newCoordinates = null;
    }

    /**
     * Resets the player dices after each board modification.
     */
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
