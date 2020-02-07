package configuration;

import java.io.IOException;
import java.util.Random;


public class PercolationGenerator extends GridGenerator {


    public PercolationGenerator(int height, int width) throws IOException {
        super(height, width, "percolation");
    }

    @Override
    public String getTypeString(){ return "Percolation"; }

    @Override
    public String getNeighborString(){ return "MOORE"; }

    @Override
    // possible states: 0.0, 1.0, 2.0
    public double generateRandomState(){
        return new Random().nextInt(3);
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new PercolationGenerator(5, 10);
    }
}
