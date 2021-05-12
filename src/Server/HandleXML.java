package Server;

import Server.AdditionalSettings;
import Server.UserSettings;

import java.beans.XMLDecoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleXML {

    public List<UserSettings> PropertyList = new ArrayList<>();
    public Map<String, String> RealToAssosicate = new HashMap<>();
    public Map<String, String> AssosicateToReal = new HashMap<>();
    public Map<String, Integer> max = new HashMap<>();
    public Map<String, Integer> min = new HashMap<>();

    public AdditionalSettings additionalSettings = new AdditionalSettings();
    public boolean WrongFormatAlert = false;
    public boolean MissingArgumentsAlert = false;


    public void deserializeFromXML(String path) {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(
                    new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            try {
                UserSettings decodedSettings = (UserSettings) decoder.readObject();
                PropertyList.add(decodedSettings);

            } catch (NumberFormatException e) {
                if (MissingArgumentsAlert == false)
                    WrongFormatAlert = true;
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                if (MissingArgumentsAlert == false)
                    WrongFormatAlert = true;
                break;
            } catch (ClassCastException e) {
                if (WrongFormatAlert == false)
                    MissingArgumentsAlert = true;
                break;
            }
        }
        try {
            additionalSettings = (AdditionalSettings) decoder.readObject();

        } catch (NumberFormatException e) {
            if (MissingArgumentsAlert == false)
                WrongFormatAlert = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (MissingArgumentsAlert == false)
                WrongFormatAlert = true;
        } catch (ClassCastException e) {
            if (WrongFormatAlert == false)
                MissingArgumentsAlert = true;
        }

        for (UserSettings userSettings : PropertyList) {
            if (userSettings.getRealName() == null || userSettings.getAssosicateName() == null || userSettings.getMin() == -1000000 || userSettings.getMax() == 1000000) {
                if (MissingArgumentsAlert == false)
                    WrongFormatAlert = true;
                break;
            }
        }
        if (additionalSettings.getDataSamplingRate() == 1000000 || additionalSettings.getProperFlightFile() == null || additionalSettings.algorithmFile == null) {
            if (MissingArgumentsAlert == false)
                WrongFormatAlert = true;
        }
        for (UserSettings userSettings : PropertyList) {
            RealToAssosicate.put(userSettings.getRealName(), userSettings.getAssosicateName());
            AssosicateToReal.put(userSettings.getAssosicateName(), userSettings.getRealName());
            max.put(userSettings.getRealName(), userSettings.getMax());
            min.put(userSettings.getRealName(), userSettings.getMin());
        }
        decoder.close();
    }
}
