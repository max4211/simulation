package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;

/**
 *  This class represents a cell in a Conway's Game of Life Simulation.
 *
 *  Possible cell states: 0 (Dead), 1 (Alive)
 *  Neighborhood Type: Moore
 *  Rules:
 *  Any live cell with two or three neighbors survives.
 *  Any dead cell with three live neighbors becomes a live cell.
 *  All other live cells die in the next generation. Similarly, all other dead cells stay dead.
 *
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
    }

    @Override
    public void createColorMap(){
        myColorMap.put(0.0, Color.WHITE);
        myColorMap.put(1.0, Color.BLACK);
    }

    @Override
    public void determineState(Collection<Double> neighborStates){
        int numAlive = 0;
        for(double state: neighborStates){
            numAlive += (int) state;
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
        if(myState!=nextState){
            System.out.println("Something changed");
        }
        else
            System.out.println("no change");
    }

    // unsure what type of state to double mapping we should do here?
    @Override
    public double mapKey(double myState){
        return myState;
    }

}
