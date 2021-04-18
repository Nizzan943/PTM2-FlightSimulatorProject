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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
            for (UserSettings userSettings: handleXML.PropertyList) {
                if ((userSettings.getRealName() == null) || (userSettings.getAssosicateName() == null) || (userSettings.getMax() == 1000000) || (userSettings.getMin() == -1000000)) {
                    WrongFormatAlert();
                    break;
                }
            }
            if ((handleXML.additionalSettings.getAlgorithmFile() == null) || (handleXML.additionalSettings.getProperFlightFile() == null)  || (handleXML.additionalSettings.getDataSamplingRate() == 1000000))
                WrongFormatAlert();

        }
    }

}
