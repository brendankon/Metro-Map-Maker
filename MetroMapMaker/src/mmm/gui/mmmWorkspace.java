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
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import static mmm.css.mmmStyle.CLASS_BUTTON;
import static mmm.css.mmmStyle.CLASS_COLOR_CHOOSER_CONTROL;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR_ROW;
import static mmm.css.mmmStyle.CLASS_MULTI_HBOX_ROW;
import static mmm.css.mmmStyle.CLASS_NAV_BOTTOM_ROW;
import static mmm.css.mmmStyle.CLASS_NAV_LABEL;
import static mmm.css.mmmStyle.CLASS_RENDER_CANVAS;
import static mmm.css.mmmStyle.CLASS_ROUTE_ROW;

/**
 *
 * @author brendan
 */
@SuppressWarnings("unchecked")
public class mmmWorkspace extends AppWorkspaceComponent{
    
    AppTemplate app;
    AppGUI gui;
    VBox editToolbar;
    FlowPane undoRedoToolbar;
    FlowPane aboutToolbar;
    Button undoButton;
    Button redoButton;
    Button exportButton;
    Button aboutButton;
    Button saveAs;
    
    HBox row1Top;
    HBox row1Bottom;
    VBox row1VBox;
    Label linesLabel;
    ComboBox lineBox;
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
    ComboBox stationsBox;
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
    ComboBox routeBox1;
    ComboBox routeBox2;
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
    
    Pane canvas;
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    
    
    
    public mmmWorkspace(AppTemplate initApp) {
        
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
        lineBox = new ComboBox();
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
        lineThicknessSlider = new Slider();
        row1Bottom.getChildren().add(lineThicknessSlider);
        
        row2Top = new HBox();
        row2Bottom = new HBox();
        row2VBox = new VBox();
        stationsLabel = new Label("Metro Stations");
        row2Top.getChildren().add(stationsLabel);
        stationsBox = new ComboBox();
        stationsBox.setMinWidth(140);
        stationsBox.setPrefWidth(140);
        stationsBox.setMaxWidth(USE_COMPUTED_SIZE);
        row2Top.getChildren().add(stationsBox);
        addStationButton = gui.initChildButton(row2Top, ADD_ICON.toString(), ADD_STATION_TOOLTIP.toString() , false);
        removeStationButton = gui.initChildButton(row2Top, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString() , false);
        stationColorPicker = new ColorPicker(Color.BLACK);
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
        stationRadiusSlider = new Slider();
        row2Bottom.getChildren().add(stationRadiusSlider);
        
        row3Top = new HBox();
        row3Bottom = new HBox();
        row3VBox = new VBox();
        routeLabel = new Label("Find Route");
        row3Top.getChildren().add(routeLabel);
        routeBox1 = new ComboBox();
        routeBox1.setMinWidth(140);
        routeBox1.setPrefWidth(140);
        routeBox1.setMaxWidth(USE_COMPUTED_SIZE);
        row3Bottom.getChildren().add(routeBox1);
        routeBox2 = new ComboBox();
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
        fontSizeBox.setValue(12);
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
        
        workspace = new BorderPane();
        ((BorderPane)workspace).setLeft(editToolbar);
        ((BorderPane)workspace).setCenter(canvas);
        controls();
    }
    
    public void controls(){
        aboutButton.setOnAction(e ->{
            handleAboutRequest();
        });
        
        exportButton.setOnAction(e ->{
            handleExportRequest();
        });
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
    
    public void handleExportRequest(){
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
    
    @Override
    public void reloadWorkspace(AppDataComponent data) {
        
    }
    
    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }
}