package Model;

public class MainTrain
{
    public static void main(String[] args)
    {
        HandleXML handleXML = new HandleXML();
        handleXML.deserializeFromXML("C:\\Users\\yuval\\Desktop\\yuval.xml");
    }
}
