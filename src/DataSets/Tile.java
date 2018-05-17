package DataSets;

/**
 * Created by mac on 12/18/2016 AD.
 */
public class Tile {
    public double x;
    public double y;
    public double rate;
    public boolean isInRange = false;

    public Tile(double x, double y , double diameter) {
        this.rate = 0;
        this.x = x;
        this.y = y;
        double distanse = Math.pow(x-(diameter/2),2.0) + Math.pow(y-(diameter/2),2.0);
        distanse = Math.sqrt(distanse);
        if ( distanse > diameter/2){
            isInRange = false;
        }else{
            isInRange = true;
        }
    }
    public void update (double rateGiven){
        this.rate = rateGiven;
    }
}
