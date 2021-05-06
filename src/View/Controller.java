package View;

import Server.HandleXML;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        board.getChildren().add(myMenu.set());
        myMenu.m1.setOnAction((e)->LoadXML()); //load XML func
        board.getChildren().addAll(myListView.set());
        myListView.open.setOnAction((e)->openCSV()); //open CSV func
        board.getChildren().addAll(myButtons.set());
        myButtons.play.setOnAction((e)->Play());
        myButtons.pause.setOnAction((e)->Pause());
        myButtons.stop.setOnAction((e)->Stop());
        myButtons.plus15.setOnAction((e)->Plus15());
        myButtons.minus15.setOnAction((e)->Minus15());
        myButtons.plus30.setOnAction((e)->Plus30());
        myButtons.minus30.setOnAction((e)->Minus30());
        myButtons.playSpeedDropDown.setOnAction((e)->GetChoice()); //GetChoice func
        board.getChildren().addAll(myJoystick.set());
        board.getChildren().addAll(myClocksPannel.set());
    }

    StringProperty resultOpenCSV;
    StringProperty chosenCSVFilePath;
    DoubleProperty minAileron;
    DoubleProperty maxAileron;
    DoubleProperty minElevator;
    DoubleProperty maxElevator;
    DoubleProperty maxtimeSlider;
    ArrayList <String> colsNames = new ArrayList<>();
    ViewModel viewModel;

    StringProperty time;
    FloatProperty aileronstep;
    FloatProperty elevatorstep;

    StringProperty altimeterstep;
    StringProperty airspeedstep;
    StringProperty directionstep;
    StringProperty pitchstep;
    StringProperty rollstep;
    StringProperty yawstep;


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        resultOpenCSV = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
        viewModel.chosenCSVFilePathProperty().bind(chosenCSVFilePath);
        resultOpenCSV.bind(viewModel.OpenCSVProperty());
        resultLoadXML = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        viewModel.chosenXMLFilePathProperty().bind(chosenXMLFilePath);
        resultLoadXML.bind(viewModel.loadXMLProperty());
        minAileron = new SimpleDoubleProperty();
        minAileron.bind(viewModel.getMinAileron());
        maxAileron = new SimpleDoubleProperty();
        maxAileron.bind(viewModel.getMaxAileron());
        minElevator = new SimpleDoubleProperty();
        minElevator.bind(viewModel.getMinElevator());
        maxElevator = new SimpleDoubleProperty();
        maxElevator.bind(viewModel.getMaxElevator());
        maxtimeSlider = new SimpleDoubleProperty();
        maxtimeSlider.bind(viewModel.getMaxTimeSlider());

        time = new SimpleStringProperty();
        time.bind(viewModel.getTime());

        aileronstep = new SimpleFloatProperty();
        aileronstep.bind(viewModel.getAileronstep());
        elevatorstep = new SimpleFloatProperty();
        elevatorstep.bind(viewModel.getElevatorstep());

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
            if (resultOpenCSV.getValue().intern() == "Missing Arguments")
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Arguments");
                alert.setContentText("Please check your settings and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "Incompatibility with XML file")
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incompatibility with XML file");
                alert.setContentText("Please check your file and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "OK")
            {
                for (String names: colsNames)
                    myListView.listView.getItems().add(names);
                myButtons.timer.setText("00:00:00.000");
                viewModel.setMaxTimeSlider();
                myButtons.slider.setMax(maxtimeSlider.getValue());
                myClocksPannel.altimeter.setText("altimeter: 0");
                myClocksPannel.airspeed.setText("airspeed: 0");
                myClocksPannel.direction.setText("direction: 0");
                myClocksPannel.pitch.setText("pitch: 0");
                myClocksPannel.roll.setText("roll: 0");
                myClocksPannel.yaw.setText("yaw: 0");
            }
        }
    }

    StringProperty resultLoadXML;
    StringProperty chosenXMLFilePath;



    public void LoadXML()  {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chosen = fc.showOpenDialog(null);
        chosenXMLFilePath.set(chosen.getAbsolutePath());
        if (chosen != null)
        {
            viewModel.VMLoadXML();
            if (resultLoadXML.getValue().equals("WrongFormatAlert"))
                WrongFormatAlert();
            else if (resultLoadXML.getValue().equals("MissingArgumentAlert"))
                MissingArgumentAlert();
            else {
                SuccessAlert();
                viewModel.VMsetMinAileron();
                myJoystick.aileron.setMin(minAileron.getValue());
                viewModel.VMsetMaxAileron();
                myJoystick.aileron.setMax(maxAileron.getValue());
                viewModel.VMsetMinElevator();
                myJoystick.elevator.setMin(minElevator.getValue());
                viewModel.VMsetMaxElevator();
                myJoystick.elevator.setMax(maxElevator.getValue());
            }
        }
    }

    public void WrongFormatAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong format of XML");
        alert.setContentText("Please check your format and try again");

        alert.showAndWait();
    }

    public void MissingArgumentAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Missing Arguments");
        alert.setContentText("Please check your settings and try again");

        alert.showAndWait();
    }

    public void SuccessAlert()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("The file was uploaded successfully");
        alert.setContentText(null);

        alert.showAndWait();
    }

    int flag = 0;
    public void Play()
    {
        myButtons.slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                viewModel.VMtimeslider(myButtons.slider.getValue());
            }
        });

        altimeterstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.altimeter.setText("altimeter: " + altimeterstep.getValue()));
            }
        });

        airspeedstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.airspeed.setText("airspeed: " + airspeedstep.getValue()));
            }
        });

        directionstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.direction.setText("direction: " + directionstep.getValue()));
            }
        });

        pitchstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.pitch.setText("pitch: " + pitchstep.getValue()));
            }
        });

        rollstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.roll.setText("roll: " + rollstep.getValue()));
            }
        });

        yawstep.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myClocksPannel.yaw.setText("yaw: " + yawstep.getValue()));
            }
        });

        time.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(()->myButtons.timer.setText(time.getValue()));
            }
        });

        aileronstep.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myJoystick.aileron.setValue(aileronstep.getValue());
            }
        });

        elevatorstep.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myJoystick.elevator.setValue(elevatorstep.getValue());
            }
        });

        if (flag == 0) {
            myButtons.playSpeedDropDown.setValue("x1.0");
            flag = 1;
        }
        viewModel.VMplay();

    }

    public void GetChoice()
    {
        String speed = (String)  myButtons.playSpeedDropDown.getValue();
        viewModel.VMGetChoice(speed);
    }

    public void Stop()
    {
        myButtons.slider.setValue(0);
        viewModel.VMstop();
    }

    public void Pause()
    {
        viewModel.VMpause();
    }

    public void Plus15()
    {
        viewModel.VMplus15();
    }

    public void Minus15()
    {
        viewModel.VMminus15();
    }

    public void Minus30()
    {
        viewModel.VMminus30();
    }

    public void Plus30()
    {
        viewModel.VMplus30();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        String p = (String)arg;
        if (p.intern() == "colNames")
        {
            for (String name: viewModel.colsNames)
            {
                colsNames.add(name);
            }
        }
    }
}
