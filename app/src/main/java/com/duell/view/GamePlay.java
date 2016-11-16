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
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
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

/**
 * This class is a Game Controller class that serves as the controller between UI and the model.
 */
public class GamePlay extends AppCompatActivity {
    // Holds the constants defining the colors and several default values.
    private final int DEFAULT_COLOR = Color.parseColor("#FCEBB6");
    private final int SELECTED_COLOR = Color.parseColor("#D3D3D3");
    private final int MOVE_INDICATOR_COLOR = Color.parseColor("#f0a830");
    private final int INVALID_SELECTION = -1;
    private final char DIRECTION_NOT_CHOSEN = 'a';
    private final String LATERAL_FIRST = "lateral first";
    private final String COMPUTER_TURN = "Computer";
    private final String HUMAN_TURN = "You";

    private Board board;

    // Coordinates that are input by the user.
    private boolean selectionMode = true;
    private Coordinates inputCoordinates;
    private Coordinates desiredCoordinates;
    private boolean computerTurn = false;

    // View variables.
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

    /**
     * This runs when the activity starts.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        // Handles the file name given and reads the board
        fileHandler = new FileHandler(getApplicationContext());

        String gameType = getIntent().getStringExtra(AppLauncher.MESSAGE_GAME);

        // Checks if the user wants to play a new game or load a game.
        if (gameType.equalsIgnoreCase("new")) {
            // Set up the board.
            int[] initialKeys = { 5, 1, 2, 6, 1, 6, 2, 1, 5 };

            // Initializes the board, human and computer scores.
            board = new Board(initialKeys);
            humanScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_HUMANSCORE));
            computerScore = Integer.parseInt(getIntent().getStringExtra(AppLauncher.MESSAGE_COMPUTERSCORE));

            String temp = getIntent().getStringExtra(AppLauncher.MESSAGE_TURN);

            // Set the turn according to what we got from the coin toss
            if (temp.equals("human")) {
                computerTurn = false;
            }
            else computerTurn = true;

            // Updates the scores.
            updateScore(humanScore, computerScore, computerTurn);
        }
        else {
            // opens the game if the user indicated to open a game.
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

        // Initializes the animation.
        initializeAnimation();
    }

    /**
     * It saves the game and exits the tournament.
     * @param view It is the view.
     */
    public void saveGame(View view) {
        // Creates the dialog box for allowing user to select what name the file should have. User does not have to input the extension
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.check, null));
        builder.setMessage("Please enter the file name");
        builder.setNeutralButton("NEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;

                EditText file = (EditText) d.findViewById(R.id.filename);
                String filename = file.getText().toString();

                try {
                    fileHandler.saveGame(filename, board, computerTurn,computerScore, humanScore);
                    endTournament();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        builder.create().show();

    }

    /**
     * Ends the game and goes to the tournament activity.
     * @param computerWon It holds if the game was won by computer.
     */
    public void endGame(boolean computerWon) {
        Intent intent = new Intent(getApplicationContext(), GameEndInfo.class);
        intent.putExtra(AppLauncher.MESSAGE_HUMANSCORE, String.valueOf(humanScore));
        intent.putExtra(AppLauncher.MESSAGE_COMPUTERSCORE, String.valueOf(computerScore));

        String winner = computerWon ? "computer" : "human";
        intent.putExtra(AppLauncher.MESSAGE_WINNER, winner);

        finish();

        startActivity(intent);
    }

    /**
     * Ends the tournament and exits to the last screen.
     */
    private void endTournament() {
        Intent intent = new Intent(getApplicationContext(), TournamentEndInfo.class);
        intent.putExtra(AppLauncher.MESSAGE_COMPUTERSCORE, computerScore+"");
        intent.putExtra(AppLauncher.MESSAGE_SAVE, "yes");
        intent.putExtra(AppLauncher.MESSAGE_HUMANSCORE,humanScore+"");
        finish();
        startActivity(intent);
    }

    /**
     * Runs when the user wants help from the computer.
     * @param view It holds the view object.
     */
    public void getHelp(View view) {
        // Check if the user is asking help in their turn.
        if (computerTurn) {
            printMessage("It is not your turn.");
            return;
        }

        resetSelectionView();

        // Play the human that goes through several algorithms and returns the appropriate suggestion to the user.
        human.play();

        // Assigns the suggested moves.
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

        // Updates the help view.
        updateHelpView(movingCoord, newCoord);

        // Prints the appropriate message as to why the move should be made.
        printMessage(human.getPrintMessage());

        //computerTurn = !computerTurn;
        //updateTurnView(computerTurn);
    }

    /**
     * Updates the score in the view.
     * @param hScore It holds the human score.
     * @param cScore It holds the bot score.
     * @param isComputer It holds if it is computer's turn.
     */
    public void updateScore(int hScore, int cScore, boolean isComputer) {
        TextView tempView = (TextView) findViewById(R.id.humanScore);
        tempView.setText(hScore+"");

        tempView = (TextView) findViewById(R.id.computerScore);
        tempView.setText(cScore+"");

        tempView = (TextView) findViewById(R.id.turn);
        tempView.setText(isComputer ? "Computer" : "You");
    }

    /**
     * Moves the computer.
     * @param view It is a view object.
     */
    public void moveComputer(View view) {
        // Checks if it is computer's turn. If not, return.
        if (!computerTurn) {
            printMessage("It is your turn.");
            return;
        }


        resetSelectionView();

        // Makes the computer's play.
        computer.play();

        Coordinates movingCoord = computer.getPrevCoordinates();
        Coordinates newCoord = computer.getNewCoordinates();

        // Updates the view according to the moves made.
        updateView(movingCoord, newCoord);

        computerTurn = !computerTurn;
        updateTurnView(computerTurn);

        printMessage(computer.getPrintMessage());

        // Checks if the computer won after the move. If yes, then end the game.
        if (computer.playerWins()) {
            computerScore++;
            endGame(true);
        }

    }

    /**
     * Prints the message.
     * @param message It holds the message.
     */
    public void printMessage(String message) {
        TextView mView = (TextView) findViewById(R.id.message);
        mView.setText(message);
    }

    /**
     * It handles the clicks on the board.
     */
    public View.OnClickListener computeAction = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Gets the position that the user clicked.
            Coordinates clickedPosition = (Coordinates) v.getTag();
            Dice diceClicked = board.getDiceAt(clickedPosition);
            RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);

            // Gets the direction clicked and sets it to enabled.
            for(int i = 0; i < directionClicked.getChildCount(); i++){
                ((RadioButton)directionClicked.getChildAt(i)).setEnabled(true);
            }

            // Checks if it is computers turn. If yes, then return.
            if (computerTurn) {
                message.setText("Not your turn playa!");
                return;
            }

            // Checks if the user clicked in an empty tile.
            if (diceClicked == null && selectionMode) {
                message.setText("You with His Mickey Mouse Tattoos and 33-Pound Head can't see you selected empty box? - The Rock \n Click a tile with something in it.");
                return;
            }

            // Checks if it is selection mode. Selection mode means that it is user's first selection.
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

                // Define directions.
                boolean[] directions = {true, true};
                char chosenDirection = getChosenDirection(inputCoordinates, desiredCoordinates, directions);

                if (human.play(inputCoordinates, desiredCoordinates, chosenDirection)) {
                    computerTurn = true;

                    updateTurnView(computerTurn);

                    // Update tiles here.
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

    /**
     * Updates the turn view in the UI.
     * @param isComputer It holds if it is computer's turn.
     */
    public void updateTurnView(boolean isComputer) {
        TextView player = (TextView) findViewById(R.id.turn);

        if (isComputer) {
            player.setText(COMPUTER_TURN);
        }
        else {
            player.setText(HUMAN_TURN);
        }
    }

    /**
     * Resets the options of the direction that user can choose.
     */
    private void resetOptions() {
        RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);
        directionClicked.clearCheck();

    }

    /**
     * Returns the direction that the user has chosen.
     * @param old It holds the old coordinates.
     * @param newC It holds the coordinates to move to.
     * @param directions It holds the possible first directions that gets modified.
     * @return Returns which direction that the user chooses.
     */
    private char getChosenDirection(Coordinates old, Coordinates newC, boolean[] directions) {
        RadioGroup directionClicked = (RadioGroup) findViewById(R.id.dicectionChoice);

            if (directionClicked.getCheckedRadioButtonId() == INVALID_SELECTION) {
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
    }

    /**
     * Resets the selection view that was previously greyed out.
     */
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

    /**
     * Updates the view by starting the animations.
     * @param oldPos It holds the previous coordinates.
     * @param newPos It holds the new coordinates.
     */
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

    /**
     * Updates the tiles that are suggested by the computer to make movement to.
     * @param oldPos It holds the old coordinates.
     * @param newPos It holds the new coordinates.
     */
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

    /**
     * It finds the view on the board. It returns the actual view of the cell in the table from the display.
     * @param inputCoordinates It holds the coordinates that the user touched.
     * @return Returns the TextView of the coordinates selected.
     */
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
     * Makes the table in the baord.
     * @param board It holds the information of the board.
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

    /**
     * It initializes the animation.
     */
    public void initializeAnimation() {
        // Helps blink the tile
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
    }
}
