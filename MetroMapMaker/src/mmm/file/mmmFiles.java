package mmm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import java.math.BigDecimal;
import java.util.ArrayList;
import mmm.data.mmmData;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import mmm.data.Draggable;
import mmm.data.DraggableText;
import mmm.data.MetroLine;
import mmm.data.Station;
import mmm.gui.mmmWorkspace;

/**
 *
 * @author brendan
 */
public class mmmFiles implements AppFileComponent{
    
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    static final String JSON_IMAGE_FILL = "image_fill";
    static final String JSON_TEXT = "text";
    static final String JSON_TEXT_FONT_SIZE = "font_size";
    static final String JSON_TEXT_FONT_STYLE = "font_style";
    static final String JSON_TEXT_FONT_ITALICIZED = "italicized";
    static final String JSON_TEXT_FONT_BOLDED = "bolded";
    static final String JSON_TEXT_OBJECT = "text_object";
    static final String JSON_LINES_ARRAY = "lines_array";
    static final String JSON_STATIONS_ARRAY = "stations_array";
    static final String JSON_NAME = "name";
    static final String JSON_METRO_LINES = "metro_lines";
    static final String JSON_STATIONS_OFF_LINE = "stations_off_line";
    static final String JSON_TOP_LABEL_X = "top_label_x";
    static final String JSON_TOP_LABEL_Y = "top_label_y";
    static final String JSON_BOTTOM_LABEL_X = "bottom_label_x";
    static final String JSON_BOTTOM_LABEL_Y = "bottom_label_y";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    ArrayList<Station> stationsList = new ArrayList<>();
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        
        mmmData dataManager = (mmmData)data;
        
        Color bgColor = dataManager.getBackgroundColor();
	JsonObject bgColorJson = makeJsonColorObject(bgColor);
        
        JsonArrayBuilder metroLinesBuilder = Json.createArrayBuilder();
	ArrayList<MetroLine> metroLines = dataManager.getMetroLines();
        
        for(MetroLine i : metroLines){
            
            String name = i.getName();
            JsonArrayBuilder linesArray = Json.createArrayBuilder();
            JsonArrayBuilder stationsOnLineArray = Json.createArrayBuilder();
            
            for(Line l : i.getLines()){
                
                JsonObject colorJson = makeJsonColorObject((Color)l.getStroke());
                double outlineThicknessJson = l.getStrokeWidth();
                JsonObject lineJson = Json.createObjectBuilder()
                    .add(JSON_FILL_COLOR, colorJson)
                    .add(JSON_OUTLINE_THICKNESS, outlineThicknessJson).build();
                linesArray.add(lineJson);
            }
            
            for(Station s : i.getStations()){
                
                JsonObject colorJson = makeJsonColorObject((Color)s.getFill());
                double outlineThicknessJson = s.getStrokeWidth();
                double centerX = s.getCenterX();
                double centerY = s.getCenterY();
                double radiusX = s.getRadiusX();
                double radiusY = s.getRadiusY();
                String stationsName = s.getName();
                JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_NAME, stationsName)
                        .add(JSON_FILL_COLOR, colorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThicknessJson)
                        .add(JSON_X, centerX)
                        .add(JSON_Y, centerY)
                        .add(JSON_WIDTH, radiusX)
                        .add(JSON_HEIGHT, radiusY).build();
                stationsOnLineArray.add(stationJson);           
            }
            double topLabelX = i.getTopLabel().getX();
            double topLabelY = i.getTopLabel().getY();
            double bottomLabelX = i.getBottomLabel().getX();
            double bottomLabelY = i.getBottomLabel().getY();
            
