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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.omg.CORBA.BAD_CONTEXT;

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
    // MenuBar mainBar = new MenuBar(fileMenu, editMenu, optionsMenu);
    BorderPane bp;

    //right
    //center
    ArrayList<SlideShowInfo> lapseList = new ArrayList<SlideShowInfo>();
    ObservableList<BorderPane> galleryItems = FXCollections.observableArrayList();
    TilePane galleryPane = new TilePane(Orientation.HORIZONTAL, 10, 10);

    TitledPane timlapsePane = new TitledPane("TimeLapse", null);
    Label testL = new Label("This amazing feature will come with future updates");
    TitledPane renderPane = new TitledPane("Render", new BorderPane(testL));
    TitledPane managePane = new TitledPane("ManageFiles", galleryPane);
    Accordion mainAccordion = new Accordion(timlapsePane, renderPane, managePane);

    BorderPane centerBorderPane = new BorderPane(mainAccordion);

    BorderPane root = new BorderPane(centerBorderPane, null, null, null, null);
    Stage primaryStage;
    Scene scene = new Scene(root, 720, 480);
    DataManager fileChooser = new DataManager();

    Stage previewOptionStage = new Stage(StageStyle.UTILITY);

    final File sequenceSaver = new File("");
    SlideShow sss;

    @Override
    public void start(Stage ps) {
        initOptionStage();
        testL.setId("name");
        initWL();

        primaryStage = ps;

        sss = new SlideShow(this);

        Button b1 = new Button("get");

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sss.initPics();
            }
        });

        Button bSave = new Button("Save");

        bSave.setOnAction((ActionEvent) -> {
            File f = new DataManager().saveWonderLapse();

            if (isLapseSet(sss.getSsi())) {
                sss.save(f);

                this.lapseList.add(sss.getSsi());
                this.galleryItems.add(mkIv(sss.getSsi()));
            }else
                System.err.println("Already Set");
        });

        bp = new BorderPane();
        Button bLoad = new Button("Load");
        bLoad.setOnAction((ActionEvent) -> {
            try {
                File f = new DataManager().chooseSingleFile("wl");
                if (f != null) {
                    sss = SlideShow.loadSlideShow(f, this);
                }
            } catch (IOException ex) {
                Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                bp.setCenter(sss);
            }
        });

        Button b2 = new Button("play");
        b2.setOnAction((ActionEvent e) -> {
            bp.setCenter(sss);

            sss.start();
        });

        Button options = new Button("Options");
        options.setOnAction((ActionEvent event) -> {
            if (!previewOptionStage.isShowing()) {
                previewOptionStage.show();
            } else {
                previewOptionStage.close();
            }
        });
        HBox topButtons = new HBox(b1, b2, bSave, bLoad, options);
        topButtons.autosize();

        bp.setCenter(sss);
        bp.setTop(topButtons);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                saveLapses();
                Platform.exit();

            }
        });

        //root.getChildren().add(bp);
        this.timlapsePane.setContent(bp);
        scene.setRoot(root);
        scene.getStylesheets().add("resources/styles.css");
        primaryStage.getIcons().add(new Image("resources/logowl.png"));

        primaryStage.setTitle("WonderLapse");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setMaximized(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                mainAccordion.setExpandedPane(timlapsePane);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10);
                            mainAccordion.setExpandedPane(managePane);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }).start();

            }
        });

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

    private void saveLapses() {

        try {
            FileOutputStream fos = new FileOutputStream(new File("lapses.wlc"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            try {
                oos.writeObject(this.lapseList);
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                oos.flush();
                fos.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

    }

    private void initWL() {
        try {
            FileInputStream fis = new FileInputStream(new File("lapses.wlc"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {
                this.lapseList = (ArrayList<SlideShowInfo>) ois.readObject();
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                ois.close();
                fis.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            for (int i = 0; i < this.lapseList.size(); i++) {

                this.galleryItems.add(mkIv(this.lapseList.get(i)));

            }
            this.galleryPane.getChildren().addAll(galleryItems);
        }
    }

    public void doubleClick(int i) {
        System.out.println("double click");
        try {

            sss = SlideShow.loadSlideShow(this.lapseList.get(i), this);

        } catch (IOException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WonderLapse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            bp.setCenter(sss);

            mainAccordion.setExpandedPane(this.timlapsePane);
            Label title = new Label(sss.getSsi().getName());
            title.setId("name");
            bp.setCenter(title);

        }

    }

    long lastClicked;

    private void initOptionStage() {

        this.previewOptionStage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Button save = new Button("Save");
                Button cancel = new Button("Cancel");

                Label nameL = new Label("Name");
                TextField nameTf = new TextField();
                nameTf.setMaxWidth(100);
                nameTf.setText(sss.getSsi().getName());
                BorderPane nameBp = new BorderPane(null, null, nameTf, null, nameL);

                Label fpsL = new Label("FPS");
                TextField fpsTf = new TextField();
                fpsTf.setMaxWidth(50);
                fpsTf.setText(Double.toString(sss.getSsi().getFrameRate()));
                BorderPane fpsBp = new BorderPane(null, null, fpsTf, null, fpsL);

                Label resL = new Label("Resolution (width x height)");
                TextField xRes = new TextField();
                xRes.setMaxWidth(50);
                xRes.setText(Integer.toString((int) Math.round(sss.getSsi().getRes().getWidth())));
                Label xL = new Label("x");
                TextField yRes = new TextField();
                yRes.setMaxWidth(50);
                yRes.setText(Integer.toString((int) Math.round(sss.getSsi().getRes().getHeight())));

                BorderPane resBp = new BorderPane(null, null, new HBox(xRes, xL, yRes), null, resL);

                save.setOnAction((ActionEvent event2) -> {
                    sss.setFps(Double.parseDouble(fpsTf.getText()));
                    sss.setResolution(Double.parseDouble(xRes.getText()), Double.parseDouble(yRes.getText()));
                    sss.getSsi().setName(nameTf.getText());
                    previewOptionStage.close();
                });
                cancel.setOnAction((ActionEvent event2) -> {
                    previewOptionStage.close();
                });

                BorderPane buttonBp = new BorderPane(null, null, save, null, cancel);

                VBox root = new VBox(nameBp, fpsBp, resBp, buttonBp);
                Scene previewOptionScene = new Scene(root, 400, 175);
                previewOptionScene.getStylesheets().add("resources/styles.css");

                previewOptionStage.setScene(previewOptionScene);
                previewOptionStage.getIcons().add(new Image("resources/logowl.png"));
                previewOptionStage.setResizable(false);

            }
        });

    }

    private BorderPane mkIv(SlideShowInfo ssi) {
        int i = this.lapseList.indexOf(ssi);
        BorderPane ivbp = new BorderPane();
        System.out.println(this.lapseList.get(i));

        final int ii = i;
        try {
            Image img = new Image(new FileInputStream(this.lapseList.get(i).getFiles().get((int) Math.round(this.lapseList.get(i).getFiles().size() / 2))), 200, 100, true, true);

            ImageView iv = new ImageView(img);
            ivbp = new BorderPane(iv);
            ivbp.setTop(new BorderPane(new Label(this.lapseList.get(i).getName())));

            ivbp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    long time = System.currentTimeMillis() - lastClicked;
                    if (time < 300) {
                        doubleClick(ii);
                    }

                    lastClicked = System.currentTimeMillis();

                }

            });

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            return ivbp;

        }

    }

    private boolean isLapseSet(SlideShowInfo ssi) {
        for (SlideShowInfo set : this.lapseList) {
            if (set.getExportLocation() == ssi.getExportLocation()) {
                return true;
            }
            if (set.getName() == ssi.getName() && ssi.getFiles() == set.getFiles()) {
                return true;
            }
        }
        return false;
    }

}
