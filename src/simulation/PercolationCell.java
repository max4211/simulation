package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;

/**
 *  This class represents a cell in a Percolation simulation.
 *
 * States:
 * 0 = blocked (black)
 * 1 = open (white)
 * 2 = percolated (sky blue)
 *
 *  Neighborhood Type: Moore
 *  Rules:
 *  If a cell is blocked, its state does not change.
 *  If a cell is open and has at least one percolated neighbor, it is percolated.
 *  If a cell is percolated, its state does not change.
 *
 * Full rules explained on pg. 3 of:
 * https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/PercolationCA.pdf
 */
public class PercolationCell extends Cell {

    /**
     * Constructs a PercolationCell using same parameters as Cell
     * @param initialState: initial state (0, 1, or 2)
     * @param row: y-coordinate of cell
     * @param col: x-coordinate of cell
     */
    public PercolationCell(double initialState, int row, int col) {
        super(initialState, row, col);
    }

    @Override
    public void createColorMap(){
        myColorMap.put(0.0, Color.BLACK);
        myColorMap.put(1.0, Color.WHITE);
        myColorMap.put(2.0, Color.SKYBLUE);
    }

    @Override
    public void determineState(Collection<Double> neighborStates){

        // Open and has a percolated neighbor -> percolates
        if(myState==1 && neighborStates.contains(2.0))
            nextState = 2.0;
        // Blocked, percolated, or open without a percolated neighbor -> constant
        else
            nextState = myState;

    }

    // unsure what type of state to double mapping we should do here?
    @Override
    public double mapKey(double myState){
        return myState;
    }

}
