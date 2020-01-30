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

    private int height;
    private int width;
    private String simType;
    private String neighborType;
    private List<String> initialCells;

    public DummySimulationConfig(String filename){
        height = 3;
        width = 3;
        simType = "MOORE";
        neighborType = "MOORE";
        String[] cells = {"0 0 "};

        initialCells.addAll(Arrays.asList(cells));
    }


    public Object[] returnData(){
        Object[] ret = {height, width, simType, neighborType, initialCells};
        return ret;
    }
}
