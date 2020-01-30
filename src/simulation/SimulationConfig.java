package simulation;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class meant to encapsulate raw data on initial simulation configuration pulled from .xml file.
 * This class is meant to keep the extraction of data from the .xml file separate from the Simulation class.
 * This class assumes files passed in constructor are of type .xml and follow this format:
 *
 * <SimulationConfig>
 *         <height>1</height> // integer
 *         <width>1</width> // integer
 *         <type>DummyCell</type> // String
 *         <neighborType>Moore</neighborType> // String
 *         <cell>0 0 0</cell> // String in the format "x-coord y-coord state" where state is an integer
 *         <cell>0 1 0</cell>
 * </SimulationConfig>
 *
 * Based on code from https://www.javatpoint.com/how-to-read-xml-file-in-java
 *
 *
 */

public class SimulationConfig {

    private File myFile;
    private int height;
    private int width;
    private String simType;
    private String neighborType;
    private List<String> initialCells = new ArrayList<>();

    public SimulationConfig(File path){
        myFile = path;
        buildDocument();
    }

    public int getHeight(){ return this.height; }
    public int getWidth(){ return this.width; }
    public String getSimType(){ return this.simType; }
    public String getNeighborType(){ return this.neighborType; }
    public List<String> getCellStates(){ return this.initialCells; }

    // Helper method meant to create a Document object based on myFile and construct a NodeList of tags of name
    // following the SimulationConfig format
    private void buildDocument(){
        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.myFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("SimulationConfig");
            readTags(nodeList);
        }
        catch (Exception e) {
            e.printStackTrace();
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


    public static void main(String[] args){
        SimulationConfig s = new SimulationConfig(new File("data/simulation_sample.xml"));
        System.out.println(s.getHeight());
        System.out.println(s.getWidth());
        System.out.println(s.getSimType());
        System.out.println(s.getNeighborType());
        for(String state: s.getCellStates()){
            System.out.print(state);
        }
    }
}
