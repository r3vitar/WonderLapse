/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wonderlapse;

import java.io.Serializable;

/**
 *
 * @author kacpe_000
 */
public class Resolution implements Serializable{

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

    public Resolution() {
    }
    public Resolution(double width, double height) {
        this.height = height;
        this.width = width;
    }
    
    public double countRes(){
        return this.width*this.height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
    
    
    
    
    
    
    
    

    
    
}
