package ViewModel;

import Model.Model;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer
{
    Model model;
    StringProperty loadXMLResult;
    StringProperty openCSVResult;
    DoubleProperty minAileron;
    DoubleProperty maxAileron;
    DoubleProperty minElevator;
    DoubleProperty maxElevator;
    DoubleProperty maxTimeSlider;
    StringProperty chosenXMLFilePath;
    StringProperty chosenCSVFilePath;
    public ArrayList<String> colsNames;

    StringProperty time;
    FloatProperty aileronstep;
    FloatProperty elevatorstep;

    StringProperty altimeterstep;
    StringProperty airspeedstep;
    StringProperty directionstep;
    StringProperty pitchstep;
    StringProperty rollstep;
    StringProperty yawstep;

    public StringProperty chosenXMLFilePathProperty() {
        return chosenXMLFilePath;
    }
    public StringProperty chosenCSVFilePathProperty() {
        return chosenCSVFilePath;
    }
    public DoubleProperty getMinAileron() {
        return minAileron;
    }
    public DoubleProperty getMaxAileron() {
        return maxAileron;
    }

    public DoubleProperty getMinElevator() {
        return minElevator;
    }
    public DoubleProperty getMaxElevator() {
        return maxElevator;
    }

    public DoubleProperty getMaxTimeSlider() {
        return maxTimeSlider;
    }


    public StringProperty getTime() {
        return time;
    }

    public FloatProperty getAileronstep()
    {
        return aileronstep;
    }

    public FloatProperty getElevatorstep()
    {
        return elevatorstep;
    }

    public StringProperty getAltimeterstep()
    {
        return altimeterstep;
    }

    public StringProperty getAirspeedstep()
    {
        return airspeedstep;
    }

    public StringProperty getDirectionstep()
    {
        return directionstep;
    }

    public StringProperty getPitchstep()
    {
        return pitchstep;
    }

    public StringProperty getRollstep()
    {
        return rollstep;
    }

    public StringProperty getYawstep()
    {
        return yawstep;
    }

    public ViewModel(Model model) {
        this.model = model;
        loadXMLResult = new SimpleStringProperty();
        openCSVResult = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
        minAileron = new SimpleDoubleProperty();
        maxAileron = new SimpleDoubleProperty();
        minElevator = new SimpleDoubleProperty();
        maxElevator = new SimpleDoubleProperty();
        maxTimeSlider = new SimpleDoubleProperty();
        colsNames = new ArrayList<>();
        time = new SimpleStringProperty();
        aileronstep = new SimpleFloatProperty();
        elevatorstep = new SimpleFloatProperty();
        altimeterstep = new SimpleStringProperty();
        airspeedstep = new SimpleStringProperty();
        directionstep = new SimpleStringProperty();
        pitchstep = new SimpleStringProperty();
        rollstep = new SimpleStringProperty();
        yawstep = new SimpleStringProperty();
    }

    public StringProperty loadXMLProperty() {
        return loadXMLResult;
    }


    public StringProperty OpenCSVProperty() {
        return openCSVResult;
    }

    public void VMLoadXML()
    {
        model.ModelLoadXML(chosenXMLFilePath.getValue());
    }

    public void VMOpenCSV()
    {
        model.ModelOpenCSV(chosenCSVFilePath.getValue());
    }

    @Override
    public void update(Observable o, Object arg)
    {
        String p = (String)arg;
        if (p.intern() == "resultLoadXML")
        {
            if (model.getResultLoadXML().intern() == "WrongFormatAlert")
                loadXMLResult.set("WrongFormatAlert");
            if (model.getResultLoadXML().intern() == "MissingArgumentAlert")
                loadXMLResult.set("MissingArgumentAlert");
            if (model.getResultLoadXML().intern() == "SuccessAlert")
                loadXMLResult.set("SuccessAlert");
        }
        if (p.intern() == "resultOpenCSV")
        {
            if (model.getResultOpenCSV().intern() == "Missing Arguments")
                openCSVResult.set("Missing Arguments");
            if (model.getResultOpenCSV().intern() == "Incompatibility with XML file")
                openCSVResult.set("Incompatibility with XML file");
            if (model.getResultOpenCSV().intern() == "OK")
            {
                openCSVResult.set("OK");
                for (String name : model.getColsNames())
                {
                    colsNames.add(name);
                }
                setChanged();
                notifyObservers("colNames");
            }
        }
        if (p.intern() == "time")
            time.set(model.gettime());
        if (p.intern() == "aileron")
            aileronstep.set(model.getAileronstep());
        if (p.intern() == "elevator")
            elevatorstep.set(model.getElevatorstep());
        if (p.intern() == "altimeter")
            altimeterstep.set(model.getAltimeterstep() + "");
        if (p.intern() == "airspeed")
            airspeedstep.set(model.getAirspeedstep() + "");
        if (p.intern() == "direction")
            directionstep.set(model.getDirectionstep() + "");
        if (p.intern() == "pitch")
            pitchstep.set(model.getPitchstep() + "");
        if (p.intern() == "roll")
            rollstep.set(model.getRollstep() + "");
        if (p.intern() == "yaw")
            yawstep.set(model.getYawstep() + "");
    }

    public void VMplay()
    {
        model.modelPlay();
    }

    public void VMGetChoice(String speed)
    {
        model.modelGetChoice(speed);
    }

    public void VMpause()
    {
        model.modelpause();
    }

    public void VMplus15()
    {
        model.modelPlus15();
    }

    public void VMminus15()
    {
        model.modelMinus15();
    }

    public void VMminus30()
    {
        model.modelMinus30();
    }

    public void VMplus30()
    {
        model.modelPlus30();
    }

    public void VMsetMinAileron()
    {
        minAileron.setValue(model.modelSetMinAileron());
    }

    public void VMsetMaxAileron()
    {
        maxAileron.setValue(model.modelSetMaxAileron());
    }

    public void VMsetMinElevator()
    {
        minElevator.setValue(model.modelSetMinElevator());
    }

    public void VMsetMaxElevator()
    {
        maxElevator.setValue(model.modelSetMaxElevator());
    }

    public void setMaxTimeSlider()
    {
        maxTimeSlider.setValue(model.modelSetMaxTimeSlider());
    }

    public void VMtimeslider(double second)
    {
        model.modelTimeSlider(second);
    }

    public void VMstop()
    {
        model.modelStop();
    }
}
