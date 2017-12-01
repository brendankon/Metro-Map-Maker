package mmm.gui;

import djf.AppTemplate;
import mmm.data.Draggable;
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
public class ChangeText_Transaction implements jTPS_Transaction {
    
    Object textSize;
    Object textStyle;
    Object oldSize;
    Object oldStyle;
    boolean textBold;
    boolean textItalic;
    DraggableText text;
    DraggableText oldText;
    AppTemplate app;
    String newString;
    String oldString;
    
    public ChangeText_Transaction(Object textSize, Object oldSize, Object textStyle, Object oldStyle, boolean textBold, boolean textItalic, DraggableText text, AppTemplate app, String newString){
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.textBold = textBold;
        this.textItalic = textItalic;
        this.text = text;
        this.app = app;
        this.oldSize = oldSize;
        this.oldStyle = oldStyle;
        this.newString = newString;
    }
    
    @Override
    public void doTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        if(textSize != null){
            oldText = text;
            workspace.processFontSizeRequest(textSize, (Shape)text);
        }
        
        else if(textStyle != null){
            oldText = text;
            workspace.processFontStyleRequest(textStyle, (Shape)text);
        }
        
        else if(textBold){
            oldText = text;
            mmmData data = (mmmData)app.getDataComponent();
            data.processBoldRequest(text);
        }
        
        else if(textItalic){
            oldText = text;
            mmmData data = (mmmData)app.getDataComponent();
            data.processItalicsRequest(text);
        }
        
        else if(newString.length() > 0){
            oldString = text.getText();
            text.setText(newString);
        }
        
    }
    
    @Override
    public void undoTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        if(textSize != null){
            workspace.processFontSizeRequest(oldSize, (Shape)oldText);
            text = oldText;

        }
        
        else if(textStyle != null){
            workspace.processFontStyleRequest(oldStyle, (Shape)oldText);
            text = oldText;
        }
        
        else if(textBold){
            mmmData data = (mmmData)app.getDataComponent();
            data.processBoldRequest(oldText);
            text = oldText;
        }
        
        else if(textItalic){
            mmmData data = (mmmData)app.getDataComponent();
            data.processItalicsRequest(oldText);
            text = oldText;
        }
        
        else if(newString.length() > 0){
            text.setText(oldString);
        }
            
    }
    
}
