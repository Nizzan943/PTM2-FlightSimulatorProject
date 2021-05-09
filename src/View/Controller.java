package View;

import Server.PluginLoader;
import Server.TimeSeriesAnomalyDetector;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.net.URLClassLoader;

public class Controller extends Pane implements Observer, Initializable, PluginLoader<TimeSeriesAnomalyDetector> {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.getChildren().add(myMenu.set());
        myMenu.loadXML.setOnAction((e) -> LoadXML()); //load XML func
        board.getChildren().addAll(myListView.set());
        myListView.open.setOnAction((e) -> openCSV()); //open CSV func
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
    }

    StringProperty resultOpenCSV;
    StringProperty chosenCSVFilePath;
    DoubleProperty minRudder;
    DoubleProperty maxRudder;
    DoubleProperty minThrottle;
    DoubleProperty maxThrottle;
    DoubleProperty maxtimeSlider;
    ArrayList<String> colsNames = new ArrayList<>();
    ViewModel viewModel;

    StringProperty time;
    FloatProperty rudderstep;
    FloatProperty throttlestep;

    StringProperty altimeterstep;
    StringProperty airspeedstep;
    StringProperty directionstep;
    StringProperty pitchstep;
    StringProperty rollstep;
    StringProperty yawstep;

    String speed;


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        resultOpenCSV = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
        viewModel.getChosenCSVFilePathProperty().bind(chosenCSVFilePath);
        resultOpenCSV.bind(viewModel.OpenCSVProperty());
        resultLoadXML = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        viewModel.getChosenXMLFilePathProperty().bind(chosenXMLFilePath);
        resultLoadXML.bind(viewModel.loadXMLProperty());
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

        altimeterstep = new SimpleStringProperty();
        altimeterstep.bind(viewModel.getAltimeterstep());
        airspeedstep = new SimpleStringProperty();
        airspeedstep.bind(viewModel.getAirspeedstep());
        directionstep = new SimpleStringProperty();
        directionstep.bind(viewModel.getDirectionstep());
        pitchstep = new SimpleStringProperty();
        pitchstep.bind(viewModel.getPitchstep());
        rollstep = new SimpleStringProperty();
        rollstep.bind(viewModel.getRollstep());
        yawstep = new SimpleStringProperty();
        yawstep.bind(viewModel.getYawstep());

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
                for (String names : colsNames)
                    myListView.listView.getItems().add(names);
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

    StringProperty resultLoadXML;
    StringProperty chosenXMLFilePath;


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
                WrongFormatAlert();
            else if (resultLoadXML.getValue().equals("MissingArgumentAlert"))
                MissingArgumentAlert();
            else {
                SuccessAlert();
                viewModel.VMsetMinRudder();
                myJoystick.rudder.setMin(minRudder.getValue());
                viewModel.VMsetMaxRudder();
                myJoystick.rudder.setMax(maxRudder.getValue());
                viewModel.VMsetMinThrottle();
                myJoystick.throttle.setMin(minThrottle.getValue());
                viewModel.VMsetMaxThrottle();
                myJoystick.throttle.setMax(maxThrottle.getValue());
            }
        }
    }

    public void WrongFormatAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong format of XML");
        alert.setContentText("Please check your format and try again");

        alert.showAndWait();
    }

    public void MissingArgumentAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Missing Arguments");
        alert.setContentText("Please check your settings and try again");

        alert.showAndWait();
    }

    public void SuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("The file was uploaded successfully");
        alert.setContentText(null);

        alert.showAndWait();
    }

    int flag = 0;

    public void Play() {
        myButtons.slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (observable.equals(onMouseDraggedProperty()))
                    viewModel.VMtimeslider(myButtons.slider.getValue());
            }
        });

        altimeterstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.altimeter.setText("altimeter: " + altimeterstep.getValue()));
            }
        });

        airspeedstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.airspeed.setText("airspeed: " + airspeedstep.getValue()));
            }
        });

        directionstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.direction.setText("direction: " + directionstep.getValue()));
            }
        });

        pitchstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.pitch.setText("pitch: " + pitchstep.getValue()));
            }
        });

        rollstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.roll.setText("roll: " + rollstep.getValue()));
            }
        });

        yawstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myClocksPannel.yaw.setText("yaw: " + yawstep.getValue()));
            }
        });

        time.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> myButtons.timer.setText(time.getValue()));

                if (speed.intern() == "x1.0")
                    myButtons.slider.setValue(myButtons.slider.getValue() + 1);
                if (speed.intern() == "x2.0")
                    myButtons.slider.setValue(myButtons.slider.getValue() + 2);
                if (speed.intern() == "x1.5")
                    myButtons.slider.setValue(myButtons.slider.getValue() + 1.5);
                if (speed.intern() == "x0.5")
                    myButtons.slider.setValue(myButtons.slider.getValue() + 0.5);
            }
        });

        rudderstep.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myJoystick.rudder.setValue(rudderstep.getValue());
            }
        });

        throttlestep.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myJoystick.throttle.setValue(throttlestep.getValue());
            }
        });

        if (flag == 0) {
            myButtons.playSpeedDropDown.setValue("x1.0");
            flag = 1;
        }
        viewModel.VMplay();

    }

    public void GetChoice() {
        speed = (String) myButtons.playSpeedDropDown.getValue();
        viewModel.VMGetChoice(speed);
    }

    public void Stop() {
        myButtons.slider.setValue(0);
        myButtons.timer.setText("00:00:00.000");
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

    @Override
    public void update(Observable o, Object arg) {
        String p = (String) arg;
        if (p.intern() == "colNames") {
            for (String name : viewModel.getColsNames()) {
                colsNames.add(name);
            }
        }
    }

    @Override
    public void LoadClass(String path, String className) {

        File file = new File(path);

        URL url;

        URL[] urlArray;

        FileInputStream fileInputStream = null;

        URLClassLoader urlClassLoader = null;

        Class<?> aClass = null;

        try {
            url = file.toURI().toURL();

            urlArray = new URL[]{url};

            urlClassLoader = URLClassLoader.newInstance(urlArray);

        } catch (MalformedURLException e) {

        }

        try {
            fileInputStream = new FileInputStream(path);

        } catch (FileNotFoundException e) {

        }

        ClassLoader.getSystemClassLoader().getResourceAsStream(fileInputStream.toString());

        try {
            aClass = urlClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
        }

        try {
            aClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
        }
    }

}
