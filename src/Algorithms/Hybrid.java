package Algorithms;


import Server.*;

import java.util.*;

public class Hybrid implements TimeSeriesAnomalyDetector {

    public ArrayList<Circle> circles = new ArrayList<>();
    public ArrayList<AnomalyReport> detections = new ArrayList<>();
    public Map<String, String> whichAlgorithm = new HashMap<>();


    public void HybridAlgorithm(TimeSeries timeSeries) {

        LinearRegression linearReg = new LinearRegression();
        timeSeries.setCorrelationTresh(0.95);
        linearReg.learnNormal(timeSeries);
        List<CorrelatedFeatures> correlatedFeatures = linearReg.getNormalModel();
        for (CorrelatedFeatures features: correlatedFeatures)
        {
            whichAlgorithm.put(features.feature1, "LinearRegression");
            whichAlgorithm.put(features.feature2, "LinearRegression");
        }

        LinearRegression linearReg_hybrid = new LinearRegression();
        timeSeries.setCorrelationTresh(0.5);
        linearReg_hybrid.learnNormal(timeSeries);
        List<CorrelatedFeatures> correlatedFeatures1 = linearReg_hybrid.getNormalModel();
        for (CorrelatedFeatures features: correlatedFeatures1)
        {
            if (!whichAlgorithm.containsKey(features.feature1))
                whichAlgorithm.put(features.feature1, "Hybrid");
            if (!whichAlgorithm.containsKey(features.feature2))
                whichAlgorithm.put(features.feature2, "Hybrid");
        }

        for (TimeSeries.col col :timeSeries.getCols())
        {
            if (!whichAlgorithm.containsKey(col.getName()))
                whichAlgorithm.put(col.getName(), "ZScore");
        }
    }

    @Override
    public void learnNormal(TimeSeries timeSeries) {
        String[] str = new String[timeSeries.getCols().length];

        for (int i = 0; i < timeSeries.getCols().length; i++)
            str[i] = timeSeries.getCols()[i].getName();

        for (int i = 0; i < str.length; i++) {
            ArrayList<Float> column_i = timeSeries.getCols()[i].getFloats();

            for (int j = i + 1; j < str.length; j++) {

                if (str[i] != str[j]) {
                    ArrayList<Float> column_j = timeSeries.getCols()[j].getFloats();
                    Vector<Point> pointsVector = new Vector<Point>();
                    for (int t = 0; t < timeSeries.getCols()[j].getFloats().size(); t++) {
                        pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
                    }
                    Circle circle = Welzl.makeCircle(pointsVector);
                    circles.add(circle);
                }

            }
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries timeSeries) {
        List<AnomalyReport> anomalyReports = new ArrayList<AnomalyReport>();
        String[] str = new String[timeSeries.getCols().length];

        for (int i = 0; i < timeSeries.getCols().length; i++)
            str[i] = timeSeries.getCols()[i].getName();

        int index = 0;
        int k = 0;
        for (int i = 0; i < str.length; i++) {
            ArrayList<Float> column_i = timeSeries.getCols()[i].getFloats();

            for (int j = i + 1; j < str.length; j++) {

                if (str[i] != str[j]) {
                    ArrayList<Float> column_j = timeSeries.getCols()[j].getFloats();
                    Vector<Point> pointsVector = new Vector<Point>();
                    for (int t = 0; t < timeSeries.getCols()[j].getFloats().size(); t++) {
                        pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
                    }

                    for (int h = 0; h < pointsVector.size(); h++) {

                        if (!circles.get(k).contains(pointsVector.get(h))) {
                            anomalyReports.add(new AnomalyReport(str[i] + "-" + str[j], h + 1));
                        }
                    }

                }
                k++;
            }

        }

        return anomalyReports;
    }
}
