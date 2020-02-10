package simulation;


import javafx.util.Pair;

import java.util.*;

/**
 *  This class represents a cell in a Preditor-Prey simulation.
 *
 * States:
 * 0 = empty (blue)
 * 1 = shark (yellow)
 * 2 = fish (green)
 *
 *  Rules:
 *  For the fish
 *  At each step, a fish moves randomly to one of the adjacent unoccupied squares. If there are no free squares,
 *  no movement takes place.
 *  Once a fish has survived a certain number of steps it may reproduce. This is done as it moves to a
 *  neighbouring square, leaving behind a new fish in its old position. Its reproduction time is also reset to
 *  zero.
 *
 *  For the sharks
 *  At each step, a shark moves randomly to an adjacent square occupied by a fish. If there is none, the shark
 *  moves to a random adjacent unoccupied square. If there are no free squares, no movement takes place.
 *  At each step, each shark is deprived of a unit of energy.
 *  Upon reaching zero energy, a shark dies.
 *  If a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of energy.
 *  Once a shark has survived a certain number of steps it may reproduce in exactly the same way as the fish.
 *
 *  Full rules explained at:
 *  https://en.wikipedia.org/wiki/Wa-Tor#For_the_sharks
 * */
public class PredatorPreyCell extends Cell{
    // TODO: change to PRIVATE
    public int reproductiveAge;
    public int energyGainFromFish;
    public int energy;
    public int age;

    public int nextReproductiveAge;
    public int nextEnergyGainFromFish;
    public int nextEnergy;
    public int nextAge;

    /**
     * Creates new PredatorPreyCell
     * @param initialState: the integer side is the cell's initial state (fish, shark, or water), the first three
     *                    numbers to the right of the decimal is the energy gained per fish eaten, the second three
     *                    are the age needed to reproduce.
     *                          __ /.__ __ __ /__ __ __
     *                          ^       ^          ^
     *                      state   energy gain   reproductive age
     *
     * @param row: cell's row
     * @param col: cell's column
     */
    public PredatorPreyCell(double initialState, int row, int col) {

        super(initialState, row, col);
        energyGainFromFish = (int) Math.floor((initialState - Math.floor(initialState)) * 1000);
        reproductiveAge = (int) (myState *1000000)%1000;
        energy = energyGainFromFish;
        age = 0;

        nextReproductiveAge = reproductiveAge;
        nextEnergyGainFromFish = energyGainFromFish;
        nextEnergy = energyGainFromFish;
        nextAge = age;
        myTypeString = "Predator Prey";

    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, BLUE);
        myColorMap.put(1.0, YELLOW);
        myColorMap.put(2.0, GREEN);
    }

    @Override
    public void createStateMap() {
        myStateMap.put(0.0, new State("Water", BLUE));
        myStateMap.put(1.0, new State("Shark", YELLOW));
        myStateMap.put(2.0, new State("Fish", GREEN));
    }

    @Override
    public void determineNextState(Map<Pair<Integer, Integer>, Cell> neighbors) {
        // for sharks
        if(Math.floor(myState) == 1) {
            // make subsets of cells that are fish and empty
            ArrayList<Cell> fishCellSublist = new ArrayList<>();
            ArrayList<Cell> emptyCellSublist = new ArrayList<>();
            for (Cell cell : neighbors.values()) {
                if (Math.floor(cell.getState()) == 2) fishCellSublist.add(cell);
                if (Math.floor(cell.getState()) == 0 && cell.nextState==0) emptyCellSublist.add(cell);
            }

            // if you're out of energy, die :(
            if(energy <= 0){
                nextState = 0;
            }
            // otherwise try to move
            // if there are fish, move into a random fish's cell
            else if(fishCellSublist.size()>0){
                moveIntoRandomTryToReproduce(fishCellSublist);
            }
            // if there are no fish, move into a random empty cell
            else if(emptyCellSublist.size()>0){
                moveIntoRandomTryToReproduce(emptyCellSublist);
            }
            // if there is no where to move just update energy and age
            else{
                nextEnergy = energy -1;
                nextAge = age + 1;
            }
        }

        // for fish
        if (Math.floor(myState) == 2){
            // make sublist of empty cells
            ArrayList<Cell> emptyCellSublist = new ArrayList<>();
            for (Cell cell : neighbors.values()) {
                if (Math.floor(cell.getState()) == 0 && cell.nextState==0) emptyCellSublist.add(cell);
            }

            // if there is space move into a random empty cell
            if(emptyCellSublist.size()>0){
                moveIntoRandomTryToReproduce(emptyCellSublist);
            }

            // if there is no where to move just update energy and age
            else{
                nextAge = age + 1;
            }
        }
    }

    private void moveIntoRandomTryToReproduce(ArrayList<Cell> fishCellSublist) {
        PredatorPreyCell cellPicked = pickRandomEntry(fishCellSublist);
        if(Math.floor(cellPicked.getState()) == 2 && Math.floor(cellPicked.nextState) == 2) energy += energyGainFromFish;
        if(age >= reproductiveAge) {
            age = 0;
            nextState = myState;
            nextAge = 0;
            nextEnergy = energyGainFromFish;
        }
        else{
            nextState = 0;
            nextAge = 0;
            nextEnergy = 0;
        }
        moveInfoInto(cellPicked);
    }

    @Override
    public void updateState() {
        this.myState = this.nextState;
        this.age = this.nextAge;
        this.energy = this.nextEnergy;
        this.reproductiveAge = this.nextReproductiveAge;
        this.energyGainFromFish = this.nextEnergyGainFromFish;
    }

    private PredatorPreyCell pickRandomEntry(ArrayList<Cell> sublist) {
        Random r = new Random();
        return (PredatorPreyCell) sublist.get(r.nextInt(sublist.size()));
    }

    /**
     * copies the information from this instance into the next values for the given cell
     * @param dest: cell to be overridden
     */
    private void moveInfoInto(PredatorPreyCell dest) {
        dest.nextState = myState;
        dest.nextReproductiveAge = reproductiveAge;
        dest.nextEnergy = energy - 1;
        dest.nextEnergyGainFromFish = energyGainFromFish;
        dest.nextAge = age + 1;
    }

    @Override
    public double mapKey(double myState) {
        return Math.floor(myState);
    }

    @Override
    public String getTypeString(){ return "Predator Prey"; }
}
