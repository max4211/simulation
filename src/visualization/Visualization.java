package visualization;

import configuration.Configuration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Simulation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Main driver of the Cellular Automata program
 * Visualization class extends Application and stores all visual components of the Simulation
 */
public class Visualization extends Application {

    // Resources for styling and properties
    private static final String RESOURCES = "visualization/resources";
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES + "/";
    private static final String LANGUAGE = "Image";
    private static final String STYLESHEET = "default.css";
    protected ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGE);

    // Sim and scene metadata
    private static final double SCENE_HEIGHT = 700;
    private static final double SCENE_WIDTH = 550;
    protected static final double SIM_HEIGHT = SCENE_HEIGHT * 0.6;
    protected static final double SIM_WIDTH = SCENE_WIDTH * 0.9;
    private static final double VBOX_HEIGHT = SCENE_HEIGHT * 0.1;
    private static final double CHART_HEIGHT = SCENE_HEIGHT * 0.25;
    private static final String BACKGROUND_COLOR = "-fx-background-color: rgb(180, 180, 180)";

    // Padding values
    private static final int TOP_PAD = 5;
    private static final int BOTTOM_PAD = 5;
    private static final int LEFT_PAD = 5;
    private static final int RIGHT_PAD = 5;
    protected static final int BUTTON_SPACING = 30;
    private static final int SLIDER_SPACING = 20;
    private static final int VBOX_SPACING = 15;

    // Viewer custom objects
    private AnimationSlider mySlider;
    private CustomToggle myPauseButton;
    private CustomToggle myPlayButton;
    private CustomToggle myStepButton;
    private CustomToggle myLoadButton;
    private CustomToggle myExitButton;
    private CustomToggle mySaveButton;
    private StateChart myChart;

    // Button metadata
    private static final double BUTTON_RADIUS = SCENE_WIDTH * 0.17;
    private static final double IMAGE_DIMENSIONS = VBOX_HEIGHT * 0.4;

    // Simulation metadata
    private static final int SLIDER_FONT = 24;
    private static final int FRAME_RATE = 20;
    private static final int SEC_TO_MILLI = 1000;
    private long updateTime;
    private BorderPane myRoot;
    private Simulation mySimulation;

    /**
     * Application default method to construct stage that holds main scene
     * @param stage the Stage object to hold the main scene
     */
    @Override
    public void start(Stage stage) {
        Scene myScene = createScene();
        stage.setTitle(myResources.getString("SimTitle"));
        stage.setScene(myScene);
        stage.sizeToScene();
        stage.show();
        KeyFrame frame = new KeyFrame(Duration.millis(FRAME_RATE), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene createScene() {
        myRoot = createRootPane();
        myRoot.setBottom(createVBox());
        createSimulation();
        createChart();
        Scene scene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        return scene;
    }

    private void createChart() {
        myChart = new StateChart(new NumberAxis(), new NumberAxis());
        myChart.setPrefSize(SIM_WIDTH, CHART_HEIGHT);
        myRoot.setTop(myChart);
    }

    private VBox createVBox() {
        VBox myVBox = new VBox();
        HBox topHBox = createTopHBox();
        HBox botHBox = createBottomHBox();
        myVBox.getChildren().add(topHBox);
        myVBox.getChildren().add(botHBox);
        myVBox.setSpacing(VBOX_SPACING);
        myVBox.setMaxHeight(VBOX_HEIGHT);
        return myVBox;
    }

    private BorderPane createRootPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(TOP_PAD, RIGHT_PAD, BOTTOM_PAD, LEFT_PAD));
        borderPane.setStyle(BACKGROUND_COLOR);
        return borderPane;
    }

    private HBox createBottomHBox() {
        HBox box = new HBox(SLIDER_SPACING);
        box.setAlignment((Pos.CENTER));
        mySlider = new AnimationSlider();
        Label sliderUnits = new Label (myResources.getString("AnimationLabel"));
        Label sliderLabel = new Label(Integer.toString((int) mySlider.getValue()));
        sliderLabel.setFont(Font.font(SLIDER_FONT));
        mySlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            sliderLabel.setText(String.format("%.1f", (double) new_val));
            myPlayButton.setSelected(true);
            setUpdateTime();
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
        myPauseButton = new CustomToggle(myResources.getString("PauseButton"), group, event -> pauseSelected(), IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        myPlayButton = new CustomToggle(myResources.getString("PlayButton"), group, event -> playSelected(), IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        myStepButton = new CustomToggle(myResources.getString("StepButton"), group, event -> stepSelected(), IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        myLoadButton = new CustomToggle(myResources.getString("LoadButton"), group, event -> loadSelected(), IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        myExitButton = new CustomToggle(myResources.getString("ExitButton"), group, event -> exitSelected(), IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        mySaveButton = new CustomToggle(myResources.getString("SaveButton"), group, event -> saveSelected(),IMAGE_DIMENSIONS, BUTTON_RADIUS, DEFAULT_RESOURCE_FOLDER);
        pauseSelected();
        box.getChildren().add(myPauseButton);
        box.getChildren().add(myPlayButton);
        box.getChildren().add(myStepButton);
        box.getChildren().add(myLoadButton);
        box.getChildren().add(myExitButton);
        box.getChildren().add(mySaveButton);
        return box;
    }

    private void updateSimulation() {
        mySimulation.updateGrid();
        updateSimPane();
        myChart.populateChart(mySimulation.countStates());
        myChart.styleChart(mySimulation.getCell(0,0).getStateMap());
    }

    private void setUpdateTime() {
        updateTime = System.currentTimeMillis() + (long) getAnimationRate();
    }

    private void step() {
        if (myPlayButton.isSelected()) { playSelected();}
    }

    private void stepSelected() {
        myStepButton.setSelected(false);
        updateSimulation();
        myChart.populateChart(mySimulation.countStates());
    }

    private void pauseSelected(){;}

    private void saveSelected(){
        try {
            mySimulation.captureGridState();
            System.out.println(myResources.getString("SaveSuccess") + new SimpleDateFormat("MM-dd-HH-mm").format(new Date()));
        } catch (IOException e) {
            System.out.println(myResources.getString("SaveError"));
        }

    }

    private void playSelected() {
        if (updateTime < System.currentTimeMillis() - getAnimationRate()) { setUpdateTime();}
        if (System.currentTimeMillis() >= updateTime) {
            updateSimulation();
            updateSimPane();
            setUpdateTime();
        }
    }

    private void exitSelected() { System.exit(0); }

    private double getAnimationRate() {
        double val = mySlider.getValue();
        if (val == 0) { return Math.sqrt(Integer.MAX_VALUE); }
        else { return Math.pow(val, -1) * SEC_TO_MILLI; }
    }

    private void loadSelected() {
        myPauseButton.setSelected(true);
        myLoadButton.setSelected(false);
        createSimulation();
    }

    private void updateSimPane() {
        myRoot.setCenter(new SimPane(mySimulation, myRoot, myChart, myPauseButton).getPane());
    }

    private void createSimulation() {
        try {
            mySimulation = new Configuration().getSimulation();
            myPauseButton.setSelected(true);
            createChart();
            updateSimPane();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            createSimulation();
        }

    }

}
