package View;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MyGraphs extends Pane
{
    XYChart.Series leftSeries = new XYChart.Series();

    XYChart.Series rightSeries = new XYChart.Series();

    XYChart.Series algorithmSeries = new XYChart.Series();
    XYChart.Series algorithmSeries1 = new XYChart.Series();
    XYChart.Series algorithmSeries2 = new XYChart.Series();

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
        final ScatterChart<Number,Number> algorithmLineChart = new ScatterChart<>(algorithmxAxis, algorithmyAxis);

        algorithmLineChart.setLayoutX(180);
        algorithmLineChart.setLayoutY(205);
        algorithmLineChart.setPrefSize(420,260);
        algorithmLineChart.getData().add(algorithmSeries);
        algorithmLineChart.getData().add(algorithmSeries1);
        algorithmLineChart.getData().add(algorithmSeries2);
        algorithmxAxis.setTickLabelsVisible(false);
        algorithmyAxis.setTickLabelsVisible(false);
        algorithmSeries.setName("chosen anomaly detector line");
        algorithmSeries1.setName("regular flight values");
        algorithmSeries2.setName("anomaly flight values");
        ret.add(algorithmLineChart);

        return ret;
    }
}
