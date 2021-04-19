package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainTrain
{
    public static void main(String[] args) throws Exception {
        Socket fg=new Socket("localhost", 5400);
        BufferedReader in=new BufferedReader(new FileReader("D:\\Repos\\PTM2-FlightSimulatorProject\\Files\\reg_flight.csv"));
        PrintWriter out=new PrintWriter(fg.getOutputStream());
        String line;
        while((line=in.readLine())!=null) {
            out.println(line);
            out.flush();
            Thread.sleep(20);
        }
        out.close();
        in.close();
        fg.close();
    }
}
