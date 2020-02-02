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

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Visualization extends Application {

    // Resources for styling and properties
    protected static final String RESOURCES = "resources";
    protected static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    protected static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES + "/";
    protected static final String LANGUAGE = "Image";
    protected static final String STYLESHEET = "default.css";
    protected static final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
    protected ResourceBundle myResources;

    // Sim and scene metadata
    private final double SCENE_HEIGHT = 520;
    private final double SCENE_WIDTH = 420;
    private final double SIM_HEIGHT = SCENE_HEIGHT * 0.75;
    private final double SIM_WIDTH = SCENE_WIDTH * 0.95;
    private final double VBOX_HEIGHT = SCENE_HEIGHT * 0.15;
    private final Color STROKE_FILL = Color.BLACK;
    private final double STROKE_WIDTH = 3;

    // Padding values
    private final int TOP_PAD = 5;
    private final int BOTTOM_PAD = 5;
    private final int LEFT_PAD = 5;
    private final int RIGHT_PAD = 5;
    private final int BUTTON_SPACING = 20;
    private final int SLIDER_SPACING = 20;
    private final int VBOX_SPACING = 15;

    // Viewer objects
    private Slider mySlider;
    private ToggleButton myPauseButton;
    private ToggleButton myPlayButton;
    private ToggleButton myStepButton;
    private ToggleButton myLoadButton;
    private ToggleButton myExitButton;

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
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGE);
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

    private Scene createScene() {
        BorderPane root = createRootPane();
        VBox myVBox = new VBox();
        HBox topHBox = createTopHBox();
        HBox botHBox = createBottomHBox();
        myVBox.getChildren().add(topHBox);
        myVBox.getChildren().add(botHBox);
        myVBox.setSpacing(VBOX_SPACING);
        myVBox.setMaxHeight(VBOX_HEIGHT);
        mySimGrid = createSimGrid();
        mySimulation = new Simulation(firstSim);
        root.setCenter(mySimGrid);
        root.setBottom(myVBox);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        return scene;
    }

    private GridPane createSimGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);
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
        Label sliderUnits = new Label (myResources.getString("AnimationLabel"));
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

    // TODO: Determine how to refactor button classes
    //  May want to extend objects and make global variables protected
    private HBox createTopHBox() {
        HBox box = new HBox(BUTTON_SPACING);
        box.setAlignment((Pos.CENTER));
        ToggleGroup group = new ToggleGroup();
        myPauseButton = new PauseButton(myResources.getString("PauseButton"), group);
        myPlayButton = new PlayButton(myResources.getString("PlayButton"), group);
        myStepButton = new StepButton(myResources.getString("StepButton"), group);
        myLoadButton = new LoadButton(myResources.getString("LoadButton"), group);
        myExitButton = new ExitButton(myResources.getString("ExitButton"), group);
        pauseSelected();
        box.getChildren().add(myPauseButton);
        box.getChildren().add(myPlayButton);
        box.getChildren().add(myStepButton);
        box.getChildren().add(myLoadButton);
        box.getChildren().add(myExitButton);
        return box;
    }

    private GridPane updateSimGrid(GridPane grid) {
        updateSimFlag = false;
        System.out.println("Updating simulation grid");
        // myColorGrid = mySimulation.getColorGrid();
        Color[][] myColorGrid = createColors(); // TODO: myColorGrid from Simulation
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
    }

    // TODO: Fix myTimer timing, stutter step on pulse (timerOn trigger?)
    private void playSelected() {
        if (!timerOn) {
            System.out.println("Scheduling timer action");
            myTimer.purge();
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
        myPauseButton.setSelected(true);
        myLoadButton.setSelected(false);
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation XML File");
        File dir = new File (System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(dir);
        File simFile = fileChooser.showOpenDialog(fileStage);
        String extension = getFileExtension(simFile);
        if (simFile == null) {
            System.out.println("No file selected, please try again");
        } else if (extension.equals("xml") || extension.equals("XML")){
            mySimulation = new Simulation(simFile);
            myPauseButton.setSelected(true);
            System.out.println("Created simulation from fileChooser \n" + simFile);
        } else {
            System.out.println("Incorrect file type selected, please try again");
        }
    }

    private String getFileExtension(File file) {
        if (file == null) {return "";}
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(index+1);
        } else {
            return "";
        }
    }

}
