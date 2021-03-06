package mmm.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import mmm.gui.mmmWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.io.File;
import java.util.List;
import java.util.Optional;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import jtps.jTPS;
import static mmm.data.mmmState.SELECTING_SHAPE;

/**
 *
 * @author brendan
 */
public class mmmData implements AppDataComponent{
    
    jTPS jTPS = new jTPS();
    
    ObservableList<Node> shapes;
    
    ArrayList<Shape> imageShapes;
    ArrayList<DraggableText> textShapes;
    ArrayList<MetroLine> metroLines;
    
    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    MetroLine selectedLine;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Shape selectedShape;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;
    String imageString;
    double mapScale = 1;
    double zoomScale = 1;

    // CURRENT STATE OF THE APP
    mmmState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;
    
    public mmmData(AppTemplate initApp) {
       	app = initApp;
        imageShapes = new ArrayList<>();
        textShapes = new ArrayList<>();
        metroLines = new ArrayList<>();

	// NO SHAPE STARTS OUT AS SELECTED
	selectedShape = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
        backgroundColor = (Color)DEFAULT_BACKGROUND_COLOR;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;  
        
        state = mmmState.SELECTING_SHAPE;
    }
    
    public ObservableList<Node> getShapes() {
	return shapes;
    }
    
    public void setMapScale(double scale){
        mapScale = scale;
    }
    
    public double getMapScale(){
        return mapScale;
    }
    
    public void setZoomScale(double scale){
        zoomScale = scale;
    }
    
    public double getZoomScale(){
        return zoomScale;
    }
    
    public void addShape(Shape shape){
        shapes.add(shape);
    }
    
    public void addMetroLine(MetroLine line){
        metroLines.add(line);
    }
    
    public void setImageString(String s){
        imageString = s;
    }
    
    public String getImageString(){
        return imageString;
    }
    
    public ArrayList<MetroLine> getMetroLines(){
        return metroLines;
    }

