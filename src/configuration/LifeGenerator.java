package configuration;

import java.io.IOException;
import java.util.Random;


/**
 * XML File generator extension of main generator for Game of Life Cells
 */
public class LifeGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 2;

    /**
     * Life generator constructor to create specific simtype
     * @param height of the simulation
     * @param width of the ismualtion
     * @throws IOException
     */
    public LifeGenerator(int height, int width) throws IOException {
        super(height, width, "gameoflife");
    }

    /**
     *
     * @return simultion type as string
     */
    @Override
    public String getTypeString(){ return "Game of Life"; }

    /**
     *
     * @return neighborhood type as String
     */
    @Override
    public String getNeighborString(){ return "MOORE"; }

    /**
     *
     * @return random state for values (currently 0 or 1)
     */
    @Override
    // possible states: 0.0 or 1.0
    protected double generateRandomState(){
        return new Random().nextInt(POSSIBLE_STATES);
    }

    /**
     * Main method to create the Life XML files
     * @param args arguments to the main method for LifeGenerator construction
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GridGenerator g = new LifeGenerator(10, 10);
    }
}
