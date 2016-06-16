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
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.SeekableByteChannel;
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
public class SlideShow extends Pane implements Serializable {

    transient DataManager fileChooser = new DataManager();
    public static long serialVerUID = 1L;

    Duration slp = Duration.UNKNOWN;
    transient Thread timelapse;
    //ArrayList<File> images = new ArrayList<File>();
    transient ProgressBar pb;
    double progress = 0;
    boolean b = false;
    SlideShowInfo ssi;

    public SlideShowInfo getSsi() {
        return ssi;
    }

    public void setSsi(SlideShowInfo ssi) {
        this.ssi = ssi;
    }
    transient SomeListener sl;

    public void setSl(SomeListener sl) {
        this.sl = sl;
    }
    Resolution res = null;
    transient ArrayList<Image> slides;

    public SlideShow(SomeListener sl, double width, double height) {
        this.sl = sl;

        ssi = new SlideShowInfo(progress, new Resolution(width, height));

        setFps(15);
        System.out.println(slp.toSeconds());

    }

    public SlideShow(SomeListener sl) {
        this.sl = sl;

        ssi = new SlideShowInfo(progress, new Resolution(1280, 720));

        setFps(15);
        System.out.println(slp.toSeconds());

    }

    public void setFileChooser(DataManager fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void setResolution(Resolution res) {
        ssi.setSequenceResolution(res);
    }

    public void setResolution(double width, double height) {
        ssi.setSequenceResolution(width, height);
    }

    public void setFps(double fps) {
        this.ssi.setFrameRate(fps);
        slp = new Duration(1000 / fps);
        System.out.println(slp.toMillis());
    }

    public SlideShow(Node... children) {
        super(children);

    }

    public void initPics() {
        ssi.getFiles().clear();
        List fileList = fileChooser.chooseMultipleFiles("img");

        for (Object o : fileList) {
            File f = (File) o;

            ssi.addFile(f);
        }
        System.out.println(ssi.getFiles().size());
    }

    public void start() {
        pb = new ProgressBar();
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
                                        Resolution tmpRes = ssi.getRes();
                                        if (tmpRes.getHeight() > sl.getTimelapsePaneRes().getHeight()
                                                || tmpRes.getWidth() > sl.getTimelapsePaneRes().getWidth()) {
                                            iv.setFitHeight(sl.getTimelapsePaneRes().getHeight());
                                            iv.setFitWidth(sl.getTimelapsePaneRes().getWidth());

                                           // iv.setPreserveRatio(true);
                                        }
                                        //System.out.printf("%f x %f\n", iv.getFitWidth(), iv.getFitHeight());
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
        pb = new ProgressBar();
        progress = 0;
        b = false;

        Thread prT = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {

                do {
                    pb.setProgress(progress / ssi.getFiles().size());
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {

                    }
                } while (progress <= ssi.getFiles().size() && !b);
                return true;

            }
        });
        prT.start();

        res = ssi.getRes();

        Thread t1 = new Thread(new Task() {

            @Override
            protected Object call() throws Exception {

                for (double idx = 0; idx < ssi.getFiles().size(); idx++) {
                    InputStream is = new FileInputStream(ssi.getFiles().get((int) idx));
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

    public void save(File f) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);

            ssi.setExportLocation(f);
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

    public static SlideShow loadSlideShow(File f, SomeListener sl) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);

        SlideShow temp = (SlideShow) ois.readObject();

        temp.setSl(sl);

        temp.setFileChooser(new DataManager());

        return temp;
    }

    public static SlideShow loadSlideShow(SlideShowInfo ssi, SomeListener sl) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(ssi.getExportLocation());
        ObjectInputStream ois = new ObjectInputStream(fis);

        SlideShow temp = (SlideShow) ois.readObject();

        temp.setSl(sl);
        temp.setSsi(ssi);

        temp.setFileChooser(new DataManager());

        return temp;

    }

    public Image getFirstImage() throws FileNotFoundException {
        return new Image(new FileInputStream(this.ssi.getFiles().get((int) Math.round(Math.random() * this.ssi.getFiles().size()))));
    }

    public static Image getFirstImage(SlideShow s) throws FileNotFoundException {
        return new Image(new FileInputStream(s.ssi.getFiles().get((int) Math.round(Math.random() * s.ssi.getFiles().size()))));
    }

}
