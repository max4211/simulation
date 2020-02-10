package configuration;

import java.io.IOException;
import java.util.Random;

/**
 * Class for XML file generation for Predator Prey cells
 * Used to generate larger files to the Data folder for testing and simulating
 */
public class PredatorPreyGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 3;
    private static final double ENERGY_GAIN = 3;
    private static final double REPRODUCTIVE_AGE = 4;

    /**
     * Predator Prey simulation constructor of specific height and simType
     * @param height of the predator prey grid
     * @param width of the simulatio ngrid
     * @throws IOException
     */
    public PredatorPreyGenerator(int height, int width) throws IOException {
        super(height, width, "predatorprey");
    }

    /**
     *
     * @return type of simulation as String
     */
    @Override
    public String getTypeString() {
        return "Predator Prey";
    }

    /**
     *
     * @return neighborhood type of sim as String (Default to VonNeumann, could be any)
     */
    @Override
    public String getNeighborString() {
        return "VON NEUMANN";
    }

    /**
     * Predator Prey double state encodes information in successive decimal places, per scaling factors
     * @return random state as double
     */
    @Override
    protected double generateRandomState() {
        return new Random().nextInt(POSSIBLE_STATES) + ENERGY_GAIN/1000 + REPRODUCTIVE_AGE/1000000;
    }

    /**
     * Generation main method to create predator prey files for XML simulation
     * @param args general arguments to main method
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GridGenerator g = new PredatorPreyGenerator(30,30);
    }
}
