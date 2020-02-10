package simulation;

import javafx.util.Pair;
import java.util.Map;
import java.util.Random;

/**
 * Represents cell from Spreading of Fire Simulation.
 *
 * States:
 * 0 = empty (empty ground or burnt tree)
 * 1 = tree (tree that is not burning)
 * 2 = burning (tree that is burning)
 * Number after decimal point = Probability of burning (ex: 1.5 has 50% chance of burning)
 *
 * Neighborhood Type: Von Neumann
 *
 * Rules:
 * If a cell is empty, it remains empty.
 * If a tree exists at a cell (type TREE)
 * If a cell is burning (i.e. a tree is burning) it becomes empty in the next step.
 *
 * Full rules described here: https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/shiflet-fire/
 */

public class FireCell extends Cell {

    private double probCatch;

    /**
     * Constructs a FireCell using same parameters as Cell.
     * Additionally, assigns probability of cell catching fire (after decimal point as a percentage)
     * @param initialState: initial state (0 or 1)
     * @param row: y-coordinate of cell
     * @param col: x-coordinate of cell
     */
    public FireCell(double initialState, int row, int col) {
        super(Math.floor(initialState), row, col);
        this.probCatch = (initialState - Math.floor(initialState));
        createColorMap();
        myTypeString = "Spreading of Fire";
    }

    /**
     * Determine next state based on rules above for Spreading of Fire
     * @param neighbors: Map with Pair keys (representing coordinates) and Cell values
     */
    @Override
    public void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors) {
        boolean hasBurningNeighbor = false;
        for(Cell n: neighbors.values()){
            if(n.getState()==2){
                hasBurningNeighbor = true;
                break;
            }
        }
        if(myState==0){
            nextState = 0.0;
        } else if(myState==1){
            if(hasBurningNeighbor && new Random().nextDouble() < this.probCatch)
                nextState = 2.0;
            else
                nextState = 1.0;
        } else if(myState==2){
            nextState = 0.0;
        }
    }

    /**
     * Colors:
     * Yellow - empty
     * Green - tree
     * Red - burning
     */
    @Override
    public void createColorMap() {
        myColorMap.put(0.0, YELLOW);
        myColorMap.put(1.0, GREEN);
        myColorMap.put(2.0, RED);
    }

    /**
     * Initializes State mappings (Empty, Tree, Burned)
     */
    @Override
    public void createStateMap() {
        myStateMap.put(0.0, new State("Empty", YELLOW));
        myStateMap.put(1.0, new State("Tree", GREEN));
        myStateMap.put(2.0, new State("Burning", RED));
    }

    /**
     * Required by Cell abstract class.
     * Returns myState, which is the map key for Fire Spreading
     * @param myState
     * @return myState
     */
    @Override
    public double mapKey(double myState) {
        return myState;
    }

    /**
     * A valid state for Fire is 0 (empty), 1 (tree), 2 (fire)
     * @param initialState
     * @return true if state is valid
     */
    @Override
    protected boolean checkValidState(double initialState) {
        return initialState==0.0 || initialState==1.0 || initialState==2.0;
    }

}

