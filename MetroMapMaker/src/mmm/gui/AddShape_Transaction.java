package mmm.gui;

import djf.AppTemplate;
import javafx.scene.shape.Line;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.Station;

/**
 *
 * @author brendankondracki
 */
public class AddShape_Transaction implements jTPS_Transaction {
    
    Shape shape;
    mmmData data;
    AppTemplate app;
    MetroLine metroLine;
    
    public AddShape_Transaction(Shape shape, MetroLine metroLine, mmmData data, AppTemplate app){
        
        this.shape = shape;
        this.data = data;
        this.app = app;
        this.metroLine = metroLine;
    }
    
    @Override
    public void doTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        if(metroLine != null){
            data.addShape(metroLine.getLines().get(0));
            data.addShape(metroLine.getTopLabel());
            data.addShape(metroLine.getBottomLabel());
            data.addMetroLine(metroLine);
            workspace.lineBox.setValue(metroLine.getName());
            workspace.setLineBox(metroLine.getName());
        }
        
        else if(shape != null){
            if(shape instanceof Station){
                data.addShape(shape);
                data.addShape(((Station)shape).getLabel());
                String name = ((Station)shape).getName();
                workspace.stationsBox.setValue(name);
                workspace.setStationBox(name);
                workspace.routeBox1.getItems().add(name);
                workspace.routeBox2.getItems().add(name);
            }

            else
                data.addShape(shape);
            workspace.undoButton.setDisable(false);
        }
    }
    
    @Override
    public void undoTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        if(metroLine != null){
            for(int i = 0; i < metroLine.getLines().size(); i++){
                data.getShapes().remove(metroLine.getLines().get(i));
            }
            data.getMetroLines().remove(metroLine);
            data.removeShape(metroLine.getTopLabel());
            data.removeShape(metroLine.getBottomLabel());
            workspace.lineBox.getItems().remove(metroLine.getName());
        }
        
        else if(shape != null){
            if(shape instanceof Station){
                Station station = (Station)shape;
                workspace.stationsBox.getItems().remove(station.getName());
                workspace.routeBox1.getItems().remove(station.getName());
                workspace.routeBox2.getItems().remove(station.getName());
                data.removeShape(shape);
                data.removeShape(station.getLabel());
            }
            else
                data.removeShape(shape);
        }
    }
    
}