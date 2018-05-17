package GridNetwork.Cells;

import DataSets.GridCellRateMap;
import GridNetwork.Connections.CCSynapse;
import GridNetwork.Connections.PCSynapse;
import GridNetwork.Network;
import GridNetwork.Stuff;

import java.util.ArrayList;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class ConjunctiveCell {
    public GridCellRateMap data;
    public int index;
    public double rate = 0;
    public PlaceCell auxilaryPlaceCell;
    private ArrayList<Double> rateHistory = new ArrayList<>();
    public ArrayList<CCSynapse> LateralSynapse = new ArrayList<>();
    public ArrayList<PCSynapse> PlaceSynapses = new ArrayList<>();
    public double prefferedDirection;
    public double Beta = 0;
    public double Alpha = 0;
    public double input = 0;
    public double averageRate = 0;

    public ConjunctiveCell(double prefferedDirection , PlaceCell auxilaryPlaceCell) {
        this.data = new GridCellRateMap(this,125,2);
        this.prefferedDirection = prefferedDirection;
        for ( int i = 0 ; i < Stuff.timeBack + 1 ; i++ ){
            rateHistory.add(new Double(0));
        }
        this.auxilaryPlaceCell = auxilaryPlaceCell;
    }
    public double getRateAtTime (int timeBack ) {
        return this.rateHistory.get(timeBack);
    }
    private void pushHistory (){
        for ( int i = 0 ; i < Stuff.timeBack ; i++ ){
            rateHistory.set(i+1,rateHistory.get(i));
        }
        rateHistory.set(0,rate);
    }
    public double calculateInput (double direction) {
        double ans = 0;
        if ( Network.time > 40) {
            for (int i = 0; i < LateralSynapse.size(); i++) {
                ans += LateralSynapse.get(i).activation();
            }
        }
//        System.out.println(ans);
        ans *= Stuff.LateralFactor;
        for ( int i = 0 ; i < PlaceSynapses.size() ; i++ ){
            ans += PlaceSynapses.get(i).activation();
        }
        ans *= Stuff.HDCellFunction(direction,this.prefferedDirection);
        this.input = ans;

        return this.input;
    }
    public void calculateAlphaBeta(){
        double tempAlpha = Alpha;
        double tempBeta = Beta;
        Alpha = tempAlpha + Stuff.b1*(this.input - tempBeta-tempAlpha);
        Beta = tempBeta + Stuff.b2*(this.input - tempBeta);
    }
    public double calculateRate (double iteratedMean , double iteratedSparsity){
        if ( Alpha < iteratedMean ){
            rate = 0;
            return 0;
        }else{
            rate = Stuff.rateOffset*Math.atan(iteratedSparsity*(Alpha - iteratedMean));

            return rate;
        }
    }
    public void setRate (){
        pushHistory();
    }
    public void calculateAverageRate(){
        this.averageRate += Stuff.averagingConst*(rate-this.averageRate);
    }
    public void renormalizeWeights() {
        double temp = 0;
        for (int i = 0; i < PlaceSynapses.size(); i++) {
            temp += Math.pow(PlaceSynapses.get(i).weight, 2.0);
        }
        temp = Math.sqrt(temp);
        for (int i = 0; i < PlaceSynapses.size(); i++) {
            PlaceSynapses.get(i).weight /= temp;
        }
    }
    public void updateAllSynapses(){
        for ( int i = 0 ; i < PlaceSynapses.size() ; i++ ){
            PlaceSynapses.get(i).updateWeight();
        }
    }
    public void renormalizeCollateral(){
        double temp = 0;
        for ( int i = 0 ; i < LateralSynapse.size() ; i++ ){
            temp += Math.pow(LateralSynapse.get(i).weight,2.0);
        }
        temp = Math.sqrt(temp);
        for ( int i = 0 ; i < LateralSynapse.size() ; i++ ){
            LateralSynapse.get(i).weight /= temp;
        }
    }
}
