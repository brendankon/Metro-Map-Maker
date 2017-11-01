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
import static djf.settings.AppPropertyType.ADD_ICON;
import static djf.settings.AppPropertyType.ADD_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.ADD_TO_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.EDIT_LINE_TOOLTIP;
import static djf.settings.AppPropertyType.LIST_ICON;
import static djf.settings.AppPropertyType.LIST_TOOLTIP;
import static djf.settings.AppPropertyType.REMOVE_ICON;
import static djf.settings.AppPropertyType.REMOVE_TOOLTIP;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
import static mmm.css.mmmStyle.CLASS_RENDER_CANVAS;

/**
 *
 * @author brendan
 */
public class mmmWorkspace extends AppWorkspaceComponent{
    
    AppTemplate app;
    AppGUI gui;
    VBox editToolbar;
    FlowPane undoRedoToolbar;
    FlowPane aboutToolbar;
    Button undoButton;
    Button redoButton;
    
    HBox row1Box;
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
    
    HBox row2Box;
    Label stationsLabel;
    ComboBox stationsBox;
    Button addStationButton;
    Button removeStationButton;
    Button snapButton;
    Button moveLabelButton;
    Button rotateButton;
    Slider stationRadiusSlider;
    ColorPicker stationColorPicker;
    
    HBox row3Box;
    ComboBox routeBox1;
    ComboBox routeBox2;
    Button findRouteButton;
    
    HBox row4Box;
    Button setImgBackgroundButton;
    Button setBackgroundColorButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
    
    HBox row5Box;
    Button boldButton;
    Button italicsButton;
    ComboBox fontSizeBox;
    ComboBox fontStyleBox;
    ColorPicker fontColorPicker;
    
    HBox row6Box;
    Button zoomInButton;
    Button zoomOutButton;
    Button increaseMapSize;
    Button decreaseMapSize;
    
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
        
        editToolbar = new VBox();
        
        row1Top = new HBox();
        row1Bottom = new HBox();
        row1VBox = new VBox();
        
        linesLabel = new Label("Metro Lines");
        row1Top.getChildren().add(linesLabel);
        lineBox = new ComboBox();
        row1Top.getChildren().add(lineBox);
        editLineButton = new Button();
        editLineButton.setDisable(false);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(EDIT_LINE_TOOLTIP.toString()));
        editLineButton.setTooltip(buttonTooltip);
        editLineButton.setText("Edit Line");
        row1Top.getChildren().add(editLineButton);
        addLineButton = gui.initChildButton(row1Bottom, ADD_ICON.toString(), ADD_LINE_TOOLTIP.toString(), false);
        removeLineButton = gui.initChildButton(row1Bottom, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), false);
        addStationToLineButton = new Button();
        addStationToLineButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(ADD_TO_LINE_TOOLTIP.toString()));
        addStationToLineButton.setTooltip(buttonTooltip);
        addStationToLineButton.setText("Add Station");
        row1Bottom.getChildren().add(addStationToLineButton);
        removeStationFromLineButton = new Button();
        removeStationFromLineButton.setDisable(false);
        buttonTooltip = new Tooltip(props.getProperty(REMOVE_TOOLTIP.toString()));
        removeStationFromLineButton.setTooltip(buttonTooltip);
        removeStationFromLineButton.setText("Remove Station");
        row1Bottom.getChildren().add(removeStationFromLineButton);
        listStationsButton = gui.initChildButton(row1Bottom, LIST_ICON.toString(), LIST_TOOLTIP.toString(), false);
        lineThicknessSlider = new Slider();
        row1Bottom.getChildren().add(lineThicknessSlider);
        
        row1VBox.getChildren().add(row1Top);
        row1VBox.getChildren().add(row1Bottom);
        editToolbar.getChildren().add(row1VBox);
        
        canvas = new Pane();
        
        workspace = new BorderPane();
        ((BorderPane)workspace).setLeft(editToolbar);
        ((BorderPane)workspace).setCenter(canvas);
    }
    
    public void initStyle(){
        
        canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        
        //row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row1Top.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row1Bottom.getStyleClass().add(CLASS_MULTI_HBOX_ROW);
        row1VBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        linesLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        editLineButton.getStyleClass().add(CLASS_BUTTON);
        addLineButton.getStyleClass().add(CLASS_BUTTON);
        removeLineButton.getStyleClass().add(CLASS_BUTTON);
        addStationToLineButton.getStyleClass().add(CLASS_BUTTON);
        removeStationFromLineButton.getStyleClass().add(CLASS_BUTTON);
        listStationsButton.getStyleClass().add(CLASS_BUTTON);
        
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent data) {
        
    }
    
    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }
}