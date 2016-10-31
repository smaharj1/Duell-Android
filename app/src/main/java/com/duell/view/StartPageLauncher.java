package com.duell.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.duell.R;
import com.duell.model.Board;
import com.duell.model.Coordinates;
import com.duell.model.Dice;

import org.w3c.dom.Text;

public class StartPageLauncher extends AppCompatActivity {
    private final int DEFAULT_COLOR = Color.parseColor("#FCEBB6");
    private final int SELECTED_COLOR = Color.parseColor("#D3D3D3");
    private Board board;

    private boolean selectionMode = true;
    private Coordinates inputCoordinates;
    private Coordinates desiredCoordinates;
    private boolean computerTurn = false;

    private TextView message;
    private View prevView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page_launcher);

        // Set up the board.
        board = new Board();

        message = (TextView) findViewById(R.id.message);

        // Make the table based on the values on the board.
        makeTable(board);
    }

    public View.OnClickListener computeAction = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Coordinates clickedPosition = (Coordinates) v.getTag();

            Dice diceClicked = board.getDiceAt(clickedPosition.getRow(),clickedPosition.getCol());

            if (computerTurn) {
                message.setText("Not your turn playa!");
                return;
            }
            if (diceClicked == null && selectionMode) {
                message.setText("Empty box selected. Please select again.");
                return;
            }

            if (selectionMode) {
                v.setBackgroundColor(SELECTED_COLOR);
                selectionMode = !selectionMode;
                inputCoordinates = clickedPosition;
                Log.v("OLA:" , diceClicked.getValue());
                prevView = v;
            }
            else {
                // User places the tile to the specified location.
                desiredCoordinates = clickedPosition;

                // define directions
                boolean[] directions = {true, true};

                if (board.isLegal(inputCoordinates, desiredCoordinates, computerTurn) &&
                        board.isPathGood(inputCoordinates, desiredCoordinates, directions))  {
                    message.setText("Move is legal");


                    // TODO: Ask for the direction here
                    board.move(inputCoordinates, desiredCoordinates, 'f');

                    // TODO: Update tiles here
                }
                else {
                    message.setText("ILLEGAL MOVE");
                    return;
                }

                //Toast.makeText(getApplicationContext(), "Selected: " + inputCoordinates.getString() + " desired: " + desiredCoordinates.getString(), Toast.LENGTH_LONG).show();

                if (prevView != null) {
                    prevView.setBackgroundColor(DEFAULT_COLOR);
                    prevView = null;
                }

                inputCoordinates = null;
                desiredCoordinates = null;
                selectionMode = !selectionMode;
            }


        }
    };


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
}
