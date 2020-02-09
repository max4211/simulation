package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import simulation.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetCell {

    private final ToggleGroup group = new ToggleGroup();
    private List<CustomToggle> myList;

    public SetCell(Map<Double, State> myMap) {
        createToggle(myMap);
    }

    private void createToggle(Map<Double, State> myMap) {
        myList = new ArrayList<CustomToggle>();
        State s;
        for (double d: myMap.keySet()) {
            s = myMap.get(d);
            myList.add(new CustomToggle(s.getString(), group, s.getColor(), s));
        }
    }

    public List<CustomToggle> getList() {
        return this.myList;
    }

}
