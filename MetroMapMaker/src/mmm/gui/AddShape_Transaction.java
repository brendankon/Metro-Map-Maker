package mmm.gui;

import djf.AppTemplate;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author brendankondracki
 */
public class AddShape_Transaction implements jTPS_Transaction {
    
    Shape shape;
    mmmData data;
    AppTemplate app;
    
    public AddShape_Transaction(Shape shape, mmmData data, AppTemplate app){
        
        this.shape = shape;
        this.data = data;
        this.app = app;
    }
    
    @Override
    public void doTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        data.addShape(shape);
        workspace.undoButton.setDisable(false);       
    }
    
    @Override
    public void undoTransaction(){
        data.removeShape(shape);
    }
    
}