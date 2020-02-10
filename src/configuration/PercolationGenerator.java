package configuration;

import java.io.IOException;
import java.util.Random;


public class PercolationGenerator extends GridGenerator {

    private static final int POSSIBLE_STATES = 3;

    public PercolationGenerator(int height, int width) throws IOException {
        super(height, width, "percolation");
    }

    @Override
    public String getTypeString(){ return "Percolation"; }

    @Override
    public String getNeighborString(){ return "MOORE"; }

    @Override
    protected double generateRandomState(){
        return new Random().nextInt(POSSIBLE_STATES);
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new PercolationGenerator(5, 10);
    }
}
