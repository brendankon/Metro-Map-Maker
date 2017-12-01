package mmm.gui;

import djf.AppTemplate;
import mmm.data.mmmData;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author brendankondracki
 */
public class AddColor_Transaction implements jTPS_Transaction{
    
    boolean isBackground;
    boolean isFill;
    Color color;
    Color oldColor;
    mmmData data;
    Shape shape;
    AppTemplate app;
    
    public AddColor_Transaction(Color color, boolean isBackground, boolean isFill, mmmData data, Shape shape, AppTemplate initApp){
        
        this.isBackground = isBackground;
        this.isFill = isFill;
        this.color = color;
        this.data = data;
        this.shape = shape;
        this.app = initApp;
    }
    
    @Override
    public void doTransaction(){
        
        if(isBackground){
            oldColor = data.getBackgroundColor();
            data.setBackgroundColor(color);
        }
        
        else if(isFill){
            oldColor = (Color)shape.getFill();
            shape.setFill(color);
        }
    }
    
    @Override
    public void undoTransaction(){
        
        if(isBackground){
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            data.setBackgroundColor(oldColor);
            workspace.getBackgroundColorPicker().setValue(oldColor);
        }
        
        else if(isFill){
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            shape.setFill(oldColor);
            workspace.getStationColorPicker().setValue(oldColor);
        }
    }
}