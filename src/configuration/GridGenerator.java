package configuration;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Abstract class to help generate large XML files for Simulation running
 * Based on LevelGenerator.java by
 * @author James Rumsey, Max Smith
 */
public abstract class GridGenerator {


    protected final static int UNIQUE_ID =  new Random().nextInt(100) + 1;;
    protected String SIM_TYPE;
    protected String myEdgeType;
    protected int myHeight;
    protected int myWidth;

    /**
     * GridGenerator basic constructor to create Simulation XML files
     * @param height of the Simulation to construct
     * @param width of the Simulation to construct
     * @param simType determines which cells to make (and rules of update)
     * @throws IOException
     */
    public GridGenerator(int height, int width, String simType) throws IOException {
        myHeight = height;
        myWidth = width;
        SIM_TYPE = simType;
        myEdgeType = "HARD";
    }

    /**
     * GridGenerator advanced constructor to create Simulation XML files, includes edgeType creation
     * @param height of the Simulation to construct
     * @param width of the Simulation to construct
     * @param simType determines which cells to make (and rules of update)
     * @throws IOException
     */
    public GridGenerator(int height, int width, String simType, String edgeType) throws IOException {
        myHeight = height;
        myWidth = width;
        SIM_TYPE = simType;
        myEdgeType = edgeType;
    }

    /**
     * Creates an XML file in the correct format to be loaded in Visualization
     * @throws IOException if a file is incorrectly loaded
     */
    public void createFile() throws IOException {
        PrintWriter writer = new PrintWriter(nameFile());
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<SimulationConfig>");
        writer.printf("\t<height>%d</height>\n\t<width>%d</width>\n", myHeight, myWidth);
        writer.printf("\t<type>%s</type>\n\t<neighborType>%s</neighborType>\n", getTypeString(), getNeighborString());
        writer.printf("\t<edgeType>%s</edgeType>\n", myEdgeType);
        generateCells(writer);
        writer.println("</SimulationConfig>");
        writer.close();
    }

    /**
     * @return Type of simulation as a one-word String
     */
    protected abstract String getTypeString();

    /**
     * @return Type of neighborhood as a one-word String
     */
    protected abstract String getNeighborString();

    /**
     * Generates a random cell state
     * @return A random state as a double valid for simulation type
     */
    protected abstract double generateRandomState();

    protected String nameFile() throws IOException {
        String fileName = "data/" + SIM_TYPE + UNIQUE_ID + ".xml";
        File file = new File(fileName);
        file.createNewFile();
        return fileName;
    }

    protected void generateCells(PrintWriter w){
        for(int r = 0; r < myHeight; r++){
            for(int c = 0; c < myWidth; c++){
                w.printf("\t<cell>%d %d %f</cell>\n", r, c, generateRandomState());
            }
        }
    }

}
