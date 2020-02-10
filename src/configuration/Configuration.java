package configuration;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import simulation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//TODO: Incorrect file type, incomplete/incorrect formatting, incorrect states

/**
 * Class meant to encapsulate raw data on initial simulation configuration pulled from .xml file.
 * This class is meant to keep the extraction of data from the .xml file separate from the Simulation class.
 * This class assumes files passed in constructor are of type .xml and follow this format:
 *
 * <Configuration>
 *         <height>1</height> // integer
 *         <width>1</width> // integer
 *         <type>Spreading of Fire</type> // String
 *         <neighborType>MOORE</neighborType> // String
 *         <cell>0 0 0</cell> // String in the format "row col state" where state is an integer
 *         <cell>0 1 0</cell>
 * </Configuration>
 *
 * Based on code from https://www.javatpoint.com/how-to-read-xml-file-in-java
 */

public class Configuration {

    // Location of simulation files
    private static final String SIMULATION_FILES = System.getProperty("user.dir") + "/data/";

    // Constants to declare resources location for error codes
    private static final String RESOURCES = "configuration/resources";
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    private static final String LANGUAGE = "English";
    private static final double CELL_DEFAULT_STATE =0.0;
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGE);

    private File myFile;
    private int myHeight;
    private int myWidth;
    private String mySimType;
    private String myNeighborType;
    private List<String> initialCells = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> myGrid = new ArrayList<ArrayList<Cell>>();
    private Simulation mySimulation = new Simulation();

    // Neighborhoods
    private static final String DEFAULT_NEIGHBOR = "MOORE";
    private static final String MOORE = "MOORE";
    private static final String VON_NEUMANN = "VON NEUMANN";
    private static final String HEXAGONAL = "HEXAGONAL";

    //Simulation Types
    private static final String GAME_OF_LIFE = "Game of Life";
    private static final String PERCOLATION = "Percolation";
    private static final String FIRE = "Spreading of Fire";
    private static final String SEGREGATION = "Segregation";
    private static final String PREDATOR_PREY = "Predator Prey";


    /**
     * Constructs a Configuration file to prepare for simulation
     * Uses FileChooser to select a file each time
     */
    public Configuration(){
        myFile = loadFile();
        buildDocument();
        createSimulation();
    }

    private File loadFile() {
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation XML File");
        File dir = new File (SIMULATION_FILES);
        fileChooser.setInitialDirectory(dir);
        File simFile = fileChooser.showOpenDialog(fileStage);
        String extension = getFileExtension(simFile);
        if (extension.equals("xml") || extension.equals("XML")) {
            return simFile;
        } else {
            throw new IllegalArgumentException(myResources.getString("InvalidFile") + simFile);
        }
    }

    private String getFileExtension(File file) {
        if (file == null) {return "";}
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index > 0) { return fileName.substring(index+1); }
        else { return ""; }
    }

    // Helper method meant to create a Document object based on myFile and construct a NodeList of tags of name
    // following the SimulationConfig format
    private void buildDocument(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.myFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("SimulationConfig");
            readTags(nodeList);
        } catch (Exception e) {
            throw new IllegalArgumentException(myResources.getString("InvalidFile") + "\n" + e.getMessage());
        }
    }

    // Helper method meant to assign contents of tags to elements using NodeList constructed in buildDocument()
    private void readTags(NodeList nodeList){
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) node;
            this.myHeight = Integer.parseInt(eElement.getElementsByTagName("height").item(0).getTextContent());
            this.myWidth = Integer.parseInt(eElement.getElementsByTagName("width").item(0).getTextContent());
            this.mySimType = eElement.getElementsByTagName("type").item(0).getTextContent();
            this.myNeighborType = eElement.getElementsByTagName("neighborType").item(0).getTextContent();

            // Initial cell states
            NodeList cellList = eElement.getElementsByTagName("cell");
            // check of list is right length
            if(cellList.getLength() > this.myHeight * this.myWidth){
                throw new ConfigException(String.format(myResources.getString("CellNumMismatch"),
                        cellList.getLength(), this.myHeight * this.myWidth));
            }

            for(int cellItr = 0; cellItr < cellList.getLength(); cellItr++){
                Element initialCell = (Element) cellList.item(cellItr);
                checkValidCellLocation(cellList, cellItr, initialCell);
                initialCells.add(initialCell.getTextContent());
            }

        }
    }

    private void checkValidCellLocation(NodeList cellList, int cellItr, Element initialCell) {
        // check if cell is in bounds
        int initialCellRow = Integer.parseInt(initialCell.getTextContent().split(" ")[0]);
        int initialCellCol = Integer.parseInt(initialCell.getTextContent().split(" ")[1]);
        if(initialCellRow >= this.myHeight || initialCellCol >= this.myWidth){
            throw new ConfigException(String.format(myResources.getString("CellOutOfBounds"),
                    cellItr, initialCellRow, initialCellCol, this.myHeight, this.myWidth));
        }

        // check if cell is a repeat
        for(int cellCheckItr=0; cellCheckItr<cellItr; cellCheckItr++){
            Element checkCell = (Element) cellList.item(cellCheckItr);
            int checkCellRow = Integer.parseInt(checkCell.getTextContent().split(" ")[0]);
            int checkCellCol = Integer.parseInt(checkCell.getTextContent().split(" ")[1]);
            if(initialCellRow == checkCellRow && initialCellCol == checkCellCol){
                throw new ConfigException(String.format(myResources.getString("RepeatedCellCoordinates"),
                        cellCheckItr, cellItr, initialCellRow, initialCellCol));
            }
        }
    }

    private void createSimulation() {
        for(int r = 0; r < myHeight; r++){
            myGrid.add(new ArrayList<Cell>(myWidth));
            for(int c = 0; c < myWidth; c++){
                myGrid.get(r).add(null);
            }
        }
        fillGrid(initialCells);
        createDeltaArrays(myNeighborType);
        mySimulation.setGrid(myGrid);
        mySimulation.setHeight(myHeight);
        mySimulation.setWidth(myWidth);
        mySimulation.setNeighborhood(myNeighborType);
    }

    private void fillGrid(List<String> initialCells) {
        for (String cellString : initialCells) {
            String[] cellData = cellString.split(" ");
            int row = Integer.parseInt(cellData[0]);
            int col = Integer.parseInt(cellData[1]);
            double state = Double.parseDouble(cellData[2]);
            setCell(row, col, createCell(row, col, state));
        }
        // fill all remaining null spaces with cells with a default state
        for(int row=0; row<myHeight; row++){
            for(int col=0; col<myWidth; col++){
                if(myGrid.get(row).get(col) == null){
                    myGrid.get(row).set(col, createCell(row, col, CELL_DEFAULT_STATE));
                }
            }
        }

    }

    private void setCell(int r, int c, Cell cell) {
        myGrid.get(r).set(c, cell);
    }

    private Cell createCell(int row, int col, double state) {
        try {
            switch (mySimType) {
                case (FIRE):
                    return new FireCell(state, row, col);
                case (GAME_OF_LIFE):
                    return new LifeCell(state, row, col);
                case (PERCOLATION):
                    return new PercolationCell(state, row, col);
                case (SEGREGATION):
                    return new SegregationCell(state, row, col, mySimulation);
                case (PREDATOR_PREY):
                    return new PredatorPreyCell(state, row, col);
                default:
                    throw new IllegalArgumentException("Unexpected value: " + mySimType);
            }
        } catch (IllegalStateException e) {
            // TODO: Prompt for valid state in viz
            throw new IllegalArgumentException("Illegal state: " + state);
        }

    }

    private void createDeltaArrays (String neighborType) {
        switch (neighborType) {
            case(MOORE):
                mySimulation.setColDelta(new int[]{-1, -1, 0, 1, 1, 1, 0, -1});
                mySimulation.setRowDelta(new int[]{0, -1, -1, -1, 0, 1, 1, 1});
                break;
            case(VON_NEUMANN):
                mySimulation.setColDelta(new int[]{-1, 0, 1, 0});
                mySimulation.setRowDelta(new int[]{0, -1, 0, 1});
                break;
            case(HEXAGONAL):
                mySimulation.setColDelta(new int[]{-1, -1, 0, 1, 1, 0});
                mySimulation.setRowDelta(new int[]{0, -1, -1, 0, 1, 1});
            default:
                System.out.println("Default neighborhood invalid/not given, defaulting to: " + DEFAULT_NEIGHBOR);
                createDeltaArrays(DEFAULT_NEIGHBOR);
        }
    }

    public Simulation getSimulation() { return mySimulation; }
    public int getHeight(){ return this.myHeight; }
    public int getWidth(){ return this.myWidth; }
    public String getNeighborType(){ return this.myNeighborType; }

}
