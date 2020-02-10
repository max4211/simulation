package visualization;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import simulation.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Chart at the top of the GUI that displays states over time.
 * Main Object held is a LineChart, plus interactions with cell objects for series styling
 */
public class StateChart extends LineChart {

    private Map<String, XYChart.Series> mySeries = new HashMap<String, XYChart.Series>();
    private final String LEGEND_STYLE = "-fx-wrap-text: false;\n"; // +
                               // "-fx-background-color: transparent\n;" +
                               // "-fx-text-size: 4";
    private boolean seriesFlag;
    private int STEP = 0;

    /**
     * Construct a new LineChart with the given axis.
     * Label the axes appropriately
     * @param xAxis  The x axis to use
     * @param yAxis The y axis to use
     */
    public StateChart(Axis xAxis, Axis yAxis) {
        super(xAxis, yAxis);
        xAxis.setLabel("Step");
        yAxis.setLabel("Total");
        seriesFlag = false;
        // this.setLegendVisible(true);
        // this.setLegendSide(Side.LEFT);
        // this.getLegend().setStyle(LEGEND_STYLE);
    }


    private void createSeries(Map<String, Integer> allStates) {
        for (String name: allStates.keySet()) {
            if (!(mySeries.containsKey(name))) {
                XYChart.Series series = new XYChart.Series();
                series.setName(name);
                this.getData().add(series);
                mySeries.put(name, series);
            }
        }
    }

    private String formatColorString(String color) {
        return String.format("-fx-stroke: %s", color);
    }

    private void appendSeries(String name, int count) {
        Series series = mySeries.get(name);
        // System.out.println("Series: " + series.getName());
        series.getData().add(new XYChart.Data(STEP, count));
    }

    /**
     * Populates the chart series elements with the current simulation status (states and counts)
     * @param allStates represent all states from the Simulation (count of current state strings and values
     */
    public void populateChart(Map<String, Integer> allStates) {
        // traverseMap(allStates);
        if (!(seriesFlag)) {
            createSeries(allStates);
            seriesFlag = true;
        }
        STEP ++;
        // System.out.println(STEP);
        for (String name: allStates.keySet()) {
            int count = allStates.get(name);
            appendSeries(name, count);
        }
    }

    private void traverseMap(Map<String, Integer> myMap) {
        for (String s: myMap.keySet()) {
            System.out.printf("State: %s, Count: %d \n", s, myMap.get(s));
        }
    }

    /**
     * Styles the series appropriately using the stateMap object inside of the base cell
     * @param stateMap contains all states of the current cell simluation
     */
    public void styleChart(Map<Double, State> stateMap) {
        for (State state: stateMap.values()) {
            for (String s: mySeries.keySet()) {
                if (state.getString().equals(s)) {
                    mySeries.get(s).getNode().setStyle(formatColorString(state.getColor()));
                    // System.out.printf("Styled series to color: %s \n", state.getColor());
                }
            }
        }
    }
}
