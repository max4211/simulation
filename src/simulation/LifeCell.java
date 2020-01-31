package simulation;

import javafx.scene.paint.Color;

import java.util.Collection;

/**
 *  0 == dead
 *  1 == alive
 */
public class LifeCell extends Cell {

    public LifeCell(double initialState, int x, int y) {
       super(initialState, x, y);
    }

    @Override
    public void createColorMap(){
        this.myColorMap.put(0.0, Color.WHITE);
        this.myColorMap.put(1.0, Color.BLACK);
    }

    @Override
    public void determineState(Collection<Double> neighborStates){
        int numAlive = 0;
        for(double state: neighborStates){
            numAlive += (int) state;
        }

        // Currently alive
        if(myState==1){
            if(numAlive == 2 || numAlive == 3)
                nextState = 1;
            else
                nextState = 0;
        }
        else{
            if(numAlive == 3)
                nextState = 1;
            else
                nextState = 0;
        }
    }

    // unsure what type of state to double mapping we should do here?
    @Override
    public double mapKey(double myState){
        return myState;
    }

}
