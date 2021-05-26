package Model;

import java.util.Observable;

public abstract class AllModels extends Observable
{
    public abstract void ModelLoadXML(String chosenPath);
    public abstract void ModelOpenCSV(String chosenPath);
    public abstract void modelPlay();
    public abstract void modelGetChoice(String speed);
    public abstract void modelpause();
    public abstract void modelPlus15();
    public abstract void modelMinus15();
    public abstract void modelMinus30();
    public abstract void modelPlus30();
    public abstract void modelStop();
    public abstract void modelSetLeftLineChart(String colName);
    public abstract void modelSetRightLineChart(String colName);
    public abstract void modelSetAlgorithmLineChart(String colName);
    public abstract void modelLoadAlgorithm(String resultClassDirectory, String resultClassName);
}
