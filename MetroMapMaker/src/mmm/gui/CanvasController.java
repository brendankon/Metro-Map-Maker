/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import djf.controller.AppFileController;
import static java.lang.Math.abs;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import mmm.data.Draggable;
import static mmm.data.Draggable.ELLIPSE;
import static mmm.data.Draggable.TEXT;
import mmm.data.DraggableRectangle;
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
    int centerPaneX = 40;
    int centerPaneY = 40;

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
                
                if(shape instanceof Line){
                    workspace.stationsBox.setValue("");
                    for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                        for(int j = 0; j < dataManager.getMetroLines().get(i).getLines().size(); j++){
                            if(dataManager.getMetroLines().get(i).getLines().get(j) == (Line)shape){
                                workspace.lineBox.setValue(dataManager.getMetroLines().get(i).getName());
                                for(int l = 0; l < dataManager.getMetroLines().get(i).getLines().size(); l++){
                                    dataManager.highlightShape(dataManager.getMetroLines().get(i).getLines().get(l));
                                }
                                workspace.updateEditToolbar(true, false, true, false, false);
                            }
                        }
                    }
                }
                
                else if(shape instanceof DraggableText &&((DraggableText)shape).isStationText()){
                     workspace.updateEditToolbar(false, false, false, false, false);
                }

                else if (shape != null) {
                    
                    scene.setCursor(Cursor.MOVE);
                    dataManager.setState(mmmState.DRAGGING_SHAPE);
                    
                    workspace.lineBox.setValue("");
                    if(shape instanceof Station){
                        Station station = (Station)shape;
                        if(workspace.stationsBox.getValue() == null || !workspace.stationsBox.getValue().equals(((Station)shape).getName()))
                            workspace.stationsBox.setValue(((Station)shape).getName());
                        
                        if(station.isEndLabel())
                            workspace.updateEditToolbar(false, false , false, false, true);
                        else
                            workspace.updateEditToolbar(false, true , false, false, false);
                    }
                    
                    if(shape instanceof Text){
                        workspace.stationsBox.setValue("");
                        if(!(shape instanceof DraggableText))
                            workspace.updateEditToolbar(false, false, false, false, false);

                    }
                    
                    if(shape instanceof DraggableRectangle){
                        workspace.updateEditToolbar(false, false, false, true, false);
                        workspace.stationsBox.setValue("");
                        workspace.lineBox.setValue("");
                    }
                    
                    if(shape instanceof DraggableText){
                        
                        if(((DraggableText)shape).getLineName() == null)
                            workspace.updateEditToolbar(false, false, true, false, false);
                       
                        
                        for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                            if(dataManager.getMetroLines().get(i).getTopLabel() == (DraggableText) shape){
                                Station lineEnd = new Station("");
                                lineEnd.setIsEndLabel(true);
                                lineEnd.setFill(Color.LIGHTCYAN);
                                lineEnd.setCenterX(x);
                                lineEnd.setCenterY(y);
                                dataManager.getMetroLines().get(i).getLines().get(0).startXProperty().unbind();
                                dataManager.getMetroLines().get(i).getLines().get(0).startYProperty().unbind();
                                dataManager.getMetroLines().get(i).getLines().get(0).startXProperty().bind(lineEnd.centerXProperty());
                                dataManager.getMetroLines().get(i).getLines().get(0).startYProperty().bind(lineEnd.centerYProperty());
                                lineEnd.addMetroLine(dataManager.getMetroLines().get(i));
                                dataManager.removeShape(dataManager.getMetroLines().get(i).getTopLabel());
                                dataManager.addShape(lineEnd);
                                dataManager.selectTopShape(x,y);
                                scene.setCursor(Cursor.DEFAULT);
                                dataManager.setState(mmmState.SELECTING_SHAPE);
                                dataManager.getMetroLines().get(i).getStations().add(0, lineEnd);
                                workspace.updateEditToolbar(false, false, false, false, false);
                                
                                if(dataManager.getMetroLines().get(i).isCircular()){
                                    dataManager.getMetroLines().get(i).getLines().get(dataManager.getMetroLines().get(i).getLines().size()-1).endXProperty().bind(lineEnd.centerXProperty());
                                    dataManager.getMetroLines().get(i).getLines().get(dataManager.getMetroLines().get(i).getLines().size()-1).endYProperty().bind(lineEnd.centerYProperty());
                                }
                                break;
                            }
                            
                            if(dataManager.getMetroLines().get(i).getBottomLabel() == (DraggableText)shape){
                                Station lineEnd = new Station("");
                                lineEnd.setIsEndLabel(true);
                                lineEnd.setFill(Color.LIGHTCYAN);
                                lineEnd.setCenterX(x);
                                lineEnd.setCenterY(y);
                                MetroLine line = dataManager.getMetroLines().get(i);
                                line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                                line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                                line.getLines().get(line.getLines().size()-1).endXProperty().bind(lineEnd.centerXProperty());
                                line.getLines().get(line.getLines().size()-1).endYProperty().bind(lineEnd.centerYProperty());
                                lineEnd.addMetroLine(line);
                                dataManager.removeShape(dataManager.getMetroLines().get(i).getBottomLabel());
                                dataManager.addShape(lineEnd);
                                dataManager.selectTopShape(x,y);
                                scene.setCursor(Cursor.DEFAULT);
                                dataManager.setState(mmmState.SELECTING_SHAPE);
                                dataManager.getMetroLines().get(i).getStations().add(lineEnd);
                                workspace.updateEditToolbar(false, false, false, false, false);
                                break;
                            }
                        }
                    }
                    app.getGUI().updateToolbarControls(false);
                    workspace.saveAs.setDisable(false);
                 
                } else {
                    workspace.lineBox.setValue("");
                    workspace.stationsBox.setValue("");
                    scene.setCursor(Cursor.DEFAULT);
                    dataManager.setState(DRAGGING_NOTHING);
                    app.getWorkspaceComponent().reloadWorkspace(dataManager);
                    workspace.updateEditToolbar(false, false, false, false, false);

                }  
                workspace.reloadWorkspace(dataManager);
            }
            
            if(dataManager.isInState(ADDING_STATION)){
                
                Shape shape = dataManager.selectTopShape(x, y);
                Scene scene = app.getGUI().getPrimaryScene();
                
                if(shape != null){ 
                    if(shape instanceof Station){
                        //controller.addToLine((Station)shape, dataManager.getSelectedLine());
                        jTPS j = dataManager.getJTPS();
                        jTPS_Transaction transaction = new AddToLine_Transaction(dataManager.getSelectedLine(), (Station)shape, app);
                        j.addTransaction(transaction);
                        workspace.undoButton.setDisable(false);
                        app.getGUI().updateToolbarControls(false);
                        workspace.saveAs.setDisable(false);
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
                            //controller.removeFromLine((Station)shape, dataManager.getSelectedLine());
                            jTPS j = dataManager.getJTPS();
                            jTPS_Transaction transaction = new RemoveFromLine_Transaction(dataManager.getSelectedLine(), (Station)shape, app);
                            j.addTransaction(transaction);
                            workspace.undoButton.setDisable(false);
                            app.getGUI().updateToolbarControls(false); 
                            workspace.saveAs.setDisable(false);
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
            mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
                if (dataManager.isInState(DRAGGING_SHAPE)) {
                    if(dataManager.getSelectedShape() instanceof Draggable){
                        if(!(centerPaneX < 60) && !(centerPaneY < 50)){
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
                            
                            else{
                                DraggableRectangle rect = (DraggableRectangle)selectedDraggableShape;
                                startX = (int)rect.getX();
                                startY = (int)rect.getY();
                            }

                            startMouseX = x;
                            startMouseY = y;
                        }
                        app.getGUI().updateToolbarControls(false);
                        workspace.saveAs.setDisable(false);
                    }
                }
            }
    }
        
    public void centerPaneMouseDrag(int x, int y){
        centerPaneX = x;
        centerPaneY = y;
    }
        
    public void processCanvasMouseRelease(int x, int y) {
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(mmmState.DRAGGING_SHAPE)) {
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
            workspace.saveAs.setDisable(false);
            
            if(dataManager.getSelectedShape() instanceof Draggable){

                if(((Draggable)dataManager.getSelectedShape()) instanceof Station){
                    Station ellipse = (Station)dataManager.getSelectedShape();
                    int endX = (int)ellipse.getCenterX();
                    int endY = (int)ellipse.getCenterY();
                    if(startX != 0){
                        jTPS j = dataManager.getJTPS();
                        jTPS_Transaction transaction = new DragShape_Transaction(null, ellipse, null,startX, startY, endX, endY, app, startMouseX, startMouseY, x, y);
                        j.addTransaction(transaction);
                        startX = 0;
                        workspace.undoButton.setDisable(false);
                    }
                }

                else if(((Draggable)dataManager.getSelectedShape()).getShapeType().equals("TEXT")){
                    DraggableText text = (DraggableText)dataManager.getSelectedShape();
                    int endX = (int)text.getX();
                    int endY = (int)text.getY();
                    if(startX != 0){
                        jTPS j = dataManager.getJTPS();
                        jTPS_Transaction transaction = new DragShape_Transaction(text, null, null,startX, startY, endX, endY, app, startMouseX, startMouseY, x, y);
                        j.addTransaction(transaction);
                        startX = 0;
                        workspace.undoButton.setDisable(false);
                    }
                }
                else{
                    DraggableRectangle rect = (DraggableRectangle)dataManager.getSelectedShape();
                    int endX = (int)rect.getX();
                    int endY = (int)rect.getY();
                    if(startX != 0){
                        jTPS j = dataManager.getJTPS();
                        jTPS_Transaction transaction = new DragShape_Transaction(null, null, rect,startX, startY, endX, endY, app, startMouseX, startMouseY, x, y);
                        j.addTransaction(transaction);
                        startX = 0;
                        workspace.undoButton.setDisable(false);
                    }
                }
                startedDragging = false;
                
                if(dataManager.getJTPS().getTransList().size() == 1){
                    workspace.redoCounter = 0;
                    workspace.redoButton.setDisable(true);
                }
            }

        } else if (dataManager.isInState(mmmState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }  
}
