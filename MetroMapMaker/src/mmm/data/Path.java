package mmm.data;

import java.util.ArrayList;

/**
 *
 * @author brendan
 */
public class Path {
    
    Station startStation;
    Station endStation;
    int stationCost;
    int transferCost;
    
    public ArrayList<MetroLine> tripLines;
    public ArrayList<String> tripLineNames;
    
    ArrayList<Station> tripStations;
    ArrayList<String> tripStationNames;
    public ArrayList<Station> boardingStations;
    
    public Path(Station startStation, Station endStation){
        
        this.startStation = startStation;
        this.endStation = endStation;
        stationCost = 3;
        transferCost = 10;
        
        tripLines = new ArrayList<>();
        tripLineNames = new ArrayList<>();
        
        tripStations = new ArrayList<>();
        tripStationNames = new ArrayList<>();
        boardingStations = new ArrayList<>();
    } 
    
    public Path clonePath(){
        
        Path clonedPath = new Path(this.startStation, this.endStation);
        for(int i = 0; i < this.tripLines.size(); i++){
            clonedPath.tripLines.add(this.tripLines.get(i));
            clonedPath.tripLineNames.add(this.tripLines.get(i).getName());  
        }
        
        for(int i = 0; i < this.tripStations.size(); i++){
            clonedPath.tripStations.add(this.tripStations.get(i));
            clonedPath.tripStationNames.add(this.tripStations.get(i).getName());
        }
        
        for(int i = 0; i < boardingStations.size(); i++){
            clonedPath.boardingStations.add(this.boardingStations.get(i));
        }
        
        return clonedPath;
    }
    
    public void addBoarding(MetroLine boardingLine, Station boardingStation){
        
        this.tripLines.add(boardingLine);
        this.tripLineNames.add(boardingLine.getName());
        
        this.boardingStations.add(boardingStation);
    }
    
    public ArrayList<Station> getTripStations(){
        
        this.tripStations = new ArrayList<>();
        this.tripStationNames = new ArrayList<>();
        
        if(this.isCompleteTrip()){
            
            int i = 0;
            while(i < this.boardingStations.size()-1){
                ArrayList<Station> stationsToAdd = this.generateStationsForPathOnLine(this.tripLines.get(i), 
                        this.boardingStations.get(i), this.boardingStations.get(i+1) );
                
                for(int j = 0; j < stationsToAdd.size(); j++){
                    Station stationToAdd = stationsToAdd.get(j);
                    if(!this.tripStationNames.contains(stationToAdd.getName())){
                        this.tripStations.add(stationToAdd);
                        this.tripStationNames.add(stationToAdd.getName());
                    }
                }
                
                i++;
            }
            
            ArrayList<Station> stationsToAdd = this.generateStationsForPathOnLine(this.tripLines.get(i), this.boardingStations.get(i), this.endStation);
            
            for(int x = 0; x < stationsToAdd.size(); x++){
                Station stationToAdd = stationsToAdd.get(x);
                this.tripStations.add(stationToAdd);
            }
        }
        
        return this.tripStations;
    }
    
    public boolean isCompleteTrip(){
        
        if(this.tripLines.isEmpty())
            return false;
        
        if(!this.tripLines.get(this.tripLines.size()-1).getStations().contains(this.endStation)){
            return false;
        }
        
        for(int i = 0; i < this.boardingStations.size(); i++){
            if(!this.tripLines.get(i).getStations().contains(this.boardingStations.get(i)))
                return false;
        }
        
        return true;
    }
    
    public ArrayList<Station> generateStationsForPathOnLine(MetroLine line, Station station1, Station station2){
        ArrayList<Station> stationsOnPath = new ArrayList<>();
        int station1Index = line.getStations().indexOf(station1);
        int station2Index = line.getStations().indexOf(station2);
        
        if(line.isCircular()){
            if(station1Index >= station2Index){
                int forward = station1Index - station2Index;
                int reverse = station2Index + line.getStations().size() - station1Index;
                
                if(forward < reverse){
                    for(int i = station1Index; i >= station2Index; i--){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
                else{
                    for(int i = station1Index; i < line.getStations().size(); i++){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                    
                    for(int i = 0; i < station2Index; i++){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
            }
            
            else{
                int forward = station2Index - station1Index;
                int reverse = station1Index + line.getStations().size() - station2Index;
                
                if(forward < reverse){
                    for(int i = station1Index; i <= station2Index; i++){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    } 
                }
                else{
                    for(int i = station1Index; i >= 0; i--){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                    for(int i = line.getStations().size()-1; i >= station2Index; i--){
                        Station stationToAdd = line.getStations().get(i);
                        stationsOnPath.add(stationToAdd);
                    }
                }
            }
        }
        
        else{
            if(station1Index >= station2Index){
                for(int i = station1Index; i >= station2Index; i--){
                    Station stationToAdd = line.getStations().get(i);
                    stationsOnPath.add(stationToAdd);
                }
            }
            else{
                for(int i = station1Index; i <= station2Index; i++){
                    Station stationToAdd = line.getStations().get(i);
                    stationsOnPath.add(stationToAdd);
                }
            }
        }
        
        return stationsOnPath;
    }
    
    public boolean hasLine(MetroLine line){
        return this.tripLines.contains(line);
    }
    
    public boolean hasLineWithStation(Station station){
        for(int i = 0; i < this.tripLines.size(); i++){
            if(this.tripLines.get(i).getStations().contains(station))
                return true;
        }
        
        return false;
    }
    
    public int calculateTimeOfTrip(){
        
        ArrayList<Station> stations = this.getTripStations();
        int stationsCost = (stations.size()-1) * this.stationCost;
        int transfersCost = (this.tripLines.size()-1) * this.transferCost;
        return stationsCost + transfersCost;
    }
}

