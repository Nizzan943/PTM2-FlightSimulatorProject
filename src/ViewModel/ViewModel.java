package ViewModel;

import Model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer
{
    Model model = new Model();
    StringProperty loadXML;

    public StringProperty chosenFilePathProperty() {
        return chosenFilePath;
    }

    StringProperty chosenFilePath;

    public ViewModel(Model model) {
        this.model = model;
        loadXML = new SimpleStringProperty();
        chosenFilePath = new SimpleStringProperty();
    }

    public StringProperty loadXMLProperty() {
        return loadXML;
    }

    public void VMLoadXML()
    {
        model.ModelLoadXML(chosenFilePath.toString());
    }

    @Override
    public void update(Observable o, Object arg)
    {
        String p = (String)arg;
        if (p.intern() == "resultLoadXML")
        {
            if (model.getResultLoadXML().intern() == "WrongFormatAlert")
                loadXML.set("WrongFormatAlert");
            if (model.getResultLoadXML().intern() == "MissingArgumentAlert")
                loadXML.set("MissingArgumentAlert");
            if (model.getResultLoadXML().intern() == "SuccessAlert")
                loadXML.set("SuccessAlert");
        }
    }
}
