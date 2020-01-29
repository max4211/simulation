package visualization;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;

public class Visualization extends Application {

    private final int SCENE_HEIGHT = 500;
    private final int SCENE_WIDTH = 500;
    private final int TOP_PAD = 20;
    private final int BOTTOM_PAD = 20;
    private final int LEFT_PAD = 20;
    private final int RIGHT_PAD = 20;
    private final int H_GAP = 10;
    private final int V_GAP = 10;
    private final int BUTTON_SPACING = 10;
    private final int SLIDER_MIN = 0;
    private final int SLIDER_MAX = 100;
    private final int SLIDER_MAJOR_TICK = 20;
    private final int SLIDER_MINOR_TICK = 5;

    @Override
    public void start(Stage primaryStage) {

        GridPane myGrid = createGrid();
        HBox myHBox = createHBox();
        myGrid.add(myHBox, 0, 0, 2, 1);

        Scene scene = new Scene(myGrid, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setTitle("CA Simulation Test");
        primaryStage.setScene(scene);
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(H_GAP);
        grid.setVgap(V_GAP);
        grid.setPadding(new Insets(TOP_PAD, RIGHT_PAD, BOTTOM_PAD, LEFT_PAD));
        return grid;
    }

    private HBox createHBox() {
        HBox box = new HBox(BUTTON_SPACING);
        box.setAlignment((Pos.BOTTOM_CENTER));
        box.getChildren().add(new Button("Pause"));
        box.getChildren().add(new Button("Play"));
        box.getChildren().add(new Button("Step"));
        box.getChildren().add(createSlider());
        return box;
    }

    private Slider createSlider() {
        Slider slider = new Slider();
        slider.setMin(SLIDER_MIN);
        slider.setMax(SLIDER_MAX);
        slider.setValue((SLIDER_MAX + SLIDER_MIN)/2);
        slider.setShowTickMarks(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(SLIDER_MAJOR_TICK);
        slider.setMinorTickCount(SLIDER_MINOR_TICK);
        slider.setBlockIncrement(SLIDER_MINOR_TICK);
        return slider;
    }

    private void step() {
        
    }
}
