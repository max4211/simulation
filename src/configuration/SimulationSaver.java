package configuration;

import simulation.Simulation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: test this with viz
public class SimulationSaver extends GridGenerator{

    private Simulation mySimulation;
    private String myNeighborType;

    public SimulationSaver(Simulation s) throws IOException {
        super(s.getHeight(), s.getWidth(), "running");
        mySimulation = s;
        myNeighborType = s.getNeighborhood();
    }

    @Override
    public String getTypeString(){ return mySimulation.getCell(0,0).getTypeString(); }


    @Override
    public String getNeighborString(){ return myNeighborType; }

    @Override
    protected double generateRandomState(){ return 0.0; }

    @Override
    protected String getEdgeType(){ return mySimulation.getEdgeType(); }

    @Override
    protected void generateCells(PrintWriter w){
        for(int r = 0; r < myHeight; r++){
            for(int c = 0; c < myWidth; c++){
                w.printf("\t<cell>%d %d %f</cell>\n", r, c, mySimulation.getCell(r, c).getState());
            }
        }
    }

    @Override
    protected String nameFile() throws IOException {
        String fileName = "data/" + getTime() + ".xml";
        File file = new File(fileName);
        file.createNewFile();
        return fileName;
    }

    private String getTime(){
        return new SimpleDateFormat("MM-dd-HH-mm").format(new Date());
    }
}
