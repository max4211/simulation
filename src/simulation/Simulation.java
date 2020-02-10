package simulation;

import configuration.SimulationSaver;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Simulation {

    protected ArrayList<ArrayList<Cell>> myGrid;
    protected int[] ROW_DELTA;
    protected int[] COL_DELTA;
    protected int SIMULATION_HEIGHT;
    protected int SIMULATION_WIDTH;
    protected String NEIGHBORHOOD_TYPE;

    /**
     * Constructs myGrid depending on the simulation type and according to the
     * information from SimulationConfig. Also builds ROW_DELTA and COL_DELTA.
     *
     */
    public Simulation() {
        ;
    }

    public Simulation(ArrayList<ArrayList<Cell>> grid, int[] rdelta, int[] cdelta){
        myGrid = grid;
        ROW_DELTA = rdelta;
        COL_DELTA = cdelta;
    }

    public void setGrid(ArrayList<ArrayList<Cell>> grid) { myGrid = grid;}
    public void setColDelta(int[] cdelta) {COL_DELTA = cdelta;}
    public void setRowDelta(int[] rdelta) {ROW_DELTA = rdelta;}
    public void setHeight(int height) {SIMULATION_HEIGHT = height;}
    public void setWidth(int width) {SIMULATION_WIDTH = width;}
    public void setNeighborhood(String n){ NEIGHBORHOOD_TYPE = n; }

    /*
    public void setCell(int r, int c, Cell cell){
        myGrid.get(r).set(c, cell);
    }

     */

    public int getHeight(){ return SIMULATION_HEIGHT; }
    public int getWidth(){ return SIMULATION_WIDTH; }
    public Cell getCell(int r, int c){ return myGrid.get(r).get(c); }
    public String getNeighborhood(){ return NEIGHBORHOOD_TYPE; }

    public void updateGrid() {
        determineUpdates();
        implementUpdates();
    }

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
        try{
            SimulationSaver s = new SimulationSaver(this);
            s.createFile();
        }
        catch(IOException e){
            //TODO: Some type of popup thrown here indicating that a valid simulation is not currently running
        }

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
