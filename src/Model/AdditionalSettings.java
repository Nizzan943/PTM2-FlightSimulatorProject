package Model;

public class AdditionalSettings {

    Float dataSamplingRate = (float)1000000;
    String properFlightFile;
    String algorithmFile;

    public AdditionalSettings(){}

    public Float getDataSamplingRate() {
        return dataSamplingRate;
    }

    public void setDataSamplingRate(Float dataSamplingRate) {
        this.dataSamplingRate = dataSamplingRate;
    }

    public String getProperFlightFile() {
        return properFlightFile;
    }

    public void setProperFlightFile(String properFlightFile) {
        this.properFlightFile = properFlightFile;
    }

    public String getAlgorithmFile() {
        return algorithmFile;
    }

    public void setAlgorithmFile(String algorithmFile) {
        this.algorithmFile = algorithmFile;
    }
}
