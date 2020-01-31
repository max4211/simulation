package simulation;

import java.util.Collection;

public class FireCell extends Cell {

    private double PROB_CATCH;

    public FireCell(double initialState, int x, int y) {
        super(Math.floor(initialState), x, y);
        this.PROB_CATCH = (initialState - Math.floor(initialState))*100;
        createColorMap();
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

