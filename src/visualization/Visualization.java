package visualization;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Simulation;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Visualization extends Application {

    // Sim and scene metadata
    private final int SCENE_HEIGHT = 520;
    private final int SCENE_WIDTH = 420;
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
    private final int SLIDER_SPACING = 20;
    private final int VBOX_SPACING = 15;

    // Viewer objects
    private Slider mySlider;
    private ToggleButton myPauseButton;
    private ToggleButton myPlayButton;
    private ToggleButton myStepButton;
    private ToggleButton myLoadButton;

    // Simulation metadata
    private final int FRAME_RATE = 10;
    private Timer myTimer = new Timer();
    private boolean timerOn = false;
    private boolean updateSimFlag = true;
    private GridPane mySimGrid;
    private Simulation mySimulation;
    private Color[][] myColorGrid;

    // First simulation to run
    private File firstSim = new File("data/simulation_sample.xml");

    @Override
    public void start(Stage stage) {
        Scene myScene = createScene();
        stage.setTitle("CA Simulation Project");
        stage.setScene(myScene);
        stage.sizeToScene();
        stage.show();
        KeyFrame frame = new KeyFrame(Duration.millis(FRAME_RATE), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // TODO: Update scene to BorderPane (then add escape button on top)
    private Scene createScene() {
        BorderPane root = createRootPane();
        VBox myVBox = new VBox();
        HBox topHBox = createTopHBox();
        HBox botHBox = createBottomHBox();
        myVBox.getChildren().add(topHBox);
        myVBox.getChildren().add(botHBox);
        myVBox.setSpacing(VBOX_SPACING);
        mySimGrid = createSimGrid();
        mySimulation = new Simulation(firstSim);
        root.setCenter(mySimGrid);
        root.setBottom(myVBox);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        return scene;
    }

    private GridPane createSimGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setMinWidth(200);
        grid.setMinHeight(200);
        return grid;
    }

    private Color[][] createColors() {
        Color[][] myColors = new Color[3][3];
        myColors[0] = new Color[]{Color.RED, Color.BLUE, Color.RED};
        myColors[1] = new Color[]{Color.BLACK, Color.BLUE, Color.PURPLE};
        myColors[2] = new Color[]{Color.BLUE, Color.BLUE, Color.PINK};
        return myColors;
    }

    private BorderPane createRootPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(TOP_PAD, RIGHT_PAD, BOTTOM_PAD, LEFT_PAD));
        return borderPane;
    }

    private HBox createBottomHBox() {
        HBox box = new HBox(SLIDER_SPACING);
        box.setAlignment((Pos.CENTER));
        mySlider = new AnimationSlider();
        Label sliderLabel = new Label(Integer.toString((int) mySlider.getValue()));
        sliderLabel.setFont(Font.font(24));
        Label sliderUnits = new Label ("animations/second");
        mySlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            sliderLabel.setText(String.format("%.2f", new_val));
            myPlayButton.setSelected(true);
            myTimer.purge();
            clearTimer();
        });
        box.getChildren().add(mySlider);
        box.getChildren().add(sliderLabel);
        box.getChildren().add(sliderUnits);
        return box;
    }

    private HBox createTopHBox() {
        HBox box = new HBox(BUTTON_SPACING);
        box.setAlignment((Pos.CENTER));
        ToggleGroup group = new ToggleGroup();
        myPauseButton = new PauseButton("Pause", group);
        myPlayButton = new PlayButton("Play", group);
        myStepButton = new StepButton("Step", group);
        myLoadButton = new LoadButton("Load", group);
        pauseSelected();
        box.getChildren().add(myPauseButton);
        box.getChildren().add(myPlayButton);
        box.getChildren().add(myStepButton);
        box.getChildren().add(myLoadButton);
        return box;
    }

    private GridPane updateSimGrid(GridPane grid) {
        updateSimFlag = false;
        System.out.println("Updating simulation grid");
        // myColorGrid = mySimulation.getColorGrid();
        Color[][] myColorGrid = createColors(); // TODO: myColorGrid
        int totalRows = myColorGrid.length;
        int totalCols = myColorGrid[0].length;
        double rectangleHeight = SIM_HEIGHT / totalRows;
        double rectangleWidth = SIM_WIDTH / totalCols;
        for (int row = 0; row < totalRows; row ++) {
            for (int col = 0; col < totalCols; col ++) {
                Rectangle myRectangle = new Rectangle(rectangleWidth, rectangleHeight);
                myRectangle.setStroke(STROKE_FILL);
                myRectangle.setStrokeWidth(STROKE_WIDTH);
                myRectangle.setFill(myColorGrid[row][col]);
                grid.add(myRectangle, col, row ); // Default to col:row span = 1
            }
        }
        return grid;
    }

    private void step() {
        if (myStepButton.isSelected()) {
            stepSelected();
        } else if (myPauseButton.isSelected()) {
            pauseSelected();
        } else if (myPlayButton.isSelected()) {
            playSelected();
        } else if (myLoadButton.isSelected()) {
            loadSelected();
        }
        if (updateSimFlag) {
            updateSimGrid(mySimGrid);
        }
    }

    private void stepSelected() {
        updateSimFlag = true;
        myStepButton.setSelected(false);
        System.out.println("myStepButton deselected");
    }

    private void pauseSelected() {
        myTimer.purge();
        // myTimer.schedule(new UpdateSimulationReminder(), Integer.MAX_VALUE);
    }

    // TODO: Fix myTimer timing, stutter step on pulse (timerOn trigger?)
    private void playSelected() {
        if (!timerOn) {
            System.out.println("Scheduling timer action");
            myTimer.purge();
            // myTimer.schedule(new UpdateSimulationReminder(), (long) getAnimationRate() * 1000);
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Timer action run");
                    updateSimFlag = true;
                    timerOn = false;
                }
            }, (long) getAnimationRate() * 1000);
            timerOn = true;
        }
    }

    private double getAnimationRate() {
        double val = mySlider.getValue();
        double guy;
        if (val == 0) {
            guy = Integer.MAX_VALUE;
        } else {
            guy = Math.pow(val, -1);
        }
        System.out.println("Animation Rate: " + guy + " seconds");
        return guy;
    }

    private void clearTimer() {
        System.out.println("Purging timer");
        myTimer.purge();
        timerOn = false;
    }

    private void loadSelected() {
        myLoadButton.setSelected(false);
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation XML File");
        File simFile = fileChooser.showOpenDialog(fileStage);
        mySimulation = new Simulation(simFile);
        myPauseButton.setSelected(true);
        System.out.println("Created simulation from fileChooser \n" + simFile);
    }

    private class UpdateSimulationReminder extends TimerTask {
        @Override
        public void run() {
            System.out.println("Timer action run");
            updateSimFlag = true;
            timerOn = false;
        }
    }

}
