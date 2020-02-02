package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomToggle extends Visualization {

    private ToggleButton myToggle;

    public CustomToggle(String label, ToggleGroup group, EventHandler<ActionEvent> handler) {
        myToggle = new ToggleButton();
        myToggle.setToggleGroup(group);
        if (label.matches(IMAGEFILE_SUFFIXES)) {
            myToggle.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
        }
        else {
            myToggle.setText(label);
        }
        myToggle.setOnAction(handler);
    }
}
