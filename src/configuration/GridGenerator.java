package configuration;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Based on LevelGenerator.java by
 * @author Max Smith
 */
public abstract class GridGenerator {


    protected final int UNIQUE_ID =  new Random().nextInt(100) + 1;;
    protected String SIM_TYPE;
    protected int myHeight;
    protected int myWidth;

    public GridGenerator(int height, int width, String simType) throws IOException {
        myHeight = height;
        myWidth = width;
        SIM_TYPE = simType;
    }

    public abstract String getTypeString();
    public abstract String getNeighborString();
    public abstract double generateRandomState();

    public void createFile() throws IOException {
        PrintWriter writer = new PrintWriter(nameFile());
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<SimulationConfig>");
        writer.printf("\t<height>%d</height>\n\t<width>%d</width>\n", myHeight, myWidth);
        writer.printf("\t<type>%s</type>\n\t<neighborType>%s</neighborType>\n", getTypeString(), getNeighborString());
        generateCells(writer);
        writer.println("</SimulationConfig>");
        writer.close();
    }

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
