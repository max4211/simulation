package simulation;


import java.util.Collection;
import java.util.Random;

/**
 * States:
 * 0 = empty (empty ground or burnt tree)
 * 1 = tree (tree that is not burning)
 * 2 = burning (tree that is burning)
 *
 * Neighborhood Type: Von Neumann
 *
 * Rules:
 * If a cell is empty, it remains empty.
 * If a tree exists at a cell (type TREE)
 * If a cell is burning (i.e. a tree is burning) it becomes empty in the next step.
 */

public class FireCell extends Cell {

    private double probCatch;

    public FireCell(double initialState, int row, int col) {
        super(Math.floor(initialState), row, col);
        this.probCatch = (initialState - Math.floor(initialState));
        createColorMap();
    }

    public double getProbCatch() { return this.probCatch; }

    @Override
    public void determineNextState(Collection<Cell> neighbors) {
        boolean hasBurningNeighbor = false;
        for(Cell n: neighbors){
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

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, YELLOW);
        myColorMap.put(1.0, GREEN);
        myColorMap.put(2.0, RED);
    }

    @Override
    public void createStateMap() {
        myStateMap.put(0.0, new State("Susceptible", YELLOW));
        myStateMap.put(1.0, new State("Tree", GREEN));
        myStateMap.put(2.0, new State("Burned", RED));
    }

    @Override
    public double mapKey(double myState) {
        return myState;
    }
}

