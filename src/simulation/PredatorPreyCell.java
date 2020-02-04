package simulation;

import javafx.scene.paint.Color;

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
    // TODO: initialize these variables
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
        energyGainFromFish= (int) Math.floor((initialState - Math.floor(initialState)) * 1000);
        reproductiveAge = (int) (initialState*1000 - Math.floor(initialState*1000) * 1000);
        energy = energyGainFromFish;
        age = 0;


    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, Color.BLUE);
        myColorMap.put(1.0, Color.YELLOW);
        myColorMap.put(2.0, Color.GREEN);
    }

    @Override
    public void determineNextState(Collection<Cell> neighbors) {

        // for sharks
        if(Math.floor(myState) == 1) {
            // make subsets of cells that are fish and empty
            ArrayList<Cell> fishCellSublist = new ArrayList<>();
            for (Cell cell : neighbors) {
                if (Math.floor(cell.getState()) == 2) fishCellSublist.add(cell);
            }
            ArrayList<Cell> emptyCellSublist = new ArrayList<>();
            for (Cell cell : neighbors) {
                if (Math.floor(cell.getState()) == 0) emptyCellSublist.add(cell);
            }
            // if you're out of energy die :(
            if(energy <= 0){
                nextState = 0;
            }
            // otherwise try to move
            // if there are fish, move into a random fish's cell and eat it
            else if(fishCellSublist.size()>0){
                PredatorPreyCell cellPicked = pickRandomEntry(fishCellSublist);
                if(Math.floor(cellPicked.nextState) == 2) energy += energyGainFromFish;
                if(age >= reproductiveAge) age=0;
                else nextState = 0;
                moveInfoInto(cellPicked);
                pickRandomEntry(fishCellSublist);
            }

            // if there are no fish, move into a random empty cell
            else if(emptyCellSublist.size()>0){
                if(age >= reproductiveAge) age=0;
                else nextState = 0;
                PredatorPreyCell cellPicked = pickRandomEntry(emptyCellSublist);
                moveInfoInto(cellPicked);
            }

            // if there is no where to move just update energy and age
            else{
                nextEnergy = energy -1;
                nextAge = age -1;
            }
        }

        // for fish
        if (Math.floor(myState) == 1){
            // make sublist of empty cells
            ArrayList<Cell> emptyCellSublist = new ArrayList<>();
            for (Cell cell : neighbors) {
                if (Math.floor(cell.getState()) == 0) emptyCellSublist.add(cell);
            }

            // if there is space move into a random empty cell
            if(emptyCellSublist.size()>0){
                if(age >= reproductiveAge) age=0;
                else nextState = 0;
                PredatorPreyCell cellPicked = pickRandomEntry(emptyCellSublist);
                moveInfoInto(cellPicked);
            }

            // if there is no where to move just update energy and age
            else{
                nextEnergy = energy -1;
                nextAge = age -1;
            }
        }
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
     * picks a random cell from the list and copies this cells information into it
     * returns the
     * @param dest
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
}
