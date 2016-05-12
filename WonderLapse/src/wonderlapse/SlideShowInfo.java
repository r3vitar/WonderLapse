/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.util.Duration;

/**
 *
 * @author Bernd
 */
public class SlideShowInfo implements Serializable {
    
    ArrayList<File> files = new ArrayList<File>();
    
    double frameRate;
    Resolution res;
    Duration sequenceDur;
    File exportLocation;

    public SlideShowInfo(double frameRate, double sequenceWidth, double sequenceHeight) {
        this.frameRate = frameRate;
        this.res = new Resolution(sequenceWidth, sequenceHeight);
        
    }
    public SlideShowInfo(double frameRate, Resolution res) {
        this.frameRate = frameRate;
        this.res = res;
        
    }
    public SlideShowInfo(double frameRate, double sequenceWidth, double sequenceHeight, Duration sequenceDur, File exportLocation) {
        this.frameRate = frameRate;
        this.res = new Resolution(sequenceWidth, sequenceHeight);
        this.sequenceDur = sequenceDur;
        this.exportLocation = exportLocation;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public double getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public double getSequenceWidth() {
        return res.getWidth();
    }

    public void setSequenceWidth(double sequenceWidth) {
        this.res.setWidth(sequenceWidth);
    }

    public double getSequenceHeight() {
        return res.getHeight();
    }

    public void setSequenceHeight(double sequenceHeight) {
        this.res.setHeight(sequenceHeight);
    }
    
    public void setSequenceResolution(double sequenceWidth, double sequenceHeight){
        this.res = new Resolution(sequenceWidth, sequenceHeight);
    }
    public void setSequenceResolution(Resolution res){
        this.res = res;
    }
    
    public Resolution getRes(){
        return this.res;
    }

    public Duration getSequenceDur() {
        return sequenceDur;
    }

    public void setSequenceDur(Duration sequenceDur) {
        this.sequenceDur = sequenceDur;
    }

    public File getExportLocation() {
        return exportLocation;
    }

    public void setExportLocation(File exportLocation) {
        this.exportLocation = exportLocation;
    }
    
    
}
