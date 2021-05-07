package Server;


public class StatLib {

    // simple average
    public static float avg(float[] x) {
        float sum = 0;

        for (float i : x)
            sum += i;

        return sum / x.length;
    }

    public static float avgList(Object[] x) {
        float sum = 0;

        for (Object i : x)
            sum += Float.parseFloat(i.toString());

        return sum / x.length;
    }


    // returns the variance of X and Y
    public static float var(float[] x) {
        float average = avg(x);

        float temp = 0;

        for (float i : x)
            temp += (i - average) * (i - average);

        return temp / (x.length);
    }

    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y) {

        float sum = 0;

        for (int i = 0; i < x.length; i++)
            sum += (x[i] - avg(x)) * (y[i] - avg(y));

        return sum / (x.length);
    }


    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y) {
        float sqrtX = (float) Math.sqrt(var(x));

        float sqrtY = (float) Math.sqrt(var(y));

        float covar = cov(x, y);

        return covar / (sqrtX * sqrtY);
    }


    // performs a linear regression and returns the line equation
    public static Line linear_reg(Point[] points) {
        float[] arrX = new float[points.length];

        float[] arrY = new float[points.length];

        for (int i = 0; i < points.length; i++)
            arrX[i] = points[i].x;

        for (int i = 0; i < points.length; i++)
            arrY[i] = points[i].y;

        float a = cov(arrX, arrY) / var(arrX);

        float b = avg(arrY) - (a * avg(arrX));

        return new Line(a, b);
    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p, Point[] points) {
        Line l = linear_reg(points);

        return dev(p, l);
    }

    // returns the deviation between point p and the line
    public static float dev(Point p, Line l) {
        float m = l.a * p.x;

        float n = l.b;

        float temp = (float) (p.y - m - n);

        return Math.abs(temp);
    }

}
