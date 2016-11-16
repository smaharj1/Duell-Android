/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Environment;

import com.duell.R;

/**
 * This class performs all the file related performances. It saves and loads the game.
 */

public class FileHandler {
    // Private variables that holds the important information from the files.
    private Board loadedBoard;
    private boolean isComputerTurn;
    private int computerScore;
    private int humanScore;
    private Context context;

    /**
     * Default constructor.
     * @param appContext It holds the application context.
     */
    public FileHandler(Context appContext) {
        loadedBoard = new Board();
        isComputerTurn = true;
        computerScore = -1;
        humanScore = -1;
        context = appContext;
    }

    /**
     * It returns the Board object.
     * @return Returns the board object.
     */
    public Board getBoard() {
        return loadedBoard;
    }

    /**
     * Returns if it is computer's turn.
     * @return Returns if it is computer's turn.
     */
    public boolean getIfComputerTurn() {
        return isComputerTurn;
    }

    /**
     * Returns the computer's score.
     * @return Returns the computer's score.
     */
    public int getComputerScore() { return computerScore;}

    /**
     * Returns the human score.
     * @return Returns the human score.
     */
    public int getHumanScore() { return humanScore;}

    /**
     * Saves the game to the desired name in the SD card.
     * @param filename It holds the file name without the extension.
     * @param givenBoard It holds the board.
     * @param isComputer It holds if it is computer's turn.
     * @param botScore It holds computer score.
     * @param hScore It holds human score.
     * @return returns true if successfully saved.
     * @throws FileNotFoundException
     */
    public boolean saveGame(String filename, Board givenBoard, boolean isComputer, int botScore, int hScore) throws FileNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory(), filename+".txt");

        if (file.exists()) file.delete();

        FileOutputStream outStream = new FileOutputStream(file);

        // Write the information to the file.
        try {
            outStream.write("Board:\n".getBytes());
            for (int i =0; i < givenBoard.getTotalRows(); i++) {
                for (int j = 0; j < givenBoard.getTotalColumns(); j++) {
                    if (givenBoard.getDiceAt(i,j) == null) {
                        outStream.write("0  ".getBytes());
                    }
                    else {
                        outStream.write(givenBoard.getDiceAt(i, j).getValue().getBytes());
                        outStream.write("  ".getBytes());
                    }
                }
                outStream.write("\n".getBytes());
            }
            outStream.write("\n".getBytes());

            outStream.write("Computer Wins: ".getBytes());
            outStream.write(Integer.toString(botScore).getBytes());
            outStream.write("\n".getBytes());
            outStream.write("\n".getBytes());


            outStream.write("Human Wins: ".getBytes());
            outStream.write(Integer.toString(hScore).getBytes());
            outStream.write("\n".getBytes());
            outStream.write("\n".getBytes());

            outStream.write("Next Player: ".getBytes());
            if (isComputer) {
                outStream.write("Computer\n".getBytes());
            }
            else {
                outStream.write("Human\n".getBytes());
            }
            outStream.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Opens the game from the file name given.
     * @param filename It holds the naem of the file.
     * @return Returns true if the file is opened.
     */
    public boolean openGame(String filename) {
        String result = "";
        result = readFromFile(filename);

        String[] lines = result.split("\n");
        String[][] board = new String[8][9];

        int index = 0;
        // Iterates through every line and parses it to correct information.
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (index == 0) {
                index++;
                continue;
            }
            if (index <= 8 && index > 0) {
                String[] dices = line.split("\\s+");

                int tempIndex = 0;
                int coreIndex = 0;
                while (coreIndex<9) {

                    if (!dices[tempIndex].isEmpty()) {
                        board[index-1][coreIndex] = dices[tempIndex];
                        coreIndex++;
                    }

                    tempIndex++;
                }

                index++;
                continue;
            }

            if (line.contains("Computer Wins")) {
                String[] temp = line.split(":\\s");
                temp[1] = temp[1].replaceAll("\\D","");
                computerScore = Integer.parseInt(temp[1]);
            } else if (line.contains("Human Wins")) {
                String[] temp = line.split(":\\s");
                temp[1] = temp[1].replaceAll("\\D","");
                humanScore = Integer.parseInt(temp[1]);
            } else if (line.contains("Next")) {
                String[] temp = line.split(":\\s");
                if (temp[1].equalsIgnoreCase("human")) {
                    isComputerTurn = false;
                } else {
                    isComputerTurn = true;
                }
            }
        }

        // Sets the board into a new board.
        loadedBoard.setBoard(board);

        return true;
    }

    /**
     * It reads everything from the file into a string
     * @return Returns the string of the content of the file
     * @throws IOException
     */
    private String readFromFile( String filename)  {
        File file = new File(Environment.getExternalStorageDirectory(), filename+".txt");

        int fileLength = (int) file.length();

        byte[] resultInBytes = new byte[fileLength];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            in.read(resultInBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new String(resultInBytes);

    }


}
