package configuration;

import java.io.IOException;
import java.util.Random;

/**
 * Class to generate Percolation files (specific XML simulation type)
 */
public class PercolationGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 3;

    /**
     * Percolatino constructor to make percolation files
     * @param height of simulation
     * @param width of simulation
     * @throws IOException
     */
    public PercolationGenerator(int height, int width) throws IOException {
        super(height, width, "percolation");
    }

    /**
     *
     * @return type of simulation as string
     */
    @Override
    public String getTypeString(){ return "Percolation"; }

    /**
     *
     * @return neighbor type of sim as string (default to "MOORE")
     */
    @Override
    public String getNeighborString(){ return "MOORE"; }

    /**
     *
     * @return random state between all possible states for this class (3)
     */
    @Override
    public double generateRandomState(){
        return new Random().nextInt(POSSIBLE_STATES);
    }

    /**
     * Main method called to create Percolation files
     * @param args general arguments to main class
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GridGenerator g = new PercolationGenerator(5, 10);
    }
}
