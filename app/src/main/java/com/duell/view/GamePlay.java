package com.duell.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.duell.R;
import com.duell.model.Board;
import com.duell.model.Computer;
import com.duell.model.Coordinates;
import com.duell.model.Dice;
import com.duell.model.FileHandler;
import com.duell.model.Human;
import com.duell.model.Player;

import java.io.FileNotFoundException;

public class GamePlay extends AppCompatActivity {
    private final int DEFAULT_COLOR = Color.parseColor("#FCEBB6");
    private final int SELECTED_COLOR = Color.parseColor("#D3D3D3");
    private final int MOVE_INDICATOR_COLOR = Color.parseColor("#f0a830");
    private final int INVALID_SELECTION = -1;
    private final char DIRECTION_NOT_CHOSEN = 'a';
    private final String LATERAL_FIRST = "lateral first";
    private final String COMPUTER_TURN = "Computer";
    private final String HUMAN_TURN = "You";


    private final String filename = "savedGame";

    private Board board;

    private boolean selectionMode = true;
    private Coordinates inputCoordinates;
    private Coordinates desiredCoordinates;
    private boolean computerTurn = false;

    private TextView message;
    private View prevView;
    private View newView;

    private Player human;
    private Player computer;


    // Tracks the total score of human and computer
    private int humanScore = 0;
    private int computerScore = 0;

    private Animation anim;

    FileHandler fileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        // Handles the file name given and reads the board
        fileHandler = new FileHandler(getApplicationContext());

        String gameType = getIntent().getStringExtra(AppLauncher.MESSAGE_GAME);

