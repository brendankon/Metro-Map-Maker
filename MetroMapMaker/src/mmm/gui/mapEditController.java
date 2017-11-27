package mmm.gui;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import mmm.data.mmmData;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppMessageDialogSingleton;
import static java.lang.Math.abs;
import mmm.data.Draggable;
import mmm.data.Station;
import mmm.data.MetroLine;
import mmm.data.DraggableText;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mmm.data.mmmState;
import properties_manager.PropertiesManager;

/**
 *
 * @author brendan
 */
public class mapEditController {
    
    AppTemplate app;
    mmmData dataManager;
    ArrayList<Shape> copiedShapes;
    
    public mapEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (mmmData)app.getDataComponent();
    }
    
    public void processNewLineRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        
        Stage newLineStage = new Stage();
        Pane newLinePane = new Pane();
        
        Text content = new Text("Select line name and color");
        content.setFont(Font.font("System", FontWeight.BOLD, 16));
        content.setLayoutX(60);
        content.setLayoutY(50);
        TextField t = new TextField();
        t.setLayoutX(90);
        t.setLayoutY(100);
        ColorPicker p = new ColorPicker();
        p.setValue(Color.BLACK);
        p.setLayoutX(100);
        p.setLayoutY(150);
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        ok.setLayoutX(100);
        ok.setLayoutY(250);
        cancel.setLayoutX(170);
        cancel.setLayoutY(250);
        
        newLinePane.getChildren().add(content);
        newLinePane.getChildren().add(t);
        newLinePane.getChildren().add(p);
        newLinePane.getChildren().add(ok);
        newLinePane.getChildren().add(cancel);
        
        Scene scene = new Scene(newLinePane, 350,300);
        newLineStage.setScene(scene);
        newLineStage.show();
        
        ok.setOnAction(e ->{
            newLineStage.close();
            String name = t.getText();
            Color fill = p.getValue();
            
            for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                if(dataManager.getMetroLines().get(i).getName().equals(name)){
                    
                    Alert alert = new Alert(AlertType.WARNING, "Error: line name already exists. Do you wish to enter a new name?",ButtonType.OK, ButtonType.CANCEL);
                    alert.setHeaderText("Name Already Exists");
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.OK){
                        processNewLineRequest();
                        return;
                    }
                    else if(result.get() == ButtonType.CANCEL){
                        return;
                    }
                }
            }
            
            MetroLine line = new MetroLine(name);
            Line startLine = line.getLines().get(0);
            startLine.setStrokeWidth(5);
            startLine.setStroke(fill);
            
            DraggableText label1 = new DraggableText("System", 18);
            label1.setFont(Font.font("System", FontWeight.BOLD, 18));
            label1.setLineName(name);
            DraggableText label2 = new DraggableText("System", 18);
            label2.setFont(Font.font("System", FontWeight.BOLD, 18));
            label2.setLineName(name);
            
            label1.setX(250);
            label1.setY(350);
            label2.setX(750);
            label2.setY(350);
            label1.setMetroLine(line);
            label2.setMetroLine(line);
            line.addTopLabel(label1);
            line.addBottomLabel(label2);
            
            startLine.startXProperty().bind(label1.xProperty().add(name.length()*10 + 30));
            startLine.startYProperty().bind(label1.yProperty().subtract(5));
            startLine.endXProperty().bind(label2.xProperty().subtract( 20));
            startLine.endYProperty().bind(label2.yProperty().subtract(5));
            
            dataManager.addShape(startLine);
            dataManager.addShape(label1);
            dataManager.addShape(label2);
            dataManager.addMetroLine(line);
            dataManager.selectTopShape((int)startLine.getStartX(), (int)startLine.getStartY());
            workspace.lineBox.setValue(name);
            workspace.setLineBox(name);
        });
        
        cancel.setOnAction(e ->{
            newLineStage.close();
        });
    }
    
    public void processNewStationRequest(){
        
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Station");
        dialog.setHeaderText("Enter station name");
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name ->{
            
            for(int i = 0; i < dataManager.getShapes().size(); i++){
                if(dataManager.getShapes().get(i) instanceof Station && ((Station)dataManager.getShapes().get(i)).getName().equals(name)){
                    
                    Alert alert = new Alert(AlertType.WARNING, "Error: station name already exists. Do you wish to enter a new name?",ButtonType.OK, ButtonType.CANCEL);
                    alert.setHeaderText("Name Already Exists");
                    
                    Optional<ButtonType> result2 = alert.showAndWait();
                    if(result2.get() == ButtonType.OK){
                        processNewStationRequest();
                        return;
                    }
                    else if(result2.get() == ButtonType.CANCEL){
                        return;
                    }
                }
            }
            
            Station s = new Station(name);
            s.setCenterX(550);
            s.setCenterY(300);
            Text t = new Text(name);
            t.setFont(Font.font("System", 14));
            t.xProperty().bind(s.centerXProperty().add(30));
            t.yProperty().bind(s.centerYProperty().subtract(20));
            s.setLabel(t);
            
            dataManager.addShape(s);
            dataManager.addShape(t);
            dataManager.selectTopShape((int)s.getCenterX(), (int)s.getCenterY());

            workspace.stationsBox.setValue(name);
            workspace.setStationBox(name);
           
        });
    }
    
    public void processAddStationToLineRequest(){
        
        if(dataManager.getSelectedShape() instanceof Line){
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.HAND);
            dataManager.setState(mmmState.ADDING_STATION);
        }
    }
    
    public void addToLine(Station station, MetroLine metroLine){
        
        if(!metroLine.getStations().contains(station)){

            if(metroLine.getLines().size() == 1){
                Line selectedLine = metroLine.getLines().get(0);
                station.setCenterX((selectedLine.getStartX() + selectedLine.getEndX())/2);
                station.setCenterY((selectedLine.getStartY() + selectedLine.getEndY())/2);

                metroLine.getLines().remove(selectedLine);
                dataManager.getShapes().remove(selectedLine);
                DraggableText topLabel = metroLine.getTopLabel();
                DraggableText bottomLabel = metroLine.getBottomLabel();

                Line topLine = new Line();
                Line bottomLine = new Line();
                topLine.setStroke(selectedLine.getStroke());
                bottomLine.setStroke(selectedLine.getStroke());
                topLine.setStrokeWidth(selectedLine.getStrokeWidth());
                bottomLine.setStrokeWidth(selectedLine.getStrokeWidth());

                topLine.startXProperty().bind(topLabel.xProperty().add(topLabel.getText().length()*10 + 30));
                topLine.startYProperty().bind(topLabel.yProperty().subtract(5));
                topLine.endXProperty().bind(station.centerXProperty());
                topLine.endYProperty().bind(station.centerYProperty());
                bottomLine.startXProperty().bind(station.centerXProperty());
                bottomLine.startYProperty().bind(station.centerYProperty());
                bottomLine.endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                bottomLine.endYProperty().bind(bottomLabel.yProperty().subtract(5));
                
                station.getMetroLines().add(metroLine);
                metroLine.addStation(station);
                metroLine.addLine(topLine);
                metroLine.addLine(bottomLine);
                dataManager.addShape(topLine);
                dataManager.addShape(bottomLine);
                dataManager.getShapes().remove(station);
                dataManager.addShape(station);
            }

            else if(metroLine.getLines().size() > 1){

                Station minStation = metroLine.getStations().get(0);
                double minDiff = abs(station.getCenterX() + station.getCenterY() - 
                                metroLine.getStations().get(0).getCenterX() - metroLine.getStations().get(0).getCenterY());

                for(int i = 0; i < metroLine.getStations().size(); i++){
                    if(abs(station.getCenterX() + station.getCenterY() - 
                        metroLine.getStations().get(i).getCenterX() - metroLine.getStations().get(i).getCenterY()) < minDiff){

                        minStation = metroLine.getStations().get(i);
                        minDiff = abs(station.getCenterX() + station.getCenterY() - 
                                  metroLine.getStations().get(i).getCenterX() - metroLine.getStations().get(i).getCenterY());
                    }
                }

                if(metroLine.getStations().indexOf(minStation) == 0){
                    Line removedLine = metroLine.getLines().get(0);
                    station.setCenterX((removedLine.getStartX() + removedLine.getEndX())/2);
                    station.setCenterY((removedLine.getStartY() + removedLine.getEndY())/2);

                    Line topLine = new Line();
                    Line bottomLine = new Line();
                    topLine.setStroke(removedLine.getStroke());
                    bottomLine.setStroke(removedLine.getStroke());
                    topLine.setStrokeWidth(removedLine.getStrokeWidth());
                    bottomLine.setStrokeWidth(removedLine.getStrokeWidth());

                    metroLine.getLines().set(0, topLine);
                    metroLine.getLines().add(1, bottomLine);
                    dataManager.getShapes().remove(removedLine);
                    DraggableText topLabel = metroLine.getTopLabel();

                    topLine.startXProperty().bind(topLabel.xProperty().add(topLabel.getText().length()*10 + 30));
                    topLine.startYProperty().bind(topLabel.yProperty().subtract(5));
                    topLine.endXProperty().bind(station.centerXProperty());
                    topLine.endYProperty().bind(station.centerYProperty());
                    bottomLine.startXProperty().bind(station.centerXProperty());
                    bottomLine.startYProperty().bind(station.centerYProperty());
                    bottomLine.endXProperty().bind(minStation.centerXProperty());
                    bottomLine.endYProperty().bind(minStation.centerYProperty());
                    
                    station.getMetroLines().add(metroLine);
                    metroLine.getStations().add(0, station);
                    dataManager.addShape(topLine);
                    dataManager.addShape(bottomLine);
                    dataManager.getShapes().remove(minStation);
                    dataManager.addShape(minStation);
                    dataManager.getShapes().remove(station);
                    dataManager.addShape(station);
                }

                else if(metroLine.getStations().indexOf(minStation) == metroLine.getStations().size()-1){

                    Line removedLine = metroLine.getLines().get(metroLine.getLines().size()-1);
                    station.setCenterX((removedLine.getStartX() + removedLine.getEndX())/2);
                    station.setCenterY((removedLine.getStartY() + removedLine.getEndY())/2);

                    Line topLine = new Line();
                    Line bottomLine = new Line();
                    topLine.setStroke(removedLine.getStroke());
                    bottomLine.setStroke(removedLine.getStroke());
                    topLine.setStrokeWidth(removedLine.getStrokeWidth());
                    bottomLine.setStrokeWidth(removedLine.getStrokeWidth());

                    metroLine.getLines().set(metroLine.getLines().size()-1, topLine);
                    metroLine.getLines().add(bottomLine);
                    dataManager.getShapes().remove(removedLine);
                    DraggableText bottomLabel = metroLine.getBottomLabel();

                    topLine.startXProperty().bind(minStation.centerXProperty());
                    topLine.startYProperty().bind(minStation.centerYProperty());
                    topLine.endXProperty().bind(station.centerXProperty());
                    topLine.endYProperty().bind(station.centerYProperty());
                    bottomLine.startXProperty().bind(station.centerXProperty());
                    bottomLine.startYProperty().bind(station.centerYProperty());
                    bottomLine.endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                    bottomLine.endYProperty().bind(bottomLabel.yProperty().subtract(5));
                    station.getMetroLines().add(metroLine);
                    metroLine.getStations().add(station);
                    dataManager.addShape(topLine);
                    dataManager.addShape(bottomLine);
                    dataManager.getShapes().remove(minStation);
                    dataManager.addShape(minStation);
                    dataManager.getShapes().remove(station);
                    dataManager.addShape(station);
                }

                else{

                    Line removedLine = metroLine.getLines().get(metroLine.getStations().indexOf(minStation));
                    station.setCenterX((removedLine.getStartX() + removedLine.getEndX())/2);
                    station.setCenterY((removedLine.getStartY() + removedLine.getEndY())/2);

                    Line topLine = new Line();
                    Line bottomLine = new Line();
                    topLine.setStroke(removedLine.getStroke());
                    bottomLine.setStroke(removedLine.getStroke());
                    topLine.setStrokeWidth(removedLine.getStrokeWidth());
                    bottomLine.setStrokeWidth(removedLine.getStrokeWidth());

                    metroLine.getLines().set(metroLine.getStations().indexOf(minStation), topLine);
                    metroLine.getLines().add((metroLine.getStations().indexOf(minStation))+1,bottomLine);
                    dataManager.getShapes().remove(removedLine);

                    topLine.startXProperty().bind(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-1).centerXProperty());
                    topLine.startYProperty().bind(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-1).centerYProperty());
                    topLine.endXProperty().bind(station.centerXProperty());
                    topLine.endYProperty().bind(station.centerYProperty());
                    bottomLine.startXProperty().bind(station.centerXProperty());
                    bottomLine.startYProperty().bind(station.centerYProperty());
                    bottomLine.endXProperty().bind(minStation.centerXProperty());
                    bottomLine.endYProperty().bind(minStation.centerYProperty());
                    
                    station.getMetroLines().add(metroLine);
                    metroLine.getStations().add(metroLine.getStations().indexOf(minStation), station);
                    dataManager.addShape(topLine);
                    dataManager.addShape(bottomLine);
                    dataManager.getShapes().remove(minStation);
                    dataManager.addShape(minStation);
                    dataManager.getShapes().remove(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-2));
                    dataManager.addShape(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-2));
                    dataManager.getShapes().remove(station);
                    dataManager.addShape(station);
                }
            }
        }

    }
    
    public void processRemoveStationFromLineRequest(){
        
        if(dataManager.getSelectedShape() instanceof Line){
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.HAND);
            dataManager.setState(mmmState.REMOVING_STATION);
        }
    }
    
    public void removeFromLine(Station station, MetroLine metroLine){

        Line topRemovedLine = metroLine.getLines().get(metroLine.getStations().indexOf(station));
        Line bottomRemovedLine = metroLine.getLines().get(metroLine.getStations().indexOf(station)+1);
        Line newLine = new Line();
        newLine.setStroke(topRemovedLine.getStroke());
        newLine.setStrokeWidth(topRemovedLine.getStrokeWidth());
        
        metroLine.getLines().set(metroLine.getLines().indexOf(topRemovedLine), newLine);
        metroLine.getLines().remove(bottomRemovedLine);
        dataManager.getShapes().set(dataManager.getShapes().indexOf(topRemovedLine), newLine);
        dataManager.getShapes().remove(bottomRemovedLine);
        
        newLine.startXProperty().bind(topRemovedLine.startXProperty());
        newLine.startYProperty().bind(topRemovedLine.startYProperty());
        newLine.endXProperty().bind(bottomRemovedLine.endXProperty());
        newLine.endYProperty().bind(bottomRemovedLine.endYProperty());
        
        if(metroLine.getStations().indexOf(station) != metroLine.getStations().size()-1){
            Station nextStation = metroLine.getStations().get(metroLine.getStations().indexOf(station)+1);
            dataManager.getShapes().remove(nextStation);
            dataManager.addShape(nextStation);
        }
        metroLine.getStations().remove(station);
        station.getMetroLines().remove(metroLine);
        station.setCenterX(station.getCenterX() - 30);
        station.setCenterY(station.getCenterY() - 30);
    }
    
    public void processRemoveLineRequest(){
        
        if(dataManager.getSelectedShape() instanceof Line){
            
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            Line line = (Line)dataManager.getSelectedShape();
            MetroLine metroLine = null;
            for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                if(dataManager.getMetroLines().get(i).getLines().contains(line))
                    metroLine = dataManager.getMetroLines().get(i);
            }
            
            Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to remove this line?",ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Remove Line");
            
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                
                for(int i = 0; i < metroLine.getLines().size(); i++){
                    dataManager.getShapes().remove(metroLine.getLines().get(i));
                }
                
                for(int j = 0; j < metroLine.getStations().size(); j++){
                    metroLine.getStations().get(j).getMetroLines().remove(metroLine);
                }
                
                dataManager.getMetroLines().remove(metroLine);
                dataManager.getShapes().remove(metroLine.getTopLabel());
                dataManager.getShapes().remove(metroLine.getBottomLabel());
                workspace.lineBox.getItems().remove(metroLine.getName());
            }
            
            if(result.get() == ButtonType.CANCEL){ }
        }
    }
    
    public void processRemoveStationRequest(){
        
        if(dataManager.getSelectedShape() instanceof Station && !((Station)dataManager.getSelectedShape()).isEndLabel()){
            
            Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to remove this station?",ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Remove Station");
            
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.get() == ButtonType.OK){
                Station station = (Station)dataManager.getSelectedShape();

                if(station.getMetroLines().isEmpty())
                    dataManager.getShapes().remove(station);

                else{ 
                    while(station.getMetroLines().size() > 0){
                        removeFromLine(station, station.getMetroLines().get(0));
                    }
                    dataManager.getShapes().remove(station);
                }

                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                workspace.stationsBox.getItems().remove(station.getName());
                dataManager.getShapes().remove(station.getLabel());
            }
            
            if(result.get() == ButtonType.CANCEL){ }
        }
    }
    
    public void processEditLineRequest(){
        
        if(dataManager.getSelectedShape() instanceof Line){
            Line line = (Line)dataManager.getSelectedShape();
            MetroLine metroLine = null;
            for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                if(dataManager.getMetroLines().get(i).getLines().contains(line))
                    metroLine = dataManager.getMetroLines().get(i);
            }
            
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();

            Stage editLineStage = new Stage();
            Pane editLinePane = new Pane();

            Text content = new Text("Select line name and color");
            content.setFont(Font.font("System", FontWeight.BOLD, 16));
            content.setLayoutX(60);
            content.setLayoutY(50);
            TextField t = new TextField();
            t.setLayoutX(90);
            t.setLayoutY(100);
            t.setText(metroLine.getName());
            ColorPicker p = new ColorPicker();
            p.setValue(Color.BLACK);
            p.setLayoutX(100);
            p.setLayoutY(150);
            p.setValue((Color)metroLine.getLines().get(0).getStroke());
            Button ok = new Button("OK");
            Button cancel = new Button("Cancel");
            ok.setLayoutX(100);
            ok.setLayoutY(250);
            cancel.setLayoutX(170);
            cancel.setLayoutY(250);

            editLinePane.getChildren().add(content);
            editLinePane.getChildren().add(t);
            editLinePane.getChildren().add(p);
            editLinePane.getChildren().add(ok);
            editLinePane.getChildren().add(cancel);

            Scene scene = new Scene(editLinePane, 350,300);
            editLineStage.setScene(scene);
            editLineStage.show();

            ok.setOnAction(e ->{
                
                Line editLine = (Line)dataManager.getSelectedShape();
                MetroLine editMetroLine = null;
                for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                    if(dataManager.getMetroLines().get(i).getLines().contains(line))
                        editMetroLine = dataManager.getMetroLines().get(i);
                }
                editLineStage.close();
                String name = t.getText();
                Color fill = p.getValue();
                
                for(int i = 0; i < editMetroLine.getLines().size(); i++){
                    editMetroLine.getLines().get(i).setStroke(fill);
                }
                
                workspace.lineBox.getItems().set(workspace.lineBox.getItems().indexOf(editMetroLine.getName()), name);
                editMetroLine.getTopLabel().setText(name);
                editMetroLine.getBottomLabel().setText(name);
                editMetroLine.setName(name);
                workspace.lineBox.setValue(name);
                Line startLine = editMetroLine.getLines().get(0);
                startLine.startXProperty().bind(editMetroLine.getTopLabel().xProperty().add(name.length()*10 + 30));
                dataManager.setSelectedShape(editMetroLine.getLines().get(0));
                editMetroLine.getTopLabel().setEffect(null);
            });
            
            cancel.setOnAction(e ->{
                editLineStage.close();
            });
        }
    }        
}
