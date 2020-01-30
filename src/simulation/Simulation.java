package simulation;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Simulation {

    private Cell[][] myGrid;
    private int[] ROW_DELTA;
    private int[] COL_DELTA;

    public Simulation(File configFile) throws Exception {
        // Construct myGrid here depending on simulation type
        // Assign neighborhoods based on neighborhood attributes

        // get data from the SimulationConfig class
        // ~~~~~ this chunk should be changed when I get a working SimulationConfig ~~~~~
        DummySimulationConfig simCon  = new DummySimulationConfig(configFile);

        int height = simCon.height;
        int width = simCon.width;
        String simType = simCon.simType;
        String neighborType = simCon.neighborType;
        List<String> initialCells = simCon.initialCells;
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // loop through initialCells and fill myGrid
        myGrid = new Cell[height][width];
        for (String cellString : initialCells){
            String[] cellData = cellString.split(" ");

            int x = Integer.parseInt(cellData[0]);
            int y = Integer.parseInt(cellData[1]);
            float state = Float.parseFloat(cellData[2]);

            Cell newCell;
            if(simType.equals("Spreading of Fire")) {
                newCell = new FireCell(state, x, y, 10);    // <- we need to discuss how probCatch will get here
            }else throw new Exception("Simulation Type Not Accepted");
            myGrid[x][y] = newCell;
        }

        // create set ROW_DELTA and COL_DELTA based on neighborType
        if(neighborType.equals("MOORE")){
            COL_DELTA = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};
            ROW_DELTA = new int[]{0, -1, -1, -1, 0, 1, 1, 1};
        }
        else if(neighborType.equals("VON NEUMANN")){
            COL_DELTA = new int[]{-1, 0, 1, 0};
            ROW_DELTA = new int[]{0, -1, 0, 1};
        }
        else throw new Exception("Neighborhood Type Not Accepted");
    }

    public Color[][] getColorGrid() {
        updateGrid();
        Color[][] colorGrid = new Color[myGrid.length][myGrid[0].length];
        for(int row=0; row<colorGrid.length; row++){
            for(int col=0; col<colorGrid.length; col++){
                colorGrid[row][col] = myGrid[row][col].getColor();
            }
        }
        return colorGrid;
    }

    private void updateGrid() {
        // Determine Cell updates
        // Implement cell updates

    }


    private boolean inBounds(int row, int col) {
        return (row <= myGrid.length) && (col <= myGrid[0].length)
                && (row >= 0) && (col >= 0);
    }


    /**
     * @param row row cell whose neighbors are being requested
     * @param col column cell whose neighbors are being requested
     * @return list of neighbors states in order of ROW_DELTA and COL_DELTA
     */
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

    // this main method is here for testing - not supposed to be part of the final build
    public static void main(String[] args) throws Exception {
        // filename doesn't matter for now because the program never actually uses it
        Simulation sim = new Simulation(new File("blah blah blah"));

        // check all the values
        System.out.println("Grid:");
        for(int row=0; row<sim.myGrid.length; row++){
            System.out.print("[");
            for(int col=0; col<sim.myGrid[0].length; col++){
                System.out.print(" " + sim.myGrid[row][col].getState() + ",");
            }
            System.out.println(" ]");
        }

        System.out.println();
        System.out.println("Row Delta:  "+ Arrays.toString(sim.ROW_DELTA));
        System.out.println("Col Delta:  "+ Arrays.toString(sim.COL_DELTA));
    }

}
