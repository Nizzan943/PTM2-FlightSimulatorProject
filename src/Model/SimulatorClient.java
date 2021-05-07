package Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SimulatorClient {
    public static volatile boolean stop = false;

    private static PrintWriter outputStream;

    private static Socket socket;

    public void Connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);

            outputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connected to the Server");
    }

    public void SendData(String[] data) throws InterruptedException {
        for (String string : data) {
            outputStream.println(string);

            outputStream.flush();

            Thread.sleep(100);
        }
    }

    public void Stop() {
        if (outputStream == null)
            return;

        outputStream.close();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
