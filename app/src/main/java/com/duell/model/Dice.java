/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

/**
 * This class holds the die and all the computations needed for the die.
 */

public class Dice {
    // Declares the variables for die.
    private boolean isComputer;
    private boolean isKing;
    private int top;
    private int right;
    private int front;
    private boolean isKilled;

    /**
     * Default constructor for the die.
     * @param given It holds the string representation fo the die.
     */
    public Dice(String given) {
        // Find the top and right from the given string.
        top = given.charAt(1)-'0';
        right= given.charAt(2) -'0';

        // Checks if top and right are same. If yes, it means its a king.
        if (top == right) {
            setKing();
        }
        else {
            // Computes the front.
            front = computeFrontFace(top, right);
            isKing = false;
        }

        // Sets if it is a computer.
        isComputer = given.charAt(0) == 'C' ? true : false;

        isKilled = false;
    }

    /**
     * Constructor with more information from parameter.
     * @param isComputer It holds if it is computer's die.
     * @param t It holds the top face of the die.
     * @param f It holds the front face of the die.
     * @param r It holds the right face of the die.
     */
    public Dice (boolean isComputer, int t, int f, int r) {
        this.isComputer = isComputer;

        // Checks if the die is king.
        if (t == f) {
            setKing();
        }
        else {
            front = f;
            top = t;
            right = r;
            isKing = false;
        }

        isKilled = false;
    }

    /**
     * Rolls the die left.
     */
    public void moveLeft() {
        if (!isKing) {
            int tmp = top;
            top = right;
            right = 7 - tmp;
        }
    }

    /**
     * Rolls the die right.
     */
    public void moveRight() {
        if (!isKing) {
            int tmp = top;
            top = 7 - right;
            right = tmp;
        }
    }

    /**
     * Rolls the die forward.
     */
    public void moveForward() {
        if (!isKing) {
            int tmp = top;
            top = front;
            front = 7 - tmp;
        }
    }

    /**
     * Rolls the die backward.
     */
    public void moveBackward() {
        if (!isKing) {
            int tmp = top;
            top = 7 - front;
            front = tmp;
        }
    }

    /**
     * Returns the value of the die.
     * @return Returns the value of the die.
     */
    public String getValue() {
        if (isComputer) {
            return "C" + getTop() + getRight();
        }

        return "H" + getTop() + getRight();
    }

    /**
     * Gets the top face.
     * @return Returns the top face.
     */
    public int getTop() { return top;}

    /**
     * Gets the right face.
     * @return Gets the right face.
     */
    public int getRight() { return right;}

    /**
     * Computes the front face if the top and right face are given.
     * @param top It holds the top face.
     * @param right It holds the right face.
     * @return Returns the front face calculated.
     */
    public static int computeFrontFace(int top, int right) {
        // It holds the total possible rolls.
        int[][] roles = { { 3,1,4,6,3,1,4,6 },{ 1,2,6,5,1,2,6,5 },{ 2,3,5,4,2,3,5,4 } };

        // It holds the remain and front face values.
        int remain = 0;
        int front = 0;

        // Checks if top is 1 and right is 2 or its equivalent opposite. Then, it finds the remaining.
        if (top == 1 || 7 - top == 1) {
            if (right == 2 || 7 - right == 2) {
                remain = 3;
            }
            else remain = 2;
        }
        // Checks if top is 2 and right is 1 or its equivalent opposite. Then, it finds the remaining.
        else if (top == 2 || 7 - top == 2) {
            if (right == 1 || 7 - right == 1) {
                remain = 3;
            }
            else remain = 1;
        }
        // Checks if top is 3 and right is 1 or its equivalent opposite. Then, it finds the remaining.
        else {
            // here top = 3
            if (right == 1 || 7 - right == 1) {
                remain = 2;
            }
            else remain = 1;
        }


        // Compute front from remain found.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (roles[i][j] == top) {
                    if (j + 1 < 8 && roles[i][j + 1] == right) {
                        front = 7 - remain;
                        break;
                    }
                    else if (j - 1 >= 0 && roles[i][j - 1] == right) {
                        front = remain;
                        break;
                    }
                }
            }
        }

        // Returns the front face.
        return front;
    }

    /**
     * Returns if the player is king.
     * @return Returns true if the player is king.
     */
    public boolean isPlayerKing() {
        return isKing;
    }

    /**
     * Returns if the player is computer.
     * @return Returns if the player is computer.
     */
    public boolean isPlayerComputer() {
        return isComputer;
    }

    /**
     * Sets the dice as king.
     * @return Returns true if successfully set.
     */
    public boolean setKing() {
        top = 1;
        right = 1;
        front = 1;
        isKing = true;

        return true;
    }

}
