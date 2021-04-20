package View;

import Model.HandleXML;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;

public class MenuBarController {

    public void LoadXML() throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chosen = fc.showOpenDialog(null);
        if (chosen != null)
        {
            HandleXML handleXML = new HandleXML();
            handleXML.deserializeFromXML(chosen.getAbsolutePath());
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
        JOptionPane.showMessageDialog(null,
                "Wrong format of XML\n" +
                        "Please check your format and try again",
                "Error",
                JOptionPane.ERROR_MESSAGE);

    }

    public void MissingArgumentAlert()
    {
        JOptionPane.showMessageDialog(null,
                "Missing Arguments\n" +
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
}
