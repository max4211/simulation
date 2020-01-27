package simulation;

import java.util.ArrayList;
import java.util.Collection;

public class Simulation {

    // Simulation dynamic variables
    private Cell[][] myGrid;

    // Simulation constant variables
    private int[] ROW_DELTA;
    private int[] COL_DELTA;

    public Simulation(String fileName) {
        readFile(fileName);
    }

    private void readFile(String fileName) {
        // Parse XML file, create pseudo constrsuctor
        String type = ""; // Read from file
        if (type.equals("Fire")) {
            createFireSim(fileName);
        } else {

        }

        /*
        Cell[][] grid; int[] rDelta; int[] cDelta;
        this.myGrid = grid;
        this.ROW_DELTA = rDelta;
        this.COL_DELTA = cDelta;
         */
    }

    private void createFireSim(String fileName) {
    }

    public double[][] updateGrid() {
        return new double[0][0];
    }

    private Collection<Double> getNeighborStates(int row, int col) {
        Collection<Double> neighborStates = new ArrayList<Double>();
        int r2; int c2;
        for (int i = 0; i < ROW_DELTA.length; i ++) {
            r2 = row + ROW_DELTA[i]; c2 = col + COL_DELTA[i];
            if (inBounds(r2, c2)) {
                neighborStates.add(myGrid[r2][c2].getState());
            }
        }
        return neighborStates;
    }

    private boolean inBounds(int r2, int c2) {
        return (r2 <= myGrid.length) && (c2 <= myGrid[0].length);
    }

}
