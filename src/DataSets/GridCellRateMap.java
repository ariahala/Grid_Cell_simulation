package DataSets;

import GridNetwork.Cells.ConjunctiveCell;
import GridNetwork.Network;
import GridNetwork.Stuff;
import RodentMap.CylindarMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mac on 12/15/2016 AD.
 */
public class GridCellRateMap {
    public ConjunctiveCell gridCell;
    public ArrayList<Tile> Map = new ArrayList<>();
    private double accuracy;
    private int n;
    public GridCellRateMap(ConjunctiveCell gridCell , double diameter , double accuracy) {
        diameter += accuracy;
        this.gridCell = gridCell;
        this.accuracy = accuracy;
        this.n = (int)diameter/(int)accuracy;
        for ( int i = 0 ; i <= n ; i++ ) {
            for ( int j = 0; j <= n; j++ ) {
                Map.add(new Tile((double) i * accuracy, (double) j * accuracy, diameter));
            }
        }
    }
    public int sizeOfMap (){
        return Map.size();
    }

    public Tile getTile ( int i ){
        return Map.get(i);
    }

    public void saveReceptiveFeild (Network brain){
        Stuff.LateralFactor = 0;
        for (int i = 0 ; i < this.Map.size() ; i++ ){
            if(!this.Map.get(i).isInRange)
                continue;
            double tempX = Map.get(i).x;
            double tempY = Map.get(i).y;
            ArrayList<Double> rategiven = new ArrayList<>();
            for ( int j = 0 ; j < brain.gridNetwork.size(); j++ ){
                rategiven.add(new Double(0));
            }
            for ( int j = 0 ; j < 12 ; j++ ){
                brain.doTimeStep(false,tempX,tempY,Math.PI*(double)2*j/12);
                for ( int k = 0 ; k < brain.gridNetwork.size() ; k++ ){
                    rategiven.set(k,rategiven.get(k)+brain.gridNetwork.get(k).rate);
                }
            }
            for ( int j = 0 ; j < brain.gridNetwork.size(); j++ ){
                brain.gridNetwork.get(j).data.getMap().get(i).update(rategiven.get(j)/(double)12);
            }
        }
    }
    public ArrayList<Tile> getMap (){
        return this.Map;
    }
    public void update (){
        double xPos = CylindarMap.X  / this.accuracy;
        double yPos = CylindarMap.Y / this.accuracy;
        int i = (int) xPos;
        int j = (int) yPos;
        Map.get((i*n) + j).update(gridCell.rate);
    }
    public void writeToFile (){
        String content = new String("");
        for ( int i = 0 ; i < Map.size() ; i++ ) {
            Tile temp = Map.get(i);
            content += temp.x + " " + temp.y + " " + temp.rate + "\n";
        }

        try {
            FileWriter fw = new FileWriter("/Users/mac/Desktop/Work/Neuroscience/Grid cell Project/Data_No."+gridCell.index+".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

                try {
                    bw.write(content);
                    bw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}

