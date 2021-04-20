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

    public void Play() throws IOException, InterruptedException {
        Socket fg=new Socket("localhost", 5400);
        BufferedReader in=new BufferedReader(new FileReader(Controller.CSVpath));
        PrintWriter out=new PrintWriter(fg.getOutputStream());
        String line;
        while((line=in.readLine())!=null) {
            out.println(line);
            out.flush();
            Thread.sleep(Controller.XML_settings.additionalSettings.getDataSamplingRate());
        }
        out.close();
        in.close();
        fg.close();
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
