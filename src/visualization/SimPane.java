package visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import simulation.Cell;
import simulation.Simulation;
import simulation.State;
import visualization.resources.StateChart;

import java.util.List;
import java.util.Map;

public class SimPane extends Visualization {

    private GridPane myPane;
    private Simulation mySimulation;
    private BorderPane myRoot;
    private StateChart myChart;
    private CustomToggle myPauseButton;

    public SimPane(Simulation simulation, BorderPane root, StateChart chart, CustomToggle pause) {
        mySimulation = simulation;
        myRoot = root;
        myChart = chart;
        myPane = updateGrid();
        myPauseButton = pause;
    }

    public GridPane updateGrid() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPrefSize(SIM_WIDTH, SIM_HEIGHT);
        double regionHeight = SIM_HEIGHT / mySimulation.getHeight();
        double regionWidth = SIM_WIDTH / mySimulation.getWidth();
        for (int row = 0; row < mySimulation.getHeight(); row ++) {
            for (int col = 0; col < mySimulation.getWidth(); col ++) {
                Cell myCell = mySimulation.getCell(row, col);
                pane.add(createRegion(regionWidth, regionHeight, myCell.getColor()), col, row );
            }
        }
        addGridEvent(pane);
        return pane;
    }

    private void addGridEvent(GridPane simGrid) {
        simGrid.getChildren().forEach(item -> {
            item.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    myPauseButton.setSelected(true);
                    Node source = (Node) event.getSource();
                    Integer col = GridPane.getColumnIndex(source);
                    Integer row = GridPane.getRowIndex(source);
                    // System.out.printf("Mouse clicked cell [%d, %d] \n", col.intValue(), row.intValue());
                    Cell myCell = mySimulation.getCell(row, col);
                    Map<Double, State> myStates = myCell.getStateMap();
                    // printStates(myStates);
                    SetCell options = new SetCell(myStates);
                    List<CustomToggle> toggles = options.getList();
                    myRoot.setTop(toggleBox(toggles, row, col));
                }
            });
        });
    }

    private HBox toggleBox(List<CustomToggle> toggles, int row, int col) {
        // System.out.printf("creating toggle box from %d toggles \n", toggles.size());
        HBox box = new HBox();
        for (ToggleButton b: toggles) {
            box.getChildren().add(b);
        }
        box.getChildren().add(confirmButton(toggles, row, col));
        box.setAlignment(Pos.CENTER);
        box.setSpacing(BUTTON_SPACING);
        return box;
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

    private Button confirmButton(List<CustomToggle> toggles, int row, int col) {
        Button button = new Button();
        button.setText(myResources.getString("ConfirmButton"));
        button.setOnAction(event -> {
            Double state = selectedState(toggles);
            System.out.printf("Setting cell [%d, %d] to state %f", row, col, state);
            mySimulation.getCell(row, col).setState(state);
            myRoot.setCenter(updateGrid());
            myRoot.setTop(myChart);
        });
        return button;
    }

    private Double selectedState(List<CustomToggle> toggles) {
        for (CustomToggle b: toggles) {
            if (b.isSelected()) {
                return b.getState();
            }
        }
        System.out.println("No toggle selected, defaulting to 0 state");
        return toggles.get(0).getState();
    }

    private void printStates(Map<Double, State> myStates) {
        for (double d: myStates.keySet()) {
            State state = myStates.get(d);
            String string = state.getString();
            String color = state.getColor();
            System.out.printf("State double: %f, string: %s, color: %s \n", d, string, color);
        }
    }

    public GridPane getPane() {return myPane;}

}