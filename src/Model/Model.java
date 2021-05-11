
package Model;

import Algorithms.LinearRegression;
import Server.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Model extends AllModels {

    static HandleXML XML_settings;
    static String CSVpath;

    Thread simulator20Thread = null;
    Thread timer20Thread = null;
    Thread simulator05Thread = null;
    Thread timer05Thread = null;
    Thread simulator10Thread = null;
    Thread timer10Thread = null;
    Thread simulator15Thread = null;
    Thread timer15Thread = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.S");

    int playFlag = 0;
    int numofrow = 0;
    long nowTime = 0;

    Socket fg = null;
    TimeSeries in = null;
    PrintWriter out = null;

    private String time;

    private String resultLoadXML;
    private String resultOpenCSV;
    String nameOfCol;
    String nameOfCoralatedCol;

    private float rudderstep;
    private float throttlestep;
    private float altimeterstep;
    private float airspeedstep;
    private float directionstep;
    private float pitchstep;
    private float aileronstep;
    private float elevatorstep;
    private float rollstep;
    private float yawstep;
    private float colValues;
    private float coralatedColValues;

    ArrayList<String> colsNames = new ArrayList<>();
    Map<String, Integer> CSVindexmap = new HashMap<>();

    TimeSeries timeSeries;
    LinearRegression linearRegression = new LinearRegression();

    public String gettime() {
        return time;
    }

    public String getResultOpenCSV() {
        return resultOpenCSV;
    }

    public String getResultLoadXML() {
        return resultLoadXML;
    }

    public ArrayList<String> getColsNames() {
        return colsNames;
    }

    public float getRudderstep() {
        return rudderstep;
    }

    public float getThrottlestep() {
        return throttlestep;
    }

    public float getAileronstep() {
        return aileronstep;
    }

    public float getElevatorstep() {
        return elevatorstep;
    }

    public float getAltimeterstep() {
        return altimeterstep;
    }

    public float getAirspeedstep() {
        return airspeedstep;
    }

    public float getDirectionstep() {
        return directionstep;
    }

    public float getPitchstep() {
        return pitchstep;
    }

    public float getRollstep() {
        return rollstep;
    }

    public float getYawstep() {
        return yawstep;
    }

    public float getColValues() {
        return colValues;
    }

    public float getCoralatedColValues() {
        return coralatedColValues;
    }


    public void ModelLoadXML(String chosenPath) {
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
       // timeSeries = new TimeSeries(XML_settings.additionalSettings.getProperFlightFile());
       // timeSeries.setCorrelationTresh(0);
       // linearRegression.learnNormal(timeSeries);
    }

    public void ModelOpenCSV(String chosenPath) {
        int openFlag = 0;
        TimeSeries timeSeries = new TimeSeries(chosenPath);
        for (int i = 0; i < timeSeries.getCols().length; i++) {
            int k = 0;
            while (k != 10) {
                if (timeSeries.getCols()[i].getName().intern() == XML_settings.PropertyList.get(k).getRealName().intern()) {
                    if (CSVindexmap.get(XML_settings.PropertyList.get(k).getAssosicateName()) == null)
                        CSVindexmap.put(XML_settings.PropertyList.get(k).getAssosicateName(), i);
                    break;
                }
                k++;
            }
            if (CSVindexmap.size() == 10)
                break;
        }

        for (String colname : XML_settings.RealToAssosicate.keySet()) {
            int index = CSVindexmap.get(XML_settings.RealToAssosicate.get(colname));
            for (Float num : timeSeries.getCols()[index].getFloats()) {
                if (num < XML_settings.min.get(colname) || num > XML_settings.max.get(colname)) {
                    resultOpenCSV = "Incompatibility with XML file";
                    openFlag = 1;
                }
                if (openFlag == 1)
                    break;
            }
            if (openFlag == 1)
                break;
        }

        if (CSVindexmap.size() != 10)
            resultOpenCSV = "Missing Arguments";
        if (CSVindexmap.size() == 10 && openFlag == 0) {
            resultOpenCSV = "OK";
            for (TimeSeries.col col : timeSeries.getCols()) {
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
    }

    public void resume(Thread simulatorThread, Thread timerThread)
    {
        if (simulatorThread != null)
        {
            simulatorThread.resume();
            timerThread.resume();
        }
    }

    public void modelPlay() {
        if (playFlag == 0) {
            simulator10Thread = new Thread(() -> {
                simulatorLoop(1);
            });
            simulator10Thread.start();
            timer10Thread = new Thread(() -> {
                timerLoop(1);
            });
            timer10Thread.start();
        }
        if (playFlag == 1)
        {
            resume(simulator10Thread, timer10Thread);
            resume(simulator20Thread, timer20Thread);
            resume(simulator15Thread, timer15Thread);
            resume(simulator05Thread, timer05Thread);

            playFlag = 0;
        }
    }

    public void changeSpeed(double speed) {
        try {
            Thread.sleep((long) (Model.XML_settings.additionalSettings.getDataSamplingRate() / speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changeTimerSpeed(double speed) {
        nowTime += 1000 * speed;
    }

    public void allChanges()
    {
        rudderstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("rudder"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("rudder");

        throttlestep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("throttle"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("throttle");

        altimeterstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("altimeter_indicated-altitude-ft"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("altimeter");

        airspeedstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("airspeed-indicator_indicated-speed-kt"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("airspeed");

        directionstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("indicated-heading-deg"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("direction");

        pitchstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("attitude-indicator_internal-pitch-deg"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("pitch");

        rollstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("attitude-indicator_indicated-roll-deg"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("roll");

        yawstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("side-slip-deg"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("yaw");

        aileronstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("aileron"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("aileron");

        elevatorstep = in.getCols()[CSVindexmap.get(XML_settings.RealToAssosicate.get("elevator"))].getFloats().get(numofrow);
        setChanged();
        notifyObservers("elevator");
/*
            colValues = in.getCols()[in.getColIndex(nameOfCol)].getFloats().get(numofrow);
            setChanged();
            notifyObservers("colValues");

            colValues = in.getCols()[in.getColIndex(nameOfCoralatedCol)].getFloats().get(numofrow);
            setChanged();
            notifyObservers("coralatedColValue");


             */
    }

    public void simulatorLoop(double speed) {
        while (numofrow != in.getRows().size() - 1) {
            out.println(in.getRows().get(numofrow));
            out.flush();

            allChanges();

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

    public void timerLoop(double speed) {
        while (true) {
            try {
                Thread.sleep(1000); //1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            changeTimerSpeed(speed);

            if (nowTime >= ((in.getCols()[0].getFloats().size() + 1) / (XML_settings.additionalSettings.getDataSamplingRate() / 10)) * 1000)
                break;

            time = simpleDateFormat.format(nowTime - 7200000);
            setChanged();
            notifyObservers("time");
        }
    }

    public void modelGetChoice(String speed) {
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

        if (speed.intern() == "x0.5") {
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

        if (speed.intern() == "x1.0") {
            if (simulator15Thread != null || simulator20Thread != null || simulator05Thread != null) {
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

        if (speed.intern() == "x1.5") {
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
        if (simulator10Thread != null)
        {
            simulator10Thread.suspend();
            timer10Thread.suspend();
        }
        else if (simulator15Thread != null)
        {
            simulator15Thread.suspend();
            timer15Thread.suspend();
        }
        else if (simulator20Thread != null)
        {
            simulator20Thread.suspend();
            timer20Thread.suspend();
        }
        else if (simulator05Thread != null)
        {
            simulator05Thread.suspend();
            timer05Thread.suspend();
        }
        playFlag = 1;
    }

    public void modelPlus15() {
        Plus_Minus_Time(15, "+");
    }

    public void modelMinus15() {
        Plus_Minus_Time(15, "-");
    }

    public void modelMinus30() {
        Plus_Minus_Time(30, "-");
    }

    public void modelPlus30() {
        Plus_Minus_Time(30, "+");
    }

    public void Plus_Minus_Time(int seconds, String math) {
        if (math.intern() == "+") {
            numofrow += (XML_settings.additionalSettings.getDataSamplingRate() / 10) * seconds;
            nowTime += seconds * 1000;
        } else {
            numofrow -= (XML_settings.additionalSettings.getDataSamplingRate() / 10) * seconds;
            nowTime -= seconds * 1000;
        }
    }

    public double modelSetMinRudder() {
        return XML_settings.min.get("rudder");
    }

    public double modelSetMaxRudder() {
        return XML_settings.max.get("rudder");
    }

    public double modelSetMinThrottle() {
        return XML_settings.min.get("throttle");
    }

    public double modelSetMaxThrottle() {
        return XML_settings.max.get("throttle");
    }

    public double modelSetMaxTimeSlider() {
        return ((double)(in.getCols()[0].getFloats().size() + 1) / (double)(XML_settings.additionalSettings.getDataSamplingRate() / 10));
    }

    public void modelTimeSlider(double second) {
        numofrow = (int) (second * (XML_settings.additionalSettings.getDataSamplingRate() / 10));
        nowTime = (long) (second * 1000);
    }

    public void modelStop()
    {
        numofrow = 0;
        nowTime = 0;
        modelpause();
        simulator10Thread = null;
        simulator05Thread = null;
        simulator15Thread = null;
        simulator20Thread = null;
        playFlag = 0;
    }

    public void modelSetLeftLineChart(String colName)
    {
        nameOfCol = colName;
    }

    public void modelSetRightLineChart(String colName)
    {
        //Thread a = new Thread( () ->
        {
            List<CorrelatedFeatures> list = linearRegression.getNormalModel();
            for (CorrelatedFeatures features : list) {
                if (features.feature1.intern() == colName.intern())
                    nameOfCoralatedCol = features.feature2;
            }
        }
       // });
       // a.start();
    }
}

