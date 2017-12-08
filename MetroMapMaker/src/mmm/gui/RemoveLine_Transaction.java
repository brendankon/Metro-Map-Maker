package mmm.gui;

import djf.AppTemplate;
import java.util.ArrayList;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.mmmData;

/**
 *
 * @author brendan
 */
public class RemoveLine_Transaction implements jTPS_Transaction {
    
    MetroLine metroLine;
    mmmWorkspace workspace;
    mmmData dataManager;
    ArrayList<Integer> index;
    
    public RemoveLine_Transaction(MetroLine metroLine, AppTemplate app){
        
        this.metroLine = metroLine;
        workspace = (mmmWorkspace)app.getWorkspaceComponent();
        dataManager = (mmmData)app.getDataComponent();
    }
    
    @Override
    public void doTransaction(){
        for(int i = 0; i < metroLine.getLines().size(); i++){
            dataManager.removeShape(metroLine.getLines().get(i));
        }

        for(int j = 0; j < metroLine.getStations().size(); j++){
            metroLine.getStations().get(j).getMetroLines().remove(metroLine);
        }

        for(int i = 0; i < metroLine.getStations().size(); i++){
            for(int j = 0; j < metroLine.getStations().get(i).getMetroLines().size(); j++){
                metroLine.getStations().get(i).getMetroLines().get(j).removeTransfer(metroLine);
            }
        }

        dataManager.getMetroLines().remove(metroLine);
        dataManager.removeShape(metroLine.getTopLabel());
        dataManager.removeShape(metroLine.getBottomLabel());
        workspace.lineBox.getItems().remove(metroLine.getName());
    }
    
    @Override 
    public void undoTransaction(){
        
        for(int i = 0; i < metroLine.getLines().size(); i++){
            dataManager.getShapes().add(metroLine.getLines().get(i));
        }
        
        for(int j = 0; j < metroLine.getStations().size(); j++){
            metroLine.getStations().get(j).getMetroLines().add(metroLine);
        }
        
        for(int i = 0; i < metroLine.getStations().size(); i++){
            for(int j = 0; j < metroLine.getStations().get(i).getMetroLines().size(); j++){
                metroLine.getStations().get(i).getMetroLines().get(j).addTransfer(metroLine);
            }
        }
        
        for(int i = 0 ; i < metroLine.getStations().size(); i++){
            dataManager.getShapes().remove(metroLine.getStations().get(i));
            dataManager.getShapes().add(metroLine.getStations().get(i));
        }
        
        dataManager.getMetroLines().add(metroLine);
        dataManager.addShape(metroLine.getTopLabel());
        if(!metroLine.isCircular())
            dataManager.addShape(metroLine.getBottomLabel());
        workspace.lineBox.getItems().add(metroLine.getName());
        workspace.lineBox.setValue(metroLine.getName());
        dataManager.selectTopShape((int)metroLine.getLines().get(0).getStartX(), (int)metroLine.getLines().get(0).getStartY());
    }
}
