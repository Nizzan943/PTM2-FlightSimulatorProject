package Server;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.io.*;

@SuppressWarnings("rawtypes")
public class TimeSeries {

    public class col {

        private String name;

        private ArrayList<Float> floats;

        public col(String name) {
            this.name = name;
            this.floats = new ArrayList<Float>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Float> getFloats() {
            return floats;
        }

    }

    private col[] cols;

    private ArrayList<String> rows = new ArrayList<>();

    public double correlationTresh = 0.9;

    String myCSVname;

    Path path_of_file;

    public void setCorrelationTresh(double correlationTresh) {
        this.correlationTresh = correlationTresh;
    }

    public col[] getCols() {
        return cols;
    }

    public ArrayList<String> getRows() {
        return rows;
    }

    public TimeSeries(String csvFileName) {

        this.myCSVname = csvFileName;
        this.path_of_file = Paths.get(csvFileName);

        try {
            @SuppressWarnings("resource")
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFileName));

            bufferedReader = Files.newBufferedReader(path_of_file, StandardCharsets.US_ASCII);

            String line = bufferedReader.readLine();

            String[] value = line.split(",");

            cols = new col[value.length];

            for (int i = 0; i < value.length; i++)
                cols[i] = new col(value[i]);

            line = bufferedReader.readLine();
            rows.add(line);

            while (line != null) {

                value = line.split(",");

                for (int j = 0; j < value.length; j++)
                    cols[j].getFloats().add(Float.parseFloat(value[j]));

                line = bufferedReader.readLine();
                rows.add(line);

            }

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] ArrListToArr(ArrayList<Float> list) {
        float[] f = new float[list.size()];

        for (int i = 0; i < list.size(); i++)
            f[i] = list.get(i);

        return f;

    }

    public Point[] ArrToPoint(float[] x, float[] y) {
        Point[] p = new Point[x.length];

        for (int i = 0; i < x.length; i++)
            p[i] = new Point(x[i], y[i]);

        return p;

    }

    public int getColIndex (String colName)
    {
        for (int i = 0; i < cols.length; i++)
        {
            if (cols[i].name.intern() == colName.intern())
                return i;
        }
        return -1;
    }

}
