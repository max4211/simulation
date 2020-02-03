package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;

/**
 *  This class represents a cell in a Percolation simulation.
 *
 */
public class PercolationCell extends Cell {

    /**
     * Constructs a LifeCell using same parameters as Cell
     * @param initialState: initial state (0 or 1)
     * @param row: y-coordinate of cell
     * @param col: x-coordinate of cell
     */
    public PercolationCell(double initialState, int row, int col) {
        super(initialState, row, col);
    }

    @Override
    public void createColorMap(){
        myColorMap.put(0.0, Color.WHITE);
        myColorMap.put(1.0, Color.BLACK);
    }

    @Override
    public void determineState(Collection<Double> neighborStates){
    }

    // unsure what type of state to double mapping we should do here?
    @Override
    public double mapKey(double myState){
        return myState;
    }

}
