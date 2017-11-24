/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import djf.controller.AppFileController;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import mmm.data.Draggable;
import static mmm.data.Draggable.ELLIPSE;
import static mmm.data.Draggable.TEXT;
import mmm.data.DraggableText;
import mmm.data.MetroLine;
import mmm.data.Station;
import mmm.data.mmmData;
import mmm.data.mmmState;
import static mmm.data.mmmState.ADDING_STATION;
import static mmm.data.mmmState.DRAGGING_NOTHING;
import static mmm.data.mmmState.DRAGGING_SHAPE;
import static mmm.data.mmmState.REMOVING_STATION;
import static mmm.data.mmmState.SELECTING_SHAPE;

/**
 *
 * @author brendan
 */
public class CanvasController {
    
    AppTemplate app;
    boolean startedDragging = false;
    int startX;
    int startY;
    int startMouseX;
    int startMouseY;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
    }
    
        public void processCanvasMousePress(int x, int y) {
            mapEditController controller = new mapEditController(app);
            mmmData dataManager = (mmmData) app.getDataComponent();
            mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
            if (dataManager.isInState(SELECTING_SHAPE)) {
                // SELECT THE TOP SHAPE
                Shape shape = dataManager.selectTopShape(x, y);
                Scene scene = app.getGUI().getPrimaryScene();
                
                // AND START DRAGGING IT
                if(shape instanceof Line){
                    workspace.stationsBox.setValue("");
                    for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                        for(int j = 0; j < dataManager.getMetroLines().get(i).getLines().size(); j++){
                            if(dataManager.getMetroLines().get(i).getLines().get(j) == (Line)shape){
                                workspace.lineBox.setValue(dataManager.getMetroLines().get(i).getName());
                                for(int l = 0; l < dataManager.getMetroLines().get(i).getLines().size(); l++){
                                    dataManager.highlightShape(dataManager.getMetroLines().get(i).getLines().get(l));
                                }
                            }
                        }
                    }
                }

                else if (shape != null) {
                    
                    workspace.lineBox.setValue("");
                    if(shape instanceof Station){
                        if(workspace.stationsBox.getValue() == null || !workspace.stationsBox.getValue().equals(((Station)shape).getName()))
                            workspace.stationsBox.setValue(((Station)shape).getName());
                    }
                    
                    if(shape instanceof Text){
                        workspace.stationsBox.setValue("");
                    }
                    
                    scene.setCursor(Cursor.MOVE);
                    dataManager.setState(mmmState.DRAGGING_SHAPE);
                    app.getGUI().updateToolbarControls(false); 

                } else {
                    workspace.lineBox.setValue("");
                    workspace.stationsBox.setValue("");
                    scene.setCursor(Cursor.DEFAULT);
                    dataManager.setState(DRAGGING_NOTHING);
                    app.getWorkspaceComponent().reloadWorkspace(dataManager);

                }  
                workspace.reloadWorkspace(dataManager);
            }
            
            if(dataManager.isInState(ADDING_STATION)){
                
                Shape shape = dataManager.selectTopShape(x, y);
                Scene scene = app.getGUI().getPrimaryScene();
                
                if(shape != null){ 
                    if(shape instanceof Station){
                        controller.addToLine((Station)shape, dataManager.getSelectedLine());
                        app.getGUI().updateToolbarControls(false);
                    }
                    
                    else {
                        scene.setCursor(Cursor.DEFAULT);
                        dataManager.setState(SELECTING_SHAPE);
                        app.getWorkspaceComponent().reloadWorkspace(dataManager);
                    }
                }
                
                else {
                    scene.setCursor(Cursor.DEFAULT);
                    dataManager.setState(DRAGGING_NOTHING);
                    app.getWorkspaceComponent().reloadWorkspace(dataManager);
                }  
            }
            
            if(dataManager.isInState(REMOVING_STATION)){
                
                Shape shape = dataManager.selectTopShape(x, y);
                Scene scene = app.getGUI().getPrimaryScene();
                
                if(shape != null){
                    if(shape instanceof Station){
                        if(((Station)shape).getMetroLines().contains(dataManager.getSelectedLine())){
                            controller.removeFromLine((Station)shape, dataManager.getSelectedLine());
                            app.getGUI().updateToolbarControls(false); 
                        }
                    }
                    
                    else {
                        scene.setCursor(Cursor.DEFAULT);
                        dataManager.setState(SELECTING_SHAPE);
                        app.getWorkspaceComponent().reloadWorkspace(dataManager);
                    }
                }
                
                else {
                    scene.setCursor(Cursor.DEFAULT);
                    dataManager.setState(DRAGGING_NOTHING);
                    app.getWorkspaceComponent().reloadWorkspace(dataManager);
                }  
            }
        }
        
        public void processCanvasMouseDragged(int x, int y) {
            mmmData dataManager = (mmmData) app.getDataComponent();
            
            if (dataManager.isInState(DRAGGING_SHAPE)) {
                if(dataManager.getSelectedShape() instanceof Draggable){
                    Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
                    selectedDraggableShape.drag(x, y);
                    if(!startedDragging){
                        startedDragging = true;
                        if(selectedDraggableShape.getShapeType().equals("ELLIPSE")){
                            Station ellipse = (Station)selectedDraggableShape;
                            startX = (int)ellipse.getCenterX();
                            startY = (int)ellipse.getCenterY();
                        }

                        else if(selectedDraggableShape.getShapeType().equals("TEXT")){
                            DraggableText text = (DraggableText)selectedDraggableShape;
                            startX = (int)text.getX();
                            startY = (int)text.getY();
                        }

                        startMouseX = x;
                        startMouseY = y;
                    }
                    app.getGUI().updateToolbarControls(false);
                }
        }
    }
        
    public void processCanvasMouseRelease(int x, int y) {
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(mmmState.DRAGGING_SHAPE)) {
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
            
            if(dataManager.getSelectedShape() instanceof Draggable){

                if(((Draggable)dataManager.getSelectedShape()).getShapeType().equals("ELLIPSE")){
                    Station ellipse = (Station)dataManager.getSelectedShape();
                    int endX = (int)ellipse.getCenterX();
                    int endY = (int)ellipse.getCenterY();
                    if(startX != 0){
                        ellipse.setCenterX((double)endX);
                        ellipse.setCenterY((double)endY);
                        ellipse.start(x, y);
                    }
                }

                else if(((Draggable)dataManager.getSelectedShape()).getShapeType().equals("TEXT")){
                    DraggableText text = (DraggableText)dataManager.getSelectedShape();
                    int endX = (int)text.getX();
                    int endY = (int)text.getY();
                    if(startX != 0){
                        text.setX((double)endX);
                        text.setY((double)endY);
                        text.start(x, y);
                        MetroLine line = text.getMetroLine();
                    }
                }
                startedDragging = false;
            }

        } else if (dataManager.isInState(mmmState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }  
}
