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
    
    double Framerate;
    double SequenceWidth;
    double SequenceHeight;
    
    Duration Sequencedur;
    
    
    
    
    
}
