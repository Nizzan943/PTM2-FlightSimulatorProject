package Server;

public class AdditionalSettings {

    long dataSamplingRate = (long) 1000000;
    String properFlightFile;
    String algorithmFile;

    public AdditionalSettings(){}

    public long getDataSamplingRate() {
        return dataSamplingRate;
    }

    public void setDataSamplingRate(long dataSamplingRate) {
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
