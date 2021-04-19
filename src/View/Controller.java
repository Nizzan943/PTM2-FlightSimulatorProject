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
import java.sql.Time;
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
    HandleXML XML_settings;

    @FXML
    ListView listView;

    public void openCSV() {
        int flag = 0;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setTitle("Open CSV file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chozen = fc.showOpenDialog(null);
        if (chozen != null) {
            TimeSeries timeSeries = new TimeSeries(chozen.getAbsolutePath());
            if (timeSeries.getCols().length != 2) {
                JOptionPane.showMessageDialog(null,
                        "Missing Arguments\n" +
                                "Please check your file and try again",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            for (int i = 0; i < timeSeries.getCols().length; i++)
            {
                flag = 0;
                if (timeSeries.getCols()[i].getName().intern() != XML_settings.PropertyList.get(i).getRealName().intern())
                {
                    JOptionPane.showMessageDialog(null,
                            "Incompatibility with XML file\n" +
                                    "Please check your file and try again",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    flag = 1;
                    break;
                }
            }
            if (timeSeries.getCols().length == 2 && flag == 0) {
                for (TimeSeries.col col : timeSeries.getCols()) {
                    listView.getItems().add(col.getName());
                }
            }
        }
    }

    public void WrongFormatAlert()
    {
        JOptionPane.showMessageDialog(null,
                "Wrong format of XML\n" +
                        "Please check your format and try again",
                "Error",
                JOptionPane.ERROR_MESSAGE);

    }

    public void MissingArgumentAlert()
    {
        JOptionPane.showMessageDialog(null,
                "Missing ArgumentS\n" +
                        "Please check your settings and try again",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void SuccessAlert()
    {
        JOptionPane.showMessageDialog(null,
                "The file was uploaded successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void LoadXML() throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
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
                XML_settings = handleXML;
            }
        }
    }

}
