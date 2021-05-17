package Algorithms;

import Server.AnomalyReport;
import Server.TimeSeries;
import Server.TimeSeriesAnomalyDetector;

import java.util.ArrayList;
import java.util.List;

import static Server.StatLib.avg;
import static Server.StatLib.var;

public class ZScore implements TimeSeriesAnomalyDetector {
    public float[] thresholdArray;
    public ArrayList<ArrayList<Float>> colZscores = new ArrayList<>();


    @Override
    public void learnNormal(TimeSeries timeSeries) {
        thresholdArray = new float[timeSeries.getCols().length];
        float max = -1;
        float xTreshold;
        ArrayList<Float> zscoresCol = null;
        for (int i = 0; i < timeSeries.getCols().length; i++) {
            zscoresCol = new ArrayList<>();
            for (int j = 0; j < timeSeries.getCols()[i].getFloats().size(); j++) {
                if (j != 0 && j != 1)
                    xTreshold = Zscore(j, ArrayListToFloat(timeSeries.getCols()[i].getFloats().subList(0, j - 1)));
                else
                    xTreshold = 0;
                zscoresCol.add(xTreshold);
                if (max < xTreshold) {
                    max = xTreshold;
                }
            }
            colZscores.add(zscoresCol);
            thresholdArray[i] = max;
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries timeSeries) {
        List<AnomalyReport> anomalyReports = new ArrayList<>();
        float xTreshold;
        for (int i = 0; i < timeSeries.getCols().length; i++) {
            String columnName = timeSeries.getCols()[i].getName();
            for (int j = 0; j < timeSeries.getCols()[i].getFloats().size(); j++) {
                if (j != 0 && j != 1)
                    xTreshold = Zscore(j, ArrayListToFloat(timeSeries.getCols()[i].getFloats().subList(0, j - 1)));
                else
                    xTreshold = 0;
                if (xTreshold > thresholdArray[i]) {
                    anomalyReports.add(new AnomalyReport(columnName, j));
                    break;
                }
            }
        }
        return anomalyReports;
    }

    public static float[] ArrayListToFloat(List<Float> floatList) {
        float[] floatArr = new float[floatList.size()];
        for (int i = 0; i < floatArr.length; i++) {
            floatArr[i] = floatList.get(i);
        }
        return floatArr;
    }

    public static float Zscore(float x, float[] arr) {
        if (var(arr) == 0)
            return 0;
        return (float) (Math.abs((x - avg(arr))
                / Math.sqrt(var(arr))));
    }
}
