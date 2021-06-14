
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

    private class simulator extends TimerTask {
        public void run() {
            simulatorLoop();
        }
    }

    private class timer10 extends TimerTask {
        boolean is_running = false;
        public void run() {
            simulatorLoop();
            timerLoop(1);
        }
    }

    private class timer20 extends TimerTask {
        boolean is_running = false;
        public void run() {
            simulatorLoop();
            timerLoop(2);
        }
    }

    private class timer05 extends TimerTask {
        boolean is_running = false;
        public void run() {
            simulatorLoop();
            timerLoop(0.5);
        }
    }

    private class timer15 extends TimerTask {
        boolean is_running = false;
        public void run() {
            simulatorLoop();
            timerLoop(1.5);
        }
    }

    Timer timer = new Timer();

    simulator simulator;
    timer10 timer10;
    timer20 timer20;
    timer05 timer05;
    timer15 timer15;


    HandleXML XML_settings;
    String CSVpath;
    String _speed;

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
    private ArrayList<Point> pointsForCircle = new ArrayList<>();

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

    public String getResultLoadAlgorithm() {
        return resultLoadAlgorithm;
    }

    public String getClassName() {
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

    public ArrayList<Float> getAnomalyAlgorithmColValues() {
        return anomalyAlgorithmColValues;
    }

    public ArrayList<Float> getAnomalyAlgorithmCoralatedColValues() {
        return anomalyAlgorithmCoralatedColValues;
    }

    public ArrayList<Float> getZScoreLine() {
        return ZScoreLine;
    }

    public ArrayList<Point> getPointsForCircle() { return pointsForCircle; }

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

    public int getMinColValue() {
        return minColValue;
    }

    public int getMaxColValue() {
        return maxColValue;
    }

    public int getNumofrow() {
        return numofrow;
    }

    public int getFlightLong() {
        return flightLong;
    }

    public Line getAlgorithmLine() {
        return algorithmLine;
    }

    public Circle getAlgorithmCircle() {
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
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter("lastXML.txt"));
                writer.println(chosenPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();
        }
        setChanged();
        notifyObservers("resultLoadXML");
    }

    @Override
    public void ModelOpenCSV(String chosenPath) {
        //load the last XML
        if (XML_settings == null) {
            String chosen = null;
            Scanner scanner;
            try {
                scanner = new Scanner(new BufferedReader(new FileReader("lastXML.txt")));
                chosen = scanner.nextLine();
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            HandleXML handleXML = new HandleXML();
            handleXML.deserializeFromXML(chosen);
            try {
                handleXML.deserializeFromXML(chosen);
            } catch (Exception e) {
                e.printStackTrace();
            }
            XML_settings = handleXML;
            regularFlight = new TimeSeries(XML_settings.additionalSettings.getProperFlightFile());
            regularFlight.setCorrelationTresh(0);
            linearRegression.learnNormal(regularFlight);
            zScore.learnNormal(regularFlight);
            hybrid.HybridAlgorithm(regularFlight);
            hybrid.learnNormal(regularFlight);
        }

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

            in = new TimeSeries(CSVpath);
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

    @Override
    public void modelPlay() {
        if (playFlag == 1) {
            if (_speed.intern() == "x2.0") {
                simulator = new simulator();
                timer20 = new timer20();
                timer.scheduleAtFixedRate(simulator, 0, XML_settings.additionalSettings.getDataSamplingRate());
                timer.scheduleAtFixedRate(timer20, 0, 1000);
                timer20.is_running = true;
            }

            if (_speed.intern() == "x0.5") {
                simulator = new simulator();
                timer05 = new timer05();
                timer.scheduleAtFixedRate(simulator, 0, XML_settings.additionalSettings.getDataSamplingRate());
                timer.scheduleAtFixedRate(timer05, 0,  1000);
                timer05.is_running = true;
            }

            if (_speed.intern() == "x1.0") {
                simulator = new simulator();
                timer10 = new timer10();
                timer.scheduleAtFixedRate(simulator, 0, XML_settings.additionalSettings.getDataSamplingRate());
                timer.scheduleAtFixedRate(timer10, 0,  1000);
                timer10.is_running = true;
            }

            if (_speed.intern() == "x1.5") {
                simulator = new simulator();
                timer15 = new timer15();
                timer.scheduleAtFixedRate(simulator, 0, XML_settings.additionalSettings.getDataSamplingRate());
                timer.scheduleAtFixedRate(timer15, 0, 1000);
                timer15.is_running = true;
            }
            playFlag = 0;
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

    protected void simulatorLoop() {
        if (numofrow >= in.getRows().size() - 2) {
            timer.cancel();
            modelStop();
            /*
            if (out != null) {
                out.close();
                try {
                    fg.close();
                } catch (IOException e) {
                }
            }
             */
        }
        else {
            if (out != null) {
                out.println(in.getRows().get(numofrow));
                out.flush();
            }
            allChanges();
            numofrow++;
            setChanged();
            notifyObservers("numofrow");
        }
    }

    protected void timerLoop(double speed) {
        changeTimerSpeed(speed);
        if (nowTime >= ((in.getCols()[0].getFloats().size() + 1) / (XML_settings.additionalSettings.getDataSamplingRate() / 10)) * 1000 - 1000)
            nowTime = ((in.getCols()[0].getFloats().size() + 1) / (XML_settings.additionalSettings.getDataSamplingRate() / 10)) * 1000 - 1000;
        time = simpleDateFormat.format(nowTime - 7200000);
        setChanged();
        notifyObservers("time");

    }

    protected void suspend(TimerTask simulator, TimerTask _timer)
    {
        simulator.cancel();
        _timer.cancel();
    }

    @Override
    public void modelGetChoice(String speed) {
        _speed = speed;

        if (speed.intern() == "x2.0") {
            if (timer10 != null && timer10.is_running) {
                suspend(simulator, timer10);
                timer10.is_running = false;
            }
            if (timer15 != null && timer15.is_running) {
                suspend(simulator, timer15);
                timer15.is_running = false;
            }
            if (timer05 != null && timer05.is_running) {
                suspend(simulator, timer05);
                timer05.is_running = false;
            }

            simulator = new simulator();
            timer20 = new timer20();
            timer.scheduleAtFixedRate(simulator, 0, XML_settings.additionalSettings.getDataSamplingRate() / 2);
            timer.scheduleAtFixedRate(timer20, 0, 1000);
            timer20.is_running = true;
        }

        if (speed.intern() == "x0.5") {
            if (timer10 != null && timer10.is_running) {
                suspend(simulator, timer10);
                timer10.is_running = false;
            }
            if (timer15 != null && timer15.is_running) {
                suspend(simulator, timer15);
                timer15.is_running = false;
            }
            if (timer20 != null && timer20.is_running) {
                suspend(simulator, timer20);
                timer20.is_running = false;
            }

            simulator = new simulator();
            timer05 = new timer05();
            timer.scheduleAtFixedRate(simulator, 0, (long) (XML_settings.additionalSettings.getDataSamplingRate() / 0.5));
            timer.scheduleAtFixedRate(timer05, 0, 1000);
            timer05.is_running = true;
        }

        if (speed.intern() == "x1.0")
        {
            if (timer05 != null && timer05.is_running) {
                suspend(simulator, timer05);
                timer05.is_running = false;
            }
            if (timer15 != null && timer15.is_running) {
                suspend(simulator, timer15);
                timer15.is_running = false;
            }
            if (timer20 != null && timer20.is_running) {
                suspend(simulator, timer20);
                timer20.is_running = false;
            }
            simulator = new simulator();
            timer10 = new timer10();
            timer.scheduleAtFixedRate(simulator, 0, (XML_settings.additionalSettings.getDataSamplingRate() / 1));
            timer.scheduleAtFixedRate(timer10, 0, 1000);
            timer10.is_running = true;
        }

        if (speed.intern() == "x1.5") {
            if (timer10 != null && timer10.is_running) {
                suspend(simulator, timer10);
                timer10.is_running = false;
            }
            if (timer20 != null && timer20.is_running) {
                suspend(simulator, timer20);
                timer20.is_running = false;
            }
            if (timer05 != null && timer05.is_running) {
                suspend(simulator, timer05);
                timer05.is_running = false;
            }

            simulator = new simulator();
            timer15 = new timer15();
            timer.scheduleAtFixedRate(simulator, 0, (long) (XML_settings.additionalSettings.getDataSamplingRate() / 1.5));
            timer.scheduleAtFixedRate(timer15, 0, 1000);
            timer15.is_running = true;
        }
    }

    @Override
    public void modelpause()
    {
        if (timer10 != null && timer10.is_running) {
            suspend(simulator, timer10);
            timer10.is_running = false;
        }
        if (timer20 != null && timer20.is_running) {
            suspend(simulator, timer20);
            timer20.is_running = false;
        }
        if (timer05 != null && timer05.is_running) {
            suspend(simulator, timer05);
            timer05.is_running = false;
        }
        if (timer15 != null && timer15.is_running) {
            suspend(simulator, timer15);
            timer15.is_running = false;
        }

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
        playFlag = 0;
        timer = new Timer();
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
                for(double i = 0; i<360; i+=0.5)
                {
                    float x=(float) (algorithmCircle.r*Math.cos(i) + algorithmCircle.c.x);
                    float y=(float) (algorithmCircle.r*Math.sin(i) + algorithmCircle.c.y);
                    pointsForCircle.add(new Point(x, y));
                }
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

