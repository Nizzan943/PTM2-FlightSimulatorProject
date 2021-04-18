package Model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleXML {

    List<UserSettings> PropertyList = new ArrayList<>();
    float Data_sampling_rate;
    String Proper_flight_file;
    String Algorithm_file;

   /* public static void serializeToXML(UserSettings settings) throws IOException {
        FileOutputStream fos = new FileOutputStream("settings.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :" + e.toString());
            }
        });
        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }

*/
    public void deserializeFromXML(String path)
    {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(
                    new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e){ e.printStackTrace(); }
        ArrayList<UserSettings> userSettings = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            try {
                UserSettings decodedSettings = (UserSettings) decoder.readObject();
                PropertyList.add(decodedSettings);

            } catch (Exception e)
            {
                System.out.println(e);
            }
        }
        Data_sampling_rate = (float)decoder.readObject();
        Proper_flight_file = (String)decoder.readObject();
        Algorithm_file = (String)decoder.readObject();
        decoder.close();
    }
}
