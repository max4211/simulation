package visualization;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import simulation.Simulation;


public class Visualization extends Application {

    // Sim and scene metadata
    private final int SCENE_HEIGHT = 500;
    private final int SCENE_WIDTH = 500;
    private final int SIM_HEIGHT = 400;
    private final int SIM_WIDTH = 400;
    private final Color STROKE_FILL = Color.BLACK;
    private final double STROKE_WIDTH = 3;
    private int SLIDER_SPEED;

    // Padding values
    private final int TOP_PAD = 5;
    private final int BOTTOM_PAD = 5;
    private final int LEFT_PAD = 5;
    private final int RIGHT_PAD = 5;
    private final int H_GAP = 10;
    private final int V_GAP = 10;
    private final int BUTTON_SPACING = 10;

    // Slider metadata
    private final int SLIDER_MIN = 0;
    private final int SLIDER_MAX = 20;
    private final int SLIDER_MAJOR_TICK = 5;
    private final int SLIDER_MINOR_TICK = 1;
    private final int SLIDER_SPACING = 20;
    private Simulation mySim;

    @Override
    public void start(Stage primaryStage) {

        GridPane root = createGrid();
        HBox topHBox = createTopHBox();
        HBox botHBox = createBottomHBox();
        GridPane myGrid = createSim();

        root.add(myGrid, 0, 0, 2, 4);
        root.add(topHBox, 0, 5, 2, 1);
        root.add(botHBox, 0, 6, 2, 1);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setTitle("CA Simulation Test");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private GridPane createSim() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setMinWidth(200);
        grid.setMinHeight(200);
        Color[][] colorGrid = createColors();
        int totalRows = colorGrid.length;
        int totalCols = colorGrid[0].length;
        double rectangleHeight = SIM_HEIGHT / totalRows;
        double rectangleWidth = SIM_WIDTH / totalCols;
        for (int row = 0; row < totalRows; row ++) {
            for (int col = 0; col < totalCols; col ++) {
                Rectangle myRectangle = new Rectangle(rectangleWidth, rectangleHeight);
                myRectangle.setStroke(STROKE_FILL);
                myRectangle.setStrokeWidth(STROKE_WIDTH);
                myRectangle.setFill(colorGrid[row][col]);
                grid.add(myRectangle, col, row, 1, 1);
            }
        }
        return grid;
    }

    private Color[][] createColors() {
        Color[][] myColors = new Color[3][3];
        myColors[0] = new Color[]{Color.RED, Color.BLUE, Color.RED};
        myColors[1] = new Color[]{Color.BLACK, Color.BLUE, Color.PURPLE};
        myColors[2] = new Color[]{Color.BLUE, Color.BLUE, Color.PINK};
        return myColors;
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(H_GAP);
        grid.setVgap(V_GAP);
        grid.setPadding(new Insets(TOP_PAD, RIGHT_PAD, BOTTOM_PAD, LEFT_PAD));
        return grid;
    }

    private HBox createBottomHBox() {
        HBox box = new HBox(SLIDER_SPACING);
        box.setAlignment((Pos.CENTER));
        Slider mySlider = createSlider();
        Label sliderLabel = new Label("speed");
        mySlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            sliderLabel.setText(String.format("%.2f", new_val));
        });
        box.getChildren().add(mySlider);
        box.getChildren().add(sliderLabel);
        return box;
    }

    private HBox createTopHBox() {
        HBox box = new HBox(BUTTON_SPACING);
        box.setAlignment((Pos.CENTER));
        Button pauseButton = createButton("Pause", "Pressed Pause");
        Button playButton = createButton("Play", "Pressed Play");
        Button stepButton = createButton("Step", "Pressed Step");
        Button loadButton = createButton("Load", "Pressed Load");
        box.getChildren().add(pauseButton);
        box.getChildren().add(playButton);
        box.getChildren().add(stepButton);
        box.getChildren().add(loadButton);
        return box;
    }

    private Button createButton(String text, String action) {
        Button btn = new Button(text);
        btn.setOnAction((ActionEvent e) -> {
            System.out.println(action);
        });
        return btn;
    }

    private Slider createSlider() {
        Slider slider = new Slider();
        slider.setMin(SLIDER_MIN);
        slider.setMax(SLIDER_MAX);
        slider.setValue((SLIDER_MAX + SLIDER_MIN)/2);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(SLIDER_MAJOR_TICK);
        slider.setMinorTickCount(SLIDER_MINOR_TICK);
        slider.setBlockIncrement(SLIDER_MINOR_TICK);
        return slider;
    }

    private void step() {

    }
}
