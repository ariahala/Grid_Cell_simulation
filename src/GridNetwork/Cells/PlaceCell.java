package GridNetwork.Cells;

import GridNetwork.Stuff;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class PlaceCell {
    public double prefferedX;
    public double prefferedY;
    public double rate = 0;
    public double averageRate = 0;

    public PlaceCell(double prefferedX, double prefferedY) {
        this.prefferedX = prefferedX;
        this.prefferedY = prefferedY;
    }

    public double calculateRate (double x , double y ){
        double temp = Math.pow(x-prefferedX,2.0) + Math.pow(y-prefferedY,2.0);
        temp /= 2.0 * Math.pow(Stuff.placeVariance,2.0);
        temp = 0-temp;
        this.rate = Math.exp(temp);
        return this.rate;
    }
    public void calculateAverageRate (){
        this.averageRate = this.averageRate + Stuff.averagingConst*(rate - averageRate);
    }
}
