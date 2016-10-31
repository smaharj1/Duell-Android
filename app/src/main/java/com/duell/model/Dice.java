package com.duell.model;

import android.util.Log;

/**
 * Created by Sujil on 10/30/2016.
 */

public class Dice {
    private boolean isComputer;
    private boolean isKing;
    private int top;
    private int right;
    private int front;
    private boolean isKilled;

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

    public void moveLeft() {
        if (!isKing) {
            int tmp = top;
            top = right;
            right = 7 - tmp;
        }
    }

    public void moveRight() {
        if (!isKing) {
            int tmp = top;
            top = 7 - right;
            right = tmp;
        }
    }

    public void moveForward() {
        if (!isKing) {
            int tmp = top;
            top = front;
            front = 7 - tmp;
        }
    }

    public void moveBackward() {
        if (!isKing) {
            int tmp = top;
            top = 7 - front;
            front = tmp;
        }
    }

    public String getValue() {
        if (isComputer) {
            return "C" + getTop() + getRight();
        }

        return "H" + getTop() + getRight();
    }

    public int getTop() { return top;}

    public int getRight() { return right;}

    public int getFront() { return front;}

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

    public boolean isPlayerKing() {
        return isKing;
    }

    public boolean isPlayerComputer() {
        return isComputer;
    }

    public boolean setKing() {
        top = 1;
        right = 1;
        front = 1;
        isKing = true;

        return true;
    }

    public boolean setKilled() {
        isKilled = true;

        return true;
    }
}
