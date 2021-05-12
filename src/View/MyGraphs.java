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
    XYChart.Series leftSeries = new XYChart.Series();

    XYChart.Series rightSeries = new XYChart.Series();

    XYChart.Series algorithmSeries = new XYChart.Series();

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();

        final NumberAxis leftxAxis = new NumberAxis();
        final NumberAxis leftyAxis = new NumberAxis();
        final LineChart<Number,Number> leftLineChart = new LineChart<Number,Number>(leftxAxis, leftyAxis);

        leftLineChart.setLayoutX(180);
        leftLineChart.setLayoutY(27);
        leftLineChart.setPrefSize(220,190);
        leftLineChart.setCreateSymbols(false);
        leftLineChart.getData().add(leftSeries);
        leftxAxis.setTickLabelsVisible(false);
        leftyAxis.setTickLabelsVisible(false);
        leftSeries.setName("parameter values");
        ret.add(leftLineChart);

        final NumberAxis rightxAxis = new NumberAxis();
        final NumberAxis rightyAxis = new NumberAxis();
        final LineChart<Number,Number> rightLineChart = new LineChart<Number,Number>(rightxAxis, rightyAxis);

        rightLineChart.setLayoutX(380);
        rightLineChart.setLayoutY(27);
        rightLineChart.setPrefSize(220,190);
        rightLineChart.setCreateSymbols(false);
        rightLineChart.getData().add(rightSeries);
        rightxAxis.setTickLabelsVisible(false);
        rightyAxis.setTickLabelsVisible(false);
        rightSeries.setName("correlated parameter values");
        ret.add(rightLineChart);

        final NumberAxis algorithmxAxis = new NumberAxis();
        final NumberAxis algorithmyAxis = new NumberAxis();
        final LineChart<Number,Number> algorithmLineChart = new LineChart<Number,Number>(algorithmxAxis, algorithmyAxis);

        algorithmLineChart.setLayoutX(180);
        algorithmLineChart.setLayoutY(205);
        algorithmLineChart.setPrefSize(420,260);
        algorithmLineChart.setCreateSymbols(false);
        algorithmLineChart.getData().add(algorithmSeries);
        algorithmxAxis.setTickLabelsVisible(false);
        algorithmyAxis.setTickLabelsVisible(false);
        algorithmSeries.setName("chosen anomaly detector");
        ret.add(algorithmLineChart);

        return ret;
    }
}
