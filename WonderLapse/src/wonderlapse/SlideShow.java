/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
public class SlideShow extends Pane {

    DataManager fileChooser = new DataManager();

    Duration slp = Duration.UNKNOWN;
    Thread timelapse = new Thread();
    ArrayList<File> images = new ArrayList<File>();
    ProgressBar pb = new ProgressBar();
    double progress = 0;
            boolean b = new Boolean(false);

    public SlideShow() {
        slp = new Duration((1 / 5) * 1000);
        System.out.println(slp.toSeconds());

    }

    public void setFps(double fps) {
        slp = new Duration((1 / fps) * 1000);
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
    }

    public void start() {

        getChildren().clear();
        getChildren().add(pb);

        timelapse = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                
                ArrayList<Image> loadedImages = loadImages();
                
                for (Image frame : loadedImages) {
                    Platform.runLater(new Task() {

                        @Override
                        protected Object call() throws Exception {
                            getChildren().clear();
                            ImageView iv = new ImageView(frame);
                            iv.setPreserveRatio(true);
                            iv.setFitHeight(frame.getHeight());
                            iv.setFitWidth(frame.getWidth());
                            
                            getChildren().add(iv);
                            return true;

                        }
                    });

                    try {
                        Thread.sleep(getFrameRateAsMilli());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
        ArrayList<Image> slides = new ArrayList<Image>();
        progress = 0;
        
        Thread prT = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                do {
                    pb.setProgress(progress / images.size() * 4);
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {

                    }
                } while (progress * 4< images.size() && !b);
                return true;

            }
        });
        prT.start();
        
        Thread t1 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {

                for (double idx = 0; idx < images.size() / 4; idx++) {
                    InputStream is = new FileInputStream(images.get((int)idx));
                    Image i = new Image(is, 1280, 720, true, false);
                    if (i.isError()) {
                        System.err.println("büd geht ned");
                    } else {

                        slides.add((int)idx, i);
                        updateProgress2();
                    }
                    is.close();

                }
                return true;
            }
        });
        t1.start();
        Thread t2 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                for (double idx = images.size() / 4; idx < images.size() / 2; idx++) {
                    InputStream is = new FileInputStream(images.get((int)idx));
                    Image i = new Image(is, 1280, 720, true, false);
                    if (i.isError()) {
                        System.err.println("büd geht ned");
                    } else {

                        slides.add((int)idx, i);
                        updateProgress2();
                    }
                    is.close();

                }
                return true;
            }
        });
        t2.start();
        Thread t3 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                for (double idx = images.size() / 2; idx < images.size() / 4 * 3; idx++) {
                    InputStream is = new FileInputStream(images.get((int)idx));
                    Image i = new Image(is, 1280, 720, true, false);
                    if (i.isError()) {
                        System.err.println("büd geht ned");
                    } else {

                        slides.add((int)idx, i);
                        updateProgress2();
                    }
                    is.close();

                }
                return true;
            }
        });
        t3.start();
        Thread t4 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {

                for (double idx = images.size() / 4 * 3; idx < images.size(); idx++) {
                    InputStream is = new FileInputStream(images.get((int)idx));
                    Image i = new Image(is, 1280, 720, true, false);
                    if (i.isError()) {
                        System.err.println("büd geht ned");
                    } else {

                        slides.add((int)idx, i);

                        updateProgress2();
                    }
                    is.close();
                }
                return true;
            }
        });
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            b=true;
        } catch (InterruptedException ex) {
            Logger.getLogger(SlideShow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return slides;
    }
    
    public void updateProgress2(){
        progress++;
    }

}
