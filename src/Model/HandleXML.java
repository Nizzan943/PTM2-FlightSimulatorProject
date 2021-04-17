package Model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleXML {

    public static void serializeToXML(UserSettings settings) throws IOException {
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


    public static void deserializeFromXML(String path) throws IOException
    {
        List<Object> PropertyList = new ArrayList<>();
        XMLDecoder decoder = new XMLDecoder(
                new BufferedInputStream(new FileInputStream(path)));
        ArrayList<UserSettings> userSettings = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            UserSettings decodedSettings = (UserSettings) decoder.readObject();
            PropertyList.add(decodedSettings);
        }

        decoder.close();
    }
}
