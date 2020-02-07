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
 *         <cell>0 0 0</cell> // String in the format "x-coord y-coord state" where state is an integer
 *         <cell>0 1 0</cell>
 * </Configuration>
 *
 * Based on code from https://www.javatpoint.com/how-to-read-xml-file-in-java
 *
 */

public class Configuration {

    // Location of simulation files
    private static final String SIMULATION_FILES = System.getProperty("user.dir") + "/data/";

    // Constants to declare resources location for error codes
    private static final String RESOURCES = "configuration/resources";
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    private static final String LANGUAGE = "English";
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGE);

    private File myFile;
    private int myHeight;
    private int myWidth;
    private String mySimType;
    private String myNeighborType;
    private List<String> initialCells = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> myGrid = new ArrayList<ArrayList<Cell>>();
    private Simulation mySimulation = new Simulation();

    /**
     * Constructs a Configuration file to prepare for simluation
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
            throw new IllegalArgumentException(myResources.getString("InvalidFile"));
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
            for(int cellItr = 0; cellItr < cellList.getLength(); cellItr++){
                Element initialCell = (Element) cellList.item(cellItr);
                initialCells.add(initialCell.getTextContent());
            }
        }
    }

    private void createSimulation() {
        for(int i = 0; i < myHeight; i++){ myGrid.add(new ArrayList<Cell>(myWidth)); }
        fillGrid(initialCells);
        createDeltaArrays(myNeighborType);
        mySimulation.setGrid(myGrid);
        mySimulation.setHeight(myHeight);
        mySimulation.setWidth(myWidth);
    }

    private void fillGrid(List<String> initialCells) {
        for (String cellString : initialCells) {
            String[] cellData = cellString.split(" ");
            int row = Integer.parseInt(cellData[0]);
            int col = Integer.parseInt(cellData[1]);
            double state = Double.parseDouble(cellData[2]);
            addCellToRow(row, col, createCell(row, col, state));
        }
    }

    public void addCellToRow(int r, int c, Cell cell){
        if(myGrid.get(r).size() <= c){ myGrid.get(r).add(cell); }
        else { throw new IndexOutOfBoundsException("Invalid cell order"); }
    }

    private Cell createCell(int row, int col, double state) {
        switch (mySimType) {
            case ("Spreading of Fire"):
                return new FireCell(state, row, col);
            case ("Game of Life"):
                return new LifeCell(state, row, col);
            case ("Percolation"):
                return new PercolationCell(state, row, col);
            case ("Segregation"):
                return new SegregationCell(state, row, col, mySimulation);
            case ("Predator Prey"):
                return new PredatorPreyCell(state, row, col);
            default:
                throw new IllegalArgumentException("Unexpected value: " + mySimType);
        }
    }

    private void createDeltaArrays (String neighborType) {
        switch (neighborType) {
            case("MOORE"):
                mySimulation.setColDelta(new int[]{-1, -1, 0, 1, 1, 1, 0, -1});
                mySimulation.setRowDelta(new int[]{0, -1, -1, -1, 0, 1, 1, 1});
                break;
            case("VON NEUMANN"):
                mySimulation.setColDelta(new int[]{-1, 0, 1, 0});
                mySimulation.setRowDelta(new int[]{0, -1, 0, 1});
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + neighborType);
        }
    }

    public Simulation getSimulation() { return mySimulation; }
    public int getHeight(){ return this.myHeight; }
    public int getWidth(){ return this.myWidth; }
    public String getNeighborType(){ return this.myNeighborType; }

}
