package simulation;

import javafx.scene.paint.Color;

import java.util.Collection;

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

    public FireCell(double initialState, int x, int y) {
        super(Math.floor(initialState), x, y);
        this.probCatch = (initialState - Math.floor(initialState))*100;
        createColorMap();
    }

    public double getProbCatch() { return this.probCatch; }

    @Override
    public void determineNextState(Collection<Double> neighborStates) {
        if(myState==0)
            nextState = myState;


    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, Color.YELLOW);
        myColorMap.put(1.0, Color.GREEN);
        myColorMap.put(2.0, Color.RED);
    }

    @Override
    public double mapKey(double myState) {
        return Math.floor(myState);
    }
}

