package mmm.gui;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.data.Station;
import mmm.data.DraggableRectangle;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 *
 * @author brendankondracki
 */
public class RemoveShape_Transaction implements jTPS_Transaction{
    
    Shape shape;
    Shape removedShape;
    mmmData data;
    AppTemplate app;
    
    public RemoveShape_Transaction(Shape shape, mmmData data, AppTemplate app){
        
        this.shape = shape;
        this.data = data;
        this.app = app;
        
        
        if(shape instanceof Station){
            Station station = (Station)shape;
            removedShape = station;
            return;
        }

        if(shape instanceof DraggableRectangle){

            for(int i = 0; i < data.getImageShapes().size(); i++){

                if(shape == data.getImageShapes().get(i)){

                   DraggableRectangle rectangle = (DraggableRectangle)shape;
                   removedShape = rectangle;
                   return;

                }
            }

            DraggableRectangle rectangle = (DraggableRectangle)shape;
            removedShape = rectangle;
            return;
          }

        if(shape instanceof DraggableText && ((DraggableText)shape).getMetroLine() == null){
            DraggableText text = (DraggableText)shape;
            data.addTextShape(text);
            removedShape = text;
        }
    }

        @Override
        public void doTransaction(){    
            data.getShapes().remove(shape);
        }

        @Override
        public void undoTransaction(){
            data.addShape(removedShape);
            shape = removedShape;
        }
}
