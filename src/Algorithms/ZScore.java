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


    @Override
    public void learnNormal(TimeSeries timeSeries) {
        float max;

        for (int j = 0; j < timeSeries.getCols().length; j++) {
            max = -1;
            Float[] floatArray = new Float[timeSeries.getCols()[j].getFloats().size()];
            for (int i = 2; i < timeSeries.getCols()[j].getFloats().size(); i++) {
                float xTreshold = Zscore(timeSeries.getCols()[j].getFloats().get(i), ArrayListToFloat(timeSeries.getCols()[j].getFloats().subList(0, i)));
                if (max < xTreshold) {
                    max = xTreshold;
                }
            }
            thresholdArray[j] = max;
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries timeSeries) {
        List<AnomalyReport> anomalyReports = new ArrayList<>();
        for (int j = 0; j < timeSeries.getCols().length; j++) {
            float max = -1;
            String columnName = timeSeries.getCols()[j].getName();
            Float[] floatArray = new Float[timeSeries.getCols()[j].getFloats().size()];
            for (int i = 2; i < timeSeries.getCols()[j].getFloats().size(); i++) {
                float xTreshold = Zscore(timeSeries.getCols()[j].getFloats().get(i), ArrayListToFloat(timeSeries.getCols()[j].getFloats().subList(0, i)));
                if (xTreshold > thresholdArray[j]) {
                    anomalyReports.add(new AnomalyReport(columnName, j + 1));
                    break;
                }
            }
        }
        return anomalyReports;
    }

    public ZScore(TimeSeries timeSeries) {
        thresholdArray = new float[timeSeries.getCols().length];
    }

    public static float[] ArrayListToFloat(List<Float> floatList) {
        float[] floatArr = new float[floatList.size()];
        for (int i = 0; i < floatArr.length; i++) {
            floatArr[i] = floatList.get(i);
        }
        return floatArr;
    }

    public static float Zscore(float x, float[] arr) {
        return (float) (Math.abs((x - avg(arr))
                / Math.sqrt(var(arr))));
    }
}
