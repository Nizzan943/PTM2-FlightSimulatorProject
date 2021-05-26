package ViewModel;

import java.util.Observable;
import java.util.Observer;

public abstract class AllViewModels extends Observable implements Observer {

    public abstract void VMLoadXML();
    public abstract void VMOpenCSV();
    public abstract void VMplay();
    public abstract void VMGetChoice(String speed);
    public abstract void VMpause();
    public abstract void VMplus15();
    public abstract void VMminus15();
    public abstract void VMminus30();
    public abstract void VMplus30();
    public abstract void VMstop();
    public abstract void VMsetLeftLineChart(String colName);
    public abstract void VMsetRightLineChart(String colName);
    public abstract void VMsetAlgorithmLineChart(String colName);
    public abstract void VMLoadAlgorithm(String resultClassDirectory, String resultClassName);

    @Override
    public abstract void update(Observable o, Object arg);
}
