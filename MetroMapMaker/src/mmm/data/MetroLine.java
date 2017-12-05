package mmm.data;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author brendan
 */
public class MetroLine {
    
    String name;
    ArrayList<Line> lines;
    ArrayList<Station> stations;
    DraggableText topLabel;
    DraggableText bottomLabel;
    ArrayList<MetroLine> transfers;
    boolean isCircular;
    
    public MetroLine(String name){
        
        this.name = name;
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        Line startLine = new Line();
        lines.add(startLine);
        isCircular = false;
        transfers = new ArrayList<>();
        
    }
    
    public void addLine(Line line){
        lines.add(line);
    }
    
    public ArrayList<Line> getLines(){
        return lines;
    }
    
    public void addStation(Station s){
        stations.add(s);
    }
    
    public ArrayList<Station> getStations(){
        return stations;
    }
    
    public String getName(){
        return name;
    }
    
    public void addTopLabel(DraggableText text){
        topLabel = text;
    }
    
    public void addBottomLabel(DraggableText text){
        bottomLabel = text;
    }
    
    public DraggableText getTopLabel(){
        return topLabel;
    }
    
    public DraggableText getBottomLabel(){
        return bottomLabel;
    }
    
    public void setName(String s){
        name = s;
    }
    
    public void setIsCircular(boolean b){
        isCircular = b;
    }
    
    public boolean isCircular(){
        return isCircular;
    }
    
    public void addTransfer(MetroLine line){
        if(!transfers.contains(line))
            this.transfers.add(line);
    }
    
    public void removeTransfer(MetroLine line){
        int lineCount = 0;
        for(int i = 0; i < stations.size(); i++){
            if(stations.get(i).getMetroLines().contains(line))
                lineCount++;
        }
        
        if(lineCount == 0)
            transfers.remove(line);
    }
    
    public ArrayList<MetroLine> getTransfers(){
        return transfers;
    }
    
    public Station findIntersectingStation(MetroLine line){
        
        for(int i = 0; i < stations.size(); i++){
            Station station1 = stations.get(i);
            
            if(line.getStations().contains(station1))
                return station1;
        }
        
        return null;
    }

       
}
