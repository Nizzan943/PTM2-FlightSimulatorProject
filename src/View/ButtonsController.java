package View;


import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;


public class ButtonsController implements Initializable {

    Thread simulatorThread;
    Thread timerThread;
    int flag = 0;
    long nowTime = 0;

    @FXML
    private ChoiceBox playSpeedDropDown;

    @FXML
    Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playSpeedDropDown.getItems().addAll("x0.5", "x1", "x1.5", "x2.0");
    }


    public void Play()
    {
        label.setFont(new Font(15));
        if (flag == 0) {
            simulatorThread = new Thread(() -> {
                Socket fg = null;
                try {
                    fg = new Socket("localhost", 5400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader(Controller.CSVpath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                PrintWriter out = null;
                try {
                    out = new PrintWriter(fg.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String line = null;
                while (true) {
                    try {
                        if (!((line = in.readLine()) != null)) break;
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
            });
            simulatorThread.start();
            timerThread = new Thread(() -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                while (true) {
                    try {
                        Thread.sleep(1000); //1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String time;
                    nowTime += 1000;
                    time = simpleDateFormat.format(nowTime - 7200000);
                    Platform.runLater(() -> {
                        label.setText(time);
                    });
                }
            });
            timerThread.start();

        }
        if (flag == 1)
        {
            simulatorThread.resume();
            timerThread.resume();
        }
        flag = 1;

    }

    public void Stop()
    {
    }

    public void Pause()
    {
        simulatorThread.suspend();
        timerThread.suspend();
    }

    public void FastForward()
    {
        System.out.println("FastForward");
    }

    public void FastBackward()
    {
        System.out.println("FastBackward");
    }

    public void Plus15()
    {
        System.out.println("+15");
    }

    public void Minus15()
    {
        System.out.println("-15");
    }

}
