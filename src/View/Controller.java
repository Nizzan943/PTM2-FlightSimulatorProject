package View;

import Model.AdditionalSettings;
import Model.HandleXML;
import Model.TimeSeries;
import Model.UserSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;

public class Controller {
    String XML_file;

    @FXML
    ListView listView;

    public void openCSV() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open CSV file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chozen = fc.showOpenDialog(null);
        if (chozen != null) {
            TimeSeries timeSeries = new TimeSeries(chozen.getAbsolutePath());
            for (TimeSeries.col col : timeSeries.getCols()) {
                listView.getItems().add(col.getName());
            }
        }
    }

    public void WrongFormatAlert()
    {
        JOptionPane.showMessageDialog(null,
                "Wrong format of file, please check your format and try again",
                "ERROR",
                JOptionPane.WARNING_MESSAGE);

    }

    public void MissingArgumentAlert()
    {
        JOptionPane.showMessageDialog(null,
                "Missing Argument, please check your settings and try again",
                "ERROR",
                JOptionPane.WARNING_MESSAGE);
    }

    public void SuccessAlert()
    {
        JOptionPane.showMessageDialog(null,
                "The file was uploaded successfully",
                "Success",
                JOptionPane.WARNING_MESSAGE);
    }

    public void LoadXML() throws Exception {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chozen = fc.showOpenDialog(null);
        if (chozen != null)
        {
            HandleXML handleXML = new HandleXML();
            handleXML.deserializeFromXML(chozen.getAbsolutePath());
            if (handleXML.WrongFormatAlert == true)
                WrongFormatAlert();
            else if (handleXML.MissingArgumentsAlert == true)
                MissingArgumentAlert();
            else {
                SuccessAlert();
                XML_file = chozen.getAbsolutePath();
            }
        }
    }

}
