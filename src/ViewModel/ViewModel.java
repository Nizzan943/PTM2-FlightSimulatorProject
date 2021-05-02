package ViewModel;

import Model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer
{
    Model model = new Model();
    StringProperty loadXMLResult;
    StringProperty openCSVResult;

    StringProperty chosenXMLFilePath;
    StringProperty chosenCSVFilePath;
    public ArrayList<String> colsNames;

    public StringProperty chosenXMLFilePathProperty() {
        return chosenXMLFilePath;
    }
    public StringProperty chosenCSVFilePathProperty() {
        return chosenCSVFilePath;
    }

    public ViewModel(Model model) {
        this.model = model;
        loadXMLResult = new SimpleStringProperty();
        openCSVResult = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
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

    }

}
