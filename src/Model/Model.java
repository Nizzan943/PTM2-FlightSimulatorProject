package Model;

import Server.TimeSeries;
import View.Controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer
{
    public ArrayList<Float> getAileron() {
        return aileron;
    }

    public ArrayList<Float> getElevator() {
        return elevator;
    }

    private ArrayList<Float> aileron;
    private ArrayList<Float> elevator;

    public void getJoystickParameters()
    {
        TimeSeries timeSeries = new TimeSeries(Controller.CSVpath);
        for (TimeSeries.col col: timeSeries.getCols())
        {
            if (col.getName().intern() == "aileron")
                aileron = col.getFloats();
            if (col.getName().intern() == "elevator")
                elevator = col.getFloats();
        }
    }


    public void setRudder(float rudder)
    {

    }

    public void setThrottle(float throttle)
    {

    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
