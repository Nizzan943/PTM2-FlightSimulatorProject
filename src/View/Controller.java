package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Controller {
    @FXML
    public ListView listView = new ListView();


    public void yuv() {
        System.out.println("yuv");
    }

    public void emo() {
        listView.getItems().addAll("test");
        listView.refresh();
    }

    public void test1() {
        listView.getItems().add("yuv");
        listView.getItems().add("niz");
        listView.getItems().add("ori");
        listView.refresh();
    }
}
