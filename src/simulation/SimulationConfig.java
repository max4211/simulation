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

    private int height;
    private int width;
    private String simType;
    private String neighborType;
    private List<String> initialCells;


    public static void main(String[] args){
        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File("data/simulation_sample.xml");
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("SimulationConfig");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    int height = Integer.parseInt(eElement.getElementsByTagName("height").item(0).getTextContent());
                    int width = Integer.parseInt(eElement.getElementsByTagName("width").item(0).getTextContent());
                    String cellType = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String neighborType = eElement.getElementsByTagName("neighborType").item(0).getTextContent();
                    System.out.println(height + " " + width + " " + cellType + " " + neighborType);

                    // All initial cell states
                    NodeList cellList = eElement.getElementsByTagName("cell");
                    for(int cellItr = 0; cellItr < cellList.getLength(); cellItr++){
                        Element initialCell = (Element) cellList.item(cellItr);
                        System.out.println(initialCell.);

                    }



                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
