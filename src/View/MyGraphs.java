package View;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyGraphs extends Pane
{
    XYChart.Series leftSeries = new XYChart.Series();
    final NumberAxis leftxAxis = new NumberAxis();
    final NumberAxis leftyAxis = new NumberAxis();
    final LineChart<Number,Number> leftLineChart = new LineChart<Number,Number>(leftxAxis, leftyAxis);

    XYChart.Series rightSeries = new XYChart.Series();
    final NumberAxis rightxAxis = new NumberAxis();
    final NumberAxis rightyAxis = new NumberAxis();
    final LineChart<Number,Number> rightLineChart = new LineChart<Number,Number>(rightxAxis, rightyAxis);

    XYChart.Series algorithmSeries = new XYChart.Series();
    XYChart.Series algorithmSeries1 = new XYChart.Series();
    XYChart.Series algorithmSeries2 = new XYChart.Series();
    final NumberAxis algorithmxAxis = new NumberAxis();
    final NumberAxis algorithmyAxis = new NumberAxis();
    final LineChart<Number,Number> algorithmLineChart = new LineChart<>(algorithmxAxis, algorithmyAxis);

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();

        leftLineChart.setLayoutX(180);
        leftLineChart.setLayoutY(27);
        leftLineChart.setPrefSize(220,190);
        leftLineChart.setCreateSymbols(false);
        leftLineChart.getData().add(leftSeries);
        leftxAxis.setTickLabelsVisible(false);
        leftyAxis.setTickLabelsVisible(false);
        leftSeries.setName("parameter values");
        setLineToBlack(leftLineChart, leftSeries);
        Platform.runLater(() -> leftLineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: WHITE;"));
        ret.add(leftLineChart);

        rightLineChart.setLayoutX(380);
        rightLineChart.setLayoutY(27);
        rightLineChart.setPrefSize(220,190);
        rightLineChart.setCreateSymbols(false);
        rightLineChart.getData().add(rightSeries);
        rightxAxis.setTickLabelsVisible(false);
        rightyAxis.setTickLabelsVisible(false);
        rightSeries.setName("correlated parameter values");
        setLineToBlack(rightLineChart, rightSeries);
        Platform.runLater(() -> rightLineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: WHITE;"));
        ret.add(rightLineChart);

        algorithmLineChart.setLayoutX(180);
        algorithmLineChart.setLayoutY(205);
        algorithmLineChart.setPrefSize(420,260);
        algorithmLineChart.getData().add(algorithmSeries);
        algorithmLineChart.getData().add(algorithmSeries1);
        algorithmLineChart.getData().add(algorithmSeries2);
        algorithmxAxis.setTickLabelsVisible(false);
        algorithmyAxis.setTickLabelsVisible(false);
        algorithmLineChart.setAnimated(false);
        algorithmLineChart.setCreateSymbols(true);
        algorithmSeries.setName("algorithm line");
        algorithmSeries1.setName("regular flight");
        algorithmSeries2.setName("anomaly flight");
        Platform.runLater(() -> algorithmLineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: WHITE;"));
        ret.add(algorithmLineChart);

        return ret;
    }

    public void setLineToBlack(LineChart<Number, Number> lineChart, XYChart.Series lineSeries)
    {
        Platform.runLater(()
                -> {

            Set<Node> nodes = lineChart.lookupAll(".series" + 0);
            for (Node n : nodes) {
                n.setStyle("-fx-background-color: black, white;\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 5px;\n"
                        + "    -fx-padding: 5px;");
            }

            lineSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;");
        });
    }
}
