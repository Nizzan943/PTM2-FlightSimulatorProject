package View;

import Model.TimeSeries;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;

public class ListViewController
{
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
                if (timeSeries.getCols()[i].getName().intern() != Controller.XML_settings.PropertyList.get(i).getRealName().intern())
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

}
