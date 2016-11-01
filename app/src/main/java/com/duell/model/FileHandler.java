package com.duell.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public boolean openGame(String filename) {
        //File ifile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        // Log.v("LCOATION::",Environment.getExternalStorageDirectory().getAbsolutePath());

        AssetManager am = context.getAssets();

        //InputStream is=null;
        InputStream is = context.getResources().openRawResource(R.raw.one);
//        try {
//             is = am.open(filename+".txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//
//        }

        InputStreamReader inputStreamReader = new InputStreamReader(is);


        String line;
        String[][] board = new String[8][9];
        try {
            BufferedReader reader = new BufferedReader(inputStreamReader);

            int index = 0;
            while ((line = reader.readLine()) != null) {
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
                    computerScore = Integer.parseInt(temp[1]);
                } else if (line.contains("Human Wins")) {
                    String[] temp = line.split(":\\s");
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

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(board[i][j]+" ");
                }
                System.out.println();
            }

            // Sets the board into a new board.
            loadedBoard.setBoard(board);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }




}
