package visualization.resources;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import simulation.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateChart extends LineChart {

    private Map<State, XYChart.Series> mySeries = new HashMap<State, XYChart.Series>();
    private boolean seriesFlag = false;
    private int STEP = 0;

    /**
     * Construct a new LineChart with the given axis.
     *
     * @param xAxis  The x axis to use
     * @param yAxis The y axis to use
     */
    public StateChart(Axis xAxis, Axis yAxis) {
        super(xAxis, yAxis);
        xAxis.setLabel("Step");
        yAxis.setLabel("Percent (%)");
    }

    // TODO: Style Series (with CSS styling)
    public void createSeries(Map<State, Integer> allStates) {
        for (State state: allStates.keySet()) {
            XYChart.Series series = new XYChart.Series();
            System.out.println("state.getString()" + state.getString());
            series.setName(state.getString());
            this.getData().add(series);
            mySeries.put(state, new XYChart.Series());
        }
    }

    private void appendSeries(State state, int count) {
        Series series = mySeries.get(state);
        series.getData().add(new XYChart.Data(STEP, count));
    }

    public void populateChart(Map<State, Integer> allStates) {
        if (!(seriesFlag)) {
            createSeries(allStates);
            seriesFlag = true;
        }
        STEP ++;
        for (State state: allStates.keySet()) {
            int count = allStates.get(state);
            appendSeries(state, count);
        }
    }
}
