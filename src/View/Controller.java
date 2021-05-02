package View;

import Server.HandleXML;
import ViewModel.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Controller implements Observer, Initializable {

    @FXML
    ListView listView;


    StringProperty resultOpenCSV;
    StringProperty chosenCSVFilePath;
    ArrayList <String> colsNames = new ArrayList<>();
    ViewModel viewModel;


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        resultOpenCSV = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
        viewModel.chosenCSVFilePathProperty().bind(chosenCSVFilePath);
        resultOpenCSV.bind(viewModel.OpenCSVProperty());
        resultLoadXML = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        viewModel.chosenXMLFilePathProperty().bind(chosenXMLFilePath);
        resultLoadXML.bind(viewModel.loadXMLProperty());
    }

    public void openCSV() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setTitle("Open CSV file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chozen = fc.showOpenDialog(null);
        chosenCSVFilePath.set(chozen.getAbsolutePath());
        if (chozen != null) {
            viewModel.VMOpenCSV();
            if (resultOpenCSV.getValue().intern() == "Missing Arguments")
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Arguments");
                alert.setContentText("Please check your settings and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "Incompatibility with XML file")
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incompatibility with XML file");
                alert.setContentText("Please check your file and try again");
                alert.showAndWait();
            }
            if (resultOpenCSV.getValue().intern() == "OK")
            {
                for (String names: colsNames)
                    listView.getItems().add(names);
                label.setFont(new Font(15));
                label.setText("00:00:00.000");
            }
        }
    }

    StringProperty resultLoadXML;
    StringProperty chosenXMLFilePath;



    public void LoadXML()  {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fc.setTitle("Load XML file"); //headline
        fc.setInitialDirectory(new File("/")); //what happens when we click
        File chosen = fc.showOpenDialog(null);
        chosenXMLFilePath.set(chosen.getAbsolutePath());
        if (chosen != null)
        {
            viewModel.VMLoadXML();
            if (resultLoadXML.getValue().equals("WrongFormatAlert"))
                WrongFormatAlert();
            else if (resultLoadXML.getValue().equals("MissingArgumentAlert"))
                MissingArgumentAlert();
            else {
                SuccessAlert();
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

    @FXML
    private ChoiceBox playSpeedDropDown;

    @FXML
    Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playSpeedDropDown.getItems().addAll("x0.5", "x1.0", "x1.5", "x2.0");
    }

    int flag = 0;
    public void Play()
    {
        label.setFont(new Font(15));
        if (flag == 0) {
            playSpeedDropDown.setValue("x1.0");
            flag = 1;
        }
        viewModel.VMplay();
    }

    public void GetChoice(ActionEvent actionEvent)
    {
        String speed = (String) playSpeedDropDown.getValue();
        viewModel.VMGetChoice(speed);
    }

    public void Stop()
    {
    }

    public void Pause()
    {
        viewModel.VMpause();
    }


    @Override
    public void update(Observable o, Object arg)
    {
        String p = (String)arg;
        if (p.intern() == "colNames")
        {
            for (String name: viewModel.colsNames)
            {
                colsNames.add(name);
            }
        }
        if (p.intern() == "time")
            label.setText(viewModel.time);
    }
}
