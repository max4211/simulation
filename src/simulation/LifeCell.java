package simulation;

import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

/**
 *  This class represents a cell in a Conway's Game of Life Simulation.
 *
 *  Possible cell states: 0 (Dead), 1 (Alive)
 *  Default Neighborhood Type: Moore
 *  Rules:
 *  Any live cell with two or three neighbors survives.
 *  Any dead cell with three live neighbors becomes a live cell.
 *  All other live cells die in the next generation. Similarly, all other dead cells stay dead.
 *
 * @author James Rumsey
 */
public class LifeCell extends Cell {

    /**
     * Constructs a LifeCell using same parameters as Cell
     * @param initialState: initial state (0 or 1)
     * @param row: y-coordinate of cell
     * @param col: x-coordinate of cell
     */
    public LifeCell(double initialState, int row, int col) {
        super(initialState, row, col);
        myTypeString = "Game of Life";
    }


    /**
     * Colors:
     * White - Dead
     * Black - Alive
     */
    @Override
    public void createColorMap(){
        myColorMap.put(0.0, WHITE);
        myColorMap.put(1.0, BLACK);
    }

    /**
     * Initializes State mappings (Dead, Alive)
     */
    @Override
    public void createStateMap() {
        myStateMap.put(0.0, new State("Dead", WHITE));
        myStateMap.put(1.0, new State("Alive", BLACK));
    }

    /**
     * Determines next state for cell based on rules above
     * @param neighbors: Map with Pair keys (representing coordinates) and Cell values
     */
    @Override
    public void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors){
        int numAlive = 0;
        for(Cell cell: neighbors.values()){
            numAlive += (int) cell.getState();
        }

        // Currently alive (1)
        if(myState==1.0){
            if(numAlive == 2 || numAlive == 3)
                nextState = 1.0;
            else
                nextState = 0.0;
        }
        // Currently dead (0)
        else{
            if(numAlive == 3)
                nextState = 1.0;
            else
                nextState = 0.0;
        }
    }

    /**
     * Required by Cell abstract class.
     * Returns myState, which is the map key for Game of Lie
     * @param myState
     * @return myState
     */
    @Override
    public double mapKey(double myState){
        return myState;
    }

    /**
     * @return "Game of Life" as String for XML files
     */
    @Override
    public String getTypeString(){ return "Game of Life"; }

    /**
     * A valid state for Game of Life is 0 (dead) or 1 (alive)
     * @param initialState
     * @return
     */
    @Override
    protected boolean checkValidState(double initialState) {
        return initialState == 0.0 || initialState == 1.0;
    }
}
