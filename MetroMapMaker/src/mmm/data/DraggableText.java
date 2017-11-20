/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

/**
 *
 * @author brendan
 */
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author brendankondracki
 */
public class DraggableText extends Text implements Draggable {
    
    public double startX;
    public double startY;
    boolean isBolded;
    boolean isItalicized;
    boolean started;
    Line topLine;
    Line bottomLine;
    double refX;
    double refY;
    String lineName;
    MetroLine metroLine;
    
    public DraggableText(String style, double size){
        
        started = false;
        this.setFont(Font.font(style, size));
        startX = 0.0;
        startY = 0.0;
        xProperty().set(0.0);
        yProperty().set(0.0);
        isBolded = false;
        isItalicized = false;
        
    }
    
    @Override
    public mmmState getStartingState(){
        return mmmState.SELECTING_SHAPE;
    }
    
    @Override
    public void start(int x, int y){
        
        if(!started){
            startX = x;
            startY = y;
            xProperty().set(startX);
            yProperty().set(startY);
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
    public void drag(int x, int y){
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
    
    @Override
    public void size(int x, int y){
        
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        xProperty().set(initX);
        yProperty().set(initY);
    }
    
    @Override
    public String getShapeType(){
        return TEXT;
    }
    
    @Override
    public double getHeight(){
        return getBoundsInLocal().getHeight();
    }
    
    @Override
    public double getWidth(){
        return getBoundsInLocal().getWidth();
    }
    
    public String getFontStyle(){
        return this.getFont().getFamily();
    }
    
    public Double getFontSize(){
        return this.getFont().getSize();
    }
    
    public void setFontStyle(String style){
        this.setFont(Font.font(style, this.getFont().getSize()));
    }
    
    public void setFontSize(Double size){
        this.setFont(Font.font(this.getFont().getFamily(), size));
    }
    
    public boolean isBolded(){
        return isBolded;
    }
    
    public boolean isItalicized(){
        return isItalicized;
    }
    
    public void setBolded(boolean b){
        isBolded = b;
    }
    
    public void setItalicized(boolean b){
        isItalicized = b;
    }
    
    public void setLineName(String s){
        lineName = s;
        this.setText(lineName);
    }
    
    public String getLineName(){
        return lineName;
    }
    
    public void setTopLabel(Line l){
        topLine = l;
    }
    
    public void setBottomlabel(Line l){
        bottomLine = l;
    }
    
    public Line getTopLine(){
        return topLine;
    }
    
    public Line getBottomLine(){
        return bottomLine;
    }
    
    public void setMetroLine(MetroLine line){
        metroLine = line;
    }
    
    public MetroLine getMetroLine(){
        return metroLine;
    }
}
