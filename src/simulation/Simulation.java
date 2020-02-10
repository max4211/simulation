package simulation;

import configuration.SimulationSaver;
import javafx.util.Pair;
import java.io.IOException;
import java.util.*;

public class Simulation {

    protected List<ArrayList<Cell>> myGrid;
    protected int[] ROW_DELTA;
    protected int[] COL_DELTA;
    protected int SIMULATION_HEIGHT;
    protected int SIMULATION_WIDTH;
    protected String NEIGHBORHOOD_TYPE;
    protected String EDGE_TYPE = "HARD";

    /**
     * Empty constructor, initializes a Simulation without setting parameters.
     */
    public Simulation() {
        ;
    }

    public Simulation(List<ArrayList<Cell>> grid, int[] rdelta, int[] cdelta){
        myGrid = grid;
        ROW_DELTA = rdelta;
        COL_DELTA = cdelta;
    }

    /**
     * Mutator method for grid
     * @param List of ArrayLists of Cells representing a "grid"
     */
    public void setGrid(List<ArrayList<Cell>> grid) { myGrid = grid;}

    /**
     * Sets column delta based on neighbor type.
     * @param cdelta array of change in column values
     */
    public void setColDelta(int[] cdelta) {COL_DELTA = cdelta;}

    /**
     * Sets row delta based on neighbor type
     * @param rdelta array of change in row values
     */
    public void setRowDelta(int[] rdelta) {ROW_DELTA = rdelta;}

    /**
     * Sets height of simulation
     * @param height number of rows
     */
    public void setHeight(int height) {SIMULATION_HEIGHT = height;}

    /**
     * Sets width of simulation
     * @param width number of columns
     */
    public void setWidth(int width) {SIMULATION_WIDTH = width;}

    /**
     * Sets neighborhood type of simulation
     * @param n
     */
    public void setNeighborhood(String n){ NEIGHBORHOOD_TYPE = n; }


    /**
     * Accessor methods for above instance variables, including edge type
     */
    public int getHeight(){ return SIMULATION_HEIGHT; }
    public int getWidth(){ return SIMULATION_WIDTH; }
    public String getNeighborhood(){ return NEIGHBORHOOD_TYPE; }
    public String getEdgeType(){ return EDGE_TYPE; }

    /**
     * Return Cell object that exists at row r and column c in grid
     * @param r
     * @param c
     * @return Cell at r, c in grid
     */
    public Cell getCell(int r, int c){ return myGrid.get(r).get(c); }

    /**
     * Determine which updates must be done cell by cell (set nextState) and then
     * implement those updates (set myState to nextState)
     */
    public void updateGrid() {
        determineUpdates();
        implementUpdates();
    }

    /**
     * Used to get count for each state for graphing purposes
     * @return a map of Strings (states) and their corresponding counts as Integers
     */
    public Map<String, Integer> countStates() {
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                Cell myCell = getCell(row, col);
                double myDouble = Math.floor(myCell.getState());
                State myState = myCell.getStateMap().get(myDouble);
                String myName = myState.getString();
                if (!(myMap.containsKey(myName))) {
                    myMap.put(myName, 0);
                }
                myMap.put(myName, myMap.get(myName) + 1);
            }
        }
        return myMap;
    }

    /**
     * Creates an instance of the SimulationSaver class, which outputs the current state of
     * the grid to an XML file that can be read to the simulation.
     * @throws IOException when a valid simulation is not currently running
     */
    public void captureGridState() throws IOException {
        SimulationSaver s = new SimulationSaver(this);
        s.createFile();
    }

    protected void determineUpdates() {
        for(int row=0; row<getHeight(); row++){
            for(int col=0; col<getWidth(); col++){
                getCell(row, col).determineNextState(getNeighbors(row, col));
            }
        }
    }

    protected void implementUpdates() {
        for(int row=0; row<getHeight(); row++){
            for(int col=0; col<getWidth(); col++){
                getCell(row, col).updateState();
            }
        }
    }

    protected boolean inBounds(int row, int col) {
        return (row < getHeight()) && (col < getWidth())
                && (row >= 0) && (col >= 0);
    }

    /**
     * @param row row cell whose neighbors are being requested
     * @param col column cell whose neighbors are being requested
     * @return list of neighbors states in order of ROW_DELTA and COL_DELTA
     */
    protected Map<Pair<Integer, Integer>, Cell> getNeighbors(int row, int col) {
        Map<Pair<Integer, Integer>, Cell> neighbors = new HashMap<>();
        int r2; int c2;
        for (int i = 0; i < ROW_DELTA.length; i ++) {
            r2 = row + ROW_DELTA[i]; c2 = col + COL_DELTA[i];
            if (inBounds(r2, c2)) {
                neighbors.put(new Pair(r2, c2), getCell(r2, c2));
            }
        }
        return neighbors;
    }
}
