package mmm.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import mmm.gui.mmmWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.io.File;
import java.util.List;
import java.util.Optional;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 *
 * @author brendan
 */
public class mmmData implements AppDataComponent{
    
     public mmmData(AppTemplate initApp) {
         
     }
     
    @Override
    public void resetData() {
        
    }
}
