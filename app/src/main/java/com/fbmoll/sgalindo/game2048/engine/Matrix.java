package com.fbmoll.sgalindo.game2048.engine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fbmoll.sgalindo.game2048.activities.GameActivity;
import com.fbmoll.sgalindo.game2048.R;
import com.fbmoll.sgalindo.game2048.data.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Matrix[row][column]
 *
 *
 * NOTAS: SE VA HASTA EL FINAL.
 */
public class Matrix extends Fragment{
    // CONSTANTS
    private final int TILES_NUMBER = 4;
    private final int INITIAL_TILES =2;

    // Variables
    private Tile[][] matrix = new Tile[TILES_NUMBER][TILES_NUMBER];
    private Tile[][] lastMatrix = new Tile[TILES_NUMBER][TILES_NUMBER];
    private Tile[][] undoMatrix = new Tile[TILES_NUMBER][TILES_NUMBER];
    private boolean undoAvailable;
    private Score score = new Score();
    private int lastScorePoints = 0;

    // Swipes
    private float x1, x2, y1, y2;
    private final int MIN_DISTANCE = 150;

    // Layout variables
    private GridLayout layout;
    private List<TextView> tileViews = new ArrayList<>();
    private GameActivity parentActivity;


    /**
     * Default empty constructor.
     */
    public Matrix() {
        //generateInitialMatrix();
    }

    //<editor-fold desc="GETTERS">
    private Tile getTile(int row, int column) {
        return matrix[row][column];
    }

    public Score getScore() {
        return score;
    }

