package visualization;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class ExitButton extends ToggleButton {

    public ExitButton(String text, ToggleGroup group) {
        super(text);
        this.setOnAction((ActionEvent e) -> {
            System.out.println("Exiting Simulation");
            System.exit(0);
        });
        this.setToggleGroup(group);
    }
}
