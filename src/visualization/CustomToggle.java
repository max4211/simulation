package visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import simulation.State;

public class CustomToggle extends ToggleButton {

    private Double myState;

    public CustomToggle(String text, ToggleGroup group, EventHandler<ActionEvent> handler) {
        super(text);
        this.setOnAction(handler);
        this.setToggleGroup(group);
    }

    public CustomToggle(String text, ToggleGroup group, String color, Double state) {
        super(text);
        this.setToggleGroup(group);
        // Currently does nothing, over ridden by properties file (as expected)
        this.setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY)));
        this.myState = state;
    }

    public Double getState() {return this.myState;}



}