package View;

import Server.PluginLoader;
import Server.TimeSeriesAnomalyDetector;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URLClassLoader;

public class Controller extends Pane implements Observer, Initializable {

    @FXML
    Pane board;

    @FXML
    MyMenu myMenu;

    @FXML
    MyListView myListView;

    @FXML
    MyButtons myButtons;

    @FXML
    MyJoystick myJoystick;

    @FXML
    MyClocksPannel myClocksPannel;

    @FXML
    MyGraphs myGraphs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.getChildren().add(myMenu.set());
        myMenu.loadXML.setOnAction((e) -> LoadXML());
        myMenu.loadAlgorithm.setOnAction((e) -> loadAlgorithm());

        board.getChildren().addAll(myListView.set());
        myListView.open.setOnAction((e) -> openCSV());
        myListView.listView.setOnMouseClicked((e) -> setLineCharts((String)myListView.listView.getSelectionModel().getSelectedItem()));

        board.getChildren().addAll(myButtons.set());
        myButtons.play.setOnAction((e) -> Play());
        myButtons.pause.setOnAction((e) -> Pause());
        myButtons.stop.setOnAction((e) -> Stop());
        myButtons.plus15.setOnAction((e) -> Plus15());
        myButtons.minus15.setOnAction((e) -> Minus15());
        myButtons.plus30.setOnAction((e) -> Plus30());
        myButtons.minus30.setOnAction((e) -> Minus30());
        myButtons.playSpeedDropDown.setOnAction((e) -> GetChoice()); //GetChoice func

        board.getChildren().addAll(myJoystick.set());

        board.getChildren().addAll(myClocksPannel.set());

