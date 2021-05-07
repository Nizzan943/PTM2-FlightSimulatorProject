package Server;

import Server.TimeSeriesAnomalyDetector;


public interface PluginLoader<T> {

    public void LoadClass(String path, String className);
}