    private List<TextView> getTextViewList() {
        final int childCount = layout.getChildCount();
        List<TextView> tileViews = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            tileViews.add((TextView) layout.getChildAt(i));  // Add the textview
        }
        return tileViews;
    }

    private List<Integer[]> getEmptyPositions(){
        List<Integer[]> positions = new ArrayList<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                if (matrix[row][column].isEmpty()){
                    positions.add(new Integer[]{row, column});
                }
            }
        }
        return positions;
    }

    private TextView getTextView(int row, int column){
        int position = row * TILES_NUMBER + column;
        return tileViews.get(position);
    }

    public int getTILES_NUMBER() {
        return TILES_NUMBER;
    }

    public Tile[][] getMatrix() {
        return matrix;
    }

    public boolean isUndoAvailable() {
        return undoAvailable;
    }
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setElements(View view){
        // Get the layout
        layout = view.findViewById(R.id.matrixLayout);
        tileViews = getTextViewList(); //
        parentActivity = ((GameActivity)getActivity());
    }

    public void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    private void setUndoAvailable(){
        undoAvailable = true;
        parentActivity.setUndo(true);
    }

    private void setUndoUnavailable(){
        undoAvailable = false;
        parentActivity.setUndo(false);
    }
    //</editor-fold>

    //<editor-fold desc="VIEW METHODS">
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.matrix, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setElements(view);
        generateInitialMatrix();
        updateTilesView();  // Do a first update
        undoAvailable = false;

        // TOUCH LISTENER
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // First save the undoMatrix
                saveMatrix(undoMatrix);
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    x1 = event.getX();
                    y1 = event.getY();
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //do something
                    x2 = event.getX();
                    y2 = event.getY();
                    //getting value for horizontal swipe
                    float valueX = x2 - x1;
                    //getting value for vertical swipe
                    float valueY = y2 - y1;
                    if (Math.abs(valueX) > MIN_DISTANCE) {
                        //detect left to right swipe
                        if (x2>x1) {
                            moveAllTiles(Direction.RIGHT);
                        }else{
                            moveAllTiles(Direction.LEFT);
                        }
                        if (!compareMatrix()){
                            generateNextRandomTile();
                            updateTilesView();
                            // Save this matrix as the last one
                            saveMatrix(lastMatrix);
                            // Undo is available
                            setUndoAvailable();
                            if (isFinished()){
                                parentActivity.endGame();
                            }
                        }
                    }else if(Math.abs(valueY) > MIN_DISTANCE){
                        //detect top to bottom swipe
                        if (y2>y1){
                            moveAllTiles(Direction.DOWN);
                        }else{
                            moveAllTiles(Direction.UP);
                        }
                        if (!compareMatrix()){
                            generateNextRandomTile();
                            updateTilesView();
                            // Save this matrix as the last one
                            saveMatrix(lastMatrix);
                            // Undo is available
                            setUndoAvailable();
                            if (isFinished()){
                                parentActivity.endGame();
                            }
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }
            });
        }
    //</editor-fold>

    //<editor-fold desc="UPDATE METHODS">
    private void updateTilesView() {
        for (Tile[] tiles : matrix) {
            for (Tile tile : tiles) {
                tile.updateView();
            }
        }
    }

    private void restScoreAndView(){
        score.restScore(lastScorePoints);
        parentActivity.setCurrentScore(score.getScore());
    }

    private void sumScoreAndView(int newScore){
        score.sumScore(newScore);
        lastScorePoints = newScore;
        parentActivity.setCurrentScore(score.getScore());
    }
    //</editor-fold>

    //<editor-fold desc="GENERATE METHODS">
    private Tile[][] generateEmptyMatrix() {
        Tile[][] localMatrix = new Tile[TILES_NUMBER][TILES_NUMBER];
        for (int row = 0; row < localMatrix.length; row++) {
            for (int column = 0; column < localMatrix[row].length; column++) {
                localMatrix[row][column] = generateTile(0, row, column);
            }
        }
        return localMatrix;
    }

    private void generateInitialMatrix() {
        // Set empty matrix
        matrix = generateEmptyMatrix();
        // Set empty matrix for last matrix
        lastMatrix = generateEmptyMatrix();
        undoMatrix = generateEmptyMatrix();
        // Set INITIAL_TILES random tiles in matrix
        for (int i = 0; i < INITIAL_TILES; i++) {
            generateAutoNewTile(2, (int) (Math.random() * (matrix.length)), (int) (Math.random() * (matrix.length)));
        }
    }

    private Tile generateTile(int value, int row, int column) {
        int posTotal = TILES_NUMBER * row + column;
        return new Tile(value, tileViews.get(posTotal));
    }

    private void generateAutoNewTile(int value, int row, int column) {
        matrix[row][column] = generateTile(value, row, column);
    }

    private void generateNextRandomTile(){
        List<Integer[]> emptyTiles= getEmptyPositions();
        Integer[] position = emptyTiles.get((int) (Math.random()*emptyTiles.size()));
        generateAutoNewTile(2, position[0], position[1]);
        matrix[position[0]][position[1]].playAppearingAnimation();
    }
    //</editor-fold>

    //<editor-fold desc="MOVE METHODS">
    /**
     * Automatically moves the position tile by setting a new one on the specified new position
     * and value and erasing the old one (setting it to 0).
     * @param value
     * @param pastRow
     * @param pastColumn
     * @param newRow
     * @param newColumn
     */
    private void moveTile(int value, int pastRow, int pastColumn, int newRow, int newColumn){
        // Create and set
        generateAutoNewTile(value, newRow, newColumn);
        // And clean the last tile
        generateAutoNewTile(0, pastRow, pastColumn);
    }

    private void moveAllTiles(Direction direction){
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                switch (direction){
                    case UP:
                        moveTileUp(row, column);
                        break;
                    case DOWN:
                        moveTileDown(TILES_NUMBER - row - 1, TILES_NUMBER - column - 1);
                        break;
                    case LEFT:
                        moveTileLeft(column, row);
                        break;
                    case RIGHT:
                        moveTileRight(column, TILES_NUMBER - 1 - row);
                        break;
                }
//                moveTileDirection(row, column, direction);
            }
        }
    }

    //------------ DIRECTIONS ------------
