package simulation;

import javafx.scene.paint.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;



public class Simulation {

    private Cell[][] myGrid;
    private int[] ROW_DELTA;
    private int[] COL_DELTA;


    /**
     * Constructs myGrid depending on the simulation type and according to the
     * information from SimulationConfig. Also builds ROW_DELTA and COL_DELTA.
     *
     * @param configFile the .XML file to build the simulation from
     * @throws Exception if simType or neighborType are unexpected values
     */
    public Simulation(File configFile) throws Exception {
        // get data from the SimulationConfig class
        // ~~~~~ this chunk should be changed when I get a working SimulationConfig ~~~~~
        SimulationConfig simCon  = new SimulationConfig(configFile);

        int height = simCon.getHeight();
        int width = simCon.getWidth();
        String simType = simCon.getSimType();
        String neighborType = simCon.getNeighborType();
        List<String> initialCells = simCon.getCellStates();
        // for SimulationConfig() use getter methods
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // loop through initialCells and fill myGrid
        myGrid = new Cell[height][width];
        fillGrid(simType, initialCells);
        createDeltaArrays(neighborType);
    }

    private void fillGrid(String simType, List<String> initialCells) throws Exception {
        for (String cellString : initialCells){
            String[] cellData = cellString.split(" ");

            int row = Integer.parseInt(cellData[0]);
            int col = Integer.parseInt(cellData[1]);
            float state = Float.parseFloat(cellData[2]);

            Cell newCell;
            if(simType.equals("Spreading of Fire")) {
                newCell = new FireCell(state, row, col);
            } else if(simType.equals("Game of Life")){
                newCell = new LifeCell(state, row, col);
            } else throw new Exception("Simulation Type Not Accepted");
            myGrid[row][col] = newCell;
        }
    }

    private void createDeltaArrays(String neighborType) throws Exception {
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
        for(int row=0; row<myGrid.length; row++){
            for(int col=0; col<myGrid[0].length; col++){
                myGrid[row][col].determineState(getNeighborStates(row, col));
            }
        }

        // Implement cell updates
        for(int row=0; row<myGrid.length; row++){
            for(int col=0; col<myGrid[0].length; col++){
                myGrid[row][col].updateState();
            }
        }

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
        Simulation sim = new Simulation(new File("data/simulation_sample.xml"));

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
