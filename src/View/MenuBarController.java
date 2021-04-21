package View;

import Model.HandleXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class MenuBarController {

    public void LoadXML()  {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chosen = fc.showOpenDialog(null);
        if (chosen != null)
        {
            HandleXML handleXML = new HandleXML();
            try {
                handleXML.deserializeFromXML(chosen.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (handleXML.WrongFormatAlert == true)
                WrongFormatAlert();
            else if (handleXML.MissingArgumentsAlert == true)
                MissingArgumentAlert();
            else {
                SuccessAlert();
                Controller.XML_settings = handleXML;
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
}
