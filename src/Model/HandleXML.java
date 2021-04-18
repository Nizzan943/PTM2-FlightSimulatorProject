package Model;

import java.beans.XMLDecoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleXML {

    public List<UserSettings> PropertyList = new ArrayList<>();
    public AdditionalSettings additionalSettings = new AdditionalSettings();

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
    public void deserializeFromXML(String path) throws Exception
    {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(
                    new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e){ e.printStackTrace(); }
        for (int i = 0; i < 2; i++) {
            try {
                UserSettings decodedSettings = (UserSettings) decoder.readObject();
                PropertyList.add(decodedSettings);

            } catch (NumberFormatException e)
            {
                System.out.println("Error: Incorrect data format, please check XML format, do you provide string-assosiated name?. \n" + e);

            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Error: index is out of array bound. \n" + e);
            }
            catch (ClassCastException e){
                System.out.println("Error: Cast error. \n" + e);
            }
        }
        additionalSettings = (AdditionalSettings) decoder.readObject();
        decoder.close();
    }
}
