package Model;

import Server.HandleXML;
import Server.TimeSeries;
import View.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable
{
    public static HandleXML XML_settings;
    public static String CSVpath;

    private String resultLoadXML;
    private String resultOpenCSV;
    ArrayList <String> colsNames = new ArrayList<>();

    public ArrayList<String> getColsNames() {
        return colsNames;
    }

    public String getResultOpenCSV() {
        return resultOpenCSV;
    }

    public String getResultLoadXML() {
        return resultLoadXML;
    }


     public void ModelLoadXML(String chosenPath)
     {
         HandleXML handleXML = new HandleXML();
         try {
             handleXML.deserializeFromXML(chosenPath);
         } catch (Exception e) {
             e.printStackTrace();
         }
         if (handleXML.WrongFormatAlert == true)
             resultLoadXML = "WrongFormatAlert";
         else if (handleXML.MissingArgumentsAlert == true)
             resultLoadXML = "MissingArgumentAlert";
         else {
             XML_settings = handleXML;
             resultLoadXML = "SuccessAlert";
         }
         setChanged();
         notifyObservers("resultLoadXML");
     }

    public void ModelOpenCSV(String chosenPath)
    {
        int flag1 = 0;
        TimeSeries timeSeries = new TimeSeries(chosenPath);
        if (timeSeries.getCols().length != 42)
            resultOpenCSV = "Missing Arguments";
        for (int i = 0; i < timeSeries.getCols().length; i++)
        {
            flag1 = 0;
            if (timeSeries.getCols()[i].getName().intern() != XML_settings.PropertyList.get(i).getRealName().intern())
            {
                resultOpenCSV = "Incompatibility with XML file";
                flag1 = 1;
                break;
            }
            for (int j = 0; j < timeSeries.getCols()[i].getFloats().size(); j++)
            {
                if (timeSeries.getCols()[i].getFloats().get(j) > XML_settings.PropertyList.get(i).getMax() || timeSeries.getCols()[i].getFloats().get(j) < XML_settings.PropertyList.get(i).getMin())
                {
                    resultOpenCSV = "Incompatibility with XML file";
                    flag1 = 1;
                    break;
                }
            }
            if (flag1 == 1)
                break;
        }
        if (timeSeries.getCols().length == 42 && flag1 == 0)
        {
            resultOpenCSV = "OK";
            for (TimeSeries.col col : timeSeries.getCols())
            {
                colsNames.add(col.getName());
            }
            CSVpath = chosenPath;
        }
        setChanged();
        notifyObservers("resultOpenCSV");
        notifyObservers("cols");
    }
}
