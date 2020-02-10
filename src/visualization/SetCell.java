package visualization;

import javafx.scene.control.ToggleGroup;
import simulation.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper Object for the SimPane which helps generate the toggle buttons to be created for new cell state selection
 */
public class SetCell {

    private final ToggleGroup group = new ToggleGroup();
    private List<CustomToggle> myList;

    /**
     * SetCell constructor, to create a group of ToggleButtonos for cell state selection
     * @param myMap of all possible state values the cell can contain
     */
    public SetCell(Map<Double, State> myMap) {
        createToggle(myMap);
    }


    private void createToggle(Map<Double, State> myMap) {
        myList = new ArrayList<CustomToggle>();
        State s;
        for (double d: myMap.keySet()) {
            s = myMap.get(d);
            myList.add(new CustomToggle(s.getString(), group, s.getColor(), d));
        }
    }

    /**
     * Called by Visualization to populate the HBox that shows toggle options for new cell states
     * @return list of all ToggleButtons, to be populated in the Hbox that shows toggle options
     */
    public List<CustomToggle> getList() {
        return this.myList;
    }

}
