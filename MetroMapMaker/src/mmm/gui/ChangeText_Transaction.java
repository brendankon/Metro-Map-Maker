package mmm.gui;

import djf.AppTemplate;
import javafx.scene.shape.Line;
import mmm.data.Draggable;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.Station;



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
    DraggableText lineText;
    DraggableText oldLineText;
    AppTemplate app;
    String newString;
    String oldString;
    
    public ChangeText_Transaction(Object textSize, Object oldSize, Object textStyle, Object oldStyle, boolean textBold, boolean textItalic, Shape text, AppTemplate app, String newString){
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.textBold = textBold;
        this.textItalic = textItalic;
        this.app = app;
        this.oldSize = oldSize;
        this.oldStyle = oldStyle;
        this.newString = newString;
        this.lineText = new DraggableText("System", 20);
        if(text instanceof DraggableText){
            this.text = (DraggableText)text;
        }
        else if(text instanceof Station){
            
            if(!((Station)text).isEndLabel()){
                this.text = ((Station)text).getLabel();
            }
        }
        
        else if(text instanceof Line){
            mmmData dataManager = (mmmData)app.getDataComponent();
            MetroLine line = new MetroLine("");
            for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                if(dataManager.getMetroLines().get(i).getLines().contains((Line)text)){
                    line = dataManager.getMetroLines().get(i);
                }
            }
            this.text = line.getTopLabel();
            this.lineText = line.getBottomLabel();
            
        }
    }
    
    @Override
    public void doTransaction(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        if(textSize != null){
            oldText = text;
            oldLineText = lineText;
            workspace.processFontSizeRequest(textSize, (Shape)text);
            workspace.processFontSizeRequest(textSize, (Shape)lineText);
        }
        
        else if(textStyle != null){
            oldText = text;
            oldLineText = lineText;
            workspace.processFontStyleRequest(textStyle, text);
            workspace.processFontStyleRequest(textStyle, lineText);
        }
        
        else if(textBold){
            oldText = text;
            oldLineText = lineText;
            mmmData data = (mmmData)app.getDataComponent();
            data.processBoldRequest(text);
            data.processBoldRequest(lineText);
        }
        
        else if(textItalic){
            oldText = text;
            oldLineText = lineText;
            mmmData data = (mmmData)app.getDataComponent();
            data.processItalicsRequest(text);
            data.processItalicsRequest(lineText);
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
            workspace.processFontSizeRequest(oldSize, (Shape)oldLineText);
            text = oldText;

        }
        
        else if(textStyle != null){
            workspace.processFontStyleRequest(oldStyle, oldText);
            workspace.processFontStyleRequest(oldStyle, oldLineText);
            text = oldText;
        }
        
        else if(textBold){
            mmmData data = (mmmData)app.getDataComponent();
            data.processBoldRequest(oldText);
            data.processBoldRequest(oldLineText);
            text = oldText;
        }
        
        else if(textItalic){
            mmmData data = (mmmData)app.getDataComponent();
            data.processItalicsRequest(oldText);
            data.processItalicsRequest(oldLineText);
            text = oldText;
        }
        
        else if(newString.length() > 0){
            text.setText(oldString);
        }
            
    }
    
}
