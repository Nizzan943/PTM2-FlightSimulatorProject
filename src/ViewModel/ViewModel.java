package ViewModel;

import Model.Model;
import Server.Point;
import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends AllViewModels {
    Model model;
    StringProperty loadXMLResult;
    StringProperty openCSVResult;
    DoubleProperty minRudder;
    DoubleProperty maxRudder;
    DoubleProperty minThrottle;
    DoubleProperty maxThrottle;
    DoubleProperty maxTimeSlider;
    StringProperty chosenXMLFilePath;
    StringProperty chosenCSVFilePath;

    private ArrayList<String> colsNames;

    StringProperty time;
    FloatProperty rudderstep;
    FloatProperty throttlestep;

    FloatProperty altimeterstep;
    FloatProperty airspeedstep;
    FloatProperty directionstep;
    FloatProperty pitchstep;
    FloatProperty rollstep;
    FloatProperty yawstep;

    FloatProperty aileronstep;
    FloatProperty elevatorstep;

    FloatProperty colValues;
    FloatProperty coralatedColValue;

    public ArrayList<String> getColsNames() {
        return colsNames;
    }

    public StringProperty getChosenXMLFilePath() {
        return chosenXMLFilePath;
    }

    public StringProperty getChosenCSVFilePath() {
        return chosenCSVFilePath;
    }

    public DoubleProperty getMinRudder() {
        return minRudder;
    }

    public DoubleProperty getMaxRudder() {
        return maxRudder;
    }

    public DoubleProperty getMinThrottle() {
        return minThrottle;
    }

    public DoubleProperty getMaxThrottle() {
        return maxThrottle;
    }

    public DoubleProperty getMaxTimeSlider() {
        return maxTimeSlider;
    }


    public StringProperty getTime() {
        return time;
    }

    public FloatProperty getRudderstep() {
        return rudderstep;
    }

    public FloatProperty getThrottlestep() {
        return throttlestep;
    }

    public FloatProperty getAltimeterstep() {
        return altimeterstep;
    }

    public FloatProperty getAirspeedstep() {
        return airspeedstep;
    }

    public FloatProperty getDirectionstep() {
        return directionstep;
    }

    public FloatProperty getPitchstep() {
        return pitchstep;
    }

    public FloatProperty getRollstep() {
        return rollstep;
    }

    public FloatProperty getYawstep() {
        return yawstep;
    }

    public FloatProperty getAileronstep() {
        return aileronstep;
    }

    public FloatProperty getElevatorstep() {
        return elevatorstep;
    }

    public FloatProperty getColValues() {
        return colValues;
    }

    public FloatProperty getCoralatedColValue()
    {
        return coralatedColValue;
    }

    public ViewModel(Model model) {
        this.model = model;
        loadXMLResult = new SimpleStringProperty();
        openCSVResult = new SimpleStringProperty();
        chosenXMLFilePath = new SimpleStringProperty();
        chosenCSVFilePath = new SimpleStringProperty();
        minRudder = new SimpleDoubleProperty();
        maxRudder = new SimpleDoubleProperty();
        minThrottle = new SimpleDoubleProperty();
        maxThrottle = new SimpleDoubleProperty();
        maxTimeSlider = new SimpleDoubleProperty();
        colsNames = new ArrayList<>();
        time = new SimpleStringProperty();
        rudderstep = new SimpleFloatProperty();
        throttlestep = new SimpleFloatProperty();
        altimeterstep = new SimpleFloatProperty();
        airspeedstep = new SimpleFloatProperty();
        directionstep = new SimpleFloatProperty();
        pitchstep = new SimpleFloatProperty();
        rollstep = new SimpleFloatProperty();
        yawstep = new SimpleFloatProperty();
        aileronstep = new SimpleFloatProperty();
        elevatorstep = new SimpleFloatProperty();
        colValues = new SimpleFloatProperty();
        coralatedColValue = new SimpleFloatProperty();
    }

    public StringProperty getLoadXMLResult() {
        return loadXMLResult;
    }

    public StringProperty getOpenCSVResult() {
        return openCSVResult;
    }

    public void VMLoadXML() {
        model.ModelLoadXML(chosenXMLFilePath.getValue());
    }

    public void VMOpenCSV() {
        model.ModelOpenCSV(chosenCSVFilePath.getValue());
    }

    @Override
    public void update(Observable o, Object arg) {
        String p = (String) arg;
        if (p.intern() == "resultLoadXML") {
            if (model.getResultLoadXML().intern() == "WrongFormatAlert")
                loadXMLResult.set("WrongFormatAlert");
            if (model.getResultLoadXML().intern() == "MissingArgumentAlert")
                loadXMLResult.set("MissingArgumentAlert");
            if (model.getResultLoadXML().intern() == "SuccessAlert")
                loadXMLResult.set("SuccessAlert");
        }
        if (p.intern() == "resultOpenCSV") {
            if (model.getResultOpenCSV().intern() == "Missing Arguments")
                openCSVResult.set("Missing Arguments");
            if (model.getResultOpenCSV().intern() == "Incompatibility with XML file")
                openCSVResult.set("Incompatibility with XML file");
            if (model.getResultOpenCSV().intern() == "OK") {
                openCSVResult.set("OK");
                for (String name : model.getColsNames()) {
                    colsNames.add(name);
                }
            }
        }
        if (p.intern() == "time")
            time.set(model.gettime());
        if (p.intern() == "rudder")
            rudderstep.set(model.getRudderstep());
        if (p.intern() == "throttle")
            throttlestep.set(model.getThrottlestep());
        if (p.intern() == "altimeter")
            altimeterstep.set(model.getAltimeterstep());
        if (p.intern() == "airspeed")
            airspeedstep.set(model.getAirspeedstep());
        if (p.intern() == "direction")
            directionstep.set(model.getDirectionstep());
        if (p.intern() == "pitch")
            pitchstep.set(model.getPitchstep());
        if (p.intern() == "roll")
            rollstep.set(model.getRollstep());
        if (p.intern() == "yaw")
            yawstep.set(model.getYawstep());
        if (p.intern() == "aileron")
            aileronstep.set(model.getAileronstep());
        if (p.intern() == "elevator")
            elevatorstep.set(model.getElevatorstep());
        if (p.intern() == "colValues")
            colValues.set(model.getColValues());
        if (p.intern() == "coralatedColValue")
            coralatedColValue.set(model.getCoralatedColValues());
    }

    public void VMplay() {
        model.modelPlay();
    }

    public void VMGetChoice(String speed) {
        model.modelGetChoice(speed);
    }

    public void VMpause() {
        model.modelpause();
    }

    public void VMplus15() {
        model.modelPlus15();
    }

    public void VMminus15() {
        model.modelMinus15();
    }

    public void VMminus30() {
        model.modelMinus30();
    }

    public void VMplus30() {
        model.modelPlus30();
    }

    public void VMsetMinRudder() {
        minRudder.setValue(model.modelSetMinRudder());
    }

    public void VMsetMaxRudder() {
        maxRudder.setValue(model.modelSetMaxRudder());
    }

    public void VMsetMinThrottle() {
        minThrottle.setValue(model.modelSetMinThrottle());
    }

    public void VMsetMaxThrottle() {
        maxThrottle.setValue(model.modelSetMaxThrottle());
    }

    public void setMaxTimeSlider() {
        maxTimeSlider.setValue(model.modelSetMaxTimeSlider());
    }

    public void VMtimeslider(double second) {
        model.modelTimeSlider(second);
    }

    public void VMstop() {
        model.modelStop();
    }

    public void VMsetLeftLineChart(String colName)
    {
        model.modelSetLeftLineChart(colName);
    }

    public void VMsetRightLineChart(String colName)
    {
        model.modelSetRightLineChart(colName);
    }
}
