package visualization;

import configuration.Configuration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Simulation;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ResourceBundle;

public class Visualization extends Application {

    // Resources for styling and properties
    private static final String RESOURCES = "visualization/resources";
    private static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    private static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES + "/";
    private static final String LANGUAGE = "Image";
    private static final String STYLESHEET = "default.css";
    private static final String PERCOLATION_FILES = System.getProperty("user.dir") + "/data/";
    private static final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
    private ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGE);

    // Sim and scene metadata
    private final double SCENE_HEIGHT = 700;
    private final double SCENE_WIDTH = 550;
    private final double SIM_HEIGHT = SCENE_HEIGHT * 0.7;
    private final double SIM_WIDTH = SCENE_WIDTH * 0.9;
    private final double VBOX_HEIGHT = SCENE_HEIGHT * 0.25;
    private final double BUTTON_RADIUS = SCENE_WIDTH * 0.17;
    private final String BACKGROUND_COLOR = "-fx-background-color: rgb(180, 180, 180)";

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
    private final int FRAME_RATE = 20;
    private long updateTime;
    private BorderPane myRoot;
    private Simulation mySimulation;

    // First simulation to run
    private final String SIM_TITLE = myResources.getString("SimTitle");
    private final File firstSim = new File("data/percolation74.xml");
    private int SIMULATION_ROWS;
    private int SIMULATION_COLS;

    @Override
    public void start(Stage stage) {
        Scene myScene = createScene();
        stage.setTitle(SIM_TITLE);
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
        createSimulation(firstSim);
        Scene scene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        return scene;
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
        Label sliderLabel = new Label(Integer.toString((int) mySlider.getValue()));
        sliderLabel.setFont(Font.font(24));
        Label sliderUnits = new Label (myResources.getString("AnimationLabel"));
        mySlider.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            sliderLabel.setText(String.format("%.1f", new_val));
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
        myPauseButton = new CustomToggle(myResources.getString("PauseButton"), group, event -> pauseSelected());
        myPlayButton = new CustomToggle(myResources.getString("PlayButton"), group, event -> playSelected());
        myStepButton = new CustomToggle(myResources.getString("StepButton"), group, event -> stepSelected());
        myLoadButton = new CustomToggle(myResources.getString("LoadButton"), group, event -> loadSelected());
        myExitButton = new CustomToggle(myResources.getString("ExitButton"), group, event -> exitSelected());
        styleButtons();
        pauseSelected();
        box.getChildren().add(myPauseButton);
        box.getChildren().add(myPlayButton);
        box.getChildren().add(myStepButton);
        box.getChildren().add(myLoadButton);
        box.getChildren().add(myExitButton);
        return box;
    }


    private void showSimGrid() {
        GridPane simGrid = new GridPane();
        simGrid.setAlignment(Pos.CENTER);
        simGrid.setPrefSize(SIM_WIDTH, SIM_HEIGHT);
        myRoot.setCenter(simGrid);
        double regionHeight = SIM_HEIGHT / SIMULATION_ROWS;
        double regionWidth = SIM_WIDTH / SIMULATION_COLS;
        for (int row = 0; row < SIMULATION_ROWS; row ++) {
            for (int col = 0; col < SIMULATION_COLS; col ++) {
                simGrid.add(createRegion(regionWidth, regionHeight, mySimulation.getCell(row, col).getColor()), col, row );
            }
        }
    }

    private Region createRegion(double regionWidth, double regionHeight, String color) {
        Region myRegion = new Region();
        Insets myInsets = new Insets(regionHeight/50);
        myRegion.setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, myInsets)));
        myRegion.setShape(new Rectangle(regionWidth, regionHeight));
        myRegion.setPrefSize(regionWidth, regionHeight);
        Border myBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        myRegion.setBorder(myBorder);
        return myRegion;
    }

    private void updateSimulation() {
        mySimulation.updateGrid();
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
        showSimGrid();
    }

    private void pauseSelected(){;}

    private void playSelected() {
        if (updateTime < System.currentTimeMillis() - getAnimationRate()) { setUpdateTime();}
        if (System.currentTimeMillis() >= updateTime) {
            updateSimulation();
            showSimGrid();
            setUpdateTime();
        }
    }

    private void exitSelected() { System.exit(0); }

    private double getAnimationRate() {
        double val = mySlider.getValue();
        if (val == 0) { return Math.sqrt(Integer.MAX_VALUE); }
        else { return Math.pow(val, -1) * 1000; }
    }

    private void loadSelected() {
        myPauseButton.setSelected(true);
        myLoadButton.setSelected(false);
        loadFile();
    }

    private void loadFile() {
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation XML File");
        File dir = new File (PERCOLATION_FILES);
        fileChooser.setInitialDirectory(dir);
        File simFile = fileChooser.showOpenDialog(fileStage);
        String extension = getFileExtension(simFile);
        if (extension.equals("xml") || extension.equals("XML")) {
            createSimulation(simFile);
        } else {
            System.out.println("Please select valid XML file and try again");
        }
    }

    private void createSimulation(File simFile) {
        mySimulation = new Configuration(simFile).getSimulation();
        showSimGrid();
        myPauseButton.setSelected(true);
        SIMULATION_ROWS = mySimulation.getHeight();
        SIMULATION_COLS = mySimulation.getWidth();
    }

    private String getFileExtension(File file) {
        if (file == null) {return "";}
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index > 0) { return fileName.substring(index+1); }
        else { return ""; }
    }

    private void styleButtons() {
        styleButton(myPauseButton);
        styleButton(myPlayButton);
        styleButton(myLoadButton);
        styleButton(myExitButton);
        styleButton(myStepButton);
    }

    private void styleButton(ToggleButton button) {
        String label = button.getText();
        if (label.matches(IMAGEFILE_SUFFIXES)) {
            button.setPrefSize(50, 50);
            button.setShape(new Circle(BUTTON_RADIUS));
            button.setText("");
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
        }
    }

}
