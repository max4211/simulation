package configuration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import simulation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


//TODO: Incorrect file type, incomplete/incorrect formatting, incorrect states

/**
 * Class meant to encapsulate raw data on initial simulation configuration pulled from .xml file.
 * This class is meant to keep the extraction of data from the .xml file separate from the Simulation class.
 * This class assumes files passed in constructor are of type .xml and follow this format:
 *
 * <SimulationConfig>
 *         <height>1</height> // integer
 *         <width>1</width> // integer
 *         <type>Spreading of Fire</type> // String
 *         <neighborType>MOORE</neighborType> // String
 *         <cell>0 0 0</cell> // String in the format "x-coord y-coord state" where state is an integer
 *         <cell>0 1 0</cell>
 * </SimulationConfig>
 *
 * Based on code from https://www.javatpoint.com/how-to-read-xml-file-in-java
 *
 */

public class Configuration {

    private File myFile;
    private int height;
    private int width;
    private String simType;
    private String neighborType;
    private List<String> initialCells = new ArrayList<>();
    private Simulation mySimulation;

    /**
     * Constructs a SimulationConfig class using a File input
     * @param path: an .xml file in format above
     */
    public Configuration(File path){
        myFile = path;
        buildDocument();
        createSimulation();
    }

    public int getHeight(){ return this.height; }
    public int getWidth(){ return this.width; }
    public String getSimType(){ return this.simType; }
    public String getNeighborType(){ return this.neighborType; }
    public List<String> getCellStates(){ return this.initialCells; }

    private void createSimulation() {
        ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();
        for(int i=0; i < height; i++){
            grid.add(new ArrayList<Cell>(width));
        }

        fillGrid(simType, initialCells);
        createDeltaArrays(neighborType);
    }

    private void fillGrid(String simType, List<String> initialCells) {
        for (String cellString : initialCells) {
            String[] cellData = cellString.split(" ");
            int row = Integer.parseInt(cellData[0]);
            int col = Integer.parseInt(cellData[1]);
            double state = Double.parseDouble(cellData[2]);
            Cell newCell = createCell(row, col, state);
            mySimulation.addCellToRow(row, col, newCell);
        }
    }

    private Cell createCell(int row, int col, double state) {
        Cell newCell;
        switch (simType) {
            case ("Spreading of Fire"):
                newCell = new FireCell(state, row, col);
                break;
            case ("Game of Life"):
                newCell = new LifeCell(state, row, col);
                break;
            case ("Percolation"):
                newCell = new PercolationCell(state, row, col);
                break;
            case ("Segregation"):
                newCell = new SegregationCell(state, row, col, mySimulation);
                break;
            case ("Predator Prey"):
                newCell = new PredatorPreyCell(state, row, col);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + simType);
        }
        return newCell;
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
                throw new IllegalStateException("Unexpected value: " + neighborType);
        }
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
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }

    // Helper method meant to assign contents of tags to elements using NodeList constructed in buildDocument()
    private void readTags(NodeList nodeList){
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) node;
            this.height = Integer.parseInt(eElement.getElementsByTagName("height").item(0).getTextContent());
            this.width = Integer.parseInt(eElement.getElementsByTagName("width").item(0).getTextContent());
            this.simType = eElement.getElementsByTagName("type").item(0).getTextContent();
            this.neighborType = eElement.getElementsByTagName("neighborType").item(0).getTextContent();

            // Initial cell states
            NodeList cellList = eElement.getElementsByTagName("cell");
            for(int cellItr = 0; cellItr < cellList.getLength(); cellItr++){
                Element initialCell = (Element) cellList.item(cellItr);
                initialCells.add(initialCell.getTextContent());
            }
        }
    }

    public Simulation getSimulation() {
        return mySimulation;
    }



}
