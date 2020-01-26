package simulation;

import java.util.Collection;

public abstract class Cell {
    private int myState;
    private int nextState;

    public abstract void determineState(Collection<Integer> neighborStates);

    public void updateState() { this.myState = this.nextState; }

    public int getState() { return this.myState; }
}
