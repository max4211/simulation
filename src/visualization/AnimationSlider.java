package visualization;

import javafx.scene.control.Slider;

public class AnimationSlider extends Slider {

    // Slider metadata
    private final double SLIDER_MIN = 0;
    private final double SLIDER_MAX = 5;
    private final double SLIDER_MAJOR_TICK = 1;
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
        // slider.setMinorTickCount(SLIDER_MINOR_TICK);
        this.setBlockIncrement(SLIDER_MINOR_TICK);
    }
}
