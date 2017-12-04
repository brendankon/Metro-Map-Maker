package mmm.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import mmm.data.mmmData;
import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.ABOUT_ICON;
import static djf.settings.AppPropertyType.ABOUT_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_ICON;
import static djf.settings.AppPropertyType.ADD_IMAGE_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_LABEL_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_STATION_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_TO_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.APP_LOGO;
import static djf.settings.AppPropertyType.BOLD_ICON;
import static djf.settings.AppPropertyType.BOLD_TOOLTIP;
import static djf.settings.AppPropertyType.DECREASE_SIZE_ICON;
import static djf.settings.AppPropertyType.DECREASE_SIZE_TOOLTIP;
import static djf.settings.AppPropertyType.EDIT_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.EXPORT_ICON;
import static djf.settings.AppPropertyType.EXPORT_TOOLTIP;
import static djf.settings.AppPropertyType.FIND_ROUTE_ICON;
import static djf.settings.AppPropertyType.FIND_ROUTE_TOOLTIP;
import static djf.settings.AppPropertyType.INCREASE_SIZE_ICON;
import static djf.settings.AppPropertyType.INCREASE_SIZE_TOOLTIP;
import static djf.settings.AppPropertyType.ITALIC_ICON;
import static djf.settings.AppPropertyType.ITALIC_TOOLTIP;
import static djf.settings.AppPropertyType.LIST_ICON;
import static djf.settings.AppPropertyType.LIST_TOOLTIP;
import static djf.settings.AppPropertyType.MOVE_LABEL_TOOLTIP;
import static djf.settings.AppPropertyType.REDO_ICON;
import static djf.settings.AppPropertyType.REDO_TOOLTIP;
import static djf.settings.AppPropertyType.REMOVE_ICON;
import static djf.settings.AppPropertyType.REMOVE_TOOLTIP;
import static djf.settings.AppPropertyType.ROTATE_ICON;
import static djf.settings.AppPropertyType.ROTATE_TOOLTIP;
import static djf.settings.AppPropertyType.SAVE_AS_ICON;
import static djf.settings.AppPropertyType.SAVE_AS_TOOLTIP;
import static djf.settings.AppPropertyType.SET_IMG_BACK_TOOLTIP;
import static djf.settings.AppPropertyType.SNAP_TOOLTIP;
import static djf.settings.AppPropertyType.UNDO_ICON;
import static djf.settings.AppPropertyType.UNDO_TOOLTIP;
import static djf.settings.AppPropertyType.ZOOM_IN_ICON;
import static djf.settings.AppPropertyType.ZOOM_IN_TOOLTIP;
import static djf.settings.AppPropertyType.ZOOM_OUT_ICON;
import static djf.settings.AppPropertyType.ZOOM_OUT_TOOLTIP;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_EXPORT;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.settings.AppStartupConstants.PATH_WORK;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import static mmm.css.mmmStyle.CLASS_BUTTON;
import static mmm.css.mmmStyle.CLASS_COLOR_CHOOSER_CONTROL;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR_ROW;
import static mmm.css.mmmStyle.CLASS_MULTI_HBOX_ROW;
import static mmm.css.mmmStyle.CLASS_NAV_BOTTOM_ROW;
import static mmm.css.mmmStyle.CLASS_NAV_LABEL;
import static mmm.css.mmmStyle.CLASS_RENDER_CANVAS;
import static mmm.css.mmmStyle.CLASS_ROUTE_ROW;
import mmm.data.DraggableText;
import mmm.data.MetroLine;
import mmm.data.Station;

/**
 *
 * @author brendan
 */
@SuppressWarnings("unchecked")
public class mmmWorkspace extends AppWorkspaceComponent{
    
    Pane centerPane;
    AppTemplate app;
    AppGUI gui;
    VBox editToolbar;
    FlowPane undoRedoToolbar;
    FlowPane aboutToolbar;
    public Button undoButton;
    public Button redoButton;
    Button exportButton;
    Button aboutButton;
    Button saveAs;
    
    HBox row1Top;
    HBox row1Bottom;
    VBox row1VBox;
    Label linesLabel;
    ComboBox<String> lineBox;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLineButton;
    Button removeStationFromLineButton;
    Button listStationsButton;
    Button editLineButton;
    Slider lineThicknessSlider;
    
    HBox row2Top;
    HBox row2Bottom;
    VBox row2VBox;
    Label stationsLabel;
    ComboBox<String> stationsBox;
    Button addStationButton;
    Button removeStationButton;
    Button snapButton;
    Button moveLabelButton;
    Button rotateButton;
    Slider stationRadiusSlider;
    ColorPicker stationColorPicker;
    
    HBox row3Top;
    HBox row3Bottom;
    VBox row3VBox;
    Label routeLabel;
    ComboBox<String> routeBox1;
    ComboBox<String> routeBox2;
    Button findRouteButton;
    
