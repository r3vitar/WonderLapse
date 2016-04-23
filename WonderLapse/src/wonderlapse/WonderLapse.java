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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author kacpe_000
 */
public class WonderLapse extends Application {
    StackPane root = new StackPane();
    Stage primaryStage;
    Scene scene = new Scene(root, 720, 480);
    DataManager fileChooser = new DataManager();
    
    
    
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
        
        root.getChildren().add(bp);
        
         scene.setRoot(root);
        
        
         
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
