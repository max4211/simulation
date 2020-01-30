package visualization;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import simulation.Simulation;


public class Visualization extends Application {

    // Sim and scene metadata
    private final int SCENE_HEIGHT = 550;
    private final int SCENE_WIDTH = 550;
    private final int SIM_HEIGHT = 400;
    private final int SIM_WIDTH = 400;
    private final Color STROKE_FILL = Color.BLACK;
    private final double STROKE_WIDTH = 3;

    // Padding values
    private final int TOP_PAD = 5;
    private final int BOTTOM_PAD = 5;
    private final int LEFT_PAD = 5;
    private final int RIGHT_PAD = 5;
    private final int H_GAP = 10;
    private final int V_GAP = 10;
    private final int BUTTON_SPACING = 20;

    // Viewer objects
    private Slider mySlider;
    private Button myPauseButton;
    private Button myPlayButton;
    private Button myStepButton;
    private Button myLoadButton;
    private ToggleButton myToggleButton;

    // Slider metadata
    private final int SLIDER_MIN = 0;
    private final int SLIDER_MAX = 20;
    private final int SLIDER_MAJOR_TICK = 4;
    private final int SLIDER_MINOR_TICK = 1;
    private final int SLIDER_SPACING = 20;

    // Simulation metadata
    private boolean SIM_PAUSED = true;
    private boolean SIM_STEP = false;
    private int ANIMATION_RATE;
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
                grid.add(myRectangle, col, row ); // Default to col:row span = 1
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
        mySlider = createSlider();
        Label sliderLabel = new Label(Integer.toString((int) mySlider.getValue()));
        sliderLabel.setFont(Font.font(24));
        Label sliderUnits = new Label ("animations/second");
        mySlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            sliderLabel.setText(String.format("%.0f", new_val));
        });
        box.getChildren().add(mySlider);
        box.getChildren().add(sliderLabel);
        box.getChildren().add(sliderUnits);
        return box;
    }

    private ToggleButton createToggleButton() {
        ToggleButton toggleButton = new ToggleButton("Animate");
        return toggleButton;
    }

    private HBox createTopHBox() {
        HBox box = new HBox(BUTTON_SPACING);
        box.setAlignment((Pos.CENTER));
        myPauseButton = createButton("Pause");
        myPlayButton = createButton("Play");
        myStepButton = createButton("Step");
        myLoadButton = createButton("Load");
        box.getChildren().add(myPauseButton);
        box.getChildren().add(myPlayButton);
        box.getChildren().add(myStepButton);
        box.getChildren().add(myLoadButton);

        return box;
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        switch (text) {
            case("Pause"):
                btn.setOnAction((ActionEvent e) -> {
                    SIM_PAUSED = true;
                    System.out.println("Pausing Simulation");
                });
                break;
            case("Play"):
                btn.setOnAction((ActionEvent e) -> {
                    SIM_PAUSED = false;
                    System.out.println("Playing Simulation");
                });
                break;
            case("Step"):
                btn.setOnAction((ActionEvent e) -> {
                    SIM_STEP = true;
                    System.out.println("Stepping Simulation");
                });
                break;
            case("Load"):
                btn.setOnAction((ActionEvent e) -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Simulation XML File");
                    System.out.println("Trying to open FileChooser");
                });
                break;
        }
        return btn;
    }

    private Slider createSlider() {
        Slider slider = new Slider();
        slider.setMin(SLIDER_MIN);
        slider.setMax(SLIDER_MAX);
        slider.setValue(SLIDER_MIN); //((double) SLIDER_MAX + SLIDER_MIN)/2);
        slider.setSnapToTicks(true);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(SLIDER_MAJOR_TICK);
        slider.setMinorTickCount(SLIDER_MINOR_TICK);
        slider.setBlockIncrement(SLIDER_MINOR_TICK);
        return slider;
    }

    // TODO: Implement step with lambda animation functionality
    // IDEA: Implement updates similar to MIPS processor
    // Set flag property to update grid, then once update is done set this to off
    private void step() {

    }
}
