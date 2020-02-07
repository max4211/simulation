package configuration;

import java.io.IOException;
import java.util.Random;


public class FireGenerator extends GridGenerator {

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
        return new Random().nextInt(3) + Math.random();
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new FireGenerator(10, 10);
    }
}
