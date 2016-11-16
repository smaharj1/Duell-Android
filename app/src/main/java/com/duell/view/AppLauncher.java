/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.duell.R;

import java.util.Random;

public class AppLauncher extends AppCompatActivity {
    // Constants that hold information from one activity to another.
    public static final String MESSAGE_TURN = "turn";
    public static final String MESSAGE_GAME = "game";
    public static final String MESSAGE_FILENAME = "filename";
    public static final String MESSAGE_HUMANSCORE = "humanScore";
    public static final String MESSAGE_COMPUTERSCORE = "computerScore";
    public static final String MESSAGE_WINNER = "winner";
    public static final String MESSAGE_SAVE = "save";
    private Random random = new Random();
    private boolean isComputerTurn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_launcher);

    }

    /**
     * Rolls a die.
     * @return
     */
    private int rollDie() {

        return random.nextInt(6)+ 1;
    }

    /**
     * Plays the rolling die game to determine the first player.
     * @param view It holds the view
     */
    public void playGame(View view) {
        // Creates an alert dialog where the game is played once the user selects "ROLL".
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Rolling the dice!");
        alertDialog.setMessage("Please click roll to roll the die to choose first player");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Roll",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Creates another dialog for the result
                        AlertDialog newDialog = new AlertDialog.Builder(alertDialog.getContext()).create();

                        // Plays the die rolling game and determines who goes first.
                        int humanDie = 0; int computerDie = 0;
                        StringBuilder result = new StringBuilder();

                        // Keep rolling the dies until its draw.
                        while (humanDie == computerDie) {
                            humanDie = rollDie();

                            computerDie = rollDie();

                            result.append("You: " + humanDie + " & Bot: " + computerDie);

                            if (humanDie == computerDie) {
                                result.append("Draw! \n Rolling again.. \n");
                            }
                        }


                        // Assign the turn according to the die roll result.
                        if (computerDie > humanDie) {
                            result.append("\nComputer won! She goes first!");
                            isComputerTurn = true;
                        }
                        else {
                            result.append("\nYou won! You go first!");
                            isComputerTurn = false;
                        }

                        newDialog.setMessage(result.toString());
                        newDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialogInterface, int whi) {
                                        Intent intent = new Intent(getApplicationContext(), GamePlay.class);
                                        intent.putExtra(MESSAGE_COMPUTERSCORE, "0");
                                        intent.putExtra(MESSAGE_HUMANSCORE,"0");
                                        if (isComputerTurn) {
                                            intent.putExtra(MESSAGE_TURN, "Computer");
                                        }
                                        else {
                                            intent.putExtra(MESSAGE_TURN, "human");
                                        }
                                        intent.putExtra(MESSAGE_GAME, "new");
                                        startActivity(intent);
                                    }
                                });

                        newDialog.show();

                    }
                });

        alertDialog.show();
    }

    /**
     * Loads if load game button is pressed.
     * @param view
     */
    public void loadGame(View view) {
        // Creates the dialog box for allowing user to select which file to open. User does not have to input the extension
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.check, null));
        builder.setMessage("Please enter the required info");
        builder.setNeutralButton("NEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;

                EditText file = (EditText) d.findViewById(R.id.filename);
                String filename = file.getText().toString();

                Intent intent = new Intent(getApplicationContext(), GamePlay.class);

                // Puts the extra message in the intent for another activity to computer likewise
                intent.putExtra(MESSAGE_GAME, "load");

                intent.putExtra(MESSAGE_FILENAME, filename);

                startActivity(intent);
            }
        });


        builder.create().show();
    }
}