            JsonObject metroLineJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_TOP_LABEL_X, topLabelX)
                    .add(JSON_TOP_LABEL_Y, topLabelY)
                    .add(JSON_BOTTOM_LABEL_X, bottomLabelX)
                    .add(JSON_BOTTOM_LABEL_Y, bottomLabelY)
                    .add(JSON_LINES_ARRAY,linesArray)
                    .add(JSON_STATIONS_ARRAY,stationsOnLineArray).build();
            metroLinesBuilder.add(metroLineJson);
        }
        
        ObservableList<Node> shapes = dataManager.getShapes();
        JsonArrayBuilder stationsOffLineArray = Json.createArrayBuilder();
        for(Node node : shapes){
            
            if(node instanceof Station && ((Station)node).getMetroLines().isEmpty()){
                
                Station s = (Station)node;
                JsonObject colorJson = makeJsonColorObject((Color)s.getFill());
                double outlineThicknessJson = s.getStrokeWidth();
                double centerX = s.getCenterX();
                double centerY = s.getCenterY();
                double radiusX = s.getRadiusX();
                double radiusY = s.getRadiusY();
                String stationsName = s.getName();
                
                JsonObject stationJson = Json.createObjectBuilder()
                    .add(JSON_NAME, stationsName)
                    .add(JSON_FILL_COLOR, colorJson)
                    .add(JSON_OUTLINE_THICKNESS, outlineThicknessJson)
                    .add(JSON_X, centerX)
                    .add(JSON_Y, centerY)
                    .add(JSON_WIDTH, radiusX)
                    .add(JSON_HEIGHT, radiusY).build();  
                stationsOffLineArray.add(stationJson);
            }
        }
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_METRO_LINES, metroLinesBuilder)
                .add(JSON_STATIONS_OFF_LINE, stationsOffLineArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    
    private JsonObject makeJsonColorObject(Color color) {
	JsonObject colorJson = Json.createObjectBuilder()
		.add(JSON_RED, color.getRed())
		.add(JSON_GREEN, color.getGreen())
		.add(JSON_BLUE, color.getBlue())
		.add(JSON_ALPHA, color.getOpacity()).build();
	return colorJson;
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        
        mmmData dataManager = (mmmData)data;
        mmmWorkspace workspace = dataManager.getWorkspace();
	dataManager.resetData();
        
        JsonObject json = loadJSONFile(filePath);
        
        Color bgColor = loadColor(json, JSON_BG_COLOR);
	dataManager.setBackgroundColor(bgColor);
        
        JsonArray jsonMetroLines = json.getJsonArray(JSON_METRO_LINES);
        for(int i = 0; i < jsonMetroLines.size(); i++){
            JsonObject jsonMetroLine = jsonMetroLines.getJsonObject(i);
            MetroLine line = loadMetroLine(jsonMetroLine, dataManager);
            workspace.setLineBox(line.getName());
            line.getLines().remove(0);
            String name = line.getName();
            dataManager.addShape(line.getTopLabel());
            dataManager.addShape(line.getBottomLabel());
            
            for(int x = 0; x < line.getLines().size(); x++){
                
                if(x == 0){
                    if(x == line.getLines().size()-1){
                        line.getLines().get(0).startXProperty().bind(line.getTopLabel().xProperty().add(name.length()*10 + 30));
                        line.getLines().get(0).startYProperty().bind(line.getTopLabel().yProperty().subtract(5));
                        line.getLines().get(0).endXProperty().bind(line.getBottomLabel().xProperty().subtract( 20));
                        line.getLines().get(0).endYProperty().bind(line.getBottomLabel().yProperty().subtract(5));
                    }
                    else{
                        line.getLines().get(0).startXProperty().bind(line.getTopLabel().xProperty().add(name.length()*10 + 30));
                        line.getLines().get(0).startYProperty().bind(line.getTopLabel().yProperty().subtract(5));
                        line.getLines().get(0).endXProperty().bind(line.getStations().get(0).centerXProperty());
                        line.getLines().get(0).endYProperty().bind(line.getStations().get(0).centerYProperty());
                    }
                }
                
                else if(x == line.getLines().size()-1){
                    line.getLines().get(x).startXProperty().bind(line.getStations().get(x-1).centerXProperty());
                    line.getLines().get(x).startYProperty().bind(line.getStations().get(x-1).centerYProperty());
                    line.getLines().get(x).endXProperty().bind(line.getBottomLabel().xProperty().subtract( 20));
                    line.getLines().get(x).endYProperty().bind(line.getBottomLabel().yProperty().subtract(5));
                }
                
                else{
                    line.getLines().get(x).startXProperty().bind(line.getStations().get(x-1).centerXProperty());
                    line.getLines().get(x).startYProperty().bind(line.getStations().get(x-1).centerYProperty());
                    line.getLines().get(x).endXProperty().bind(line.getStations().get(x).centerXProperty());
                    line.getLines().get(x).endYProperty().bind(line.getStations().get(x).centerYProperty());
                }
                
                dataManager.addShape(line.getLines().get(x));
            }
               
        }
        
        for(int j = 0; j < stationsList.size(); j++){
                dataManager.addShape(stationsList.get(j));
                dataManager.addShape(stationsList.get(j).getLabel());
                workspace.setStationBox(stationsList.get(j).getName());
            }
        
        JsonArray stationsOffLine = json.getJsonArray(JSON_STATIONS_OFF_LINE);
        for(int m = 0; m < stationsOffLine.size(); m++){
            JsonObject stationJson = stationsOffLine.getJsonObject(m);
            Station s = loadStation(stationJson, dataManager);
            dataManager.addShape(s);
            dataManager.addShape(s.getLabel());
            workspace.setStationBox(s.getName());
        }
    }
    
    private MetroLine loadMetroLine(JsonObject obj, mmmData data){
        
        boolean isInList = false;
        mmmData dataManager = (mmmData)data;
        String name = obj.getString(JSON_NAME);
        MetroLine metroLine = new MetroLine(name);
        JsonArray lineArray = obj.getJsonArray(JSON_LINES_ARRAY);
        for(int i = 0; i < lineArray.size(); i++){
            Line line = loadLine(lineArray.getJsonObject(i), data);
            metroLine.addLine(line);
        }
        
        JsonArray stationArray = obj.getJsonArray(JSON_STATIONS_ARRAY);
        for(int j = 0; j < stationArray.size(); j++){
            Station s = loadStation(stationArray.getJsonObject(j), data);
            for(int l = 0; l < stationsList.size(); l++){
                if(s.getName().equals(stationsList.get(l).getName())){
                    stationsList.get(l).addMetroLine(metroLine);
                    metroLine.addStation(stationsList.get(l));
                    isInList = true;
                }
            }
            if(!isInList){
                s.addMetroLine(metroLine);
                metroLine.addStation(s);
                stationsList.add(s);
            }
        }
        
        DraggableText topLabel = new DraggableText(name, 18);
        DraggableText bottomLabel = new DraggableText(name, 18);
        topLabel.setText(name);
        topLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        bottomLabel.setText(name);
        bottomLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        double topLabelX = getDataAsDouble(obj, JSON_TOP_LABEL_X);
        double topLabelY = getDataAsDouble(obj, JSON_TOP_LABEL_Y);
        double bottomLabelX = getDataAsDouble(obj, JSON_BOTTOM_LABEL_X);
        double bottomLabelY = getDataAsDouble(obj, JSON_BOTTOM_LABEL_Y);
        topLabel.setX(topLabelX);
        topLabel.setY(topLabelY);
        bottomLabel.setX(bottomLabelX);
        bottomLabel.setY(bottomLabelY);
        metroLine.addTopLabel(topLabel);
        metroLine.addBottomLabel(bottomLabel);
        dataManager.addMetroLine(metroLine);
        return metroLine;
    }
    
    private Line loadLine(JsonObject obj, mmmData data){
        
        Line l = new Line();
        
        Color stroke = loadColor(obj, JSON_FILL_COLOR);
        double outlineThickness = getDataAsDouble(obj, JSON_OUTLINE_THICKNESS);
        
        l.setStroke(stroke);
        l.setStrokeWidth(outlineThickness);
        return l;
    }
    
    private Station loadStation(JsonObject obj, mmmData data){
        
        String name = obj.getString(JSON_NAME);
        Station s = new Station(name);
        
        Color fill = loadColor(obj, JSON_FILL_COLOR);
        double outlineThickness = getDataAsDouble(obj, JSON_OUTLINE_THICKNESS);
        double x = getDataAsDouble(obj, JSON_X);
        double y = getDataAsDouble(obj, JSON_Y);
        double width = getDataAsDouble(obj, JSON_WIDTH);
        double height = getDataAsDouble(obj, JSON_HEIGHT);
        Text text = new Text();
        text.setText(name);
        text.setFont(Font.font("System", 14));
        
        s.setFill(fill);
        s.setStrokeWidth(outlineThickness);
        s.setCenterX(x);
        s.setCenterY(y);
        s.setRadiusX(width);
        s.setRadiusY(height);
        s.setLabel(text);
        text.xProperty().bind(s.centerXProperty().add(30));
        text.yProperty().bind(s.centerYProperty().subtract(20));
        
        return s;
    }
    
    private double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }
    
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        // WE ARE NOT USING THIS, THOUGH PERHAPS WE COULD FOR EXPORTING
        // IMAGES TO VARIOUS FORMATS, SOMETHING OUT OF THE SCOPE
        // OF THIS ASSIGNMENT
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
    
}