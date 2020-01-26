package simulation;

import java.util.ArrayList;
import java.util.Collection;

public class Simulate {

    // Simulation dynamic variables
    private Cell[][] myGrid;
    private int myStep;

    // Simulation constant variables
    private int[] ROW_DELTA;
    private int[] COL_DELTA;

    public Simulate(Cell[][] grid, int[] rDelta, int[] cDelta) {
        this.myGrid = grid;
        this.ROW_DELTA = rDelta;
        this.COL_DELTA = cDelta;
    }

    private void updateGrid() {
        Cell cell;
        // Iterate through cells, first determining updates, then doing them
    }

    private Collection<Integer> getNeighborStates(int row, int col) {
        Collection<Integer> neighborStates = new ArrayList<Integer>();
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
