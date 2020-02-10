package simulation;


import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 *  This class represents a cell in a Schelling's Model of Segregation simulation.
 *
 * States:
 * 0 = empty (white)
 * 1 = race 1 (red)
 * 2 = race 2 (blue)
 *
 *  Rules:
 *  If the percentage of cells around this cell that are a different race is greater than the cell's
 *  myPercentTolerance, it is unsatisfied.
 *  If a cell is unsatisfied it can move to an empty cell
 *  Else, a cell is satisfied and it stays still
 *
 * Full rules explained at:
 * https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/mccown-schelling-model-segregation/
 */
public class SegregationCell extends Cell{
    private Simulation myCurrentSim;
    private boolean changedAlready = false;
    private final double BOUNDARY_OF_POSSIBLE_STATES = 3.0;

    /**
     * Creates a new SegregationCell
     * @param initialState: the state; the integer part is the RACE and everything to the right of the decimal
     *                    is the PERCENT TOLERANCE
     * @param row: x position
     * @param col: y position
     */
    public SegregationCell(double initialState, int row, int col, Simulation sim){
        super(initialState, row, col);
        myCurrentSim = sim;
        myTypeString = "Segregation";
    }

    public double getPercentTolerance(){ return myState - Math.floor(myState); }

    public boolean isOccupied(){
        return Math.floor(myState) != 0;
    }

    @Override
    protected boolean checkValidState(double initialState) {
        return initialState < BOUNDARY_OF_POSSIBLE_STATES;
    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, WHITE);
        myColorMap.put(1.0, RED);
        myColorMap.put(2.0, BLUE);
    }

    @Override
    public void createStateMap() {
        myStateMap.put(0.0, new State("Open", WHITE));
        myStateMap.put(1.0, new State("Red", RED));
        myStateMap.put(2.0, new State("Blue", BLUE));
    }

    //TODO: fix if-else tree
    @Override
    public void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors) {
        if(! changedAlready){
            if(Math.floor(myState)!=0){
                double likeNeighbors = 0; double totalNeighbors = 0;
                for (Cell n : neighbors.values()){
                    double neighborState = Math.floor(n.getState());
                    if(neighborState != 0){
                        totalNeighbors++;
                        if (neighborState == Math.floor(myState)) likeNeighbors++;
                    }
                }
                if(totalNeighbors > 0 && likeNeighbors/totalNeighbors < getPercentTolerance())
                    findVacantCell();
                else
                    nextState = myState;
            }
            else{
                nextState = myState;
            }
        }
    }

    @Override
    public double mapKey(double myState) {
        return Math.floor(myState);
    }

    @Override
    public void setNextState(double state){
        this.nextState = state;
        changedAlready = true;
    }

    @Override
    public void updateState() {
        this.myState = this.nextState;
        changedAlready = false;
    }

    @Override
    public String getTypeString(){ return "Segregation"; }


    private void findVacantCell(){
        int height = myCurrentSim.getHeight();
        int width = myCurrentSim.getWidth();
        boolean foundEmpty = false;
        while(!foundEmpty){
            int randRow = new Random().nextInt(height);
            int randCol = new Random().nextInt(width);
            if(Math.floor(myCurrentSim.getCell(randRow, randCol).getNextState())==0 &&
                    Math.floor(myCurrentSim.getCell(randRow, randCol).getState())==0 &&
                    !(randRow == myRow && randCol == myCol)){
                foundEmpty = true;
                moveTo(randRow, randCol);
            }
        }
    }

    private void moveTo(int r, int c){
        // Switch states by setting cell being moved to's next state
        myCurrentSim.getCell(r, c).setNextState(myState);

        // Now empty
        this.nextState = 0;
    }
}
