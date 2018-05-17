package GridNetwork;


import GridNetwork.Cells.ConjunctiveCell;
import GridNetwork.Cells.PlaceCell;
import GridNetwork.Connections.CCSynapse;
import GridNetwork.Connections.PCSynapse;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class Network {
    static public BufferedWriter bw;
    public static int time = 0;
    public ArrayList<PlaceCell> PlaceNetwork = new ArrayList<>();
    public ArrayList<ConjunctiveCell> gridNetwork = new ArrayList<>();
    public double iteratedSparsity = 0.01;
    public double iteratedMean = 0.01;
    public double rodentX = 0;
    public double rodentY = 0;
    public double rodentDirection = 0;

    public void makeNetworkCells ( int numberOfGridCells , double diameterOfMap ){
        Random rnd = new Random();
        for ( int i = 0 ; (double)i < 2*diameterOfMap/(Math.sqrt(3)*Stuff.unitSize) ; i++ ){
            for ( int j = 0 ; (double)j < ((Math.sqrt(3)/3)+1)*diameterOfMap/Stuff.unitSize ; j++ ){
                double tempX = Math.sqrt(3)/2;
                tempX *= (double)i*Stuff.unitSize;
                double tempY = (double)j - ((double)i/2);
                tempY *= Stuff.unitSize;
                double distance = Math.pow(tempX-(diameterOfMap/2),2.0) + Math.pow(tempY-(diameterOfMap/2),2.0);
                distance = Math.sqrt(distance);
                if ( distance < diameterOfMap/2){
                    PlaceNetwork.add(new PlaceCell(tempX,tempY));
                }
            }
        }
        ArrayList<Integer> rands = new ArrayList<>();
        for ( int i = 0 ; i < numberOfGridCells ; i++ ){
            int g = rnd.nextInt(PlaceNetwork.size());
            boolean flag = true;
            while ( flag ){
                g = rnd.nextInt(PlaceNetwork.size());
                flag = false;
                for ( int u = 0 ; u < rands.size() ; u++ ){
                    if ( rands.get(u) == g){
                        flag = true;
                        break;
                    }
                }
            }
            rands.add(g);
            System.out.println(g);
            gridNetwork.add(new ConjunctiveCell(rnd.nextDouble()*2*Math.PI,PlaceNetwork.get(g)));
            gridNetwork.get(i).index = i;
        }
        for( int i = 0 ; i < gridNetwork.size() ; i++ ){
            for ( int j = 0 ; j < gridNetwork.size() ; j++ ){
                if ( i!=j ){
                    CCSynapse tempCCSynapse = new CCSynapse(gridNetwork.get(j),gridNetwork.get(i));
                    if ( tempCCSynapse.weight > 0 ) {
                        gridNetwork.get(i).LateralSynapse.add(tempCCSynapse);
                    }
                }
            }
            for ( int j = 0 ; j < PlaceNetwork.size() ; j++ ){
                gridNetwork.get(i).PlaceSynapses.add(new PCSynapse(gridNetwork.get(i),PlaceNetwork.get(j)));
            }
        }
        for (int i = 0; i < gridNetwork.size(); i++) {
            gridNetwork.get(i).renormalizeWeights();
            gridNetwork.get(i).renormalizeCollateral();
        }
        for ( int i = 0 ; i < PlaceNetwork.size() ; i++ ){
            PlaceNetwork.get(i).calculateRate(rodentX,rodentY);
        }
    }

    public Network(double rodentX, double rodentY, double rodentDirection) {
        this.rodentX = rodentX;
        this.rodentY = rodentY;
        this.rodentDirection = rodentDirection;
    }

    public void updateRodentPosition ( double rodentX , double rodentY , double direction ){
        this.rodentDirection = direction;
        this.rodentX = rodentX;
        this.rodentY = rodentY;
    }
    public double calculateMean (){
        double ans = 0;
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            ans += gridNetwork.get(i).calculateRate(iteratedMean,iteratedSparsity);
        }
        ans /= gridNetwork.size();
        return ans;
    }
    public double calculateSparsity (){
        double ans = 0;
        double temp = 0;
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            double j = gridNetwork.get(i).calculateRate(iteratedMean,iteratedSparsity);
            ans += j;
            temp += Math.pow(j,2.0);
        }
        ans = Math.pow(ans,2.0);
        temp *= gridNetwork.size();
        if ( temp > 0 ){
            ans /= temp;
            return ans;
        }
        return 0;
    }
    public void iterateMeanSparsity (){
        double a = calculateMean();
        double s = calculateSparsity();
        int u = 0;
        while ( (Math.abs((Stuff.a0-a)/Stuff.a0) > 0.1 || Math.abs((Stuff.s0-s)/Stuff.s0) > 0.1 )&& u < 20){
            a = calculateMean();
            s = calculateSparsity();
            iteratedMean += Stuff.b3*(a-Stuff.a0);
            iteratedSparsity += Stuff.b4*iteratedSparsity*(s-Stuff.s0);
            u++;
        }
    }
    public void updateData(){
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).data.update();
        }
    }


    public void writeData(){
            gridNetwork.get(0).data.saveReceptiveFeild(this);
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).data.writeToFile();
        }
    }



    public void doTimeStep (boolean toLearn , double rodentX , double rodentY , double direction){
        time++;
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).calculateInput(rodentDirection);
        }
        this.updateRodentPosition(rodentX,rodentY,direction);
        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).calculateAlphaBeta();
        }

        iterateMeanSparsity();

        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).calculateRate(iteratedMean,iteratedSparsity);
            gridNetwork.get(i).setRate();
        }

        for ( int i = 0 ; i < PlaceNetwork.size() ; i++ ){
            PlaceNetwork.get(i).calculateRate(rodentX,rodentY);
        }

        if (toLearn) {

            for (int i = 0; i < gridNetwork.size(); i++) {
                gridNetwork.get(i).updateAllSynapses();
            }
        }

        for ( int i = 0 ; i < gridNetwork.size() ; i++ ){
            gridNetwork.get(i).calculateAverageRate();
        }

        for (int i = 0; i < PlaceNetwork.size(); i++) {
            PlaceNetwork.get(i).calculateAverageRate();
        }
        if(toLearn) {

            for (int i = 0; i < gridNetwork.size(); i++) {
                gridNetwork.get(i).renormalizeWeights();
            }
        }
    }
}
