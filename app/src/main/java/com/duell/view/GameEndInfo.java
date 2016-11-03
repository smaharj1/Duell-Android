package com.duell.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.duell.R;

import org.w3c.dom.Text;

import java.util.Random;

public class GameEndInfo extends AppCompatActivity {

    int humanScore=0;
    int computerScore=0;
    private Random random = new Random();
    boolean isComputerTurn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end_info);

        if (getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE) != null) {
            computerScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE));
        }
        if (getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE) != null) {
            humanScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE));
        }

        TextView temp = (TextView) findViewById(R.id.computerScore);
        temp.setText(computerScore+"");

        temp = (TextView) findViewById(R.id.winner);
        if (humanScore > computerScore) {
            temp.setText("Congratulations! You won!");
        }
        else if (humanScore < computerScore) {
            temp.setText("Better luck next time");
        }
        else {
            temp.setText("It's a draw.");
        }


        temp = (TextView) findViewById(R.id.humanScore);
        temp.setText(humanScore+"");
    }

    public void playAgain(View view) {

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
                                        intent.putExtra(AppLauncher.MESSAGE_COMPUTERSCORE, computerScore+"");
                                        intent.putExtra(AppLauncher.MESSAGE_HUMANSCORE,humanScore+"");
                                        if (isComputerTurn) {
                                            intent.putExtra(AppLauncher.MESSAGE_TURN, "Computer");
                                        }
                                        else {
                                            intent.putExtra(AppLauncher.MESSAGE_TURN, "human");
                                        }
                                        intent.putExtra(AppLauncher.MESSAGE_GAME, "new");
                                        startActivity(intent);
                                    }
                                });

                        newDialog.show();

                    }
                });

        alertDialog.show();

    }

    private int rollDie() {

        return random.nextInt(6)+ 1;
    }

    public void exitGame(View view) {
        System.exit(0);
    }
}
