package simulation;

import javafx.util.Pair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The Cell abstract class represents a single cell in a simulation. These
 * Cell objects are added to the grid in Simulation in the Configuration class.
 * All classes that extend Cell represent different simulation types and are
 * initialized based on the specifications of the XML file input.
 *
 * @author James Rumsey, Max Smith, Braeden Ward
 */
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

    /**
     * Constructs Cell object by creating color and state mappings (doubles to color string/State),
     * setting myState to its initial value, and setting row and column values
     * @param initialState: a valid double that maps to a state, dependent on cell type
     * @param row: row the cell is in
     * @param col: column the cell is in
     */
    public Cell(double initialState, int row, int col) {
        createColorMap();
        createStateMap();

        if (!checkValidState(initialState))
          throw new IllegalArgumentException("Invalid Cell type");

        this.myState = initialState;
        this.myRow = row;
        this.myCol = col;
    }

    /**
     * Constructs a cell without an initial state, using 0.0 as the default.
     * @param row: row the cell is in
     * @param col: column the cell is in
     */
    public Cell(int row, int col) {
        this(0.0, row, col);
    }


    /**
     * Determines next state of cell based on simulation rules/
     * @param neighbors: Map with Pair keys (representing coordinates) and Cell values
     */
    public abstract void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors);

    /**
     * Returns simulation type as a single word string, meant for creating an
     * XML file
     * @return simulation type as a single-word String
     */
    public String getTypeString(){
        return myTypeString;
    }

    /**
     * Update myState so it is now the already determined nextState.
     * Called on all cells sequentially after nextState has been updated.
     */
    public void updateState() { this.myState = this.nextState; }

    /**
     * @return Current state
     */
    public double getState() { return this.myState; }

    /**
     * @return Pre-determined next state
     */
    public double getNextState(){ return this.nextState; }

    /**
     * Set the next state of the cell
     * @param state: a double that maps to a valid state for the cell type
     */
    public void setNextState(double state){ this.nextState = state; }

    /**
     * Set the current state of the cell
     * @param state: a double that maps to a valid state for the cell type
     */
    public void setState(double state){ this.myState = state; }

    /**
     * Get the color (as a hexadecimal String) for the cell based on state
     * @return a hexidecimal String representing a color
     */
    public String getColor() {
        double key = mapKey(this.myState);
        return this.myColorMap.get(key);
    }

    /**
     * Get map of doubles and their corresponding State objects
     * @return map of state objects and colors for chart processing
     */
    public Map<Double, State> getStateMap() {
        return this.myStateMap;
    }

    /**
     * Creates mapping of Doubles to hexadecimal color Strings
     */
    protected abstract void createColorMap();

    /**
     * Creates mapping of Doubles to State objects
     */
    protected abstract void createStateMap();


    /**
     * Checks if a cell state is valid based on simulation rules
     */
    protected abstract boolean checkValidState(double initialState);

    protected abstract double mapKey(double myState);
}