    HBox row4Top;
    HBox row4Bottom;
    VBox row4VBox;
    Label decorLabel;
    Button setImgBackgroundButton;
    ColorPicker setBackgroundColorPicker;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
    
    HBox row5Top;
    HBox row5Bottom;
    VBox row5VBox;
    Label fontLabel;
    Button boldButton;
    Button italicsButton;
    ComboBox fontSizeBox;
    ComboBox fontStyleBox;
    ColorPicker fontColorPicker;
    
    HBox row6Top;
    HBox row6Bottom;
    VBox row6VBox;
    Label navLabel;
    CheckBox toggleGrid;
    Button zoomInButton;
    Button zoomOutButton;
    Button increaseMapSize;
    Button decreaseMapSize;
    Label gridLabel;
    
    public int redoCounter;
    boolean startDrag = false;
    int startOutline;
    Pane canvas;
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    CanvasController canvasController;
    
    mapEditController controller;
    
    
    
    
    public mmmWorkspace(AppTemplate initApp) {
        
        redoCounter = 0;
        app = initApp;
        gui = app.getGUI();
        
        initLayout();
        
        initStyle();
        
    }
    
    public void initLayout(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FlowPane fileToolbar = gui.getFileToolbar();
        FlowPane sp1 = new FlowPane();
        FlowPane sp2 = new FlowPane();
        sp1.setPrefWrapLength(100);
        sp2.setPrefWrapLength(100);
        saveAs = gui.initChildButton(fileToolbar, SAVE_AS_ICON.toString(), SAVE_AS_TOOLTIP.toString(), true);
        exportButton = gui.initChildButton(fileToolbar, EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), false);
        fileToolbar.setPrefWrapLength(235);
        FlowPane topToolBar = gui.getTopToolbarPane();
        undoRedoToolbar = new FlowPane();
        undoButton = gui.initChildButton(undoRedoToolbar, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);
        redoButton = gui.initChildButton(undoRedoToolbar, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
        undoRedoToolbar.setPrefWrapLength(85);
        aboutToolbar = new FlowPane();
        aboutButton = gui.initChildButton(aboutToolbar, ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), false);
        aboutToolbar.setPrefWrapLength(40);
        topToolBar.getChildren().add(sp1);
        topToolBar.getChildren().add(undoRedoToolbar);
        topToolBar.getChildren().add(sp2);
        topToolBar.getChildren().add(aboutToolbar);
        
        editToolbar = new VBox();
        
