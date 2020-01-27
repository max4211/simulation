package simulation;

import java.util.Collection;

public class FireCell extends Cell {

    private double PROB_CATCH;

    public FireCell(double initialState, double probCatch) {
        super(initialState);
        this.PROB_CATCH = probCatch;
    }

    @Override
    public void determineState(Collection<Double> neighborStates) {

    }

    @Override
    public void createColorMap() {

    }

    @Override
    public double mapKey(double myState) {

        return myState;
    }
}

