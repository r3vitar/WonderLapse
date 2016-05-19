/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author kacpe_000
 */
public class SlideShow extends Pane implements Serializable{

    DataManager fileChooser = new DataManager();

    Duration slp = Duration.UNKNOWN;
    Thread timelapse = new Thread();
    ArrayList<File> images = new ArrayList<File>();
    ProgressBar pb = new ProgressBar();
    double progress = 0;
    boolean b = new Boolean(false);
    SlideShowInfo ssi;
    SomeListener sl;
    Resolution res = null;
    ArrayList<Image> slides;

    public SlideShow(SomeListener sl, double width, double height) {
        this.sl = sl;
        slp = new Duration((1 / 5) * 1000);
        System.out.println(slp.toSeconds());
        
        ssi = new SlideShowInfo(progress, new Resolution(width, height));

    }
    
    public SlideShow(SomeListener sl) {
        this.sl = sl;
        slp = new Duration((1 / 5) * 1000);
        System.out.println(slp.toSeconds());
        
        ssi = new SlideShowInfo(progress, new Resolution(1280, 720));

    }
    
    public void setResolution(Resolution res){
        ssi.setSequenceResolution(res);
    }
    public void setResolution(double width, double height){
        ssi.setSequenceResolution(width, height);
    }

    public void setFps(double fps) {
        slp = new Duration(1000 / fps);
        System.out.println(slp.toMillis());
    }

    public SlideShow(Node... children) {
        super(children);

    }

    public void initPics() {
        List fileList = fileChooser.chooseMultipleFiles("img");

        for (Object o : fileList) {
            File f = (File) o;
            

            images.add(f);
        }
        System.out.println(images.size());
    }

    public void start() {

        getChildren().clear();
        getChildren().add(pb);

        timelapse = new Thread(new Task() {
            
            int cnt = 0;

            @Override
            protected Object call() throws Exception {
                try {
                    ArrayList<Image> loadedImages = loadImages();

                    for (Image frame : loadedImages) {
                        
                        
                        try {
                            Platform.runLater(new Task() {

                                @Override
                                protected Object call() throws Exception {
                                    try {
                                        getChildren().clear();
                                        ImageView iv = new ImageView(frame);
                                        iv.setFitHeight(sl.getTimelapsePaneRes().getHeight());
                                        iv.setFitWidth(sl.getTimelapsePaneRes().getWidth());
                                        iv.setPreserveRatio(true);
                                        
                                        Platform.runLater(new Runnable() {

                                            @Override
                                            public void run() {
                                                cnt++;
                                                System.out.println(cnt);
                                            }
                                        });

                                        getChildren().add(iv);
                                    } catch (Exception e) {
                                        System.err.println(e);
                                        return false;
                                    }
                                    return true;

                                }
                            });

                            try {
                                Thread.sleep(getFrameRateAsMilli());
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (Exception e) {
                            System.err.println(e);
                        }

                    }

                } catch (Exception e) {
                    System.err.println(e);
                }

                return true;
            }

        });

        timelapse.start();

    }

    private long getFrameRateAsMilli() {
        return (long) slp.toMillis();
    }

    public ArrayList<Image> loadImages() {
        slides = new ArrayList<Image>();
        progress = 0;
        b = false;
        
        
        
        Thread prT = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                do {
                    pb.setProgress(progress / images.size());
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {

                    }
                } while (progress <= images.size() && !b);
                return true;

            }
        });
        prT.start();
        
        if(sl.getTimelapsePaneRes().countRes() < ssi.getRes().countRes()){
            res = sl.getTimelapsePaneRes();
        }else
            res = ssi.getRes();
        
        

        Thread t1 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {

                for (double idx = 0; idx < images.size() /*/ 4*/; idx++) {
                    InputStream is = new FileInputStream(images.get((int) idx));
                    Image i = new Image(is, res.getWidth(), res.getHeight(), true, false);
                    
                    if (i.isError()) {
                        System.err.println("büd geht ned");
                    } else {

                        slides.add((int) idx, i);
                        updateProgress2();
                    }
                    is.close();

                }
                return true;
            }
        });
        
  
        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();

        try {
            t1.join();
//            t2.join();
//            t3.join();
//            t4.join();
            b = true;
        } catch (InterruptedException ex) {
            Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return slides;
    }

    public void updateProgress2() {
        progress++;
    }

   public void save() {
       FileOutputStream fos = null;
       ObjectOutputStream oos = null;
        try {
            File f = new File(ssi.getName());
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            
            oos.writeObject(this);
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
       
        
    }

}
