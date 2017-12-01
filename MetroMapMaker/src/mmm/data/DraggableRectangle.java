package mmm.data;

import javafx.scene.shape.Rectangle;

/**
 * This is a draggable rectangle for our application.
 * 
 * @author Brendan Kondracki
 * @version 1.0
 */
public class DraggableRectangle extends Rectangle implements Draggable {
    public double startX;
    public double startY;
    boolean started;
    double refX;
    double refY;
    String imageString;
    
    public DraggableRectangle() {
        started = false;
	setX(0.0);
	setY(0.0);
	setWidth(0.0);
	setHeight(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    @Override
    public mmmState getStartingState() {
	return mmmState.SELECTING_SHAPE;
    }
    
    @Override
    public void start(int x, int y) {
        if(!started){
            startX = x;
            startY = y;
            setX(x);
            setY(y);
            started = true;
        }
        else
            select(x,y);
    }
    
    public void select(int x, int y){
        startX = x;
        startY = y;
        refX = x;
        refY = y;
    }
    
    @Override
    public void drag(int x, int y) {

	double diffX = refX - x;
	double diffY = refY - y;
	double newX = getX() - diffX;
	double newY = getY() - diffY;
	xProperty().set(newX);
	yProperty().set(newY);
        startX = x;
	startY = y;
        refX -= diffX;
        refY -= diffY;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	widthProperty().set(width);
	double height = y - getY();
	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	widthProperty().set(initWidth);
	heightProperty().set(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return RECTANGLE;
    }
    
    public void setImageString(String s){
        imageString = s;
    }
    
    public String getImageString(){
        return imageString;
    }
}
