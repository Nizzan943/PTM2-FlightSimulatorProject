
package Model;

import Algorithms.*;
import Server.*;

import java.io.*;
import java.net.MalformedURLException;
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
    int saveI = -1;
    int flightLong;
    private int minColValue = 10000;
    private int maxColValue = -10000;
    long nowTime = 0;

    Socket fg = null;
    TimeSeries in = null;
    PrintWriter out = null;

    private String time;
    private String resultLoadXML;
    private String resultOpenCSV;
    private String resultLoadAlgorithm;
    String nameOfCol;
    String nameOfCoralatedCol;
    String className;

    private int realHybrid = 0;

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

    private Line algorithmLine;
    private Circle algorithmCircle;

    private ArrayList<String> colsNames = new ArrayList<>();
    private Map<String, Integer> CSVindexmap = new HashMap<>();
    private ArrayList<Float> algorithmColValues = new ArrayList<>();
    private ArrayList<Float> algorithmCoralatedColValues = new ArrayList<>();
    private ArrayList<Float> anomalyAlgorithmCoralatedColValues = new ArrayList<>();
    private ArrayList<Float> anomalyAlgorithmColValues = new ArrayList<>();
    private ArrayList<Float> ZScoreLine = new ArrayList<>();

    TimeSeriesAnomalyDetector ad;
    TimeSeries regularFlight;
    LinearRegression linearRegression = new LinearRegression();
    ZScore zScore = new ZScore();
    Hybrid hybrid = new Hybrid();
    List<AnomalyReport> reports = new ArrayList<>();

    public String gettime() {
        return time;
    }

    public String getResultOpenCSV() {
        return resultOpenCSV;
    }

    public String getResultLoadXML() {
        return resultLoadXML;
    }

    public String getResultLoadAlgorithm()
    {
        return resultLoadAlgorithm;
    }

    public String getClassName()
    {
        return className;
    }

    public ArrayList<String> getColsNames() {
        return colsNames;
    }

    public ArrayList<Float> getAlgorithmColValues() {
        return algorithmColValues;
    }

    public ArrayList<Float> getAlgorithmCoralatedColValues() {
        return algorithmCoralatedColValues;
    }

    public ArrayList<Float> getAnomalyAlgorithmColValues()
    {
        return anomalyAlgorithmColValues;
    }

    public ArrayList<Float> getAnomalyAlgorithmCoralatedColValues()
    {
        return anomalyAlgorithmCoralatedColValues;
    }

    public ArrayList<Float> getZScoreLine() {
        return ZScoreLine;
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

    public int getMinColValue()
    {
        return minColValue;
    }

    public int getMaxColValue()
    {
        return maxColValue;
    }

    public int getNumofrow()
    {
        return numofrow;
    }

    public int getFlightLong()
    {
        return flightLong;
    }

    public Line getAlgorithmLine() {
        return algorithmLine;
    }

    public Circle getAlgorithmCircle()
    {
        return algorithmCircle;
    }

    @Override
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
            regularFlight = new TimeSeries(XML_settings.additionalSettings.getProperFlightFile());
            regularFlight.setCorrelationTresh(0);
            linearRegression.learnNormal(regularFlight);
            zScore.learnNormal(regularFlight);
            hybrid.HybridAlgorithm(regularFlight);
            hybrid.learnNormal(regularFlight);
        }
        setChanged();
        notifyObservers("resultLoadXML");
    }

    @Override
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
            }

            in = new TimeSeries(Model.CSVpath);
            flightLong = in.getCols()[0].getFloats().size() + 1;

            try {
                if (fg != null)
                    out = new PrintWriter(fg.getOutputStream());
            } catch (IOException e) {
            }
        }
        setChanged();
        notifyObservers("resultOpenCSV");
    }

    protected void resume(Thread simulatorThread, Thread timerThread)
    {
        if (simulatorThread != null)
        {
            simulatorThread.resume();
            timerThread.resume();
        }
    }

    @Override
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

    protected void changeSpeed(double speed) {
        try {
            Thread.sleep((long) (Model.XML_settings.additionalSettings.getDataSamplingRate() / speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void changeTimerSpeed(double speed) {
        nowTime += 1000 * speed;
    }

    protected void allChanges()
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

        for (int i = 0; i < reports.size(); i++)
        {
            if (reports.get(i).timeStep == numofrow && reports.get(i).description.contains(nameOfCol))
            {
                setChanged();
                notifyObservers("report");
                saveI = i;
                break;
            }
            else
            {
                if (saveI == i - 1) {
                    setChanged();
                    notifyObservers("reportDone");
                }
            }
        }
    }

    protected void simulatorLoop(double speed) {
        while (numofrow < in.getRows().size() - 2) {
            if (out != null) {
                out.println(in.getRows().get(numofrow));
                out.flush();
            }

            allChanges();

            changeSpeed(speed);
            numofrow++;
            setChanged();
            notifyObservers("numofrow");
        }
        modelStop();
        if (out != null)
            out.close();
        try {
            fg.close();
        } catch (IOException e) {
        }
    }

    protected void timerLoop(double speed) {
        while (true) {
            try {
                Thread.sleep(1000); //1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            changeTimerSpeed(speed);

            if (nowTime >= ((in.getCols()[0].getFloats().size() + 1) / (XML_settings.additionalSettings.getDataSamplingRate() / 10)) * 1000 - 1000)
            {
                nowTime = ((in.getCols()[0].getFloats().size() + 1) / (XML_settings.additionalSettings.getDataSamplingRate() / 10)) * 1000 - 1000;
                time = simpleDateFormat.format(nowTime - 7200000);
                setChanged();
                notifyObservers("time");
                break;
            }

            time = simpleDateFormat.format(nowTime - 7200000);
            setChanged();
            notifyObservers("time");
        }
    }

    protected void suspendForPlay(Thread simulatorThread, Thread timerThread)
    {
        if (simulatorThread != null) {
            simulatorThread.suspend();
            timerThread.suspend();
        }
    }

    @Override
    public void modelGetChoice(String speed) {
        if (speed.intern() == "x2.0") {
            suspendForPlay(simulator05Thread, timer05Thread);
            suspendForPlay(simulator10Thread, timer10Thread);
            suspendForPlay(simulator15Thread, timer15Thread);
            simulator05Thread = null;
            simulator10Thread = null;
            simulator15Thread = null;

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
            suspendForPlay(simulator10Thread, timer10Thread);
            suspendForPlay(simulator15Thread, timer15Thread);
            suspendForPlay(simulator20Thread, timer20Thread);
            simulator10Thread = null;
            simulator15Thread = null;
            simulator20Thread = null;
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
                suspendForPlay(simulator05Thread, timer05Thread);
                suspendForPlay(simulator15Thread, timer15Thread);
                suspendForPlay(simulator20Thread, timer20Thread);
                simulator05Thread = null;
                simulator15Thread = null;
                simulator20Thread = null;
                modelPlay();
            }
        }

        if (speed.intern() == "x1.5") {
            suspendForPlay(simulator05Thread, timer05Thread);
            suspendForPlay(simulator10Thread, timer10Thread);
            suspendForPlay(simulator20Thread, timer20Thread);
            simulator05Thread = null;
            simulator10Thread = null;
            simulator20Thread = null;
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

    protected void suspendForPause(Thread simulatorThread, Thread timerThread)
    {
        if (simulatorThread != null)
        {
            simulatorThread.suspend();
            timerThread.suspend();
        }
    }

    @Override
    public void modelpause()
    {
        suspendForPause(simulator05Thread, timer05Thread);
        suspendForPause(simulator10Thread, timer10Thread);
        suspendForPause(simulator15Thread, timer15Thread);
        suspendForPause(simulator20Thread, timer20Thread);

        playFlag = 1;
    }

    @Override
    public void modelPlus15() {
        Plus_Minus_Time(15, "+");
    }

    @Override
    public void modelMinus15() {
        Plus_Minus_Time(15, "-");
    }

    @Override
    public void modelMinus30() {
        Plus_Minus_Time(30, "-");
    }

    @Override
    public void modelPlus30() {
        Plus_Minus_Time(30, "+");
    }

    protected void Plus_Minus_Time(int seconds, String math) {
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
        return ((double)(in.getCols()[0].getFloats().size() + 1) / (double)(XML_settings.additionalSettings.getDataSamplingRate() / 10)) - 1;
    }

    public void modelTimeSlider(double second) {
        numofrow = (int) (second * (XML_settings.additionalSettings.getDataSamplingRate() / 10));
        nowTime = (long) (second * 1000);
    }

    @Override
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

    @Override
    public void modelSetLeftLineChart(String colName)
    {
        nameOfCol = colName;
    }

    @Override
    public void modelSetRightLineChart(String colName)
    {
        List<CorrelatedFeatures> list = linearRegression.getNormalModel();
        for (CorrelatedFeatures features : list) {
            if (features.feature1.intern() == colName.intern())
                nameOfCoralatedCol = features.feature2;
        }
    }

    @Override
    public void modelSetAlgorithmLineChart(String colName)
    {
        if (realHybrid == 1)
            className = "class Model.Hybrid";
        reports = ad.detect(in);
        modelSetRightLineChart(colName);
        algorithmColValues.clear();
        algorithmCoralatedColValues.clear();
        anomalyAlgorithmColValues.clear();
        anomalyAlgorithmCoralatedColValues.clear();

        for (float value : regularFlight.getCols()[regularFlight.getColIndex(colName)].getFloats()) {
            algorithmColValues.add(value);
            if (minColValue > value)
                minColValue = (int) value;
            if (maxColValue < value)
                maxColValue = (int) value;
        }

        for (float value : regularFlight.getCols()[regularFlight.getColIndex(nameOfCoralatedCol)].getFloats()) {
            algorithmCoralatedColValues.add(value);
        }

        if (className.intern() == "class Model.Hybrid")
        {
            realHybrid = 1;
            if (hybrid.whichAlgorithm.get(colName).intern() == "LinearRegression") {
                className = "class Model.LinearRegression";
            }
            if (hybrid.whichAlgorithm.get(colName).intern() == "ZScore") {
                className = "class Model.ZScore";
            }
            if (hybrid.whichAlgorithm.get(colName).intern() == "Hybrid") {
                for (float value :in.getCols()[in.getColIndex(colName)].getFloats()) {
                    anomalyAlgorithmColValues.add(value);
                }

                for (float value : in.getCols()[in.getColIndex(nameOfCoralatedCol)].getFloats()) {
                    anomalyAlgorithmCoralatedColValues.add(value);
                }

                algorithmCircle = hybrid.whoCircle.get(colName);
            }
        }

        if (className.intern() == "class Model.LinearRegression") {
            for (float value :in.getCols()[in.getColIndex(colName)].getFloats()) {
                anomalyAlgorithmColValues.add(value);
            }

            for (float value : in.getCols()[in.getColIndex(nameOfCoralatedCol)].getFloats()) {
                anomalyAlgorithmCoralatedColValues.add(value);
            }
            List<CorrelatedFeatures> list = linearRegression.getNormalModel();
            for (CorrelatedFeatures features : list) {
                if (features.feature1.intern() == colName.intern() && features.feature2.intern() == nameOfCoralatedCol.intern()) {
                    algorithmLine = features.lin_reg;
                    break;
                }
            }
        }

        if (className.intern() == "class Model.ZScore")
        {
            ZScoreLine = zScore.colZscores.get(regularFlight.getColIndex(colName));
        }

    }

    @Override
    public void modelLoadAlgorithm(String resultClassDirectory, String resultClassName)
    {
        URL[] urls = new URL[1];
        try {
            urls[0] = new URL("file://" + resultClassDirectory);
            URLClassLoader classLoader = new URLClassLoader(urls);
            Class<?> classInstance = null;
            classInstance = classLoader.loadClass(resultClassName);
            ad = (TimeSeriesAnomalyDetector) classInstance.newInstance();
            resultLoadAlgorithm = "success";
            setChanged();
            notifyObservers("resultLoadAlgorithm");
            className =  ad.getClass().toString();
            ad.learnNormal(regularFlight);
            realHybrid = 0;
        } catch (IllegalAccessException | ClassNotFoundException e) {
            resultLoadAlgorithm = "failed";
            setChanged();
            notifyObservers("resultLoadAlgorithm");
        } catch (MalformedURLException e) {
            resultLoadAlgorithm = "failed";
            setChanged();
            notifyObservers("resultLoadAlgorithm");
        } catch (InstantiationException e) {
            resultLoadAlgorithm = "failed";
            setChanged();
            notifyObservers("resultLoadAlgorithm");
        }
    }
}

