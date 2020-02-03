package simulation;

import java.util.Collection;

public class PredatorPreyCell extends Cell{
    public PredatorPreyCell(double initialState, int row, int col) {
        super(initialState, row, col);

    }

    @Override
    public void createColorMap() {

    }

    @Override
    public void determineNextState(Collection<Double> neighborStates) {

    }

    @Override
    public double mapKey(double myState) {
        return 0;
    }
}
