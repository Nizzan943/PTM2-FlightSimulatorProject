
package Model;

import Server.HandleXML;
import Server.TimeSeries;
import javafx.application.Platform;


import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable
{
    public static HandleXML XML_settings;
    public static String CSVpath;

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
    TimeSeries in = null;
    PrintWriter out = null;
    private String time;

    public String gettime() {
        return time;
    }

    private String resultLoadXML;
    private String resultOpenCSV;
    ArrayList <String> colsNames = new ArrayList<>();

    public ArrayList<String> getColsNames() {
        return colsNames;
    }

    public String getResultOpenCSV() {
        return resultOpenCSV;
    }

    public String getResultLoadXML() {
        return resultLoadXML;
    }


    public void ModelLoadXML(String chosenPath)
    {
        HandleXML handleXML = new HandleXML();
        try {
            handleXML.deserializeFromXML(chosenPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (handleXML.WrongFormatAlert == true)
            resultLoadXML = "WrongFormatAlert";
        else if (handleXML.MissingArgumentsAlert == true)
            resultLoadXML = "MissingArgumentAlert";
        else {
            XML_settings = handleXML;
            resultLoadXML = "SuccessAlert";
        }
        setChanged();
        notifyObservers("resultLoadXML");
    }

    public void ModelOpenCSV(String chosenPath)
    {
        int flag1 = 0;
        TimeSeries timeSeries = new TimeSeries(chosenPath);
        if (timeSeries.getCols().length != 42)
            resultOpenCSV = "Missing Arguments";
        for (int i = 0; i < timeSeries.getCols().length; i++)
        {
            flag1 = 0;
            if (timeSeries.getCols()[i].getName().intern() != XML_settings.PropertyList.get(i).getRealName().intern())
            {
                resultOpenCSV = "Incompatibility with XML file";
                flag1 = 1;
                break;
            }
            for (int j = 0; j < timeSeries.getCols()[i].getFloats().size(); j++)
            {
                if (timeSeries.getCols()[i].getFloats().get(j) > XML_settings.PropertyList.get(i).getMax() || timeSeries.getCols()[i].getFloats().get(j) < XML_settings.PropertyList.get(i).getMin())
                {
                    resultOpenCSV = "Incompatibility with XML file";
                    flag1 = 1;
                    break;
                }
            }
            if (flag1 == 1)
                break;
        }
        if (timeSeries.getCols().length == 42 && flag1 == 0)
        {
            resultOpenCSV = "OK";
            for (TimeSeries.col col : timeSeries.getCols())
            {
                colsNames.add(col.getName());
            }
            CSVpath = chosenPath;
            try {
                fg = new Socket("localhost", 5400);
            } catch (IOException e) {
                e.printStackTrace();
            }

            in = new TimeSeries(Model.CSVpath);

            try {
                out = new PrintWriter(fg.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers("resultOpenCSV");
        notifyObservers("cols");
    }

    public void modelPlay()
    {
        if (flag == 0) {
            simulator10Thread = new Thread(() -> {
                simulatorLoop(1);
            });
            simulator10Thread.start();
            timer10Thread = new Thread(() -> {
                timerLoop(1);
            });
            timer10Thread.start();

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

    int numofrow = 0;

    public void simulatorLoop (double speed)
    {
        while (numofrow != in.getRows().size() - 1)
        {
            out.println(in.getRows().get(numofrow));
            out.flush();
            changeSpeed(speed);
            numofrow++;
        }
        out.close();
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
            changeTimerSpeed(speed);
            time = simpleDateFormat.format(nowTime - 7200000);
            Platform.runLater(() -> {
                setChanged();
                notifyObservers("time");
            });
        }
    }

    public void modelGetChoice(String speed)
    {
        if (speed.intern() == "x2.0") {
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
                modelPlay();
            }
        }

        if (speed.intern() == "x1.5")
        {
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

    public void modelpause()
    {
        flag = 1;
        if (simulator10Thread != null) {
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
}
