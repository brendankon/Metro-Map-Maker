package mmm.gui;

import djf.AppTemplate;
import java.util.ArrayList;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.Station;
import mmm.data.mmmData;

/**
 *
 * @author brendan
 */
public class RemoveStation_Transaction implements jTPS_Transaction{
    
    Station station;
    mmmWorkspace workspace;
    mmmData dataManager;
    mapEditController controller;
    ArrayList<MetroLine> metroLines;
    double x;
    double y;
    
    public RemoveStation_Transaction(Station station, AppTemplate app){
        this.station = station;
        workspace = (mmmWorkspace)app.getWorkspaceComponent();
        dataManager = (mmmData)app.getDataComponent();
        controller = new mapEditController(app);
        metroLines = new ArrayList<>();
        x = station.getCenterX();
        y = station.getCenterY();
    }
    
    @Override
    public void doTransaction(){
        
        if(station.getMetroLines().isEmpty())
            dataManager.removeShape(station);

        else{ 
            while(station.getMetroLines().size() > 0){
                metroLines.add(station.getMetroLines().get(0));
                controller.removeFromLine(station, station.getMetroLines().get(0));
            }
            dataManager.removeShape(station);
        }

        workspace.stationsBox.getItems().remove(station.getName());
        workspace.routeBox1.getItems().remove(station.getName());
        workspace.routeBox2.getItems().remove(station.getName());
        dataManager.removeShape(station.getLabel());
        for(int i = 0; i < station.getMetroLines().size(); i++){
            for(int j = 0; j < station.getMetroLines().size(); j++){
                station.getMetroLines().get(i).removeTransfer(station.getMetroLines().get(j));
                station.getMetroLines().get(j).removeTransfer(station.getMetroLines().get(i));
            }
        }
    }
    
    @Override
    public void undoTransaction(){
        
        if(metroLines.isEmpty()){
            dataManager.getShapes().add(station);
            dataManager.getShapes().add(station.getLabel());
        }
        
        else{
            dataManager.getShapes().add(station);
            dataManager.getShapes().add(station.getLabel());
            for(int i = 0; i < metroLines.size(); i++){
                controller.addToLine(station, metroLines.get(i));
            }
        }
        
        station.setCenterX(x);
        station.setCenterY(y);
        workspace.stationsBox.getItems().add(station.getName());
        workspace.routeBox1.getItems().add(station.getName());
        workspace.routeBox2.getItems().add(station.getName());
        
    }
}
