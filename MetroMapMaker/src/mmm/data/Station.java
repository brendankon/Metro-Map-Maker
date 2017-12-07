package mmm.data;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

/**
 *
 * @author brendan
 */
public class Station extends Ellipse implements Draggable{
    
    String name;
    double startCenterX;
    double startCenterY;
    ArrayList<MetroLine> metroLines;
    DraggableText label;
    boolean isEndLabel;
    int isRotated;
    int positionNumber;
    
    public Station(String name){
        this.name = name;
        this.setRadiusX(10);
        this.setRadiusY(10);
        this.setFill(Color.WHITE);
        startCenterX = 0;
        startCenterY = 0;
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
        metroLines = new ArrayList<>();
        positionNumber = 0;
        isRotated = 0;
        
    }
    
    @Override
    public mmmState getStartingState() {
	return mmmState.SELECTING_SHAPE;
    }
    
    @Override
    public void size(int x, int y){
        
    }
    
    @Override 
    public void start(int x, int y){
        startCenterX = x;
        startCenterY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startCenterX;
	double diffY = y - startCenterY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
    }
    
     @Override
    public double getX() {
	return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
	return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
	return getRadiusY() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadiusX(initWidth/2);
	setRadiusY(initHeight/2);
    }
    
    @Override
    public String getShapeType() {
	return ELLIPSE;
    }
    
    public void addMetroLine(MetroLine line){
        metroLines.add(line);
    }
    
    public ArrayList<MetroLine> getMetroLines(){
        return metroLines;
    }
    
    public String getName(){
        return name;
    }
    
    public void setLabel(DraggableText text){
        label = text;
    }
    
    public DraggableText getLabel(){
        return label;
    }
    
    public boolean isEndLabel(){
        return isEndLabel;
    }
    
    public void setIsEndLabel(boolean b){
        isEndLabel = b;
    }
    
    public int getPositionNumber(){
        return positionNumber;
    }
    
    public void setPositionNumber(int n){
        positionNumber = n;
    }
    
    public int getIsRotated(){
        return isRotated;
    }
    
    public void setIsRotated(int b){
        isRotated = b;
    }
}
