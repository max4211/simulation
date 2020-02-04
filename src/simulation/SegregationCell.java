package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;

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
    private Double myPercentTolerance;      //double between 0 and 1

    /**
     * Creates a new SegregationCell
     * @param initialState: the state; the integer part is the RACE and everything to the right of the decimal
     *                    is the PERCENT TOLERANCE
     * @param row: x position
     * @param col: y position
     */
    public SegregationCell(double initialState, int row, int col){
        super(initialState, row, col);
        myPercentTolerance = initialState - Math.floor(initialState);
    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, Color.WHITE);
        myColorMap.put(1.0, Color.RED);
        myColorMap.put(2.0, Color.BLUE);
    }

    @Override
    public void determineNextState(Collection<Cell> neighbors) {
        int otherNeighbors = 0;
        int totalNeighbors = 0;
        boolean satisfied;
        for (Cell c : neighbors ){
            totalNeighbors++;
            if (Math.floor(c.getState()) != Math.floor(myState) && Math.floor(c.myState) != 0) otherNeighbors++;
        }
        satisfied = otherNeighbors*0.1/totalNeighbors >= myPercentTolerance;

        if(!satisfied){
            // find another cell and switch information with them
        }
    }

    @Override
    public double mapKey(double myState) {
        return Math.floor(myState);
    }
}
