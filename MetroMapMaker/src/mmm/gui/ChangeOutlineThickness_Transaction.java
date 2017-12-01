package mmm.gui;

import djf.AppTemplate;
import javafx.scene.shape.Line;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import mmm.data.MetroLine;
import mmm.data.Station;

/**
 *
 * @author brendankondracki
 */
public class ChangeOutlineThickness_Transaction implements jTPS_Transaction {
    
    int startOutline;
    int endOutline;
    Shape shape;
    AppTemplate app;
    
    public ChangeOutlineThickness_Transaction(int startOutline, int endOutline, Shape shape, AppTemplate app){
        
        this.startOutline = startOutline;
        this.endOutline = endOutline;
        this.shape = shape;
        this.app = app;
    }
    
    @Override
    public void doTransaction(){
        mmmData data = (mmmData)app.getDataComponent();
        if(shape instanceof Line){
            MetroLine line = data.getSelectedLine();
            for(int i = 0; i < line.getLines().size(); i++){
                line.getLines().get(i).setStrokeWidth(endOutline);
            }
        }
        
        else if(shape instanceof Station){
            Station station = (Station)shape;
            station.setRadiusX(endOutline);
            station.setRadiusY(endOutline);
        }
    }
    
    @Override
    public void undoTransaction(){
        mmmData data = (mmmData)app.getDataComponent();
        if(shape instanceof Line){
            MetroLine line = data.getSelectedLine();
            for(int i = 0; i < line.getLines().size(); i++){
                line.getLines().get(i).setStrokeWidth(startOutline);
            }
        }
        
        else if(shape instanceof Station){
            Station station = (Station)shape;
            station.setRadiusX(startOutline);
            station.setRadiusY(startOutline);
        }
    }
}