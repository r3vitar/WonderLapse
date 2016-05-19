/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author kacpe_000
 */
public class WonderLapse extends Application implements SomeListener {
   
    
    //top
    MenuItem newMenuItem = new MenuItem("new");
    
    Menu fileMenu = new Menu("File", null, newMenuItem),
            editMenu = new Menu("Edit"),
            optionsMenu = new Menu("Options");
    MenuBar mainBar = new MenuBar(fileMenu, editMenu, optionsMenu);
    
    //right
    
    
    //center
    ObservableList<ImageView> galleryItems = FXCollections.observableArrayList();
    TilePane galleryPane = new TilePane();
    
    TitledPane timlapsePane= new TitledPane("TimeLapse", null);
    TitledPane renderPane= new TitledPane("Render", null);
    TitledPane managePane= new TitledPane("ManageFiles", galleryPane);
    Accordion mainAccordion = new Accordion(timlapsePane, renderPane, managePane);
    
    BorderPane centerBorderPane = new BorderPane(mainAccordion);
    
    
     BorderPane root = new BorderPane(centerBorderPane, mainBar, null, null, null);
    Stage primaryStage;
    Scene scene = new Scene(root, 720, 480);
    DataManager fileChooser = new DataManager();
    
    final File sequenceSaver = new File("");
    SlideShow sss;
    
    
    
    
    
    
    
    
    @Override
    public void start(Stage ps) {
        primaryStage = ps;
        
        
        sss = new SlideShow(this);
        
        
        Button b1 = new Button("get");
        
        Button bSave = new Button("Save");
        
        bSave.setOnAction((ActionEvent) -> {
            File f = new DataManager().saveWonderLapse();
                    
            sss.save(f);
        });
        
        b1.setOnAction((ActionEvent e) -> sss.initPics());
        
        Button setFps = new Button("setFps");
        BorderPane bp = new BorderPane();
        Button bLoad = new Button("Load");
        bLoad.setOnAction((ActionEvent) -> {
            try {
                File f = new DataManager().chooseSingleFile("wl");
                sss = SlideShow.loadSlideShow(f, this);
            } catch (IOException ex) {
                Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                bp.setCenter(sss);
            }
        });
        
        TextField tf = new TextField();
        setFps.setOnAction((ActionEvent event) -> sss.setFps(Double.parseDouble(tf.getText())));
        
        
        HBox h = new HBox(tf, setFps);
        
        Button b2 = new Button("play");
        b2.setOnAction((ActionEvent e) -> sss.start());
        
        bp.setCenter(sss);
        bp.setLeft(b1);
        bp.setRight(new VBox(b2, bSave, bLoad));
        bp.setBottom(h);
        
        
        
        //root.getChildren().add(bp);
        this.timlapsePane.setContent(bp);
        scene.setRoot(root);
        
        
         
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setMaximized(true);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Resolution getTimelapsePaneRes() {
        return new Resolution(timlapsePane.getWidth(), timlapsePane.getHeight());
    }
    
}
