package mmm;

import java.util.Locale;
import mmm.data.mmmData;
import mmm.file.mmmFiles;
import mmm.gui.mmmWorkspace;
import djf.AppTemplate;
import static javafx.application.Application.launch;
/**
 *
 * @author brendan
 */
public class MetroMapMaker extends AppTemplate{
    
    @Override
    public void buildAppComponentsHook() {

        fileComponent = new mmmFiles();
        dataComponent = new mmmData(this);
        workspaceComponent = new mmmWorkspace(this);
    }
    
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}
