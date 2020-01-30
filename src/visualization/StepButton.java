package visualization;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class StepButton extends ToggleButton {

    public StepButton(String text, ToggleGroup group) {
        super(text);
        this.setOnAction((ActionEvent e) -> {
            System.out.println("Stepping through simulation");
        });
        this.setToggleGroup(group);
    }
}
