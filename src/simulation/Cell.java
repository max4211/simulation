package simulation;

import javafx.util.Pair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Cell {
    protected static final String WHITE = "#FFFFFF";
    protected static final String BLACK = "#000000";
    protected static final String SKYBLUE = "#87CEEB";
    protected static final String YELLOW = "#FFFF00";
    protected static final String GREEN = "#008000";
    protected static final String RED = "#FF0000";
    protected static final String BLUE = "#0000FF";

    protected static final Pair<Integer, Integer> N = new Pair<>(-1, 0);
    protected static final Pair<Integer, Integer> NW = new Pair<>(-1, -1);
    protected static final Pair<Integer, Integer> W = new Pair<>(0, -1);
    protected static final Pair<Integer, Integer> SW = new Pair<>(1, -1);
    protected static final Pair<Integer, Integer> S = new Pair(1, 0);
    protected static final Pair<Integer, Integer> SE = new Pair(1, 1);
    protected static final Pair<Integer, Integer> E = new Pair<>(0, 1);
    protected static final Pair<Integer, Integer> NE = new Pair<>(-1, 1);

    protected double myState;
    protected double nextState;
    protected int myRow;
    protected int myCol;
    protected Map<Double, String> myColorMap = new HashMap<>();
    protected Map<Double, State> myStateMap = new HashMap<>();
    protected String myTypeString;

    public Cell(double initialState, int row, int col) {
        createColorMap();
        createStateMap();

        if (!checkValidState(initialState))
          throw new IllegalArgumentException("Invalid Cell type");

        this.myState = initialState;
        this.myRow = row;
        this.myCol = col;
    }

    public Cell(int row, int col) {
        this(0.0, row, col);
    }

    protected abstract boolean checkValidState(double initialState);

    public abstract void createColorMap();
    public abstract void createStateMap();
    public abstract void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors);
    public abstract double mapKey(double myState);

    public String getTypeString(){
        return myTypeString;
    }

    public void updateState() { this.myState = this.nextState; }

    public double getState() { return this.myState; }

    public double getNextState(){ return this.nextState; }

    public void setNextState(double state){ this.nextState = state; }

    public void setState(double state){ this.myState = state; }

    public String getColor() {
        double key = mapKey(this.myState);
        return this.myColorMap.get(key);
    }

    /**
     *
     * @return map of colors and states to simulation for chart processing
     */
    public Map<Double, String> getColorMap() {
        return this.myColorMap;
    }

    public Map<Double, State> getStateMap() {
        return this.myStateMap;
    }
}
