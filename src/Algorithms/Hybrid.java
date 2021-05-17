package Algorithms;


import Server.*;

import java.util.*;

public class Hybrid implements TimeSeriesAnomalyDetector {

    public ArrayList<Circle> circles = new ArrayList<>();
    public Map<String, String> whichAlgorithm = new HashMap<>();
    public ArrayList<CorrelatedFeatures> theCorrelatedFeatures = new ArrayList<>();


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
            if (!whichAlgorithm.containsKey(features.feature1)) {
                whichAlgorithm.put(features.feature1, "Hybrid");
                theCorrelatedFeatures.add(features);
            }
        }

        for (TimeSeries.col col :timeSeries.getCols())
        {
            if (!whichAlgorithm.containsKey(col.getName()))
                whichAlgorithm.put(col.getName(), "ZScore");
        }
    }

    @Override
    public void learnNormal(TimeSeries timeSeries) {
        for (CorrelatedFeatures features: theCorrelatedFeatures)
        {
            ArrayList<Float> column_i = timeSeries.getCols()[timeSeries.getColIndex(features.feature1)].getFloats();
            ArrayList<Float> column_j = timeSeries.getCols()[timeSeries.getColIndex(features.feature2)].getFloats();
            Vector<Point> pointsVector = new Vector<>();
            for (int t = 0; t < timeSeries.getCols()[0].getFloats().size(); t++) {
                pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
            }
            Circle circle = Welzl.makeCircle(pointsVector);
            circles.add(circle);
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries timeSeries) {
        List<AnomalyReport> anomalyReports = new ArrayList<AnomalyReport>();
        int k = 0;

        for (CorrelatedFeatures features: theCorrelatedFeatures)
        {
            ArrayList<Float> column_i = timeSeries.getCols()[timeSeries.getColIndex(features.feature1)].getFloats();
            ArrayList<Float> column_j = timeSeries.getCols()[timeSeries.getColIndex(features.feature2)].getFloats();
            Vector<Point> pointsVector = new Vector<>();
            for (int t = 0; t < timeSeries.getCols()[0].getFloats().size(); t++) {
                pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
            }
            for (int h = 0; h < pointsVector.size(); h++) {
                if (!circles.get(k).contains(pointsVector.get(h))) {
                    anomalyReports.add(new AnomalyReport(features.feature1 + "-" + features.feature2, h));
                }
            }
            k++;
        }
        return anomalyReports;
    }
}
