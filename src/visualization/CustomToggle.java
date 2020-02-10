package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;

/**
 * Customizable toggle button, used to help with design when only a single option should be selected a once
 * The series of pause/play/step/load/exit/save buttons are instances of CustomToggle
 * The choices of which Cell to update the value to are also toggles, part of a single group so only one is selected
 * at once
 */
public class CustomToggle extends ToggleButton {

    private static final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
    private static double BUTTON_RADIUS;
    private static double IMAGE_DIMENSIONS;
    private static String RESOURCE_FOLDER;
    private Double myState;

    public CustomToggle(String text, ToggleGroup group, EventHandler<ActionEvent> handler, double dimensions, double radius, String resources) {
        super(text);
        this.setOnAction(handler);
        this.setToggleGroup(group);
        IMAGE_DIMENSIONS = dimensions;
        BUTTON_RADIUS = radius;
        RESOURCE_FOLDER = resources;
        this.styleButton();
    }

    /**
     * CustomToggle constructor for all ToggleButtons used for selecting the updated cell state
     * @param text to show over the toggle button
     * @param group to assign the toggles so only one can be selected at a time
     * @param color of the ToggleButton, currently overriden by cell styling CSS sheet
     * @param state of the CustomToggle, retrieved when Confirm button is selected
     */
    public CustomToggle(String text, ToggleGroup group, String color, Double state) {
        super(text);
        this.setToggleGroup(group);
        this.setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY)));
        this.myState = state;
    }

    /**
     * When custom toggle is created to edit the current (selected) cell state, each toggle holds a state value
     * to send to the region/cell in the simulation to udpate its value
     * @return state of the toggle
     */
    public Double getState() {return this.myState;}

    public void styleButton() {
        String label = this.getText();
        if (label.matches(IMAGEFILE_SUFFIXES)) {
            this.setPrefSize(IMAGE_DIMENSIONS, IMAGE_DIMENSIONS);
            this.setShape(new Circle(BUTTON_RADIUS));
            this.setText("");
            this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(RESOURCE_FOLDER + label))));
        }
    }

}