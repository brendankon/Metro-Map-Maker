package mmm.gui;

import jtps.jTPS_Transaction;
import djf.AppTemplate;
import mmm.data.Station;
import mmm.data.DraggableRectangle;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import mmm.data.MetroLine;

/**
 *
 * @author brendankondracki
 */
public class DragShape_Transaction implements jTPS_Transaction {
    
    Station station;
    DraggableText text;
    DraggableRectangle rect;
    int startX;
    int startY;
    int endX;
    int endY;
    int startMouseX;
    int startMouseY;
    int endMouseX;
    int endMouseY;
    MetroLine metroLine;
    DraggableText lineLabel;
    AppTemplate app;
    
    public DragShape_Transaction(DraggableText text, Station station, DraggableRectangle rect, int startX, 
            int startY, int endX, int endY, AppTemplate app, int startMouseX, int startMouseY, int endMouseX, int endMouseY){
        
        mmmData data = (mmmData)app.getDataComponent();
        this.station = station;
        this.text = text;
        this.rect = rect;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.app = app;
        this.startMouseX = startMouseX;
        this.startMouseY = startMouseY;
        this.endMouseX = endMouseX;
        this.endMouseY = endMouseY;
        
        if(station != null){
            for(int i = 0; i < data.getMetroLines().size(); i++){
                if(data.getMetroLines().get(i).isCircular()){
                    if(!data.getShapes().contains(data.getMetroLines().get(i).getTopLabel())){
                        metroLine = data.getMetroLines().get(i);
                        lineLabel = metroLine.getTopLabel();
                    }
                }
                
                else{
                    if(!data.getShapes().contains(data.getMetroLines().get(i).getTopLabel())){
                        metroLine = data.getMetroLines().get(i);
                        lineLabel = data.getMetroLines().get(i).getTopLabel();
                    }
                    else if(!data.getShapes().contains(data.getMetroLines().get(i).getBottomLabel())){
                        metroLine = data.getMetroLines().get(i);
                        lineLabel = data.getMetroLines().get(i).getBottomLabel();
                    }
                }
            }
        }
        
    }
    
    @Override
    public void doTransaction(){
        mmmData data = (mmmData)app.getDataComponent();
        
        if(text != null){
            text.setX((double)endX);
            text.setY((double)endY);
            text.start(endMouseX, endMouseY);
        }
        
        else if(station != null){
            if(data.getSelectedShape() == station || lineLabel == null){
                station.setCenterX((double)endX);
                station.setCenterY((double)endY);
                station.start(endMouseX, endMouseY);
            }
            else{
                lineLabel.setX(endX);
                lineLabel.setY(endY);
            }
        }
        
        else if(rect != null){
            rect.setX((double)endX);
            rect.setY((double)endY);
            rect.start(endMouseX, endMouseY);
        }
    }
    
    @Override
    public void undoTransaction(){
        mmmData data = (mmmData)app.getDataComponent();
        
        if(text != null){
            text.setX((double)startX);
            text.setY((double)startY);
            text.start(startMouseX, startMouseY);
        }
        
        if(station != null){
            if(metroLine!= null){
                lineLabel.setX(startX);
                lineLabel.setY(startY);
            }
            station.setCenterX((double)startX);
            station.setCenterY((double)startY);
            station.start(startMouseX, startMouseY);
        }
        
        else if(rect != null){
            rect.setX((double)startX);
            rect.setY((double)startY);
            rect.start(startMouseX, startMouseY);
        }
        
    }
    
}
