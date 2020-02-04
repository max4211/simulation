package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

public class CustomToggle extends ToggleButton {

    private static final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));

    public CustomToggle(String text, ToggleGroup group, EventHandler<ActionEvent> handler) {
        super(text);
        this.setOnAction(handler);
        this.setToggleGroup(group);
    }
}