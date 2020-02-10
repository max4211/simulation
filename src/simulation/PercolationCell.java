package simulation;

import javafx.util.Pair;
import java.util.Map;

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
        myTypeString = "Percolation";
    }

    /**
     * Determine next state based on rules above
     * @param neighbors: Map with Pair keys (representing coordinates) and Cell values
     */
    @Override
    public void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors){
        boolean neighborPercolates = false;
        for(Cell n: neighbors.values()){
            if(n.getState()==2){
                neighborPercolates = true;
                break;
            }
        }
        // Open and has a percolated neighbor -> percolates
        if(myState==1 && neighborPercolates)
            nextState = 2.0;
        // Blocked, percolated, or open without a percolated neighbor -> constant
        else
            nextState = myState;

    }

    /**
     * @return "Percolation" as String for XML output
     */
    @Override
    public String getTypeString(){ return "Percolation"; }

    /**
     * A valid state for Percolation is 0 (blocked), 1 (empty), 2 (percolating)
     * @param initialState
     * @return true if valid
     */
    @Override
    protected boolean checkValidState(double initialState) {
        return initialState==0.0 || initialState==1.0 || initialState==2.0;
    }

    @Override
    protected void createColorMap(){
        myColorMap.put(0.0, BLACK);
        myColorMap.put(1.0, WHITE);
        myColorMap.put(2.0, SKYBLUE);
    }

    @Override
    protected void createStateMap() {
        myStateMap.put(0.0, new State("Blocked", BLACK));
        myStateMap.put(1.0, new State("Open", WHITE));
        myStateMap.put(2.0, new State("Water", SKYBLUE));
    }

    /**
     * Required by Cell abstract class.
     * Returns myState, which is the map key for Percolation
     * @param myState
     * @return myState
     */
    @Override
    protected double mapKey(double myState){
        return myState;
    }
}
