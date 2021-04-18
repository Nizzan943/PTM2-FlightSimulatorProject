package Model;

public class MainTrain
{
    public static void main(String[] args) throws Exception {
        HandleXML handleXML = new HandleXML();
        handleXML.deserializeFromXML("C:\\Users\\yuval\\Desktop\\yuval2.xml");
    }
}
