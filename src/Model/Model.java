package Model;

import Server.HandleXML;
import Server.TimeSeries;
import View.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable
{
    public static HandleXML XML_settings;
    public static String CSVpath;

    public String getResultLoadXML() {
        return resultLoadXML;
    }

    private String resultLoadXML;

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
         notifyObservers();
     }

}
