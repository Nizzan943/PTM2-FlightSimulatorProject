package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MainTrain
{
/*
    public static void main(String[] args) throws Exception {
        Socket fg=new Socket("localhost", 5400);
        BufferedReader in=new BufferedReader(new FileReader("C:\\Users\\yuval\\Downloads\\reg_flight (1).csv"));
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
*/

    public static void main(String[] args) throws Exception {
        TimeSeries ts=new TimeSeries("C:\\Users\\yuval\\Desktop\\reg_flight (4).csv");
        TimeSeries ts2=new TimeSeries("C:\\Users\\yuval\\Desktop\\reg_flight (4).csv");
        Hybrid ad=new Hybrid();
        List<AnomalyReport> temp =  ad.HybridAlgorithm(ts,ts2);
        for (AnomalyReport a : temp)
        {
            System.out.println(a.description + "-" + a.timeStep);
        }
        System.out.println("done");
    }

}
