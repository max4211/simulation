package visualization;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import simulation.Simulation;

public class AnimationSlider extends Slider {

    // Slider metadata
    private final double SLIDER_MIN = 0;
    private final double SLIDER_MAX = 10;
    private final double SLIDER_MAJOR_TICK = 2;
    private final double SLIDER_MINOR_TICK = 0.2;

    public AnimationSlider() {
        super();
        this.setMin(SLIDER_MIN);
        this.setMax(SLIDER_MAX);
        this.setValue(SLIDER_MIN); //((double) SLIDER_MAX + SLIDER_MIN)/2);
        this.setSnapToTicks(true);
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(SLIDER_MAJOR_TICK);
        this.setBlockIncrement(SLIDER_MINOR_TICK);
    }

}
