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
    
    public MetroLine(String name){
        
        this.name = name;
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        Line startLine = new Line();
        lines.add(startLine);
        
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

       
}
