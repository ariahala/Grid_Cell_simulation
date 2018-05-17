package GridNetwork.Connections;

import GridNetwork.Cells.ConjunctiveCell;
import GridNetwork.Stuff;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class CCSynapse {
    public ConjunctiveCell preSynaptic;
    public ConjunctiveCell postSynaptic;
    public double weight;

    public CCSynapse ( ConjunctiveCell preSynaptic , ConjunctiveCell postSynaptic ) {
        this.postSynaptic = postSynaptic;
        this.preSynaptic = preSynaptic;
        double tan = (preSynaptic.auxilaryPlaceCell.prefferedY - postSynaptic.auxilaryPlaceCell.prefferedY)/(preSynaptic.auxilaryPlaceCell.prefferedX - postSynaptic.auxilaryPlaceCell.prefferedX);
        double diretion = Math.atan(tan);
        double d = 0;
        d += Math.pow(preSynaptic.auxilaryPlaceCell.prefferedX - postSynaptic.auxilaryPlaceCell.prefferedX -(Stuff.l*Math.cos(diretion)),2.0);
        d += Math.pow(preSynaptic.auxilaryPlaceCell.prefferedY - postSynaptic.auxilaryPlaceCell.prefferedY -(Stuff.l*Math.cos(diretion)),2.0);
        d /= 2.* Math.pow(Stuff.fVariance,2.0);
        d = 0-d;
        double temp = Math.exp(d);
        temp *= Stuff.HDCellFunction(diretion,preSynaptic.prefferedDirection);
        temp *= Stuff.HDCellFunction(diretion,postSynaptic.prefferedDirection);
        temp -= Stuff.inhibitionParam;
        if ( temp < 0 ){
            temp = 0;
        }
        this.weight = temp;
    }
    public double activation (){
        return preSynaptic.getRateAtTime(Stuff.timeBack)*weight;
    }
}
