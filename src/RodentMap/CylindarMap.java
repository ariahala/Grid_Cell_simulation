package RodentMap;

import GridNetwork.Network;
import GridNetwork.Stuff;

import java.util.Random;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class CylindarMap {
    public static double X;
    public static double Y;
    public double diameter;
    public Network brain;

    public CylindarMap( double diameter , int gridCellCount) {
        this.diameter = diameter;
        this.X = diameter/2;
        this.Y = diameter/2;
        this.brain = new Network(this.X,this.Y,0);
        this.brain.makeNetworkCells(gridCellCount,diameter);
    }

    public void updatePosition (){
        double distanse = 0;
        double newX , newY;
        double nextDirection;
        int i = 0;
        do {
            if ( i < 6 ) {
                nextDirection = new Random().nextGaussian();
                nextDirection *= Stuff.displacmentVariance;
                nextDirection += brain.rodentDirection;
            }else{
                nextDirection = new Random().nextDouble() * 2 * Math.PI;
            }
            newX = X + Math.cos(nextDirection) * Stuff.speed * Stuff.timeSlice * 100;
            newY = Y + Math.sin(nextDirection) * Stuff.speed * Stuff.timeSlice * 100;
            distanse = Math.pow(newX - (diameter / 2), 2.0) + Math.pow(newY - (diameter / 2), 2.0);
            distanse = Math.sqrt(distanse);
            i++;
        }while(!(distanse < diameter/2));
        this.X = newX;
        this.Y = newY;
        brain.doTimeStep(true,X,Y,nextDirection);
    }
}
