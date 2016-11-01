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
 * Created by Sujil on 10/31/2016.
 */

public class FileHandler {

    private Board loadedBoard;
    private boolean isComputerTurn;
    private int computerScore;
    private int humanScore;
    private Context context;

    public FileHandler(Context appContext) {
        loadedBoard = new Board();
        isComputerTurn = true;
        computerScore = -1;
        humanScore = -1;
        context = appContext;
    }

    public Board getBoard() {
        return loadedBoard;
    }

    public boolean getIfComputerTurn() {
        return isComputerTurn;
    }

    public int getComputerScore() { return computerScore;}
    public int getHumanScore() { return humanScore;}

    public boolean saveGame(String filename, Board givenBoard, boolean isComputer, int botScore, int hScore) throws FileNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory(), filename+".txt");

        if (file.exists()) file.delete();

        FileOutputStream outStream = new FileOutputStream(file);

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

    public boolean openGame(String filename) {
        String result = "";
        result = readFromFile(filename);

        String[] lines = result.split("\n");
        String[][] board = new String[8][9];

        int index = 0;
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