        board.getChildren().addAll(myGraphs.set());
    }

    ArrayList<String> colsNames = new ArrayList<>();

    ViewModel viewModel;

    String speed;

    StringProperty resultOpenCSV;
    StringProperty chosenCSVFilePath;
    StringProperty time;
    StringProperty resultLoadXML;
    StringProperty chosenXMLFilePath;

    DoubleProperty minRudder;
    DoubleProperty maxRudder;
    DoubleProperty minThrottle;
    DoubleProperty maxThrottle;
    DoubleProperty maxtimeSlider;

    FloatProperty rudderstep;
    FloatProperty throttlestep;
    FloatProperty aileronstep;
    FloatProperty elevatorstep;
    FloatProperty altimeterstep;
    FloatProperty airspeedstep;
    FloatProperty directionstep;
    FloatProperty pitchstep;
    FloatProperty rollstep;
    FloatProperty yawstep;
    FloatProperty colValues;
    FloatProperty coralatedColValue;

    int numOfRow = 0;
    int playStart = 0;

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;

        resultOpenCSV = new SimpleStringProperty();
        resultOpenCSV.bind(viewModel.getOpenCSVResult());

        resultLoadXML = new SimpleStringProperty();
        resultLoadXML.bind(viewModel.getLoadXMLResult());

        chosenCSVFilePath = new SimpleStringProperty();
        viewModel.getChosenCSVFilePath().bind(chosenCSVFilePath);

        chosenXMLFilePath = new SimpleStringProperty();
        viewModel.getChosenXMLFilePath().bind(chosenXMLFilePath);

        minRudder = new SimpleDoubleProperty();
        minRudder.bind(viewModel.getMinRudder());

        maxRudder = new SimpleDoubleProperty();
        maxRudder.bind(viewModel.getMaxRudder());

        minThrottle = new SimpleDoubleProperty();
        minThrottle.bind(viewModel.getMinThrottle());

        maxThrottle = new SimpleDoubleProperty();
        maxThrottle.bind(viewModel.getMaxThrottle());

        maxtimeSlider = new SimpleDoubleProperty();
        maxtimeSlider.bind(viewModel.getMaxTimeSlider());

        time = new SimpleStringProperty();
        time.bind(viewModel.getTime());

        rudderstep = new SimpleFloatProperty();
        rudderstep.bind(viewModel.getRudderstep());

        throttlestep = new SimpleFloatProperty();
        throttlestep.bind(viewModel.getThrottlestep());

        altimeterstep = new SimpleFloatProperty();
        altimeterstep.bind(viewModel.getAltimeterstep());

        airspeedstep = new SimpleFloatProperty();
        airspeedstep.bind(viewModel.getAirspeedstep());

        directionstep = new SimpleFloatProperty();
        directionstep.bind(viewModel.getDirectionstep());

        pitchstep = new SimpleFloatProperty();
        pitchstep.bind(viewModel.getPitchstep());

        rollstep = new SimpleFloatProperty();
        rollstep.bind(viewModel.getRollstep());

        yawstep = new SimpleFloatProperty();
        yawstep.bind(viewModel.getYawstep());

        aileronstep = new SimpleFloatProperty();
        aileronstep.bind(viewModel.getAileronstep());

        elevatorstep = new SimpleFloatProperty();
        elevatorstep.bind(viewModel.getElevatorstep());

        colValues = new SimpleFloatProperty();
        colValues.bind(viewModel.getColValues());

        coralatedColValue = new SimpleFloatProperty();
        coralatedColValue.bind(viewModel.getCoralatedColValue());
    }

    public void openCSV() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setTitle("Open CSV file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chozen = fc.showOpenDialog(null);
        chosenCSVFilePath.set(chozen.getAbsolutePath());
        if (chozen != null) {
            viewModel.VMOpenCSV();
            if (resultOpenCSV.getValue().intern() == "Missing Arguments") {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Arguments");
                alert.setContentText("Please check your settings and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "Incompatibility with XML file") {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incompatibility with XML file");
                alert.setContentText("Please check your file and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "OK") {
                for (String names : viewModel.getColsNames()) {
                    myListView.listView.getItems().add(names);
                }
                myButtons.timer.setText("00:00:00.000");
                viewModel.setMaxTimeSlider();
                myButtons.slider.setMax(maxtimeSlider.getValue());
                myClocksPannel.altimeter.setText("altimeter: 0.0");
                myClocksPannel.airspeed.setText("airspeed: 0.0");
                myClocksPannel.direction.setText("direction: 0.0");
                myClocksPannel.pitch.setText("pitch: 0.0");
                myClocksPannel.roll.setText("roll: 0.0");
                myClocksPannel.yaw.setText("yaw: 0.0");
            }
        }
    }

    public void setListeners()
    {
        colValues.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> myGraphs.leftSeries.getData().add((new XYChart.Data(numOfRow, colValues.getValue()))));
            numOfRow++;
        });


        coralatedColValue.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> myGraphs.rightSeries.getData().add((new XYChart.Data(numOfRow, coralatedColValue.getValue()))));
            numOfRow++;
        });

        aileronstep.addListener((observable, oldValue, newValue) -> myJoystick.innerCircle.setCenterX(aileronstep.getValue() * 100));

        elevatorstep.addListener((observable, oldValue, newValue) -> myJoystick.innerCircle.setCenterY(elevatorstep.getValue() * 100));

        myButtons.slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (((double)oldValue + 1 != (double)newValue) && (((double)oldValue + 0.5) != (double)newValue) && (((double)oldValue + 1.5) != (double)newValue) && (((double)oldValue + 2) != (double)newValue))
                viewModel.VMtimeslider(myButtons.slider.getValue());
        });

        altimeterstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.altimeter.setText("altimeter: " + altimeterstep.getValue())));

        airspeedstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.airspeed.setText("airspeed: " + airspeedstep.getValue())));

        directionstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.direction.setText("direction: " + directionstep.getValue())));

        pitchstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.pitch.setText("pitch: " + pitchstep.getValue())));

        rollstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.roll.setText("roll: " + rollstep.getValue())));

        yawstep.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> myClocksPannel.yaw.setText("yaw: " + yawstep.getValue())));

        time.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> myButtons.timer.setText(time.getValue()));
            if (speed.intern() == "x1.0")
                myButtons.slider.setValue(myButtons.slider.getValue() + 1);
            if (speed.intern() == "x2.0")
                myButtons.slider.setValue(myButtons.slider.getValue() + 2);
            if (speed.intern() == "x1.5")
                myButtons.slider.setValue(myButtons.slider.getValue() + 1.5);
            if (speed.intern() == "x0.5")
                myButtons.slider.setValue(myButtons.slider.getValue() + 0.5);
        });

        rudderstep.addListener((observable, oldValue, newValue) -> myJoystick.rudder.setValue(rudderstep.getValue()));

        throttlestep.addListener((observable, oldValue, newValue) -> myJoystick.throttle.setValue(throttlestep.getValue()));
    }

    public void LoadXML() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chosen = fc.showOpenDialog(null);
        chosenXMLFilePath.set(chosen.getAbsolutePath());
        if (chosen != null) {
            viewModel.VMLoadXML();
            if (resultLoadXML.getValue().equals("WrongFormatAlert"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong format of XML");
                alert.setContentText("Please check your format and try again");
                alert.showAndWait();
            }
            else if (resultLoadXML.getValue().equals("MissingArgumentAlert"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Arguments");
                alert.setContentText("Please check your settings and try again");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("The file was uploaded successfully");
                alert.setContentText(null);
                alert.showAndWait();
                viewModel.VMsetMinRudder();
                myJoystick.rudder.setMin(minRudder.getValue());
                viewModel.VMsetMaxRudder();
                myJoystick.rudder.setMax(maxRudder.getValue());
                viewModel.VMsetMinThrottle();
                myJoystick.throttle.setMin(minThrottle.getValue());
                viewModel.VMsetMaxThrottle();
                myJoystick.throttle.setMax(maxThrottle.getValue());

                setListeners();
            }
        }
    }

    public void Play() {
        if (playStart == 0) {
            myButtons.playSpeedDropDown.setValue("x1.0");
            playStart = 1;
        }
        viewModel.VMplay();
    }

    public void GetChoice() {
        speed = (String) myButtons.playSpeedDropDown.getValue();
        viewModel.VMGetChoice(speed);
    }

    public void Stop()
    {
        myButtons.timer.setText("00:00:00.000");
        myClocksPannel.altimeter.setText("altimeter: 0.0");
        myClocksPannel.airspeed.setText("airspeed: 0.0");
        myClocksPannel.direction.setText("direction: 0.0");
        myClocksPannel.pitch.setText("pitch: 0.0");
        myClocksPannel.roll.setText("roll: 0.0");
        myClocksPannel.yaw.setText("yaw: 0.0");
        myButtons.slider.setValue(0);
        myJoystick.innerCircle.setCenterX(0);
        myJoystick.innerCircle.setCenterY(0);
        myJoystick.throttle.setValue(0);
        myJoystick.rudder.setValue(0);
        viewModel.VMstop();
    }

    public void Pause() {
        viewModel.VMpause();
    }

    public void Plus15() {
        viewModel.VMplus15();
        myButtons.slider.setValue(myButtons.slider.getValue() + 15);
    }

    public void Minus15() {
        viewModel.VMminus15();
        myButtons.slider.setValue(myButtons.slider.getValue() - 15);
    }

    public void Minus30() {
        viewModel.VMminus30();
        myButtons.slider.setValue(myButtons.slider.getValue() - 30);
    }

    public void Plus30() {
        viewModel.VMplus30();
        myButtons.slider.setValue(myButtons.slider.getValue() + 30);
    }

    public void setLineCharts (String colName)
    {
        setLeftLineChart(colName);
        setRightLineChart(colName);
        setAlgorithmLineChart(colName);
    }

    public void setLeftLineChart(String colName)
    {
        viewModel.VMsetLeftLineChart(colName);
    }

    public void setRightLineChart(String colName)
    {
        viewModel.VMsetRightLineChart(colName);
    }

    public void setAlgorithmLineChart(String colName)
    {
        viewModel.VMsetAlgorithmLineChart(colName);
    }

    public void loadAlgorithm()
    {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Load Algorithm");
        dialog.setHeaderText("Choose Algorithm");

        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField classDirectory = new TextField();
        classDirectory.setPromptText("class directory");
        TextField className = new TextField();
        className.setPromptText("class name");

        grid.add(new Label("class directory:"), 0, 0);
        grid.add(classDirectory, 1, 0);
        grid.add(new Label("class name:"), 0, 1);
        grid.add(className, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();

        while (classDirectory.getText() == null);
        String resultClassDirectory = classDirectory.getText();

        while (className.getText() == null);
        String resultClassName = className.getText();

        viewModel.VMLoadAlgorithm(resultClassDirectory, resultClassName);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
