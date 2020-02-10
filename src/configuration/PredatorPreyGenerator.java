package configuration;

import java.io.IOException;
import java.util.Random;

public class PredatorPreyGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 3;
    private static final double ENERGY_GAIN = 3;
    private static final double REPRODUCTIVE_AGE = 4;

    public PredatorPreyGenerator(int height, int width) throws IOException {
        super(height, width, "predatorprey");
    }

    @Override
    public String getTypeString() {
        return "Predator Prey";
    }

    @Override
    public String getNeighborString() {
        return "VON NEUMANN";
    }

    @Override
    public double generateRandomState() {
        return new Random().nextInt(POSSIBLE_STATES) + ENERGY_GAIN/1000 + REPRODUCTIVE_AGE/1000000;
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new PredatorPreyGenerator(30,30);
    }
}
