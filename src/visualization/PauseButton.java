package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class PauseButton extends ToggleButton {

    public PauseButton(String text, ToggleGroup group, EventHandler<ActionEvent> handler) {
        super(text);
        this.setOnAction(handler);
        this.setToggleGroup(group);
    }
}
