package simulation;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;

public class SimulationConfig {

    private File myFile;
    private int height;
    private int width;
    private String simType;
    private String neighborType;
    private List<String> initialCells;

    public SimulationConfig(File path){
        //myFile = path;
        myFile = new File("data/simulation_sample.xml");
        buildDocument();
    }

    private void buildDocument(){
        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.myFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("SimulationConfig");
            // nodeList is not iterable, so we are using for loop
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readTags(NodeList nodeList){
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
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
    }


    public static void main(String[] args){


    }
}
