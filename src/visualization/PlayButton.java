package visualization;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class PlayButton extends ToggleButton {

    public PlayButton(String text, ToggleGroup group) {
        super(text);
        this.setOnAction((ActionEvent e) -> {
            System.out.println("Playing Simulation");
        });
        this.setToggleGroup(group);
    }
}
