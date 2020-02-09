package configuration;

import simulation.Simulation;

import java.io.IOException;
import java.io.PrintWriter;


public class SimulationSaver extends GridGenerator{

    private Simulation mySimulation;
    private String myNeighborType;

    //TODO: handle exception
    public SimulationSaver(Simulation s) throws IOException {
        super(s.getHeight(), s.getWidth(), s.getCell(0,0).getTypeString());
        mySimulation = s;
        myNeighborType = s.getNeighborhood();
    }

    @Override
    public String getTypeString(){ return SIM_TYPE; }

    @Override
    public String getNeighborString(){ return myNeighborType; }

    @Override
    public double generateRandomState(){ return 0.0; }

    @Override
    protected void generateCells(PrintWriter w){
        for(int r = 0; r < myHeight; r++){
            for(int c = 0; c < myWidth; c++){
                w.printf("\t<cell>%d %d %f</cell>\n", r, c, mySimulation.getCell(r, c).getState());
            }
        }
    }




}
