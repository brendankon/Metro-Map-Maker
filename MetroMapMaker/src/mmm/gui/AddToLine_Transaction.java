package mmm.gui;

import djf.AppTemplate;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.Station;

/**
 *
 * @author brendan
 */
public class AddToLine_Transaction implements jTPS_Transaction{
    
    MetroLine metroLine;
    Station station;
    mapEditController controller;
    double x;
    double y;
    
    public AddToLine_Transaction(MetroLine metroLine, Station station, AppTemplate app){
        this.metroLine = metroLine;
        this.station = station;
        controller = new mapEditController(app);
        x = station.getCenterX();
        y = station.getCenterY();
    }
    
    @Override
    public void doTransaction(){
        controller.addToLine(station, metroLine);
    }
    
    @Override 
    public void undoTransaction(){
        controller.removeFromLine(station, metroLine);
        station.setCenterX(x);
        station.setCenterY(y);
    }
}
