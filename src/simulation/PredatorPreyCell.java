package simulation;

import javafx.scene.paint.Color;

import java.util.Collection;

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
    public int reproductiveAge;

    /**
     * Creates new PredatorPreyCell
     * @param initialState: the integer side is the cell's initial state, the decimal side is the cell's
     * @param row:
     * @param col
     */
    public PredatorPreyCell(double initialState, int row, int col) {

        super(initialState, row, col);

    }

    @Override
    public void createColorMap() {
        myColorMap.put(0.0, Color.BLUE);
        myColorMap.put(1.0, Color.YELLOW);
        myColorMap.put(2.0, Color.GREEN);
    }

    @Override
    public void determineNextState(Collection<Double> neighborStates) {
        for(Double d : neighborStates){

        }
    }

    @Override
    public double mapKey(double myState) {
        return Math.floor(myState);
    }
}
