package simulation;

import java.io.IOException;
import java.util.Random;


public class LifeGenerator extends GridGenerator {


    public LifeGenerator(int height, int width) throws IOException {
        super(height, width, "gameoflife");
    }

    @Override
    public String getTypeString(){ return "Game of Life"; }

    @Override
    public String getNeighborString(){ return "MOORE"; }

    @Override
    // possible states: 0.0 or 1.0
    public double generateRandomState(){
        return new Random().nextInt(2);
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new LifeGenerator(100, 100);
    }
}
