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
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import mmm.data.DraggableRectangle;
import mmm.data.GridLine;
import mmm.data.Path;
import mmm.data.mmmState;
import properties_manager.PropertiesManager;

/**
 *
 * @author brendan
 */
public class mapEditController {
    
    AppTemplate app;
    mmmData dataManager;
    ArrayList<GridLine> gridLinesV;
    ArrayList<GridLine> gridLinesH;
    
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
            t.xProperty().bind(s.centerXProperty().add(20 + s.getRadiusX()));
            t.yProperty().bind(s.centerYProperty().subtract(10 + s.getRadiusY()));
            s.setLabel(t);
            
            dataManager.addShape(s);
            dataManager.addShape(t);
            dataManager.selectTopShape((int)s.getCenterX(), (int)s.getCenterY());

            workspace.stationsBox.setValue(name);
            workspace.setStationBox(name);
            workspace.routeBox1.getItems().add(name);
            workspace.routeBox2.getItems().add(name);
           
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
                dataManager.removeShape(selectedLine);
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
                metroLine.addStation(0, station);
                metroLine.addLine(topLine);
                metroLine.addLine(bottomLine);
                dataManager.addShape(topLine);
                dataManager.addShape(bottomLine);
                dataManager.removeShape(station);
                dataManager.addShape(station);
            }

            else if(metroLine.getLines().size() > 1){

                Station minStation = metroLine.getStations().get(0);
                double minDiff = findDistance(station.getCenterX(), station.getCenterY(), 
                                metroLine.getStations().get(0).getCenterX(), metroLine.getStations().get(0).getCenterY());

                for(int i = 0; i < metroLine.getStations().size(); i++){
                    if(findDistance(station.getCenterX(), station.getCenterY(), 
                        metroLine.getStations().get(i).getCenterX(), metroLine.getStations().get(i).getCenterY()) < minDiff){

                        minStation = metroLine.getStations().get(i);
                        minDiff = findDistance(station.getCenterX(), station.getCenterY(), 
                                  metroLine.getStations().get(i).getCenterX(), metroLine.getStations().get(i).getCenterY());
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
                    dataManager.removeShape(removedLine);
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
                    dataManager.removeShape(minStation);
                    dataManager.addShape(minStation);
                    dataManager.removeShape(station);
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
                    dataManager.removeShape(removedLine);
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
                    dataManager.removeShape(minStation);
                    dataManager.addShape(minStation);
                    dataManager.removeShape(station);
                    dataManager.addShape(station);
                    
                    if(metroLine.isCircular()){
                        Line endLine = metroLine.getLines().get(metroLine.getLines().size()-1);
                        endLine.endXProperty().bind(metroLine.getTopLabel().xProperty().add(metroLine.getName().length()*10 + 30));
                        endLine.endYProperty().bind(metroLine.getTopLabel().yProperty().subtract(5));
                        dataManager.getShapes().remove(metroLine.getBottomLabel());
                    }
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
                    dataManager.removeShape(removedLine);

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
                    dataManager.removeShape(minStation);
                    dataManager.addShape(minStation);
                    dataManager.removeShape(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-2));
                    dataManager.addShape(metroLine.getStations().get((metroLine.getStations().indexOf(minStation))-2));
                    dataManager.removeShape(station);
                    dataManager.addShape(station);
                }
            }
            
            for(int i = 0; i < station.getMetroLines().size(); i++){
                if(station.getMetroLines().get(i) != metroLine){
                    station.getMetroLines().get(i).addTransfer(metroLine);
                    metroLine.addTransfer(station.getMetroLines().get(i));
                }
            }
        }

    }
    
    public double findDistance(double x1, double y1, double x2, double y2){
        double x = Math.pow(x2-x1, 2);
        double y = Math.pow(y2-y1, 2);
        return Math.sqrt(x + y);
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
        dataManager.removeShape(bottomRemovedLine);
        
        newLine.startXProperty().bind(topRemovedLine.startXProperty());
        newLine.startYProperty().bind(topRemovedLine.startYProperty());
        newLine.endXProperty().bind(bottomRemovedLine.endXProperty());
        newLine.endYProperty().bind(bottomRemovedLine.endYProperty());
        
        if(metroLine.getStations().indexOf(station) != metroLine.getStations().size()-1){
            Station nextStation = metroLine.getStations().get(metroLine.getStations().indexOf(station)+1);
            dataManager.removeShape(nextStation);
            dataManager.addShape(nextStation);
        }
        metroLine.getStations().remove(station);
        station.getMetroLines().remove(metroLine);
        station.setCenterX(station.getCenterX() - 30);
        station.setCenterY(station.getCenterY() - 30);
        
        for(int i = 0; i < station.getMetroLines().size(); i++){
            metroLine.removeTransfer(station.getMetroLines().get(i));
            station.getMetroLines().get(i).removeTransfer(metroLine);
        }
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
                    dataManager.removeShape(metroLine.getLines().get(i));
                }
                
                for(int j = 0; j < metroLine.getStations().size(); j++){
                    metroLine.getStations().get(j).getMetroLines().remove(metroLine);
                }
                
                dataManager.getMetroLines().remove(metroLine);
                dataManager.removeShape(metroLine.getTopLabel());
                dataManager.removeShape(metroLine.getBottomLabel());
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
                    dataManager.removeShape(station);

                else{ 
                    while(station.getMetroLines().size() > 0){
                        removeFromLine(station, station.getMetroLines().get(0));
                    }
                    dataManager.removeShape(station);
                }

                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                workspace.stationsBox.getItems().remove(station.getName());
                workspace.routeBox1.getItems().remove(station.getName());
                workspace.routeBox2.getItems().remove(station.getName());
                dataManager.removeShape(station.getLabel());
            }
            
            if(result.get() == ButtonType.CANCEL){ }
        }
    }
    
    public void processZoomInRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        canvas.setScaleX(canvas.getScaleX() + .1);
        canvas.setScaleY(canvas.getScaleY() + .1);
        dataManager.setZoomScale(canvas.getScaleX());
        canvas.requestFocus();
    }
    
    public void processZoomOutRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        Pane ws = workspace.getWorkspace();
        Pane center = workspace.getCenterPane();
        Pane canvas = workspace.getCanvas();
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        canvas.setScaleX(canvas.getScaleX() - .1);
        canvas.setScaleY(canvas.getScaleY() - .1);
        dataManager.setZoomScale(canvas.getScaleX());
        canvas.requestFocus();
    }
    
    public void processDecreaseMapSize(){
        if(dataManager.getMapScale() > .4){
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            Pane centerPane = workspace.getCenterPane();
            centerPane.setScaleX(centerPane.getScaleX() - .1);
            centerPane.setScaleY(centerPane.getScaleY() - .1);
            dataManager.setMapScale(centerPane.getScaleX());
        }
    }
    
    public void processIncreaseMapSize(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        Pane centerPane = workspace.getCenterPane();
        centerPane.setScaleX(centerPane.getScaleX() + .1);
        centerPane.setScaleY(centerPane.getScaleY() + .1);
        dataManager.setMapScale(centerPane.getScaleX());
    }
    
    public void processPanRequest(KeyEvent e){
        if(dataManager.getZoomScale() > 1){
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            if(e.getCode() == KeyCode.W){
                canvas.setTranslateY(canvas.getTranslateY() + 100);
            }
            if(e.getCode() == KeyCode.A){
                canvas.setTranslateX(canvas.getTranslateX() + 100);
            }
            
            if(e.getCode() == KeyCode.S){
                canvas.setTranslateY(canvas.getTranslateY() - 100);
            }
            
            if(e.getCode() == KeyCode.D){
                canvas.setTranslateX(canvas.getTranslateX() - 100);
            }
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
            String lineName = metroLine.getName();
            t.setText(metroLine.getName());
            ColorPicker p = new ColorPicker();
            p.setValue(Color.BLACK);
            p.setLayoutX(40);
            p.setLayoutY(170);
            p.setValue((Color)metroLine.getLines().get(0).getStroke());
            CheckBox circular = new CheckBox();
            circular.setSelected(metroLine.isCircular());
            circular.setLayoutX(200);
            circular.setLayoutY(175);
            Text circularText = new Text("Circular");
            circularText.setLayoutX(230);
            circularText.setLayoutY(185);
            Button ok = new Button("OK");
            Button cancel = new Button("Cancel");
            ok.setLayoutX(100);
            ok.setLayoutY(250);
            cancel.setLayoutX(170);
            cancel.setLayoutY(250);

            editLinePane.getChildren().add(content);
            editLinePane.getChildren().add(t);
            editLinePane.getChildren().add(p);
            editLinePane.getChildren().add(circular);
            editLinePane.getChildren().add(circularText);
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
                
                for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                    if(dataManager.getMetroLines().get(i).getName().equals(name) && !name.equals(lineName)){

                        Alert alert = new Alert(AlertType.WARNING, "Error: line name already exists. Do you wish to enter a new name?",ButtonType.OK, ButtonType.CANCEL);
                        alert.setHeaderText("Name Already Exists");

                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.get() == ButtonType.OK){
                            processEditLineRequest();
                            return;
                        }
                        else if(result.get() == ButtonType.CANCEL){
                            return;
                        }
                    }
                }
                
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
                
                if(circular.isSelected() == true && editMetroLine.isCircular() == false){
                    
                    Line endLine = editMetroLine.getLines().get(editMetroLine.getLines().size()-1);
                    endLine.endXProperty().bind(editMetroLine.getTopLabel().xProperty().add(editMetroLine.getName().length()*10 + 30));
                    endLine.endYProperty().bind(editMetroLine.getTopLabel().yProperty().subtract(5));
                    dataManager.getShapes().remove(editMetroLine.getBottomLabel());
                    editMetroLine.setIsCircular(true);
                }
                
                else if(circular.isSelected() == false && editMetroLine.isCircular() == true){
                    Line endLine = editMetroLine.getLines().get(editMetroLine.getLines().size()-1);
                    dataManager.getShapes().add(editMetroLine.getBottomLabel());
                    endLine.endXProperty().bind(editMetroLine.getBottomLabel().xProperty().subtract( 20));
                    endLine.endYProperty().bind(editMetroLine.getBottomLabel().yProperty().subtract(5));
                    editMetroLine.setIsCircular(false);
                }
            });
            
            cancel.setOnAction(e ->{
                editLineStage.close();
            });
        }
    } 
    
    public void processBackgroundColorRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
	    //dataManager.setBackgroundColor(selectedColor);
            jTPS j = dataManager.getJTPS();
            jTPS_Transaction transaction = new AddColor_Transaction(selectedColor, true, false, dataManager, dataManager.getSelectedShape(), app);
            j.addTransaction(transaction);
            if(dataManager.getJTPS().getTransList().size() == 1){
                workspace.redoCounter = 0;
                workspace.redoButton.setDisable(true);
            }
	    app.getGUI().updateToolbarControls(false);
            workspace.undoButton.setDisable(false);
	}
    }
    
    public void processNewImage(){
       
        
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
                  
            if(selectedFile != null){
                
                if (dataManager.getSelectedShape() != null) {
                    dataManager.unhighlightShape(dataManager.getSelectedShape());
                    dataManager.setSelectedShape(null);
                }
                
                Image image = new Image(selectedFile.toURI().toString());
                DraggableRectangle rect = new DraggableRectangle();
                rect.setImageString(selectedFile.toURI().toString());
                rect.start(200,200);
                rect.setWidth((int)image.getWidth());
                rect.setHeight((int)image.getHeight());
                ImagePattern iP = new ImagePattern(image);
                Shape newShape = rect;
                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                newShape.setFill(iP);
                newShape.setStroke(Color.BLACK);
                newShape.setStrokeWidth(0);
                
                //shapes.add(newShape);
                jTPS_Transaction transaction = new AddShape_Transaction(newShape, dataManager, app);
                dataManager.getJTPS().addTransaction(transaction);
                if(dataManager.getJTPS().getTransList().size() == 1){
                    workspace.redoCounter = 0;
                    workspace.redoButton.setDisable(true);
                }
                workspace.undoButton.setDisable(false);
                dataManager.getImageShapes().add(newShape);
            }                  
    }
    
    public void processListStationsRequest(){
        
        if(dataManager.getSelectedShape() instanceof Line){
            Stage listStationsStage = new Stage();
            Pane listStationsPane = new Pane();
            
            MetroLine line = dataManager.getSelectedLine();
            Text metroText = new Text("Metro Line: " + line.getName());
            metroText.setFont(Font.font("System", FontWeight.BOLD, 16));
            metroText.setX(10);
            metroText.setY(40);
            Text stationsText = new Text("Stations:\n");
            for(int i = 0; i < line.getStations().size(); i++){
                stationsText.setText(stationsText.getText() + "         " + line.getStations().get(i).getName() + "\n");
            }
            stationsText.setFont(Font.font("System", FontWeight.BOLD, 16));
            stationsText.setX(10);
            stationsText.setY(60);
            
            listStationsPane.getChildren().add(metroText);
            listStationsPane.getChildren().add(stationsText);
            Scene scene = new Scene(listStationsPane, 400, 100 + (20 * line.getStations().size()));
            listStationsStage.setScene(scene);
            listStationsStage.setTitle("Stations");
            listStationsStage.show();
        }
    }
    
    public void processMoveStationLabelRequest(){
        
        if(dataManager.getSelectedShape() instanceof Station){
           Station station = (Station)dataManager.getSelectedShape();
           Text label = station.getLabel();
           
           if(station.getPositionNumber() == 0){
                label.xProperty().bind(station.centerXProperty().subtract((10 + station.getRadiusX())+(7*label.getText().length())));
                label.yProperty().bind(station.centerYProperty().subtract(10 + station.getRadiusY()));
                station.setPositionNumber(1);
           }
           
           else if(station.getPositionNumber() == 1){
               
               label.xProperty().bind(station.centerXProperty().subtract((10 + station.getRadiusX())+(7*label.getText().length())));
               label.yProperty().bind(station.centerYProperty().add(10 + station.getRadiusY()));
               station.setPositionNumber(2);
           }
           
           else if(station.getPositionNumber() == 2){
               
               label.xProperty().bind(station.centerXProperty().add(20 + station.getRadiusX()));
               label.yProperty().bind(station.centerYProperty().add(10 + station.getRadiusY()));
               station.setPositionNumber(3);
           }
           
           else if(station.getPositionNumber() == 3){
               label.xProperty().bind(station.centerXProperty().add(20 + station.getRadiusX()));
               label.yProperty().bind(station.centerYProperty().subtract(10 + station.getRadiusY()));
               station.setPositionNumber(0);
           }
        }
    }
    
    public void processRotateRequest(){
        if(dataManager.getSelectedShape() instanceof Station){
            Station station = (Station)dataManager.getSelectedShape();
            Text label = station.getLabel();
            
            if(station.getIsRotated() == 0){
                label.setRotate(270);
                station.setIsRotated(1);
            }
            
            else{
                label.setRotate(0);
                station.setIsRotated(0);
            }
        }
    }
    
    public void processGridRequest(){
        
        if(dataManager.getShapes().isEmpty() || (!(dataManager.getShapes().get(0) instanceof GridLine) && !(dataManager.getShapes().get(1) instanceof GridLine))){
            gridLinesV = new ArrayList<>();
            gridLinesH = new ArrayList<>();

            for(int i = 1; i < 100; i++){
                GridLine line1 = new GridLine();
                line1.setStartX(i * 100);
                line1.setEndX(i * 100);
                line1.setStartY(0);
                line1.setEndY(5000);
                line1.setStrokeWidth(2);
                line1.setStroke(Color.BLACK);
                gridLinesV.add(line1);

                GridLine line2 = new GridLine();
                line2.setStartX(0);
                line2.setEndX(10000);
                line2.setStartY(i * 100);
                line2.setEndY(i * 100);
                line2.setStrokeWidth(2);
                line2.setStroke(Color.BLACK);
                gridLinesH.add(line2);
            }
            
            if(dataManager.getShapes().isEmpty() || !(dataManager.getShapes().get(0) instanceof ImageView)){
                for(int j = 0; j < gridLinesV.size(); j++){
                    dataManager.getShapes().add(j, gridLinesV.get(j));
                    dataManager.getShapes().add(j, gridLinesH.get(j));
                }
            }
            
            else{
                for(int j = 1; j < gridLinesV.size(); j++){
                    dataManager.getShapes().add(j, gridLinesV.get(j-1));
                    dataManager.getShapes().add(j, gridLinesH.get(j-1));
                }
            }
            
        }
        
        else{
            for(int i = 0; i < gridLinesV.size(); i++){
                dataManager.removeShape(gridLinesV.get(i));
                dataManager.removeShape(gridLinesH.get(i));
            }
        }
    }
    
    public void processSelectOutlineThickness(Slider selectedSlider) {
	int outlineThickness = (int)selectedSlider.getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processSnapToGridRequest(){
        
        if(dataManager.getSelectedShape() instanceof Station){
            if(dataManager.getShapes().get(1) instanceof GridLine){
                
                Shape selectedShape = dataManager.getSelectedShape();
                Station station = (Station)dataManager.getSelectedShape();
                double newX;
                double newY;
                
                if(station.getX() % 100 < 50)
                    newX = station.getX() - (station.getX() % 100);
                else
                    newX = station.getX() + (100 - (station.getX() % 100));
                
                if(station.getY() % 100 < 50)
                    newY = station.getY() - (station.getY() % 100);
                else
                    newY = station.getY() + (100 - (station.getY() % 100));
                
                station.setCenterX(newX);
                station.setCenterY(newY);
                
                if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                    MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                    if(!dataManager.getShapes().contains(line.getTopLabel())){
                        line.getLines().get(0).startXProperty().unbind();
                        line.getLines().get(0).startYProperty().unbind();
                        DraggableText topLabel = line.getTopLabel();
                        topLabel.setX(((Station)selectedShape).getCenterX());
                        topLabel.setY(((Station)selectedShape).getCenterY());
                        line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                        line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                        line.getStations().remove(0);
                        dataManager.getShapes().remove(selectedShape);
                        dataManager.getShapes().add(topLabel);
                    }

                    else if(!dataManager.getShapes().contains(line.getBottomLabel())){
                        line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                        line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                        DraggableText bottomLabel = line.getBottomLabel();
                        bottomLabel.setX(((Station)selectedShape).getCenterX());
                        bottomLabel.setY(((Station)selectedShape).getCenterY());
                        line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                        line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                        line.getStations().remove(line.getStations().size()-1);
                        dataManager.getShapes().remove(selectedShape);
                        dataManager.getShapes().add(bottomLabel);
                    }
                }
                dataManager.unhighlightShape(station);
                dataManager.setSelectedShape(null);
            }
        }
    }
    
    public void processStationColorRequest(){
        if(dataManager.getSelectedShape() instanceof Station){
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            Color selectedColor = workspace.getStationColorPicker().getValue();
            if (selectedColor != null) {
                //dataManager.setCurrentOutlineColor(selectedColor);
                if(dataManager.getSelectedShape() != null){
                    jTPS j = dataManager.getJTPS();
                    jTPS_Transaction transaction = new AddColor_Transaction(selectedColor, false, true, dataManager, dataManager.getSelectedShape(), app);
                    j.addTransaction(transaction);
                    if(dataManager.getJTPS().getTransList().size() == 1){
                        workspace.redoCounter = 0;
                        workspace.redoButton.setDisable(true);
                    }
                }

                app.getGUI().updateToolbarControls(false);
                workspace.undoButton.setDisable(false);
            }
        }
    }
    
    public void processRemoveSelectedShape() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	//dataManager.removeSelectedShape();
        if(dataManager.getSelectedShape() instanceof DraggableRectangle || (dataManager.getSelectedShape() instanceof DraggableText && ((DraggableText)dataManager.getSelectedShape()).getMetroLine() == null)){
            jTPS j = dataManager.getJTPS();
            jTPS_Transaction transaction = new RemoveShape_Transaction(dataManager.getSelectedShape(), dataManager, app);
            j.addTransaction(transaction);

            // ENABLE/DISABLE THE PROPER BUTTONS
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            if(dataManager.getJTPS().getTransList().size() == 1){
                    workspace.redoCounter = 0;
                    workspace.redoButton.setDisable(true);
            }
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
            workspace.undoButton.setDisable(false);
        }
    }
    
    public void processNewText(){
        
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        TextInputDialog txtDialog = new TextInputDialog();
        txtDialog.setTitle("New Text");
        txtDialog.setHeaderText("Entry for new text");
        txtDialog.setContentText("Please enter Text:");

        
        Optional<String> input = txtDialog.showAndWait();
        if(input.isPresent()){
            DraggableText text = new DraggableText("Arial", 20.0);
            text.setText(input.get());
            text.start(500,300);
            jTPS_Transaction transaction = new AddShape_Transaction((Shape)text, dataManager, app);
            dataManager.getJTPS().addTransaction(transaction);
            if(dataManager.getJTPS().getTransList().size() == 1){
                workspace.redoCounter = 0;
                workspace.redoButton.setDisable(true);
            }
            workspace.undoButton.setDisable(false);
            dataManager.getTextShapes().add(text);
        }    
    }
    
    public void processImageBackgroundRequest(){
        
        Alert alert = new Alert(AlertType.CONFIRMATION, "Would you like to set an image background?",ButtonType.YES, ButtonType.NO);
                        alert.setHeaderText("Set Image Background");

                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.get() == ButtonType.YES){
                            setImageBackground();
                            return;
                        }
                        else if(result.get() == ButtonType.NO){
                            if(dataManager.getShapes().get(0) instanceof ImageView){
                                dataManager.getShapes().remove(dataManager.getShapes().get(0));
                            }
                            return;
                        }
        
    }
    
    public void setImageBackground(){
        
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
                  
            if(selectedFile != null){
                
                if(!dataManager.getShapes().isEmpty() &&  dataManager.getShapes().get(0) instanceof ImageView){
                    dataManager.getShapes().remove(0);
                }
                
                Image image = new Image(selectedFile.toURI().toString());
                dataManager.setImageString(selectedFile.toURI().toString());
                ImageView v = new ImageView(image);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                v.setX(workspace.getWorkspace().getWidth()/2 - 250 - image.getWidth()/2);
                v.setY(workspace.getWorkspace().getHeight()/2 - image.getHeight()/2);
                dataManager.getShapes().add(0, v);
            } 
    }
    
    public void processFindRouteRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        if(!((String)workspace.getRouteBox1().getValue()).isEmpty() && !((String)workspace.getRouteBox2().getValue()).isEmpty()){
            Station station1 = null; 
            Station station2 = null;
            
            for(int i = 0; i < dataManager.getShapes().size(); i++){
                if(dataManager.getShapes().get(i) instanceof Station && !((Station)dataManager.getShapes().get(i)).isEndLabel() 
                        && ((Station)dataManager.getShapes().get(i)).getName().equals(workspace.getRouteBox1().getValue())){
                    station1 = (Station)dataManager.getShapes().get(i);
                }
                
                if(dataManager.getShapes().get(i) instanceof Station && !((Station)dataManager.getShapes().get(i)).isEndLabel() 
                        && ((Station)dataManager.getShapes().get(i)).getName().equals(workspace.getRouteBox2().getValue())){
                    station2 = (Station)dataManager.getShapes().get(i);
                }
            }
            
            Path path = findMinimumTransferPath(station1, station2);
            showPathDescription(path, station1, station2);
        }
    }
    
    public Path findMinimumTransferPath(Station startStation, Station endStation){
        
        ArrayList<MetroLine> linesToTest = new ArrayList<>();
        ArrayList<MetroLine> visitedLines = new ArrayList<>();
        
        int numTransfers = 0;
        ArrayList<Path> testPaths = new ArrayList<>();
        
        for(int i = 0; i < startStation.getMetroLines().size(); i++){
            Path path = new Path(startStation, endStation);
            testPaths.add(path);
            path.addBoarding(startStation.getMetroLines().get(i), startStation);
            
        }
        
        boolean found = false;
        boolean morePathsPossible = true;
        ArrayList<Path> completedPaths = new ArrayList<>();
        
        while(!found && morePathsPossible){
            ArrayList<Path> updatedPaths = new ArrayList<>();
            for(int i = 0; i < testPaths.size(); i++){
                Path testPath = testPaths.get(i);
                
                if(testPath.hasLineWithStation(endStation)){
                    completedPaths.add(testPath);
                    found = true;
                    morePathsPossible = false;
                }
                
                else if(morePathsPossible){
                    MetroLine lastLine = testPath.tripLines.get(testPath.tripLines.size()-1);
                    for(int j = 0; j < lastLine.getTransfers().size(); j++){
                        MetroLine testLine = lastLine.getTransfers().get(j);
                        if(!testPath.hasLine(testLine)){
                            Path newPath = testPath.clonePath();
                            Station intersectingStation = lastLine.findIntersectingStation(testLine);
                            newPath.addBoarding(testLine, intersectingStation);
                            updatedPaths.add(newPath);
                        }
                    }
                }
            }
            
            if(updatedPaths.size() > 0){
                testPaths = updatedPaths;
                numTransfers++;
            }
            
            else{
                morePathsPossible = false;
            }
        }
        
        if(found){
            Path shortestPath = completedPaths.get(0);
            int shortestTime = shortestPath.calculateTimeOfTrip();
            
            for(int i = 1; i < completedPaths.size(); i++){
                Path testPath = completedPaths.get(i);
                int timeOfTrip = testPath.calculateTimeOfTrip();
                
                if(timeOfTrip < shortestTime){
                    shortestPath = testPath;
                    shortestTime = timeOfTrip;
                }
            }
            return shortestPath;
        }
        
        else{
            return null;
        }
        
    }
    
    public void showPathDescription(Path path, Station startStation, Station endStation){
        
        try{
            Stage pathStage = new Stage();
            Pane pathPane = new Pane();
            
            Text text = new Text("PATH FOUND\n\n");
            MetroLine line = path.tripLines.get(0);
            int i = 0;
            for(;i < path.tripLines.size(); i++){
                line = path.tripLines.get(i);
                text.setText(text.getText() + (i+1) + ". Board " + line.getName() + " at " + path.boardingStations.get(i).getName() + "\n");
            }
            
            text.setText(text.getText() + (i+1) + ". Disembark " + line.getName() + " at " + endStation.getName());
            text.setFont(Font.font("System", FontWeight.BOLD, 16));
            text.setLayoutX(50);
            text.setLayoutY(50);
            pathPane.getChildren().add(text);
            Scene scene = new Scene (pathPane, 400, 350);
            pathStage.setScene(scene);
            pathStage.setTitle("Path Description");
            pathStage.show();
        }
        catch(NullPointerException e){
            
            Alert alert = new Alert(AlertType.ERROR, "Selected route is not obtainable",ButtonType.CLOSE);
            alert.setHeaderText("Error: Invalid Path");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.CLOSE){
                return;
            }
        }
 
    }
    
    public void processUndoRequest(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        jTPS j = dataManager.getJTPS();
        j.undoTransaction();
        workspace.redoButton.setDisable(false);
        
    }
    
    public void processRedoRequest(){
        jTPS j = dataManager.getJTPS();
        j.doTransaction();
    }
}
