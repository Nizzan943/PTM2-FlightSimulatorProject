package View;


import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Platform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class ButtonsController implements Initializable, Observer {
    ViewModel viewModel;
    @FXML
    private ChoiceBox playSpeedDropDown;

    @FXML
    Label label;


    public void setViewModel(ViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playSpeedDropDown.getItems().addAll("x0.5", "x1.0", "x1.5", "x2.0");
        playSpeedDropDown.setValue("x1.0");
    }




    public void Play()
    {
        label.setFont(new Font(15));
        viewModel.VMplay();
    }

    public void Stop()
    {
    }
/*
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
*/
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

    @Override
    public void update(Observable o, Object arg)
    {
        String p = (String)arg;
        if (p.intern() == "time")
            label.setText(viewModel.time);
    }

/*
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

*/


}
