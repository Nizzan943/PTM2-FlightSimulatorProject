package Model;

import java.beans.XMLDecoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleXML {

    public List<UserSettings> PropertyList = new ArrayList<>();
    public AdditionalSettings additionalSettings = new AdditionalSettings();
    public boolean WrongFormatAlert = false;
    public boolean MissingArgumentsAlert = false;



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
                WrongFormatAlert = true;
                break;
            }
            catch (ArrayIndexOutOfBoundsException e){
                WrongFormatAlert = true;
                break;
            }
            catch (ClassCastException e){
                MissingArgumentsAlert = true;
                break;
            }
        }
        try {
            additionalSettings = (AdditionalSettings) decoder.readObject();

        } catch (NumberFormatException e)
        {
            WrongFormatAlert = true;
        }
        catch (ArrayIndexOutOfBoundsException e){
            WrongFormatAlert = true;
        }
        catch (ClassCastException e){
            MissingArgumentsAlert = true;
        }

        for (UserSettings userSettings: PropertyList)
        {
            if (userSettings.getRealName() == null || userSettings.getAssosicateName() == null || userSettings.getMin() == -1000000 || userSettings.getMax() == 1000000 ) {
                WrongFormatAlert = true;
                break;
            }
        }
        if (additionalSettings.getDataSamplingRate() == 1000000 || additionalSettings.getProperFlightFile() == null || additionalSettings.algorithmFile == null)
            WrongFormatAlert = true;

        decoder.close();
    }
}
