package wonderlapse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Diese  Klasse ist für das einlesen der Dateien zuständig.
 * 
 * 
 */
public class DataManager {
    
    private HashMap<String, FileChooser> fileChooser = new HashMap<String, FileChooser>();
    
    /**
     * Der Kunstruktor beinhaltet alle fileChooser für die verschiedenen Datentypen in einer HashMap.
     */
    public DataManager() {
        


        fileChooser.put("img", new FileChooser());
        fileChooser.get("img").getExtensionFilters().add(new ExtensionFilter("IMG", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.get("img").setTitle("img");
        
        fileChooser.put("wl", new FileChooser());
        fileChooser.get("wl").getExtensionFilters().add(new ExtensionFilter("WonderLapse-Sequence", "*.wls"));
       
    }
    
    /**
     * 
     * @param key Der Schlüssel, der angibt, welche Dateitypen man haben soll.
     * @return Man bekommt eine Liste zurück, welche alle ausgewählten Files beinhaltet.
     */
    public List chooseMultipleFiles(String key) {
        List selectedFiles = fileChooser.get(key).showOpenMultipleDialog(new Stage());

        return selectedFiles;

    }
    
    /**
     * 
     * @param key Der Schlüssel, der angibt, welche Dateitypen man haben soll.
     * @return File, der alle passenden Dateien zurückgibt.
     */
    public File chooseSingleFile(String key) {
        return fileChooser.get(key).showOpenDialog(new Stage());

    }
    
    /**
     * 
     * @return Gibt die aktuelle Playlist als File zurück.
     */
    public File saveWonderLapse() {
        return fileChooser.get("wl").showSaveDialog(new Stage());
    }

}
