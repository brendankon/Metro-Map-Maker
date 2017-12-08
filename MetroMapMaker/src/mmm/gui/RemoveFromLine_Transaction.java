package mmm.gui;

import djf.AppTemplate;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.Station;

/**
 *
 * @author brendan
 */
public class RemoveFromLine_Transaction implements jTPS_Transaction{
    
    MetroLine metroLine;
    Station station;
    mapEditController controller;
    
    public RemoveFromLine_Transaction(MetroLine metroLine, Station station, AppTemplate app){
        this.metroLine = metroLine;
        this.station = station;
        controller = new mapEditController(app);
    }
    
    @Override
    public void doTransaction(){
        controller.removeFromLine(station, metroLine);
    }
    
    @Override
    public void undoTransaction(){
        controller.addToLine(station, metroLine);
    }
    
}
