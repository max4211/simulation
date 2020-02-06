package simulation;

import java.io.IOException;
import java.util.Random;

public class PredatorPreyGenerator extends GridGenerator{

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
        return new Random().nextInt(3) + 0.003004;
    }

    public static void main(String[] args) throws IOException {
        GridGenerator g = new PredatorPreyGenerator(30,30);
    }
}
