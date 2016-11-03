package com.duell.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.duell.R;
import com.duell.view.AppLauncher;

public class TournamentEndInfo extends AppCompatActivity {
    private int humanScore = 0;
    private int computerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_end_info);

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
            temp.setText("Congratulations! You won the tournament!");
        }
        else if (humanScore < computerScore) {
            temp.setText("Better luck next time. You lost the tournament");
        }
        else {
            temp.setText("It's a draw!");
        }


        temp = (TextView) findViewById(R.id.humanScore);
        temp.setText(humanScore+"");
    }

    public void exitGame(View view) {
        System.exit(0);
    }
}
