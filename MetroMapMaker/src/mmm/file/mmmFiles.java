package mmm.file;

import djf.AppTemplate;
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
import djf.ui.AppGUI;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import mmm.data.mmmData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
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
import static mmm.data.Draggable.RECTANGLE;
import static mmm.data.Draggable.TEXT;
import mmm.data.DraggableRectangle;
import mmm.data.DraggableText;
import mmm.data.GridLine;
import mmm.data.MetroLine;
import mmm.data.Station;
import mmm.gui.mapEditController;
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
    static final String JSON_METRO_LINES = "lines";
    static final String JSON_STATIONS_OFF_LINE = "stations_off_line";
    static final String JSON_TOP_LABEL_X = "top_label_x";
    static final String JSON_TOP_LABEL_Y = "top_label_y";
    static final String JSON_BOTTOM_LABEL_X = "bottom_label_x";
    static final String JSON_BOTTOM_LABEL_Y = "bottom_label_y";
    static final String JSON_STATION_NAMES = "station_names";
    static final String JSON_CIRCULAR = "circular";
    static final String JSON_COLOR = "color";
    static final String JSON_STATIONS = "stations";
    static final String JSON_POSITION_NUMBER = "position_number";
    static final String JSON_IS_ROTATED = "is_rotated";
    static final String JSON_BG_IMAGE = "bg_image";
    static final String JSON_IS_CIRCULAR = "is_circular";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    ArrayList<Station> stationsList = new ArrayList<>();
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        
        mmmData dataManager = (mmmData)data;
        mmmWorkspace workspace = dataManager.getWorkspace();
        mapEditController controller = workspace.getController();
        
        if(dataManager.getShapes().get(1) instanceof GridLine){
            controller.processGridRequest();
            workspace.getToggleGrid().setSelected(false);
        }
        
        Color bgColor = dataManager.getBackgroundColor();
	JsonObject bgColorJson = makeJsonColorObject(bgColor);
        JsonObject bgImageJson = makeImageViewObject(dataManager);
        
        JsonArrayBuilder metroLinesBuilder = Json.createArrayBuilder();
	ArrayList<MetroLine> metroLines = dataManager.getMetroLines();
        
        Shape selectedShape = dataManager.getSelectedShape();
        if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                if(!dataManager.getShapes().contains(line.getTopLabel())){
                    line.getLines().get(0).startXProperty().unbind();
                    line.getLines().get(0).startYProperty().unbind();
                    DraggableText topLabel = line.getTopLabel();
                    topLabel.setX(((Station)selectedShape).getCenterX());
                    topLabel.setY(((Station)selectedShape).getCenterY());
                    line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                    line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                    line.getStations().remove(0);
                    dataManager.removeShape(selectedShape);
                    dataManager.addShape(topLabel);
                }
                
                else if(!dataManager.getShapes().contains(line.getBottomLabel())){
                    line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                    line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                    DraggableText bottomLabel = line.getBottomLabel();
                    bottomLabel.setX(((Station)selectedShape).getCenterX());
                    bottomLabel.setY(((Station)selectedShape).getCenterY());
                    line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                    line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                    line.getStations().remove(line.getStations().size()-1);
                    dataManager.removeShape(selectedShape);
                    dataManager.addShape(bottomLabel);
                }
            }
        
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
                        .add(JSON_POSITION_NUMBER, (double)s.getPositionNumber())
                        .add(JSON_IS_ROTATED, (double)s.getIsRotated())
                        .add(JSON_HEIGHT, radiusY).build();
                stationsOnLineArray.add(stationJson);           
            }
            double topLabelX = i.getTopLabel().getX();
            double topLabelY = i.getTopLabel().getY();
            double bottomLabelX = i.getBottomLabel().getX();
            double bottomLabelY = i.getBottomLabel().getY();
            double isCircular = 0;
            if(i.isCircular())
                isCircular = 1;
            JsonObject metroLineJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_IS_CIRCULAR, isCircular)
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
        JsonArrayBuilder shapesArray = Json.createArrayBuilder();
        for(Node node : shapes){
            
            if(node instanceof ImageView){
                
            }
            
            else if(node instanceof Station && ((Station)node).getMetroLines().isEmpty()){
                
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
                    .add(JSON_POSITION_NUMBER, (double)s.getPositionNumber())
                    .add(JSON_IS_ROTATED, (double)s.getIsRotated())
                    .add(JSON_HEIGHT, radiusY).build();  
                stationsOffLineArray.add(stationJson);
            }
            
            else if((Shape)node instanceof DraggableRectangle){
                DraggableRectangle draggableShape = (DraggableRectangle)node;
                String type = draggableShape.getShapeType();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                double width = draggableShape.getWidth();
                double height = draggableShape.getHeight();
                JsonObject outlineColorJson = makeJsonColorObject((Color)draggableShape.getStroke());
                double outlineThickness = draggableShape.getStrokeWidth();

                JsonObject shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_IMAGE_FILL, draggableShape.getImageString())
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
                shapesArray.add(shapeJson);
            }
            
            else if((Shape)node instanceof DraggableText && ((DraggableText)node).getMetroLine() == null){
                DraggableText text = (DraggableText)node;
                double x = text.getX();
                double y = text.getY();
                String type = text.getShapeType();
                JsonObject textObj = makeJsonTextObject(text); 

                JsonObject shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_TEXT_OBJECT, textObj).build();
                        
                shapesArray.add(shapeJson);
            }
        }
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_BG_IMAGE, bgImageJson)
                .add(JSON_METRO_LINES, metroLinesBuilder)
                .add(JSON_SHAPES, shapesArray)
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
    
    private JsonObject makeJsonTextObject(DraggableText text){
        String bold;
        String italicized;
        
        if(text.isItalicized()){
            italicized = "true";
        }
        else{
            italicized = "false";
        }
        if(text.isBolded()){
            bold = "true";
        }
        else
            bold = "false";
        JsonObject textObject = Json.createObjectBuilder()
                .add(JSON_TEXT, text.getText())
                .add(JSON_TEXT_FONT_SIZE, text.getFontSize())
                .add(JSON_TEXT_FONT_STYLE, text.getFontStyle())
                .add(JSON_TEXT_FONT_ITALICIZED, italicized)
                .add(JSON_TEXT_FONT_BOLDED, bold).build();
        return textObject;
    }
    
    private JsonObject makeImageViewObject(mmmData dataManager){
        if(dataManager.getShapes().get(0) instanceof ImageView){
            ImageView iv = (ImageView)dataManager.getShapes().get(0);
            String path = dataManager.getImageString();
            double x = iv.getX();
            double y = iv.getY();
            JsonObject imageViewObject = Json.createObjectBuilder()
                    .add(JSON_IMAGE_FILL, path)
                    .add(JSON_X, x)
                    .add(JSON_Y, y).build();
            return imageViewObject;
        }
        
        JsonObject imageViewObject = Json.createObjectBuilder()
                .add(JSON_IMAGE_FILL, "").build();
        return imageViewObject;
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
        dataManager.resetData();
        mmmWorkspace workspace = dataManager.getWorkspace();
	
        
        JsonObject json = loadJSONFile(filePath);
        
        JsonObject bgImage = json.getJsonObject(JSON_BG_IMAGE);
        if(!bgImage.getString(JSON_IMAGE_FILL).equals("")){
            Image i = new Image(bgImage.getString(JSON_IMAGE_FILL));
            ImageView v = new ImageView(i);
            v.setX(getDataAsDouble(bgImage, JSON_X));
            v.setY(getDataAsDouble(bgImage, JSON_Y));
            dataManager.getShapes().add(v);
            dataManager.setImageString(bgImage.getString(JSON_IMAGE_FILL));
        }
        
        Color bgColor = loadColor(json, JSON_BG_COLOR);
	dataManager.setBackgroundColor(bgColor);
        workspace.getBackgroundColorPicker().setValue(bgColor);
        
        JsonArray jsonMetroLines = json.getJsonArray(JSON_METRO_LINES);
        for(int i = 0; i < jsonMetroLines.size(); i++){
            JsonObject jsonMetroLine = jsonMetroLines.getJsonObject(i);
            MetroLine line = loadMetroLine(jsonMetroLine, dataManager);
            line.getTopLabel().setMetroLine(line);
            line.getBottomLabel().setMetroLine(line);
            workspace.setLineBox(line.getName());
            line.getLines().remove(0);
            String name = line.getName();
            dataManager.addShape(line.getTopLabel());
            dataManager.addShape(line.getBottomLabel());
            dataManager.addMetroLine(line);
            dataManager.getMetroLines().remove(dataManager.getMetroLines().size()-1);
            
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
            
            if(line.isCircular()){
                    Line endLine = line.getLines().get(line.getLines().size()-1);
                    endLine.endXProperty().bind(line.getTopLabel().xProperty().add(line.getName().length()*10 + 30));
                    endLine.endYProperty().bind(line.getTopLabel().yProperty().subtract(5));
                    dataManager.getShapes().remove(line.getBottomLabel());
                }
               
        }
        
        for(int j = 0; j < stationsList.size(); j++){
                dataManager.addShape(stationsList.get(j));
                dataManager.addShape(stationsList.get(j).getLabel());
                workspace.setStationBox(stationsList.get(j).getName());
                workspace.getRouteBox1().getItems().add(stationsList.get(j).getName());
                workspace.getRouteBox2().getItems().add(stationsList.get(j).getName());
            }
        
        JsonArray stationsOffLine = json.getJsonArray(JSON_STATIONS_OFF_LINE);
        for(int m = 0; m < stationsOffLine.size(); m++){
            JsonObject stationJson = stationsOffLine.getJsonObject(m);
            Station s = loadStation(stationJson, dataManager);
            dataManager.addShape(s);
            dataManager.addShape(s.getLabel());
            workspace.setStationBox(s.getName());
            workspace.getRouteBox1().getItems().add(s.getName());
            workspace.getRouteBox2().getItems().add(s.getName());
        }
        
        stationsList = new ArrayList<>();
        
        JsonArray jsonShapeArray = json.getJsonArray(JSON_SHAPES);
	for (int i = 0; i < jsonShapeArray.size(); i++) {
	    JsonObject jsonShape = jsonShapeArray.getJsonObject(i);
	    Shape shape = loadShape(jsonShape, dataManager);
	    dataManager.addShape(shape);
	}
    }
    
    private Shape loadShape(JsonObject jsonShape, mmmData data) {
	// FIRST BUILD THE PROPER SHAPE TYPE
	String type = jsonShape.getString(JSON_TYPE);
	Shape shape = null;
	if (type.equals(RECTANGLE)) {

                DraggableRectangle rect = new DraggableRectangle();
                
                Image image = new Image(jsonShape.getString(JSON_IMAGE_FILL));
                ImagePattern ip = new ImagePattern(image);
                rect.setFill(ip);
                rect.setImageString(jsonShape.getString(JSON_IMAGE_FILL));
                Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
                double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
                rect.setStroke(outlineColor);
                rect.setStrokeWidth(outlineThickness);

                // AND THEN ITS DRAGGABLE PROPERTIES
                double x = getDataAsDouble(jsonShape, JSON_X);
                double y = getDataAsDouble(jsonShape, JSON_Y);
                double width = getDataAsDouble(jsonShape, JSON_WIDTH);
                double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
                //Draggable draggableShape = (Draggable)shape;
                rect.setLocationAndSize(x, y, width, height);

                // ALL DONE, RETURN IT
                shape = (Shape)rect;
                data.getImageShapes().add(shape);
                return shape;
        }
        
        if(type.equals(TEXT)){
            
            shape = loadText(jsonShape, JSON_TEXT_OBJECT);
            double x = getDataAsDouble(jsonShape, JSON_X);
            double y = getDataAsDouble(jsonShape, JSON_Y);
            DraggableText text = (DraggableText)shape;
            text.setLocationAndSize(x,y,0,0);
            data.addTextShape(text);
            

            // ALL DONE, RETURN IT
            return shape;
        }
        return shape;
    }
    
    private DraggableText loadText(JsonObject json, String textObj){
        JsonObject jsonText = json.getJsonObject(textObj);
        String textString = jsonText.getString(JSON_TEXT);
        double fontSize = getDataAsDouble(jsonText, JSON_TEXT_FONT_SIZE);
        String textStyle = jsonText.getString(JSON_TEXT_FONT_STYLE);
        String boolI = jsonText.getString(JSON_TEXT_FONT_ITALICIZED);
        String boolB = jsonText.getString(JSON_TEXT_FONT_BOLDED);
        boolean isItalicized = Boolean.parseBoolean(boolI);
        boolean isBolded = Boolean.parseBoolean(boolB);
        
        DraggableText text = new DraggableText(textStyle, fontSize);
        text.setText(textString);
        
        if(!isBolded && !isItalicized){
            text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.REGULAR, fontSize));
            text.setBolded(false);
            text.setItalicized(false);
        }

        else if(!isBolded && isItalicized){
            text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.ITALIC, fontSize));
            text.setBolded(false);
            text.setItalicized(true);
        }

        else if(isBolded && !isItalicized){
            text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.REGULAR, fontSize));
            text.setBolded(true);
            text.setItalicized(false);
        }

        else if(isBolded && isItalicized){
            text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.ITALIC, fontSize));
            text.setBolded(true);
            text.setItalicized(true);
        }
        
        return text;
    }
    
    private MetroLine loadMetroLine(JsonObject obj, mmmData data){
        
        boolean isInList = false;
        mmmData dataManager = (mmmData)data;
        String name = obj.getString(JSON_NAME);
        double isCircular = getDataAsDouble(obj, JSON_IS_CIRCULAR);
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
            else
                isInList = false;
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
        metroLine.setIsCircular(isCircular == 1);
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
        int positionNumber = (int)getDataAsDouble(obj, JSON_POSITION_NUMBER);
        int isRotated = (int)getDataAsDouble(obj, JSON_IS_ROTATED);
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
        
        if(isRotated == 0){
                text.setRotate(0);
                s.setIsRotated(0);
            }
            
        else{
            text.setRotate(270);
            s.setIsRotated(1);
        }
        
        if(positionNumber == 0){
            text.xProperty().bind(s.centerXProperty().add(20 + s.getRadiusX()));
            text.yProperty().bind(s.centerYProperty().subtract(10 + s.getRadiusY()));
            s.setPositionNumber(0);

           }
           
           else if(positionNumber == 1){
               text.xProperty().bind(s.centerXProperty().subtract((10 + s.getRadiusX())+(7*text.getText().length())));
                text.yProperty().bind(s.centerYProperty().subtract(10 + s.getRadiusY()));
                s.setPositionNumber(1);
               
           }
           
           else if(positionNumber == 2){
               text.xProperty().bind(s.centerXProperty().subtract((10 + s.getRadiusX())+(7*text.getText().length())));
               text.yProperty().bind(s.centerYProperty().add(10 + s.getRadiusY()));
               s.setPositionNumber(2);
               
           }
           
           else if(positionNumber == 3){
               text.xProperty().bind(s.centerXProperty().add(20 + s.getRadiusX()));
               text.yProperty().bind(s.centerYProperty().add(10 + s.getRadiusY()));
               s.setPositionNumber(3);
           }
        
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
    public void exportData(AppDataComponent data, String filePath, AppTemplate app) throws IOException {
        
        mmmData dataManager = (mmmData)data;
        AppGUI gui = app.getGUI();
        mmmWorkspace workspace = dataManager.getWorkspace();
        mapEditController controller = workspace.getController();
        if(gui.getCurrentFile() != null){
            
            if(dataManager.getShapes().get(1) instanceof GridLine){
                controller.processGridRequest();
                workspace.getToggleGrid().setSelected(false);
            }
            
            Shape selectedShape = dataManager.getSelectedShape();
            if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                    MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                    if(!dataManager.getShapes().contains(line.getTopLabel())){
                        line.getLines().get(0).startXProperty().unbind();
                        line.getLines().get(0).startYProperty().unbind();
                        DraggableText topLabel = line.getTopLabel();
                        topLabel.setX(((Station)selectedShape).getCenterX());
                        topLabel.setY(((Station)selectedShape).getCenterY());
                        line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                        line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                        line.getStations().remove(0);
                        dataManager.removeShape(selectedShape);
                        dataManager.addShape(topLabel);
                    }

                    else if(!dataManager.getShapes().contains(line.getBottomLabel())){
                        line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                        line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                        DraggableText bottomLabel = line.getBottomLabel();
                        bottomLabel.setX(((Station)selectedShape).getCenterX());
                        bottomLabel.setY(((Station)selectedShape).getCenterY());
                        line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                        line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                        line.getStations().remove(line.getStations().size()-1);
                        dataManager.removeShape(selectedShape);
                        dataManager.addShape(bottomLabel);
                    }
            }
            
            Pane canvas = workspace.getCenterPane();
            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
            File file = new File(filePath + "/" + gui.getCurrentFile().getName() + ".png");
            workspace.getWorkspace().toBack();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
            
            
            JsonArrayBuilder stationsArray = Json.createArrayBuilder();
            JsonArrayBuilder linesArray = Json.createArrayBuilder();
            
            for(int i = 0; i < dataManager.getMetroLines().size(); i++){
                for(int j = 0; j < dataManager.getMetroLines().get(i).getStations().size(); j++){
                    stationsArray.add(dataManager.getMetroLines().get(i).getStations().get(j).getName());
                }
                
                String lineName = dataManager.getMetroLines().get(i).getName();
                boolean isCircular = dataManager.getMetroLines().get(i).isCircular();
                Color lineColor = (Color)dataManager.getMetroLines().get(i).getLines().get(0).getStroke();
                JsonObject lineColorJson = makeJsonColorObject(lineColor);
                JsonObject lineObject = Json.createObjectBuilder()
                        .add(JSON_NAME, lineName)
                        .add(JSON_CIRCULAR, isCircular)
                        .add(JSON_COLOR, lineColorJson)
                        .add(JSON_STATION_NAMES, stationsArray).build();
                linesArray.add(lineObject);
                
                stationsArray = Json.createArrayBuilder();
            }
            
            for(int i = 0; i < dataManager.getShapes().size(); i++){
                if(dataManager.getShapes().get(i) instanceof Station){
                    Station station = (Station)dataManager.getShapes().get(i);
                    String name = station.getName();
                    double x = station.getCenterX();
                    double y = station.getCenterY();
                    JsonObject stationJson = Json.createObjectBuilder()
                            .add(JSON_NAME, name)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
                    stationsArray.add(stationJson);
                }
            }
            
            String projectName = gui.getCurrentFile().getName();
            JsonObject projectJson = Json.createObjectBuilder()
                    .add(JSON_NAME, projectName)
                    .add(JSON_METRO_LINES, linesArray)
                    .add(JSON_STATIONS, stationsArray).build();
            
            
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = writerFactory.createWriter(sw);
            jsonWriter.writeObject(projectJson);
            jsonWriter.close();

            // INIT THE WRITER
            OutputStream os = new FileOutputStream(filePath + "/" + gui.getCurrentFile().getName());
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(projectJson);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(filePath + "/" + gui.getCurrentFile().getName());
            pw.write(prettyPrinted);
            pw.close();  
        }
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
    
}