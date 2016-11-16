/************************************************************
 * Name:  Sujil Maharjan                                    *
 * Project : Project 2, Duell game                          *
 * Class : Organization of Programming Language(CMPS 366-01)*
 * Date : 11-15-2016                                         *
 ************************************************************/
package com.duell.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.duell.R;
import com.duell.view.AppLauncher;

/**
 * This class is the end controller. It occurs at the end of the whole game.
 */
public class TournamentEndInfo extends AppCompatActivity {
    private int humanScore = 0;
    private int computerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_end_info);

        // Gets the required information from the user.
        if (getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE) != null) {
            computerScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE));
        }
        if (getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE) != null) {
            humanScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE));
        }

        TextView temp = (TextView) findViewById(R.id.computerScore);
        temp.setText(computerScore+"");

        temp = (TextView) findViewById(R.id.winner);

        String saveGame = getIntent().getStringExtra(AppLauncher.MESSAGE_SAVE);
        if (saveGame.equalsIgnoreCase("yes")) {

        }
        else {
            if (humanScore > computerScore) {
                temp.setText("Congratulations! You won the tournament!");
            } else if (humanScore < computerScore) {
                temp.setText("Better luck next time. You lost the tournament");
            } else {
                temp.setText("The tournament is a draw!");
            }
        }


        temp = (TextView) findViewById(R.id.humanScore);
        temp.setText(humanScore+"");
    }

    /**
     * Exits the application.
     * @param view
     */
    public void exitGame(View view) {
        finish();
        System.exit(0);
    }
}