        if (gameType.equalsIgnoreCase("new")) {
            // Set up the board.
            int[] initialKeys = { 5, 1, 2, 6, 1, 6, 2, 1, 5 };
            board = new Board(initialKeys);
            humanScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE));
            computerScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE));

            String temp = getIntent().getStringExtra(AppLauncher.MESSAGE_TURN);

            // Set the turn according to what we got from the coin toss
            if (temp.equals("human")) {
                computerTurn = false;
            }
            else computerTurn = true;

            updateScore(humanScore, computerScore, computerTurn);
        }
        else {
            fileHandler.openGame(getIntent().getStringExtra(AppLauncher.MESSAGE_FILENAME));
            board = fileHandler.getBoard();
            humanScore = fileHandler.getHumanScore();
            computerScore = fileHandler.getComputerScore();
            computerTurn = fileHandler.getIfComputerTurn();

            updateScore(humanScore, computerScore, computerTurn);
        }

        message = (TextView) findViewById(R.id.message);

        // Make the table based on the values on the board.
        makeTable(board);

        computer = new Computer(board);
        human = new Human(board);

        initializeAnimation();
    }

    public void saveGame(View view) {

        try {
            fileHandler.saveGame(filename,board,computerTurn,computerScore,humanScore);

            endTournament();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void endGame(boolean computerWon) {
        Intent intent = new Intent(getApplicationContext(), GameEndInfo.class);
        intent.putExtra(AppLauncher.MESSAGE_HUMANSCORE, String.valueOf(humanScore));
        intent.putExtra(AppLauncher.MESSAGE_COMPUTERSCORE, String.valueOf(computerScore));

        String winner = computerWon ? "computer" : "human";
        intent.putExtra(AppLauncher.MESSAGE_WINNER, winner);

        startActivity(intent);
    }

    private void endTournament() {
        Intent intent = new Intent(getApplicationContext(), TournamentEndInfo.class);
        intent.putExtra(AppLauncher.MESSAGE_COMPUTERSCORE, computerScore+"");
        intent.putExtra(AppLauncher.MESSAGE_HUMANSCORE,humanScore+"");

        startActivity(intent);
    }

    public void getHelp(View view) {
        if (computerTurn) {
            printMessage("It is not your turn.");
            return;
        }

        resetSelectionView();

        human.play();

        Coordinates movingCoord = human.getPrevCoordinates();
        Coordinates newCoord = human.getNewCoordinates();

        RadioGroup rg = (RadioGroup) findViewById(R.id.dicectionChoice);
        if (human.isBothDirectionPossible()) {
            for(int i = 0; i < rg.getChildCount(); i++){
                ((RadioButton)rg.getChildAt(i)).setEnabled(true);
            }
        }
        else {
            for(int i = 0; i < rg.getChildCount(); i++){
                ((RadioButton)rg.getChildAt(i)).setEnabled(false);
            }
        }

        updateHelpView(movingCoord, newCoord);

        printMessage(human.getPrintMessage());

        //computerTurn = !computerTurn;
        //updateTurnView(computerTurn);
    }

    public void updateScore(int hScore, int cScore, boolean isComputer) {
        TextView tempView = (TextView) findViewById(R.id.humanScore);
        tempView.setText(hScore+"");

        tempView = (TextView) findViewById(R.id.computerScore);
        tempView.setText(cScore+"");

        tempView = (TextView) findViewById(R.id.turn);
        tempView.setText(isComputer ? "Computer" : "You");
    }

    public void moveComputer(View view) {
        if (!computerTurn) {
            printMessage("It is your turn.");
            return;
        }


        resetSelectionView();

        computer.play();

        Coordinates movingCoord = computer.getPrevCoordinates();
        Coordinates newCoord = computer.getNewCoordinates();

        updateView(movingCoord, newCoord);

        computerTurn = !computerTurn;
        updateTurnView(computerTurn);

        printMessage(computer.getPrintMessage());

        if (computer.playerWins()) {
            computerScore++;
            endGame(true);
        }

    }

    public void printMessage(String message) {
        TextView mView = (TextView) findViewById(R.id.message);
        mView.setText(message);
    }

    public View.OnClickListener computeAction = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Coordinates clickedPosition = (Coordinates) v.getTag();
            //Log.v("OLA: " , "Clicked pos: " + clickedPosition.getRow() + " " + clickedPosition.getCol());
            Dice diceClicked = board.getDiceAt(clickedPosition);
            RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);

            for(int i = 0; i < directionClicked.getChildCount(); i++){
                ((RadioButton)directionClicked.getChildAt(i)).setEnabled(true);
            }


            if (computerTurn) {
                message.setText("Not your turn playa!");
                return;
            }
            if (diceClicked == null && selectionMode) {
                message.setText("You with His Mickey Mouse Tattoos and 33-Pound Head can't see you selected empty box? - The Rock \n Click a tile with something in it.");
                return;
            }


            if (selectionMode) {
                resetSelectionView();
                if (diceClicked != null && diceClicked.isPlayerComputer()) {
                    message.setText("Excuse me? - Vickie Guerrero (Not your dice)");
                    return;
                }
                v.setBackgroundColor(SELECTED_COLOR);
                selectionMode = !selectionMode;
                inputCoordinates = clickedPosition;

                prevView = v;
            }
            else {
                // User places the tile to the specified location.
                desiredCoordinates = clickedPosition;

                // define directions
                boolean[] directions = {true, true};
                char chosenDirection = getChosenDirection(inputCoordinates, desiredCoordinates, directions);

                if (human.play(inputCoordinates, desiredCoordinates, chosenDirection)) {
                    computerTurn = true;

                    updateTurnView(computerTurn);

                    // Update tiles here
                    updateView(inputCoordinates, desiredCoordinates);

                    if (human.playerWins()) {
                        humanScore++;
                        endGame(false);
                    }
                }


                printMessage(human.getPrintMessage());

                selectionMode = !selectionMode;
                resetOptions();
            }

        }
    };

    public void updateTurnView(boolean isComputer) {
        TextView player = (TextView) findViewById(R.id.turn);

        if (isComputer) {
            player.setText(COMPUTER_TURN);
        }
        else {
            player.setText(HUMAN_TURN);
        }
    }

    private void resetOptions() {
        RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);
        directionClicked.clearCheck();

    }

    private char getChosenDirection(Coordinates old, Coordinates newC, boolean[] directions) {
        RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);

        //if (directions[0] == true && directions[1] == true) {
            //System.out.println("Both paths are possible");
            if (directionClicked.getCheckedRadioButtonId() == INVALID_SELECTION) {
                //selectionMode = true;
                //resetSelectionView();
                //message.setText("You have not selected the direction you want to move first");
                return DIRECTION_NOT_CHOSEN;
            }
            else {
                int selectedId = directionClicked.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) findViewById(selectedId);
                String text = selectedButton.getText().toString();
                if (text.equalsIgnoreCase(LATERAL_FIRST)) {
                    return 'l';
                }
                else return 'f';
            }
        //}

        // At this point, either lateral or horizontal direction is possible
        //return directions[0] == true ? 'f' : 'l';

    }

    public void resetSelectionView() {
        if (prevView != null) {
            prevView.setBackgroundColor(DEFAULT_COLOR);
            prevView.clearAnimation();
            prevView = null;
        }

        if (newView != null) {
            newView.setBackgroundColor(DEFAULT_COLOR);
            newView.clearAnimation();
            newView = null;
        }

        inputCoordinates = null;
        desiredCoordinates = null;
    }


    public void updateView(Coordinates oldPos, Coordinates newPos) {
        TextView tempView = findViewInTable(oldPos);
        tempView.setText("");
        tempView.setBackgroundColor(MOVE_INDICATOR_COLOR);
        tempView.startAnimation(anim);
        prevView = tempView;

        tempView = findViewInTable(newPos);
        tempView.setText(board.getDiceAt(newPos).getValue());
        tempView.setBackgroundColor(MOVE_INDICATOR_COLOR);
        tempView.startAnimation(anim);
        newView = tempView;
    }

    public void updateHelpView(Coordinates oldPos, Coordinates newPos) {
        TextView tempView = findViewInTable(oldPos);
        //tempView.setText("");
        tempView.setBackgroundColor(MOVE_INDICATOR_COLOR);
        tempView.startAnimation(anim);
        prevView = tempView;

        tempView = findViewInTable(newPos);
        //tempView.setText(board.getDiceAt(newPos).getValue());
        tempView.setBackgroundColor(MOVE_INDICATOR_COLOR);
        tempView.startAnimation(anim);
        newView = tempView;
    }


    private TextView findViewInTable(Coordinates inputCoordinates) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.givenGrid);

        // Loops through each cell view and checks if it matches with the coordinates with the view tag
        for (int rows = 0; rows < tableLayout.getChildCount(); ++rows) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(rows);

            for (int col = 0; col < tableRow.getChildCount(); ++col) {
                TextView box = (TextView) tableRow.getChildAt(col);
                Coordinates boxCoordinates = (Coordinates) box.getTag();
                if (boxCoordinates.getRow() == inputCoordinates.getRow() && boxCoordinates.getCol() == inputCoordinates.getCol()) {
                    return box;
                }
            }
        }
        return null;

    }

    /**
     * Makes the table in the board
     */
    public void makeTable(Board board) {
        // Finds the table that we will be working with
        TableLayout table = (TableLayout) findViewById(R.id.givenGrid);

        // Adds the grid in the android activity
        // It uses the TableLayout to create the overall table of the board
        for (int rowIndex = 0; rowIndex < 8; ++rowIndex) {
            TableRow row = new TableRow(this);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 4, 4, 4);
            for (int columnIndex = 0; columnIndex < 9; ++columnIndex) {
                // Specifies each cell of the table with TextView
                TextView columns = new TextView(this);
                columns.setWidth(130);
                columns.setHeight(100);
                columns.setTextSize(22);
                columns.setGravity(Gravity.CENTER);

                // Sets up the initial values to be empty
                Dice tempDice = board.getDiceAt(rowIndex,columnIndex);
                if (tempDice != null) {
                    columns.setText(tempDice.getValue());
                }
                else {
                    columns.setText("");
                }
                columns.setBackgroundColor(DEFAULT_COLOR);

                Coordinates tableCoordinates = new Coordinates(rowIndex, columnIndex);
                columns.setTag(tableCoordinates);

                columns.setOnClickListener(computeAction);

                //columns.setOnClickListener(calculatePosition);
                row.addView(columns, params);
            }
            table.addView(row);
        }

        // Displays the row numbers of the board from this table
        TableLayout rowIndexing = (TableLayout) findViewById(R.id.rowIndexing);

        // Adds the grid in the android activity
        // It uses the TableLayout to create the overall table of the board
        for (int rowIndex = 8; rowIndex >=0; rowIndex--) {
            TableRow row = new TableRow(this);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 4, 4, 4);

            TextView rowView = new TextView(this);
            rowView.setText(rowIndex + "");
            rowView.setWidth(130);
            rowView.setHeight(100);
            rowView.setTextSize(22);
            rowView.setGravity(Gravity.CENTER);

            row.addView(rowView, params);
            rowIndexing.addView(row);
        }

        // Displays the column numbers of the board
        TableLayout colIndexing = (TableLayout) findViewById(R.id.columnIndexing);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 4, 4, 4);

        for (int col = 0; col < 9; col++) {
            TextView colView = new TextView(this);
            colView.setText(col + 1 + "");
            colView.setWidth(130);
            colView.setHeight(100);
            colView.setTextSize(22);
            colView.setGravity(Gravity.CENTER);
            row.addView(colView, params);
        }
        colIndexing.addView(row);
    }


    public void initializeAnimation() {
        // Helps blink the tile
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
    }
}
