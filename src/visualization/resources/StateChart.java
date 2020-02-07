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

    private Map<String, XYChart.Series> mySeries = new HashMap<String, XYChart.Series>();
    private boolean seriesFlag;
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
        seriesFlag = false;
    }

    // TODO: Style Series (with CSS styling)
    public void createSeries(Map<String, Integer> allStates) {
        System.out.println("creating series...");
        for (String name: allStates.keySet()) {
            if (!(mySeries.containsKey(name))) {
                XYChart.Series series = new XYChart.Series();
                series.setName(name);
                this.getData().add(series);
                mySeries.put(name, new XYChart.Series());
            }
        }
    }

    private void appendSeries(String name, int count) {
        Series series = mySeries.get(name);
        System.out.println("Series: " + series.getName());
        series.getData().add(new XYChart.Data(STEP, count));
    }

    public void populateChart(Map<String, Integer> allStates) {
        if (!(seriesFlag)) {
            createSeries(allStates);
            seriesFlag = true;
        }
        STEP ++;
        System.out.println(STEP);
        for (String name: allStates.keySet()) {
            int count = allStates.get(name);
            appendSeries(name, count);
        }
    }
}
