/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

/**
 *
 * @author kacpe_000
 */
public class Resolution {

    double width = 0;
    double height = 0;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    
    public void setResolution(double x, double y){
        this.height = y;
        this.width = x;
        
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
    
    
    
    
    
    
    
    

    
    
}