        row1Top = new HBox();
        row1Bottom = new HBox();
        row1VBox = new VBox();     
        linesLabel = new Label("Metro Lines");
        row1Top.getChildren().add(linesLabel);
        lineBox = new ComboBox<>();
        lineBox.setMinWidth(140);
        lineBox.setPrefWidth(140);
        lineBox.setMaxWidth(USE_COMPUTED_SIZE);
        row1Top.getChildren().add(lineBox);
        editLineButton = new Button();
        editLineButton.setDisable(false);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(EDIT_LINE_TOOLTIP.toString()));
        editLineButton.setTooltip(buttonTooltip);
        editLineButton.setText("Edit Line");
        editLineButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        editLineButton.setMinSize(70,23);
        editLineButton.setPrefSize(70,23);
        editLineButton.setMaxSize(70,23);
        row1Top.getChildren().add(editLineButton);
        addLineButton = gui.initChildButton(row1Top, ADD_ICON.toString(), ADD_LINE_TOOLTIP.toString(), false);
        removeLineButton = gui.initChildButton(row1Top, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), false);
        addStationToLineButton = new Button();
        addStationToLineButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(ADD_TO_LINE_TOOLTIP.toString()));
        addStationToLineButton.setTooltip(buttonTooltip);
        addStationToLineButton.setText("Add Station");
        addStationToLineButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        addStationToLineButton.setMinSize(85,23);
        addStationToLineButton.setPrefSize(85,23);
        addStationToLineButton.setMaxSize(85,23);
        row1Bottom.getChildren().add(addStationToLineButton);
        removeStationFromLineButton = new Button();
        removeStationFromLineButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(REMOVE_TOOLTIP.toString()));
        removeStationFromLineButton.setTooltip(buttonTooltip);
        removeStationFromLineButton.setText("Remove Station");
        removeStationFromLineButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        removeStationFromLineButton.setMinSize(105,23);
        removeStationFromLineButton.setPrefSize(105,23);
        removeStationFromLineButton.setMaxSize(105,23);
        row1Bottom.getChildren().add(removeStationFromLineButton);
        listStationsButton = gui.initChildButton(row1Bottom, LIST_ICON.toString(), LIST_TOOLTIP.toString(), false);
        lineThicknessSlider = new Slider(1,15,5);
        row1Bottom.getChildren().add(lineThicknessSlider);
        
        row2Top = new HBox();
        row2Bottom = new HBox();
        row2VBox = new VBox();
        stationsLabel = new Label("Metro Stations");
        row2Top.getChildren().add(stationsLabel);
        stationsBox = new ComboBox<>();
        stationsBox.setMinWidth(140);
        stationsBox.setPrefWidth(140);
        stationsBox.setMaxWidth(USE_COMPUTED_SIZE);
        row2Top.getChildren().add(stationsBox);
        addStationButton = gui.initChildButton(row2Top, ADD_ICON.toString(), ADD_STATION_TOOLTIP.toString() , false);
        removeStationButton = gui.initChildButton(row2Top, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString() , false);
        stationColorPicker = new ColorPicker(Color.WHITE);
        row2Bottom.getChildren().add(stationColorPicker);
        snapButton = new Button();
        snapButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(SNAP_TOOLTIP.toString()));
        snapButton.setTooltip(buttonTooltip);
        snapButton.setText("Snap");
        snapButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        snapButton.setMinSize(50,26);
        snapButton.setPrefSize(50,26);
        snapButton.setMaxSize(50,26);
        row2Bottom.getChildren().add(snapButton);
        moveLabelButton = new Button();
        moveLabelButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(MOVE_LABEL_TOOLTIP.toString()));
        moveLabelButton.setTooltip(buttonTooltip);
        moveLabelButton.setText("Move Label");
        moveLabelButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        moveLabelButton.setMinSize(80,26);
        moveLabelButton.setPrefSize(80,26);
        moveLabelButton.setMaxSize(80,26);
        row2Bottom.getChildren().add(moveLabelButton);
        rotateButton = gui.initChildButton(row2Top, ROTATE_ICON.toString(), ROTATE_TOOLTIP.toString() , false);
        stationRadiusSlider = new Slider(5,20,10);
        row2Bottom.getChildren().add(stationRadiusSlider);
        
        row3Top = new HBox();
        row3Bottom = new HBox();
        row3VBox = new VBox();
        routeLabel = new Label("Find Route");
        row3Top.getChildren().add(routeLabel);
        routeBox1 = new ComboBox<>();
        routeBox1.setMinWidth(140);
        routeBox1.setPrefWidth(140);
        routeBox1.setMaxWidth(USE_COMPUTED_SIZE);
        row3Bottom.getChildren().add(routeBox1);
        routeBox2 = new ComboBox<>();
        routeBox2.setMinWidth(140);
        routeBox2.setPrefWidth(140);
        routeBox2.setMaxWidth(USE_COMPUTED_SIZE);
        row3Bottom.getChildren().add(routeBox2);
        findRouteButton = gui.initChildButton(row3Bottom, FIND_ROUTE_ICON.toString(), FIND_ROUTE_TOOLTIP.toString() , false);
        
        row4Top = new HBox();
        row4Bottom = new HBox();
        row4VBox = new VBox();
        decorLabel = new Label("Decor");
        row4Top.getChildren().add(decorLabel);
        setImgBackgroundButton = new Button();
        setImgBackgroundButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(SET_IMG_BACK_TOOLTIP.toString()));
        setImgBackgroundButton.setTooltip(buttonTooltip);
        setImgBackgroundButton.setText("Set Image" + "\n" + "Background");
        setImgBackgroundButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        setImgBackgroundButton.setMinSize(85,38);
        setImgBackgroundButton.setPrefSize(85,38);
        setImgBackgroundButton.setMaxSize(85,38);
        row4Bottom.getChildren().add(setImgBackgroundButton);
        addImageButton = new Button();
        addImageButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(ADD_IMAGE_TOOLTIP.toString()));
        addImageButton.setTooltip(buttonTooltip);
        addImageButton.setText("Add" + "\n" + "Image");
        addImageButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        addImageButton.setMinSize(55,38);
        addImageButton.setPrefSize(55,38);
        addImageButton.setMaxSize(55,38);
        row4Bottom.getChildren().add(addImageButton);
        addLabelButton = new Button();
        addLabelButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(ADD_LABEL_TOOLTIP.toString()));
        addLabelButton.setTooltip(buttonTooltip);
        addLabelButton.setText("Add" + "\n" + "Label");
        addLabelButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        addLabelButton.setMinSize(50,38);
        addLabelButton.setPrefSize(50,38);
        addLabelButton.setMaxSize(50,38);
        row4Bottom.getChildren().add(addLabelButton);
        removeElementButton = new Button();
        removeElementButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(REMOVE_TOOLTIP.toString()));
        removeElementButton.setTooltip(buttonTooltip);
        removeElementButton.setText("Remove" + "\n" + "Element");
        removeElementButton.setFont(Font.font("System", FontWeight.BOLD, 11));
        removeElementButton.setMinSize(65,38);
        removeElementButton.setPrefSize(65,38);
        removeElementButton.setMaxSize(65,38);
        row4Bottom.getChildren().add(removeElementButton);
        setBackgroundColorPicker = new ColorPicker();
        row4Bottom.getChildren().add(setBackgroundColorPicker);
        
        row5Top = new HBox();
        row5Bottom = new HBox();
        row5VBox = new VBox();
        fontLabel = new Label("Font");
        row5Top.getChildren().add(fontLabel);
        boldButton = gui.initChildButton(row5Bottom, BOLD_ICON.toString(), BOLD_TOOLTIP.toString() , false);
        italicsButton = gui.initChildButton(row5Bottom, ITALIC_ICON.toString(), ITALIC_TOOLTIP.toString() , false);
        fontSizeBox = new ComboBox();
        fontSizeBox.getItems().addAll(10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48);
        fontSizeBox.setValue(20);
        row5Bottom.getChildren().add(fontSizeBox);
        fontStyleBox = new ComboBox();
        fontStyleBox.getItems().addAll("Arial", "Courier", "Papyrus", "PT Serif",  "Times New Roman");
        fontStyleBox.setValue("Arial");
        row5Bottom.getChildren().add(fontStyleBox);
        fontColorPicker = new ColorPicker(Color.BLACK);
        row5Bottom.getChildren().add(fontColorPicker);
        
        row6Top = new HBox();
        row6Bottom = new HBox();
        row6VBox = new VBox();
        navLabel = new Label("Navigation");
        row6Top.getChildren().add(navLabel);
        zoomInButton = gui.initChildButton(row6Bottom, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString() , false);
        zoomOutButton = gui.initChildButton(row6Bottom, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        increaseMapSize = gui.initChildButton(row6Bottom, INCREASE_SIZE_ICON.toString(), INCREASE_SIZE_TOOLTIP.toString() , false);
        decreaseMapSize = gui.initChildButton(row6Bottom, DECREASE_SIZE_ICON.toString(), DECREASE_SIZE_TOOLTIP.toString() , false);
        toggleGrid = new CheckBox();
        row6Top.getChildren().add(toggleGrid);
        gridLabel = new Label("Show Grid");
        gridLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        row6Top.getChildren().add(gridLabel);
        
        
        
        row1VBox.getChildren().add(row1Top);
        row1VBox.getChildren().add(row1Bottom);
        editToolbar.getChildren().add(row1VBox);
        
        row2VBox.getChildren().add(row2Top);
        row2VBox.getChildren().add(row2Bottom);
        editToolbar.getChildren().add(row2VBox);
        
        row3VBox.getChildren().add(row3Bottom);
        editToolbar.getChildren().add(row3VBox);
        
        row4VBox.getChildren().add(row4Top);
        row4VBox.getChildren().add(row4Bottom);
        editToolbar.getChildren().add(row4VBox);
        
        row5VBox.getChildren().add(row5Top);
        row5VBox.getChildren().add(row5Bottom);
        editToolbar.getChildren().add(row5VBox);
        
        row6VBox.getChildren().add(row6Top);
        row6VBox.getChildren().add(row6Bottom);
        editToolbar.getChildren().add(row6VBox);

        canvas = new Pane();
        
        canvas.prefHeightProperty().bind(app.getGUI().getPrimaryStage().heightProperty());
        canvas.prefWidthProperty().bind(app.getGUI().getPrimaryStage().widthProperty());
        canvas.setBorder(editToolbar.getBorder());
        centerPane = new Pane();
        centerPane.getChildren().add(canvas);
        centerPane.setBackground(new Background(new BackgroundFill((Paint)Color.WHITE, null, null)));
        centerPane.prefHeightProperty().bind(app.getGUI().getPrimaryStage().heightProperty());
        centerPane.prefWidthProperty().bind(app.getGUI().getPrimaryStage().widthProperty());
        
        workspace = new BorderPane();
        workspace.setBackground(new Background(new BackgroundFill((Paint)Color.WHITE, null, null)));
        ((BorderPane)workspace).setCenter(centerPane);
        ((BorderPane)workspace).setLeft(editToolbar);
        
        controls();
        updateEditToolbar(false, false, false, false, false);
        
        mmmData data = (mmmData)app.getDataComponent();
        data.setShapes(canvas.getChildren());
    }
    
    public Pane getCanvas(){
        return canvas;
    }
    
    public Pane getCenterPane(){
        return centerPane;
    }
    
    public Pane getWorkspace(){
        return workspace;
    }
    
    public CheckBox getToggleGrid(){
        return toggleGrid;
    }
    
    public mapEditController getController(){
        return controller;
    }
    
    public ColorPicker getBackgroundColorPicker(){
        return setBackgroundColorPicker;
    }
    
    public ColorPicker getStationColorPicker(){
        return stationColorPicker;
    }
    
    public void setLineBox(String s){
        lineBox.getItems().add(s);
    }
    
    public void setStationBox(String s){
        stationsBox.getItems().add(s);
    }
    
    public ComboBox getRouteBox1(){
        return routeBox1;
    }
    
    public ComboBox getRouteBox2(){
        return routeBox2;
    }
    
    public void resetLineBox(){
        lineBox.getItems().removeAll(lineBox.getItems());
        lineBox.setValue("");
    }
    
    public void resetStationBox(){
        stationsBox.getItems().removeAll(stationsBox.getItems());
        stationsBox.setValue("");
    }
    
    public void resetRouteBoxes(){
        routeBox1.getItems().removeAll(routeBox1.getItems());
        routeBox1.setValue("");
        routeBox2.getItems().removeAll(routeBox2.getItems());
        routeBox2.setValue("");
    }
    
    public void controls(){
        controller = new mapEditController(app);
        mmmData data = (mmmData)app.getDataComponent();
        
        aboutButton.setOnAction(e ->{
            handleAboutRequest();
        });
        
        exportButton.setOnAction(e ->{
            try {
                handleExportRequest();
            } catch (IOException ex) {
                Logger.getLogger(mmmWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        addLineButton.setOnAction(e ->{
            controller.processNewLineRequest();
        });
        
        addStationButton.setOnAction(e ->{
            controller.processNewStationRequest();
        });
        
        addStationToLineButton.setOnAction(e ->{
            controller.processAddStationToLineRequest();
        });
        
        removeStationFromLineButton.setOnAction(e ->{
            controller.processRemoveStationFromLineRequest();
        });
        
        removeLineButton.setOnAction(e ->{
            controller.processRemoveLineRequest();
        });
        
        removeStationButton.setOnAction(e ->{
           controller.processRemoveStationRequest(); 
        });
        
        editLineButton.setOnAction(e ->{
           controller.processEditLineRequest();
        });
        
        setBackgroundColorPicker.setOnAction(e ->{
           controller.processBackgroundColorRequest(); 
        });
        
        addImageButton.setOnAction(e ->{
            controller.processNewImage();
        });
        
        listStationsButton.setOnAction(e ->{
            controller.processListStationsRequest();
        });
        
        moveLabelButton.setOnAction(e ->{
           controller.processMoveStationLabelRequest();
        });
        
        rotateButton.setOnAction(e ->{
           controller.processRotateRequest();
        });
        
        lineThicknessSlider.valueProperty().addListener(e-> {           
	    controller.processSelectOutlineThickness(lineThicknessSlider);
	});
        lineThicknessSlider.setOnMouseDragged(e->{
            processThicknessSliderPress(lineThicknessSlider);
        });
        lineThicknessSlider.setOnMouseReleased(e->{
            processThicknessSliderRelease(lineThicknessSlider);
        });
        
        stationRadiusSlider.valueProperty().addListener(e-> {           
	    controller.processSelectOutlineThickness(stationRadiusSlider);
	});
        stationRadiusSlider.setOnMouseDragged(e->{
            processThicknessSliderPress(stationRadiusSlider);
        });
        stationRadiusSlider.setOnMouseReleased(e->{
            processThicknessSliderRelease(stationRadiusSlider);
        });
        addLabelButton.setOnAction(e ->{
           controller.processNewText(); 
        });
        stationColorPicker.setOnAction(e ->{
            controller.processStationColorRequest();
        });
        toggleGrid.setOnAction(e ->{
            controller.processGridRequest();
        });
        setImgBackgroundButton.setOnAction(e ->{
           controller.processImageBackgroundRequest(); 
        });
        fontStyleBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
            jTPS j = data.getJTPS();
            jTPS_Transaction transaction = new ChangeText_Transaction(null,null, t1,t, false, false, (DraggableText)data.getSelectedShape(), app, "");
            j.addTransaction(transaction);
            if(data.getJTPS().getTransList().size() == 1){
                redoCounter = 0;
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
        });
        fontSizeBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
            jTPS j = data.getJTPS();
            jTPS_Transaction transaction = new ChangeText_Transaction(t1,t, null,null, false, false, (DraggableText)data.getSelectedShape(), app, "");
            j.addTransaction(transaction);
            if(data.getJTPS().getTransList().size() == 1){
                redoCounter = 0;
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
        });
        boldButton.setOnAction(e->{
            jTPS j = data.getJTPS();
            jTPS_Transaction transaction = new ChangeText_Transaction(null,null, null,null, true, false, (DraggableText)data.getSelectedShape(), app, "");
            j.addTransaction(transaction);
            if(data.getJTPS().getTransList().size() == 1){
                redoCounter = 0;
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
            //data.processBoldRequest();
        });
        italicsButton.setOnAction(e->{
            jTPS j = data.getJTPS();
            jTPS_Transaction transaction = new ChangeText_Transaction(null,null, null,null, false, true, (DraggableText)data.getSelectedShape(), app, "");
            j.addTransaction(transaction);
            if(data.getJTPS().getTransList().size() == 1){
                redoCounter = 0;
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
        });
        removeElementButton.setOnAction(e ->{
            controller.processRemoveSelectedShape();
        });
        zoomInButton.setOnAction(e ->{
           controller.processZoomInRequest(); 
        });
        zoomOutButton.setOnAction(e ->{
           controller.processZoomOutRequest(); 
        });
        snapButton.setOnAction(e ->{
            controller.processSnapToGridRequest();
        });
        
        lineBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue ov, String t, String t1){
                
                if(t1 != null && !t1.equals("")){
                    stationsBox.setValue("");
                    updateEditToolbar(true, false, false, false, false);
                    for(int i = 0; i < data.getMetroLines().size(); i++){
                        if(t1.equals(data.getMetroLines().get(i).getName())){
                            if(data.getSelectedShape() != null){
                                data.unhighlightShape(data.getSelectedShape());
                                
                                Shape selectedShape = data.getSelectedShape();
                                    if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                                        MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                                        if(!data.getShapes().contains(line.getTopLabel())){
                                            line.getLines().get(0).startXProperty().unbind();
                                            line.getLines().get(0).startYProperty().unbind();
                                            DraggableText topLabel = line.getTopLabel();
                                            topLabel.setX(((Station)selectedShape).getCenterX());
                                            topLabel.setY(((Station)selectedShape).getCenterY());
                                            line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                                            line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                                            line.getStations().remove(0);
                                            data.removeShape(selectedShape);
                                            data.addShape(topLabel);
                                        }

                                        else if(!data.getShapes().contains(line.getBottomLabel())){
                                            line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                                            line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                                            DraggableText bottomLabel = line.getBottomLabel();
                                            bottomLabel.setX(((Station)selectedShape).getCenterX());
                                            bottomLabel.setY(((Station)selectedShape).getCenterY());
                                            line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                                            line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                                            line.getStations().remove(line.getStations().size()-1);
                                            data.removeShape(selectedShape);
                                            data.addShape(bottomLabel);
                                        }
                                    }
                            }
                            
                            if(data.getSelectedShape() instanceof Line){
                                for(int k = 0; k < data.getMetroLines().size(); k++){
                                    for(int j = 0; j < data.getMetroLines().get(k).getLines().size(); j++){
                                        if((Line)data.getSelectedShape() == data.getMetroLines().get(k).getLines().get(j)){
                                            data.setSelectedLine(data.getMetroLines().get(k));

                                            for(int l = 0; l < data.getSelectedLine().getLines().size(); l++){
                                                 data.getSelectedLine().getLines().get(l).setEffect(null);
                                            }
                                        }
                                    }
                                }
                            }
                            
                            data.setSelectedShape(data.getMetroLines().get(i).getLines().get(0));
                            data.setSelectedLine(data.getMetroLines().get(i));
                            

                            for(int j = 0; j < data.getMetroLines().get(i).getLines().size(); j++){
                                data.highlightShape(data.getMetroLines().get(i).getLines().get(j));
                            }
                        }
                    }
                }
            }
        });
        
        stationsBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue ov, String t, String t1){
                
                if(t1 != null && !t1.equals("")){
                    lineBox.setValue("");
                    updateEditToolbar(false, true, false, false, false);
                    for(int i = 0; i < data.getShapes().size(); i++){
                        if(data.getShapes().get(i) instanceof Station){
                            if(((Station)data.getShapes().get(i)).getName().equals(t1)){
                                if(data.getSelectedShape() != null){
                                    data.unhighlightShape(data.getSelectedShape());
                                    
                                    Shape selectedShape = data.getSelectedShape();
                                    if(selectedShape instanceof Station && ((Station)selectedShape).isEndLabel()){
                                        MetroLine line = ((Station)selectedShape).getMetroLines().get(0);
                                        if(!data.getShapes().contains(line.getTopLabel())){
                                            line.getLines().get(0).startXProperty().unbind();
                                            line.getLines().get(0).startYProperty().unbind();
                                            DraggableText topLabel = line.getTopLabel();
                                            topLabel.setX(((Station)selectedShape).getCenterX());
                                            topLabel.setY(((Station)selectedShape).getCenterY());
                                            line.getLines().get(0).startXProperty().bind(topLabel.xProperty().add(line.getName().length()*10 + 30));
                                            line.getLines().get(0).startYProperty().bind(topLabel.yProperty().subtract(5));
                                            line.getStations().remove(0);
                                            data.removeShape(selectedShape);
                                            data.addShape(topLabel);
                                        }

                                        else if(!data.getShapes().contains(line.getBottomLabel())){
                                            line.getLines().get(line.getLines().size()-1).endXProperty().unbind();
                                            line.getLines().get(line.getLines().size()-1).endYProperty().unbind();
                                            DraggableText bottomLabel = line.getBottomLabel();
                                            bottomLabel.setX(((Station)selectedShape).getCenterX());
                                            bottomLabel.setY(((Station)selectedShape).getCenterY());
                                            line.getLines().get(line.getLines().size()-1).endXProperty().bind(bottomLabel.xProperty().subtract( 20));
                                            line.getLines().get(line.getLines().size()-1).endYProperty().bind(bottomLabel.yProperty().subtract(5));
                                            line.getStations().remove(line.getStations().size()-1);
                                            data.removeShape(selectedShape);
                                            data.addShape(bottomLabel);
                                        }
                                    }
                                }
                                
                                if(data.getSelectedShape() instanceof Line){
                                    for(int k = 0; k < data.getMetroLines().size(); k++){
                                        for(int j = 0; j < data.getMetroLines().get(k).getLines().size(); j++){
                                            if((Line)data.getSelectedShape() == data.getMetroLines().get(k).getLines().get(j)){
                                                data.setSelectedLine(data.getMetroLines().get(k));

                                                for(int l = 0; l < data.getSelectedLine().getLines().size(); l++){
                                                     data.getSelectedLine().getLines().get(l).setEffect(null);
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                data.setSelectedShape((Station)data.getShapes().get(i));
                                data.highlightShape((Station)data.getShapes().get(i));
                                ((Station)data.getShapes().get(i)).start((int)((Station)data.getShapes().get(i)).getCenterX(), (int)((Station)data.getShapes().get(i)).getCenterY());
                            }
                        }
                    }

                }
            }
        });
        
        undoButton.setOnAction(e->{
            redoButton.setDisable(false);
            mapEditController mapEditController = new mapEditController(app);
            mapEditController.processUndoRequest();
            redoCounter++;
            if((data.getJTPS().getTransactions()) < 0){
                undoButton.setDisable(true);
            }
                
        });
        redoButton.setOnAction(e->{
            mapEditController mapEditController = new mapEditController(app);
            mapEditController.processRedoRequest();
            if(data.getJTPS().getTransactions() > -1)
                undoButton.setDisable(false);
            redoCounter--;
            if(redoCounter == 0){
                redoButton.setDisable(true);
            }
        });
        
        // MAKE THE CANVAS CONTROLLER	
	canvasController = new CanvasController(app);
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
	});
    }
    
    public void processThicknessSliderPress(Slider selectedSlider){
        if(!startDrag){
            startOutline = (int)selectedSlider.getValue();
            startDrag = true;
        }
    }
    
    public void processThicknessSliderRelease(Slider selectedSlider){
        mmmData data = (mmmData)app.getDataComponent();
        
        if(data.getSelectedShape() != null){
            int endOutline = (int)selectedSlider.getValue();
            jTPS j = data.getJTPS();
            jTPS_Transaction transaction = new ChangeOutlineThickness_Transaction(startOutline, endOutline, data.getSelectedShape(), app);
            j.addTransaction(transaction);
            if(data.getJTPS().getTransList().size() == 1){
                redoCounter = 0;
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
            startDrag = false;
        }
    }
    
    public void loadSelectedShapeSettings(Shape shape){

    }
    
    public void initStyle(){
        
        canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        undoRedoToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        aboutToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        
        //row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row1Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row1Bottom.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row1VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row2Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row2Bottom.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row2VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row3Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row3Bottom.getStyleClass().add(CLASS_ROUTE_ROW);
        row3VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row4Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row4Bottom.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row4VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row5Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row5Bottom.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row5VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row6Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row6Bottom.getStyleClass().add(CLASS_NAV_BOTTOM_ROW);
        row6VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        
        linesLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        decorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        fontLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        routeLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        navLabel.getStyleClass().add(CLASS_NAV_LABEL);
        
        stationsLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        
    }
    
    public void handleAboutRequest(){
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        Text aboutText = new Text("Application Name: Metro Map Maker\n"
                                    + "Frameworks Used: DesktopJavaFramework, PropertiesManager, jTPS\n"
                                    + "Developer: Brendan Kondracki (Frameworks by Richard Mckenna)\n"
                                    + "Date: November 2017");
        aboutText.setFont(Font.font("System", FontWeight.BOLD, 18));
        aboutText.setLayoutY(40);
        Image i = new Image(appIcon);
        ImageView v = new ImageView(i);
        v.setLayoutX(160);
        v.setLayoutY(170);
        Pane aboutPane = new Pane();
        aboutPane.getChildren().add(aboutText);
        aboutPane.getChildren().add(v);
        
        Stage aboutStage = new Stage();
        Scene aboutScene = new Scene(aboutPane, 635,500);
        aboutStage.setScene(aboutScene);
        aboutStage.setTitle("About");
        aboutStage.showAndWait();
    }
    
    public void handleExportRequest() throws IOException{
        
        mmmData data = (mmmData)app.getDataComponent();
        if(app.getGUI().getCurrentFile() != null){
            File workDir = new File(PATH_EXPORT);
            File[] files = workDir.listFiles();

            for(int i = 0; i < files.length; i++){
                if(app.getGUI().getCurrentFile().getName().equals(files[i].getName())){
                    app.getFileComponent().exportData(data, files[i].getAbsolutePath(), app);
                }
            }

            Text t = new Text("Map Exported Successfully");
            Button ok = new Button("OK");
            Pane exportPane = new Pane();
            t.setLayoutX(20);
            t.setLayoutY(50);
            ok.setLayoutX(75);
            ok.setLayoutY(75);
            exportPane.getChildren().add(t);
            exportPane.getChildren().add(ok);

            Stage exportStage = new Stage();
            Scene exportScene = new Scene(exportPane, 190,150);
            exportStage.setScene(exportScene);
            exportStage.setTitle("Export");
            exportStage.show();

            ok.setOnAction(e ->{
                exportStage.close();
            });
        }
        
    }
    
    public void processFontSizeRequest(Object t1, Shape shape){

            if(t1 != null){
                mmmData data = (mmmData)app.getDataComponent();
                for(int i = 0; i < data.getTextShapes().size(); i++){
                    if(shape == data.getTextShapes().get(i)){
                        DraggableText text = data.getTextShapes().get(i);
                        //data.getTextShapes().get(i).setFont(Font.font(data.getTextShapes().get(i).getFont().getFamily(), (double)((Integer)t1)));
                        
                        if(!text.isBolded() && !text.isItalicized()){
                            text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.REGULAR, (double)((Integer)t1)));
                            text.setBolded(false);
                            text.setItalicized(false);
                        }

                        else if(!text.isBolded() && text.isItalicized()){
                            text.setFont(Font.font(text.getFontStyle(), FontWeight.NORMAL, FontPosture.ITALIC, (double)((Integer)t1)));
                            text.setBolded(false);
                            text.setItalicized(true);
                        }

                        else if(text.isBolded() && !text.isItalicized()){
                            text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.REGULAR, (double)((Integer)t1)));
                            text.setBolded(true);
                            text.setItalicized(false);
                        }

                        else if(text.isBolded() && text.isItalicized()){
                            text.setFont(Font.font(text.getFontStyle(), FontWeight.BOLD, FontPosture.ITALIC, (double)((Integer)t1)));
                            text.setBolded(true);
                            text.setItalicized(true);
                        }
                        break;
                    }
                }
            }
    }
    
    public void processFontStyleRequest(Object t1, Shape shape){
        
        if (t1 != null) {
            mmmData data = (mmmData)app.getDataComponent();
            for(int i = 0; i < data.getTextShapes().size(); i++){
                if(shape == data.getTextShapes().get(i)){
                    data.getTextShapes().get(i).setFont(Font.font((String)t1, data.getTextShapes().get(i).getFont().getSize()));
                    break;
                }
            }
        }
    }
    
    public void updateEditToolbar(boolean isLine, boolean isStation, boolean isText, boolean isImage, boolean isEndLabel){

            editLineButton.setDisable(!isLine);
            removeLineButton.setDisable(!isLine);
            addStationToLineButton.setDisable(!isLine);
            removeStationFromLineButton.setDisable(!isLine);
            lineThicknessSlider.setDisable(!isLine);
            
            removeStationButton.setDisable(!isStation);
            stationColorPicker.setDisable(!isStation);
            snapButton.setDisable(!(isStation || isEndLabel));
            moveLabelButton.setDisable(!isStation);
            rotateButton.setDisable(!isStation);
            stationRadiusSlider.setDisable(!isStation);
            
            removeStationButton.setDisable(isEndLabel || !isStation);
            stationColorPicker.setDisable(isEndLabel || !isStation);
            moveLabelButton.setDisable(isEndLabel || !isStation);
            rotateButton.setDisable(isEndLabel || !isStation);
            stationRadiusSlider.setDisable(isEndLabel || !isStation);
            
            removeElementButton.setDisable(!(isText || isImage));
            
            boldButton.setDisable(!isText);
            italicsButton.setDisable(!isText);
            fontSizeBox.setDisable(!isText);
            fontStyleBox.setDisable(!isText);

    }
    
    @Override
    public void reloadWorkspace(AppDataComponent data) {
        
    }
    
    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }
    
    public void setExportButton(boolean b){
        exportButton.setDisable(b);
    }
}