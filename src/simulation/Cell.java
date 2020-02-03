package simulation;

import javafx.scene.paint.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Cell {
    protected double myState;
    protected double nextState;
    protected int myRow;
    protected int myCol;
    protected Map<Double, Color> myColorMap = new HashMap<>();

    public Cell(double initialState, int row, int col) {
        this.myState = initialState;
        this.myRow = row;
        this.myCol = col;
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
