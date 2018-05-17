package GridNetwork;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class Stuff {
    static private double c = 0.2;
    static private double v = 0.8;
    static public double HDCellFunction ( double HeadAngle , double prefferedDirection){
        double ans = Math.exp(v*(Math.cos(prefferedDirection-HeadAngle)-1));
        ans *= 1-c;
        ans += c;
        return ans;
    }
    static public double LateralFactor = 0.2;
    static public double b1 = 0.1;
    static public double b2 = 0.033;
    static public double b3 = 0.01;
    static public double b4 = 0.1;
    static public double epsilon = 0.005;
    static public double averagingConst = 0.05;
    static public double placeVariance = 5;
    static public double l = 10;
    static public double inhibitionParam = 0.05;
    static public double fVariance = 10;
    static public int timeBack = 25;
    static public double rateOffset = 2.0/Math.PI;
    static public double a0 = 0.1;
    static public double s0 = 0.3;
    static public double unitSize = 5;
    static public double displacmentVariance = 0.2;
    static public double speed = 0.4;
    static public double timeSlice = 0.01;
}
