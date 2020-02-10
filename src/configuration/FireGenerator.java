package configuration;

import java.io.IOException;
import java.util.Random;

/**
 * Extension of general file generator class to create Fire specific simulation XML files
 */
public class FireGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 3;

    public FireGenerator(int height, int width) throws IOException {
        super(height, width, "fire");
    }

    @Override
    public String getTypeString(){ return "Spreading of Fire"; }

    @Override
    public String getNeighborString(){ return "VON NEUMANN"; }

    @Override
    // possible states: 0.0, 1.0, 2.0 plus probCatch ([0, 1.0))
    public double generateRandomState(){
        return new Random().nextInt(POSSIBLE_STATES) + Math.random();
    }

}
