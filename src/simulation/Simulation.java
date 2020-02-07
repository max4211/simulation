package simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Simulation {

    private ArrayList<ArrayList<Cell>> myGrid;
    private int[] ROW_DELTA;
    private int[] COL_DELTA;
    private int SIMULATION_HEIGHT;
    private int SIMULATION_WIDTH;

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

    public void setCell(int r, int c, Cell cell){
        myGrid.get(r).set(c, cell);
    }

    public int getHeight(){ return SIMULATION_HEIGHT; }
    public int getWidth(){ return SIMULATION_WIDTH; }
    public Cell getCell(int r, int c){ return myGrid.get(r).get(c); }

    public void updateGrid() {
        determineUpdates();
        implementUpdates();
    }

    public Map<State, Integer> countStates() {
        Map<State, Integer> myMap = new HashMap<State, Integer>();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                Cell myCell = getCell(row, col);
                double myDouble = myCell.getState();
                State myState = myCell.getStateMap().get(myDouble);
                if (!(myMap.containsKey(myState))) {
                    myMap.put(myState, 0);
                    myMap.put(myState, myMap.get(myState) + 1);
                }
            }
        }
        return myMap;
    }

    private void determineUpdates() {
        for(int row=0; row<getHeight(); row++){
            for(int col=0; col<getWidth(); col++){
                getCell(row, col).determineNextState(getNeighbors(row, col));
            }
        }
    }

    private void implementUpdates() {
        for(int row=0; row<getHeight(); row++){
            for(int col=0; col<getWidth(); col++){
                getCell(row, col).updateState();
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return (row < getHeight()) && (col < getWidth())
                && (row >= 0) && (col >= 0);
    }

    /**
     * @param row row cell whose neighbors are being requested
     * @param col column cell whose neighbors are being requested
     * @return list of neighbors states in order of ROW_DELTA and COL_DELTA
     */
    private Collection<Cell> getNeighbors(int row, int col) {
        Collection<Cell> neighbors = new ArrayList<Cell>();
        int r2; int c2;
        for (int i = 0; i < ROW_DELTA.length; i ++) {
            r2 = row + ROW_DELTA[i]; c2 = col + COL_DELTA[i];
            if (inBounds(r2, c2)) {
                neighbors.add(getCell(r2, c2));
            }
        }
        return neighbors;
    }

}
