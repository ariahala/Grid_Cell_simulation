package GridNetwork.Connections;

import GridNetwork.Cells.ConjunctiveCell;
import GridNetwork.Cells.PlaceCell;
import GridNetwork.Stuff;

import java.util.Random;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class PCSynapse {
    public PlaceCell placeCell;
    public ConjunctiveCell gridCell;
    public double weight = 0;
    public double previousWeight = 0;

    public PCSynapse(ConjunctiveCell gridCell, PlaceCell placeCell) {
        this.gridCell = gridCell;
        this.placeCell = placeCell;
        this.weight = 0.9 + (0.1 * new Random().nextDouble());
        this.previousWeight = this.weight;
    }
    public void updateWeight(){
        previousWeight = weight;
        weight += Stuff.epsilon*((placeCell.rate*gridCell.rate) - (placeCell.averageRate*gridCell.averageRate));
    }
    public double activation (){
        return previousWeight * placeCell.rate;
    }
}
