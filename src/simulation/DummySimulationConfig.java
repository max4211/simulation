package simulation;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummySimulationConfig {

    public int height;
    public int width;
    public String simType;
    public String neighborType;
    public List<String> initialCells;

    public DummySimulationConfig(File configFile){
        height = 3;
        width = 3;
        simType = "Spreading of Fire";
        neighborType = "MOORE";
        String[] cells = {"0 0 1", "0 1 1", "0 2 1",
                          "1 0 1", "1 1 2", "1 2 1",
                          "2 0 1", "2 1 1", "2 2 1",};

        initialCells = new ArrayList<>(Arrays.asList(cells));
    }


    public Object[] returnData(){
        Object[] ret = {height, width, simType, neighborType, initialCells};
        return ret;
    }
}
