package ViewModel;

import Model.Model;
import javafx.beans.property.*;

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
    StringProperty chosenXMLFilePath;
    StringProperty chosenCSVFilePath;
    public ArrayList<String> colsNames;

    public String time;
    public float aileronstep;
    public float elevatorstep;

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
        colsNames = new ArrayList<>();
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
        {
            time = model.gettime();
            setChanged();
            notifyObservers("time");
        }
        if (p.intern() == "aileron")
        {
            aileronstep = model.getAileronstep();
            setChanged();
            notifyObservers("aileron");
        }
        if (p.intern() == "elevator")
        {
            elevatorstep = model.getElevatorstep();
            setChanged();
            notifyObservers("elevator");
        }
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
}