//    private void moveTileDirection(int row, int column, Direction direction){
//        // Get the position tile
//        Tile currentTile = getTile(row, column);
//        if (!currentTile.isEmpty()){
//            boolean isPossible = true;
//            int checker=0, value=0;
//            switch (direction){
//                case UP:
//                    checker = -row;
//                    value = 0;
//                    break;
//                case DOWN:
//                    checker = row;
//                    value = TILES_NUMBER - 1;
//                    break;
//                case LEFT:
//                    checker = -column;
//                    value = 0;
//                    break;
//                case RIGHT:
//                    checker = column;
//                    value = TILES_NUMBER - 1;
//                    break;
//            }
//            while (checker < value && isPossible){
//                Tile objectiveTile = getTile(row + direction.getRow_offset(),
//                        column + direction.getColumn_offset());
//                if (objectiveTile.isEmpty()){
//                    // Play
//                    moveTile(currentTile.getValue(), row, column, row + direction.getRow_offset(),
//                            column + direction.getColumn_offset());
//                    // Set the correct position
//                    checker++;
//                    row = direction.getProperPosition(row, column)[0];
//                    column = direction.getProperPosition(row, column)[1];
//                }
//                else{
//                    if (objectiveTile.getValue() == currentTile.getValue()){
//                        moveTile(currentTile.getValue() + objectiveTile.getValue(), row, column, row + direction.getRow_offset(),
//                                column + direction.getColumn_offset());
//                        updateScoreAndView(objectiveTile.getValue() + currentTile.getValue());
//                        //
//                        checker++;
//                        //
//                        row = direction.getProperPosition(row, column)[0];
//                        column = direction.getProperPosition(row, column)[1];
//                        //
//                        matrix[row][column].playSumAnimation();
//                    }else isPossible = false;
//                }
//            }
//        }
//    }

    private void moveTileLeft(int row, int column) {
        // Get the position tile
        Tile tile = getTile(row, column);
        if (tile.getValue() > 0) {
            boolean isPossible = true;
            while (column > 0 && isPossible) {
                Tile leftTile = getTile(row, column - 1);
                // Check if there is empty space
                if (leftTile.isEmpty()) {
                    // Play
                    moveTile(tile.getValue(), row, column, row, column - 1);
                    // Set the correct position
                    column--;
                }
                // The tile is occupied
                else{
                    // Check if the value is the same
                    if (leftTile.getValue() == tile.getValue()) {
                        // Move the tile and sum the score
                        moveTile(leftTile.getValue() + tile.getValue(), row, column, row, column - 1);
                        sumScoreAndView(leftTile.getValue() + tile.getValue());
                        // Set the correct position
                        column--;
                        // Play the animation
                        matrix[row][column].playSumAnimation();
                    }else isPossible = false;
                    // If it is not the same, nothing is done
                }
            }
        }
    }

    private void moveTileRight(int row, int column) {
        // Get the position tile
        Tile tile = getTile(row, column);
        if (tile.getValue() > 0) {
            boolean isPossible = true;
            while (column < TILES_NUMBER - 1 && isPossible) {
                Tile leftTile = getTile(row, column + 1);
                // Check if there is empty space
                if (leftTile.isEmpty()) {
                    moveTile(tile.getValue(), row, column, row, column + 1);
                    // Set the correct position
                    column++;
                }
                // The tile is occupied
                else{
                    // Check if the value is the same
                    if (leftTile.getValue() == tile.getValue()) {
                        moveTile(leftTile.getValue() + tile.getValue(), row, column, row, column + 1);
                        sumScoreAndView(leftTile.getValue() + tile.getValue());
                        // Set the correct position
                        column++;
                        // Play the animation
                        matrix[row][column].playSumAnimation();
                    }else isPossible = false;
                    // If it is not the same, nothing is done
                }
            }
        }
    }

    private void moveTileUp(int row, int column) {
        // Get the position tile
        Tile tile = getTile(row, column);
        if (tile.getValue() > 0) {
            boolean isPossible = true;
            while (row > 0 && isPossible) {
                Tile leftTile = getTile(row - 1, column);
                // Check if there is empty space
                if (leftTile.isEmpty()) {
//                    tile.playMoveAnimationY(leftTile.getLocationOnScreen());
                    moveTile(tile.getValue(), row, column, row - 1, column);
                    // Set the correct position
                    row--;
                }
                // The tile is occupied
                else{
                    // Check if the value is the same
                    if (leftTile.getValue() == tile.getValue()) {
                        moveTile(leftTile.getValue() + tile.getValue(), row, column, row - 1, column);
                        sumScoreAndView(leftTile.getValue() + tile.getValue());
                        // Set the correct position
                        row--;
                        // Play the animation
                        matrix[row][column].playSumAnimation();
                    }else isPossible = false;
                    // If it is not the same, nothing is done
                }
            }
        }
    }

    private void moveTileDown(int row, int column) {
        // Get the position tile
        Tile tile = getTile(row, column);
        if (tile.getValue() > 0) {
            boolean isPossible = true;
            while (row < TILES_NUMBER - 1 && isPossible) {
                Tile leftTile = getTile(row + 1, column);
                // Check if there is empty space
                if (leftTile.isEmpty()) {
                    //tile.playMoveAnimationY(leftTile.getLocationOnScreen());
                    moveTile(tile.getValue(), row, column, row + 1, column);
                    // Set the correct position
                    row++;
                }
                // The tile is occupied
                else{
                    // Check if the value is the same
                    if (leftTile.getValue() == tile.getValue()) {
                        moveTile(leftTile.getValue() + tile.getValue(), row, column, row + 1, column);
                        sumScoreAndView(leftTile.getValue() + tile.getValue());
                        // Set the correct position
                        row++;
                        // Play the animation
                        matrix[row][column].playSumAnimation();
                    }else isPossible = false;
                    // If it is not the same, nothing is done
                }
            }
        }
    }
    //</editor-fold>

    private void saveMatrix(Tile[][] saveMatrix){
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                saveMatrix[row][column].setValue(matrix[row][column].getValue());
            }
        }
    }

    private boolean compareMatrix(){
        for (int row = 0; row < lastMatrix.length; row++) {
            for (int column = 0; column < lastMatrix[row].length; column++) {
                if (lastMatrix[row][column].getValue() != matrix[row][column].getValue()){
                    return false;
                }
            }
        }
        return true;
    }

    public void undoChanges(){
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                matrix[row][column].setValue(undoMatrix[row][column].getValue());
            }
        }
        restScoreAndView();
        updateTilesView();
        setUndoUnavailable();
    }

    /**
     * Checks if the game has finished by:
     * 1. Check if any 2048 tile. -- finish
     * 2. Check if any empty tile. -- continue
     * 3. Check if any tile can be summed -- continue
     * Otherwise it has finished.
     * @return
     */
    private boolean isFinished(){
        // First check if any tile is 2048 - win
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                Tile currentTile = matrix[row][column];
                if (currentTile.getValue() == 2048){
                    return true;
                }
            }
        }

        // Then check if any tile is empty - keep playing
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                Tile currentTile = matrix[row][column];
                if (currentTile.isEmpty()){
                    return false;
                }
            }
        }

        // Last check if any tile can be summed
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                Tile currentTile = matrix[row][column];
                int[] rowOffset = {-1,1}, columnOffset = {-1,1};

                // Rows
                if (row == 0){
                    rowOffset = new int[]{1};
                }else if (row == matrix.length - 1){
                    rowOffset = new int[]{-1};
                }

                // Columns
                if (column == 0){
                    columnOffset = new int[]{1};
                }else if (column == matrix[row].length - 1){
                    columnOffset = new int[]{-1};
                }

                // Check the row neighbor tiles
                for (int i : rowOffset) {
                    Tile neighborTile = getTile(row + i, column);
                    if (neighborTile.getValue() == currentTile.getValue()) {
                        return false;
                    }
                }
                // Check the column neighbor tiles
                for (int i : columnOffset) {
                    Tile neighborTile = getTile(row, column + i);
                    if (neighborTile.getValue() == currentTile.getValue()) {
                        return false;
                    }
                }
            }
        }

        // The loop cycle has ended with no false returning - no move is allowed
        return true;
    }
}
