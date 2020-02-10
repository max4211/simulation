package simulation;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToroidalSimulation extends Simulation {

    public ToroidalSimulation(){
        super();
        EDGE_TYPE = "TOROIDAL";
    }

    public ToroidalSimulation(List<ArrayList<Cell>> grid, int[] rdelta, int[] cdelta){
        super(grid, rdelta, cdelta);
        EDGE_TYPE = "TOROIDAL";
    }

    @Override
    protected Map<Pair<Integer, Integer>, Cell> getNeighbors(int row, int col) {
        Map<Pair<Integer, Integer>, Cell> neighbors = new HashMap<>();
        int r2; int c2;
        for (int i = 0; i < ROW_DELTA.length; i ++) {
            r2 = row + ROW_DELTA[i]; c2 = col + COL_DELTA[i];
            if(inBounds(r2, c2)){
                neighbors.put(new Pair(r2, c2), getCell(r2, c2));
            }
            else{
                int[] toroidalNeighbor = getToroidalNeighbor(row, col, ROW_DELTA[i], COL_DELTA[i]);
                neighbors.put(new Pair(toroidalNeighbor[0], toroidalNeighbor[1]), getCell(toroidalNeighbor[0], toroidalNeighbor[1]));
            }
        }
        return neighbors;
    }

    private int[] getToroidalNeighbor(int row, int col, int rowDelta, int colDelta){
        int r2; int c2;
        if(row + rowDelta < 0)
            r2 = SIMULATION_HEIGHT - 1;
        else if(row + rowDelta >= SIMULATION_HEIGHT)
            r2 = 0;
        else
            r2 = row + rowDelta;

        if(col + colDelta < 0)
            c2 = SIMULATION_WIDTH - 1;
        else if(col + colDelta >= SIMULATION_WIDTH)
            c2 = 0;
        else
            c2 = col + colDelta;

        return new int[]{r2, c2};
    }

}
