package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;
import java.util.Random;

/**
 *  This class represents a cell in a Schelling's Model of Segregation simulation.
 *
 * States:
 * 0 = empty (white)
 * 1 = race 1 (red)
 * 2 = race 2 (blue)
 *
 *  Rules:
 *  If the percentage of cells around this cell that are a different race is greater than the cell's
 *  myPercentTolerance, it is unsatisfied.
 *  If a cell is unsatisfied it can move to an empty cell
 *  Else, a cell is satisfied and it stays still
 *
 * Full rules explained at:
 * https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/mccown-schelling-model-segregation/
 */
public class SegregationCell extends Cell{
    private double myPercentTolerance;      //double between 0 and 1
    private double nextTolerance;
    private SegregationCell[][] myGrid;

    /**
     * Creates a new SegregationCell
     * @param initialState: the state; the integer part is the RACE and everything to the right of the decimal
     *                    is the PERCENT TOLERANCE
     * @param row: x position
     * @param col: y position
     */
    public SegregationCell(double initialState, int row, int col, SegregationCell[][] grid){
        super(initialState, row, col);
        myPercentTolerance = initialState - Math.floor(initialState);
        myGrid = grid;
    }

    public void setMyPercentTolerance(double tolerance){
        myPercentTolerance = tolerance;
    }

    public double getPercentTolerance(){ return myPercentTolerance; }

    public void setNextTolerance(double tol){ nextTolerance = tol; }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, Color.WHITE);
        myColorMap.put(1.0, Color.RED);
        myColorMap.put(2.0, Color.BLUE);
    }

    @Override
    public void determineNextState(Collection<Cell> neighbors) {
        double otherNeighbors = 0;
        double totalNeighbors = 0;
        for (Cell n : neighbors){
            totalNeighbors++;
            double neighborState = Math.floor(n.getState());
            if (neighborState != myState && neighborState != 0) otherNeighbors++;
        }
        boolean satisfied = otherNeighbors/totalNeighbors >= myPercentTolerance;
        if(!satisfied) { findVacantCell(); }
        else{
            nextState = myState;
            nextTolerance = myPercentTolerance; // TODO: override updateState method so tolerance updates too
        }
    }

    @Override
    public double mapKey(double myState) {
        return myState;
    }

    private void findVacantCell(){
        int height = myGrid.length;
        int width = myGrid[0].length;
        boolean foundEmpty = false;
        while(!foundEmpty){
            int randRow = new Random().nextInt(height);
            int randCol = new Random().nextInt(width);
            if(myGrid[randRow][randCol].getNextState()==0){
                foundEmpty = true;
                moveTo(randRow, randCol);
            }
        }
    }

    private void moveTo(int r, int c){
        // Switch states by setting cell being moved to's next state
        myGrid[r][c].setNextState(myState);
        this.nextState = 0;

        // Switch tolerances
        myGrid[r][c].setNextTolerance(myPercentTolerance);
        this.nextTolerance = 0;
    }
}
