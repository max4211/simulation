package visualization;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class LoadButton extends ToggleButton {

    public LoadButton(String text, ToggleGroup group) {
        super(text);
        this.setOnAction((ActionEvent e) -> {
            System.out.println("Trying to open FileChooser");
        });
        this.setToggleGroup(group);
    }
}
