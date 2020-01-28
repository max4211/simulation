package simulation;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public abstract class Cell {
    private double myState;
    private double nextState;
    private Map<Double, Color> myColorMap;

    public Cell(double initialState, int x, int y) {
        this.myState = initialState;
        createColorMap();
    }

    public abstract void createColorMap();

    public abstract void determineState(Collection<Double> neighborStates);

    public abstract double mapKey(double myState);

    public void updateState() {
        this.myState = this.nextState;
    }

    public double getState() { return this.myState; }

    public Color getColor() {
        double key = mapKey(this.myState);
        return this.myColorMap.get(key);
    }
}
