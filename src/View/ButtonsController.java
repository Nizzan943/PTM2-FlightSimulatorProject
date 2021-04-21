package View;

import Model.TimeSeries;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ButtonsController implements Initializable {

    Timer timer = new Timer();
    Thread thread;

    @FXML
    private ChoiceBox playSpeedDropDown;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playSpeedDropDown.getItems().addAll("x0.5", "x1", "x1.5", "x2.0");
    }

    public void Play()
    {
        thread = new Thread(() -> {
            Socket fg= null;
            try {
                fg = new Socket("localhost", 5400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in= null;
            try {
                in = new BufferedReader(new FileReader(Controller.CSVpath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter out= null;
            try {
                out = new PrintWriter(fg.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = null;
            while(true) {
                try {
                    if (!((line=in.readLine())!=null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println(line);
                out.flush();
                try {
                    Thread.sleep(Controller.XML_settings.additionalSettings.getDataSamplingRate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }) ;

        thread.start();

    }

    public void Stop()
    {
    }

    public void Pause(ActionEvent event) {
        System.out.println("Pause");
    }

    public void FastForward(ActionEvent event) {
        System.out.println("FastForward");
    }

    public void FastBackward(ActionEvent event) {
        System.out.println("FastBackward");
    }

    public void Plus15(ActionEvent event) {
        System.out.println("+15");
    }

    public void Minus15(ActionEvent event) {
        System.out.println("-15");
    }

}
