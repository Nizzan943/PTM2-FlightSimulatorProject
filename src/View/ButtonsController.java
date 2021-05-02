package View;


import Model.Model;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import sun.awt.windows.ThemeReader;


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
    Thread simulator10Thread = null;
    Thread timer10Thread = null;
    Thread simulator15Thread = null;
    Thread timer15Thread = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.S");
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
        playSpeedDropDown.getItems().addAll("x0.5", "x1.0", "x1.5", "x2.0");
        playSpeedDropDown.setValue("x1.0");
    }




    public void Play()
    {
       // JoystickController joystickController = new JoystickController();
       // Thread joystickAileron = new Thread(() ->{joystickController.joystickAileron();  });
       // joystickAileron.start();
       // joystickController.joystickAileron();
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
                        in = new BufferedReader(new FileReader(Model.CSVpath));
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
            });
            simulatorThread.start();
            timerThread = new Thread(() -> {
                timerLoop(1);
            });
            timerThread.start();

        }
        if (flag == 1)
        {
            if (simulatorThread != null) {
                simulatorThread.resume();
                timerThread.resume();
            }
            if (simulator20Thread != null) {
                simulator20Thread.resume();
                timer20Thread.resume();
            }
            if (simulator05Thread != null) {
                simulator05Thread.resume();
                timer05Thread.resume();
            }
            if (simulator15Thread != null) {
                simulator15Thread.resume();
                timer15Thread.resume();
            }
            if (simulator10Thread != null) {
                simulator10Thread.resume();
                timer10Thread.resume();
            }
        }
        flag = 1;

    }

    public void Stop()
    {
    }

    public void Pause()
    {
        if (simulatorThread != null) {
            simulatorThread.suspend();
            timerThread.suspend();
        }
        else if (simulator10Thread != null) {
            simulator10Thread.suspend();
            timer10Thread.suspend();
        }
        else if (simulator15Thread != null) {
            simulator15Thread.suspend();
            timer15Thread.suspend();
        }
        else if (simulator05Thread != null) {
            simulator05Thread.suspend();
            timer05Thread.suspend();
        }
        else if (simulator20Thread != null) {
            simulator20Thread.suspend();
            timer20Thread.suspend();
        }
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
            if (simulatorThread != null) {
                simulatorThread.suspend();
                timerThread.suspend();
                simulatorThread = null;
            }
            if (simulator05Thread != null) {
                simulator05Thread.suspend();
                timer05Thread.suspend();
                simulator05Thread = null;
            }
            if (simulator10Thread != null) {
                simulator10Thread.suspend();
                timer10Thread.suspend();
                simulator10Thread = null;
            }
            if (simulator15Thread != null) {
                simulator15Thread.suspend();
                timer15Thread.suspend();
                simulator15Thread = null;
            }
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
            if (simulatorThread != null) {
                simulatorThread.suspend();
                timerThread.suspend();
                simulatorThread = null;
            }
            if (simulator20Thread != null) {
                simulator20Thread.suspend();
                timer20Thread.suspend();
                simulator20Thread = null;
            }
            if (simulator10Thread != null) {
                simulator10Thread.suspend();
                timer10Thread.suspend();
                simulator10Thread = null;
            }
            if (simulator15Thread != null) {
                simulator15Thread.suspend();
                timer15Thread.suspend();
                simulator15Thread = null;
            }
            simulator05Thread = new Thread(() ->
            {
                simulatorLoop(0.5);
            });
            simulator05Thread.start();
            timer05Thread = new Thread(() ->
            {
                timerLoop(0.5);
            });
            timer05Thread.start();
        }

        if (speed.intern() == "x1.0")
        {
            if (simulator15Thread != null || simulator20Thread != null || simulator05Thread != null) {
                if (simulatorThread != null) {
                    simulatorThread.suspend();
                    timerThread.suspend();
                    simulatorThread = null;
                }
                if (simulator05Thread != null) {
                    simulator05Thread.suspend();
                    timer05Thread.suspend();
                    simulator05Thread = null;
                }
                if (simulator20Thread != null) {
                    simulator20Thread.suspend();
                    timer20Thread.suspend();
                    simulator20Thread = null;
                }
                if (simulator15Thread != null) {
                    simulator15Thread.suspend();
                    timer15Thread.suspend();
                    simulator15Thread = null;
                }
                simulator10Thread = new Thread(() ->
                {
                    simulatorLoop(1);
                });
                simulator10Thread.start();
                timer10Thread = new Thread(() ->
                {
                    timerLoop(1);
                });
                timer10Thread.start();
            }
        }

        if (speed.intern() == "x1.5")
        {
            if (simulatorThread != null) {
                simulatorThread.suspend();
                timerThread.suspend();
                simulatorThread = null;
            }
            if (simulator05Thread != null) {
                simulator05Thread.suspend();
                timer05Thread.suspend();
                simulator05Thread = null;
            }
            if (simulator10Thread != null) {
                simulator10Thread.suspend();
                timer10Thread.suspend();
                simulator10Thread = null;
            }
            if (simulator20Thread != null) {
                simulator20Thread.suspend();
                timer20Thread.suspend();
                simulator20Thread = null;
            }
            simulator15Thread = new Thread(() ->
            {
                simulatorLoop(1.5);
            });
            simulator15Thread.start();
            timer15Thread = new Thread(() ->
            {
                timerLoop(1.5);
            });
            timer15Thread.start();
        }
    }

    public void changeSpeed (double speed)
    {
        try {
            Thread.sleep((long)(Model.XML_settings.additionalSettings.getDataSamplingRate() / speed));
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
        int i = 0;
        Model model = new Model();
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
