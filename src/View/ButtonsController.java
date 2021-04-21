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
    private String path = "D:\\Repos\\PTM2-FlightSimulatorProject\\Files\\reg_flight.csv";

    @FXML
    private ChoiceBox playSpeedDropDown;
  //  @FXML
  //  static Button play;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playSpeedDropDown.getItems().addAll("x0.5", "x1", "x1.5", "x2.0");
        //String[] buttonLabels = {"\u25b6 Play", "\u23f8 Pause", "\u23F9 Stop", "\u23EA Fast Backward", "\u23E9  Fast Forward"};
        //play.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("Images/Play.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        //String absolutePathToIcon =
        //        getClass().getResource("Play.png").toExternalForm();
        //play.setGraphic(new ImageView(new Image("file:View\\Play.png")));
    }

    public void Play()
    {
        Thread thread = new Thread(() -> {
            Socket fg= null;
            try {
                fg = new Socket("localhost", 5400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in= null;
            try {
                in = new BufferedReader(new FileReader("D:\\Repos\\PTM2-FlightSimulatorProject\\Files\\reg_flight.csv"));
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
                    Thread.sleep(20);
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
