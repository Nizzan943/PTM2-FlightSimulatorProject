package View;

import Server.TimeSeries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

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
            if (timeSeries.getCols().length != 42) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Arguments");
                alert.setContentText("Please check your settings and try again");
                alert.showAndWait();
            }
            for (int i = 0; i < timeSeries.getCols().length; i++)
            {
                flag = 0;
                if (timeSeries.getCols()[i].getName().intern() != Controller.XML_settings.PropertyList.get(i).getRealName().intern())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incompatibility with XML file");
                    alert.setContentText("Please check your file and try again");

                    alert.showAndWait();
                    flag = 1;
                    break;
                }
                for (int j = 0; j < timeSeries.getCols()[i].getFloats().size(); j++)
                {
                    if (timeSeries.getCols()[i].getFloats().get(j) > Controller.XML_settings.PropertyList.get(i).getMax() || timeSeries.getCols()[i].getFloats().get(j) < Controller.XML_settings.PropertyList.get(i).getMin())
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Incompatibility with XML file");
                        alert.setContentText("Please check your file and try again");

                        alert.showAndWait();
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1)
                    break;
            }
            if (timeSeries.getCols().length == 42 && flag == 0) {
                for (TimeSeries.col col : timeSeries.getCols()) {
                    listView.getItems().add(col.getName());
                }
                Controller.CSVpath = chozen.getAbsolutePath();
            }
        }
    }

}
