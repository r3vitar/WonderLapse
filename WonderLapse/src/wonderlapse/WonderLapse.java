/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author kacpe_000
 */
public class WonderLapse extends Application {
   
    
    //top
    MenuItem newMenuItem = new MenuItem("new");
    
    Menu fileMenu = new Menu("File", null, newMenuItem),
            editMenu = new Menu("Edit"),
            optionsMenu = new Menu("Options");
    MenuBar mainBar = new MenuBar(fileMenu, editMenu, optionsMenu);
    
    //right
    TitledPane timlapsePane= new TitledPane("TimeLapse", null);
    TitledPane renderPane= new TitledPane("Render", null);
    TitledPane managePane= new TitledPane("ManageFiles", null);
    Accordion sideAccordion = new Accordion(timlapsePane, renderPane, managePane);
    
    //center
    ObservableList<ImageView> galleryItems = FXCollections.observableArrayList();
    TilePane galleryPane = new TilePane();
    
    BorderPane centerBorderPane = new BorderPane(galleryPane);
    
    
     BorderPane root = new BorderPane(null, mainBar, sideAccordion, null, null);
    Stage primaryStage;
    Scene scene = new Scene(root, 720, 480);
    DataManager fileChooser = new DataManager();
    final File sequenceSaver = new File("");
    
    
    
    
    
    
    
    @Override
    public void start(Stage ps) {
        primaryStage = ps;
        
        
        SlideShow sss = new SlideShow();
        
        
        Button b1 = new Button("get");
        b1.setOnAction((ActionEvent e) -> sss.initPics());
        
        Button setFps = new Button("setFps");
        
        TextField tf = new TextField();
        setFps.setOnAction((ActionEvent event) -> sss.setFps(Double.parseDouble(tf.getText())));
        
        
        HBox h = new HBox(tf, setFps);
        
        Button b2 = new Button("play");
        b2.setOnAction((ActionEvent e) -> sss.start());
        
        BorderPane bp = new BorderPane(sss);
        bp.setLeft(b1);
        bp.setRight(b2);
        bp.setBottom(h);
        
        
        
        //root.getChildren().add(bp);
        
        scene.setRoot(root);
        
        
         
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
