package View;


import javafx.application.Platform;

import javafx.event.ActionEvent;
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

    Thread simulatorThread = null;
    Thread simulator20Thread = null;
    Thread timerThread = null;
    Thread timer20Thread = null;
    Thread simulator05Thread = null;
    Thread timer05Thread = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    int flag = 0;
    long nowTime = 0;

    Socket fg = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String line = null;

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

                try {
                    fg = new Socket("localhost", 5400);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                {
                    try {
                        in = new BufferedReader(new FileReader(Controller.CSVpath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                {
                    try {
                        out = new PrintWriter(fg.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                simulatorLoop(1);
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
                timerLoop(1);
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


    public void GetChoice(ActionEvent actionEvent) {
        String speed = (String) playSpeedDropDown.getValue();
        if (speed.intern() == "x2.0") {
            simulatorThread.suspend();
            timerThread.suspend();
            if (simulator05Thread!=null)
                simulator05Thread.suspend();
            if (timer05Thread != null)
                timer05Thread.suspend();
            simulator20Thread = new Thread(() ->
            {
                simulatorLoop(2);
            });
            simulator20Thread.start();
            timer20Thread = new Thread(() ->
            {
                timerLoop(2);
            });
            timer20Thread.start();
        }
        if (speed.intern() == "x0.5")
        {
            simulatorThread.suspend();
            timerThread.suspend();
            //simulator20Thread.suspend();
          //  timer20Thread.suspend();
            simulator05Thread = new Thread(() ->
            {
                simulatorLoop((long)0.5);
            });
            simulator05Thread.start();
            timer05Thread = new Thread(() ->
            {
                timerLoop(0.5);
            });
            timer05Thread.start();
        }
    }

    public void changeSpeed (double speed)
    {
        try {
            Thread.sleep((long)(Controller.XML_settings.additionalSettings.getDataSamplingRate() / speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changeTimerSpeed (double speed)
    {
        nowTime += 1000 * speed;
    }

    public void simulatorLoop (double speed)
    {
        while (true) {
            try {
                if (!((line = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println(line);
            out.flush();
            changeSpeed(speed);
        }
    }

    public void timerLoop (double speed)
    {
        while (true) {
            try {
                Thread.sleep(1000); //1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String time;
            changeTimerSpeed(speed);
            time = simpleDateFormat.format(nowTime - 7200000);
            Platform.runLater(() -> {
                label.setText(time);
            });
        }
    }


}