    public Color getBackgroundColor() {
	return backgroundColor;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    
    public ArrayList<Shape> getImageShapes(){
        return imageShapes;
    }
    
    public ArrayList<DraggableText> getTextShapes(){
        return textShapes;
    }
    
    public void addTextShape(DraggableText text){
        textShapes.add(text);
    }
    
    public mmmWorkspace getWorkspace(){
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        return workspace;
    }
    
    public void setShapes(ObservableList<Node> initShapes) {
	shapes = initShapes;
    }
    
    public void setState(mmmState state){
        this.state = state;
    }
    
    public mmmState getState(){
        return state;
    }
    
    public boolean isInState(mmmState state){
        return this.state == state;
    }
    
    public void removeSelectedShape() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    selectedShape = null;
	}
    }
    
    public void removeShape(Shape shape){
        if(shape != null){
            shapes.remove(shape);
        }
    }
    
    public void unhighlightShape(Shape shape) {
	selectedShape.setEffect(null);
    }
    
    public void highlightShape(Shape shape) {
	shape.setEffect(highlightedEffect);
    }
    
    public Shape getSelectedShape() {
	return selectedShape;
    }
    
    public void setSelectedShape(Shape initSelectedShape) {
	selectedShape = initSelectedShape;
    }
    
    public void setCurrentOutlineThickness(int initBorderWidth) {
	currentBorderWidth = initBorderWidth;
	if (selectedShape != null) {
            if(selectedShape instanceof Line){
                for(int i = 0; i < selectedLine.getLines().size(); i++){
                    selectedLine.getLines().get(i).setStrokeWidth(currentBorderWidth);
                }
            }
        
            else if(selectedShape instanceof Station){
                Station station = (Station)selectedShape;
                station.setRadiusX(currentBorderWidth);
                station.setRadiusY(currentBorderWidth);
            }
	}
    }
    
    public void setBackgroundColor(Color initBackgroundColor) {
	backgroundColor = initBackgroundColor;
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
	Background background = new Background(fill);
	canvas.setBackground(background);
        workspace.getWorkspace().setBackground(background);
        workspace.getCenterPane().setBackground(background);
    }
    
    public Shape selectTopShape(int x, int y) {
	Shape shape = getTopShape(x, y);
        
        if(shape instanceof GridLine)
            return null;
        
	if (shape == selectedShape){
                return shape;
        }
        
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
            
            if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                if(!shapes.contains(line.getTopLabel())){
                    line.getLines().get(0).startXProperty().unbind();
                    line.getLines().get(0).startYProperty().unbind();
                    DraggableText topLabel = line.getTopLabel();
                    topLabel.setX(((Station)selectedShape).getCenterX());
                    topLabel.setY(((Station)selectedShape).getCenterY());
                    line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                    line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                    
                    if(line.isCircular()){
                        line.getLines().get(line.getLines().size()-1).endXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                        line.getLines().get(line.getLines().size()-1).endYProperty().bind(topLabel.yProperty().subtract(5));
                    }
                    line.getStations().remove(0);
                    shapes.remove(selectedShape);
                    shapes.add(topLabel);
                }
                
                else if(!shapes.contains(line.getBottomLabel())){
                    line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                    line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                    DraggableText bottomLabel = line.getBottomLabel();
                    bottomLabel.setX(((Station)selectedShape).getCenterX());
                    bottomLabel.setY(((Station)selectedShape).getCenterY());
                    line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                    line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                    line.getStations().remove(line.getStations().size()-1);
                    shapes.remove(selectedShape);
                    shapes.add(bottomLabel);
                }
            }
	}
        
        if(selectedShape instanceof Line){
            for(int i = 0; i < metroLines.size(); i++){
                for(int j = 0; j < metroLines.get(i).getLines().size(); j++){
                    if((Line)selectedShape == metroLines.get(i).getLines().get(j)){
                        selectedLine = metroLines.get(i);
                                                                                            
                        for(int l = 0; l < selectedLine.getLines().size(); l++){
                             selectedLine.getLines().get(l).setEffect(null);
                        }
                    }
                }
            }
        }
	if (shape != null) {
            selectedShape = shape;
	    highlightShape(shape);
	    mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	    workspace.loadSelectedShapeSettings(shape);
	}
	selectedShape = shape;
        
        if(shape instanceof Draggable){
            
            if (shape != null && ((Draggable)shape).getShapeType().equals("ELLIPSE")) {
                ((Station)shape).start(x, y);
            }
            
            else if(shape != null && ((Draggable)shape).getShapeType().equals("RECTANGLE")){
                ((DraggableRectangle)shape).started = true;
                ((DraggableRectangle)shape).start(x, y);
            }

            else if(shape != null && ((Draggable)shape).getShapeType().equals("TEXT")){
                ((DraggableText)shape).started = true;
                ((DraggableText)shape).start(x, y);
            }
        }
        return shape;
    }
    
    public Shape getTopShape(int x, int y) {
	for (int i = shapes.size() - 1; i >= 0; i--) {
            if(!(shapes.get(i) instanceof ImageView)){
                Shape shape = (Shape)shapes.get(i);
                if (shape.contains(x, y)) {
                    return shape;
                }
            }
	}
	return null;
    }
    
    public MetroLine getSelectedLine(){
        return selectedLine;
    }
    
    public void setSelectedLine(MetroLine line){
        selectedLine = line;
    }
     
    @Override
    public void resetData() {
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        setState(SELECTING_SHAPE);
        jTPS = new jTPS();
	selectedShape = null;
        workspace.resetLineBox();
        workspace.resetStationBox();
        workspace.resetRouteBoxes();

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	
        metroLines.clear();
	shapes.clear();
	((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
    
     public void processBoldRequest(Shape shape){
        
        for(int i = 0; i < getShapes().size(); i++){
            if(shape == getShapes().get(i)){
                DraggableText text = (DraggableText)getShapes().get(i);
                if(text.isBolded() && !text.isItalicized()){
                    text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.REGULAR, text.getFontSize()));
                    text.isBolded = false;
                }
                
                else if(text.isBolded() && text.isItalicized()){
                    text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.ITALIC, text.getFontSize()));
                    text.isBolded = false;
                }
                
                else if(!text.isBolded() && !text.isItalicized()){
                    text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.REGULAR, text.getFontSize()));
                    text.isBolded = true;
                }
                
                else if(!text.isBolded() && text.isItalicized()){
                    text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.ITALIC, text.getFontSize()));
                    text.isBolded = true;
                }
            }
        }
    }
    
    public void processItalicsRequest(Shape shape){
        for(int i = 0; i < getShapes().size(); i++){
            if(shape == getShapes().get(i)){
                DraggableText text = (DraggableText)getShapes().get(i);
                if(text.isItalicized() && !text.isBolded()){
                    text.setFont(Font.font(text.getFontStyle(),FontWeight.NORMAL, FontPosture.REGULAR, text.getFontSize()));
                    text.isItalicized = false;
                }
                
                else if(text.isItalicized() && text.isBolded()){
                    text.setFont(Font.font(text.getFontStyle(),FontWeight.BOLD, FontPosture.REGULAR, text.getFontSize()));
                    text.isItalicized = false;
                }
                
                else if(!text.isItalicized() && !text.isBolded()){
                    text.setFont(Font.font(text.getFontStyle(),FontWeight.NORMAL, FontPosture.ITALIC, text.getFontSize()));
                    text.isItalicized = true;
                }
                
                else if(!text.isItalicized() && text.isBolded()){
                    text.setFont(Font.font(text.getFontStyle(),FontWeight.BOLD, FontPosture.ITALIC, text.getFontSize()));
                    text.isItalicized = true;
                }
            }
        }
    }
    
    public jTPS getJTPS(){
        return jTPS;
    }
}
