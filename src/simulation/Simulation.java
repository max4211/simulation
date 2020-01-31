package simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.paint.Color;

public class Simulation {

    private Cell[][] myGrid;
    private int[] ROW_DELTA;
    private int[] COL_DELTA;

    public Simulation(File configFile){
        // Construct myGrid here depending on simulation type
        // Assign neighborhoods based on neighborhood attributes

    }

    public Color[][] getColorGrid() {
        updateGrid();
        return new Color[0][0];
    }

    private void updateGrid() {
        // Determine Cell updates
        // Implement cell updates
    }

    private boolean inBounds(int row, int col) {
        return (row <= myGrid.length) && (col <= myGrid[0].length);
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

}
