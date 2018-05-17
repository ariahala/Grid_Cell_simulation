import GridNetwork.Network;
import RodentMap.CylindarMap;


/**
 * Created by mac on 12/15/2016 AD.
 */
public class Main {
    public static void main (String[] args){
        CylindarMap map = new CylindarMap(125,250);
        while (Network.time <= 8000000){
            if ( Network.time % 8000 == 0 ) {
                System.out.println((float)Network.time/80000 + " %");
            }
            map.updatePosition();
        }
        map.brain.writeData();
    }
}
