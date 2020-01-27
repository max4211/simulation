package simulation;

import java.util.Collection;

public abstract class Cell {
    private double myState;
    private double nextState;

    /**
     *
     * @param initialState cell of set initial state
     */
    public Cell(double initialState) {
        this.myState = initialState;
    }

    public abstract void determineState(Collection<Integer> neighborStates);

    public void updateState() { this.myState = this.nextState; }

    public double getState() { return this.myState; }
}
