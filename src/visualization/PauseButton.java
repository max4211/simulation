package visualization;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class PauseButton extends ToggleButton {

    public PauseButton(String text, ToggleGroup group) {
        super(text);
        this.setOnAction((ActionEvent e) -> {
            System.out.println("Pausing Simulation");
        });
        this.setToggleGroup(group);
    }
}
