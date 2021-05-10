package View;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MyGraphs extends Pane
{
    XYChart.Series series = new XYChart.Series();
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    //creating the chart
    final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();
        lineChart.setLayoutX(180);
        lineChart.setLayoutY(27);
        lineChart.setPrefSize(220,190);
        lineChart.setCreateSymbols(false);
        lineChart.getData().add(series);
        ret.add(lineChart);
        return ret;
    }
}
