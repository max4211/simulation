package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Circle;

public class CustomToggle extends ToggleButton {

    public CustomToggle(String text, ToggleGroup group, EventHandler<ActionEvent> handler) {
        super(text);
        this.setOnAction(handler);
        this.setToggleGroup(group);
    }
}