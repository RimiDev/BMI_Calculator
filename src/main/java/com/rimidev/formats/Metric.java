package com.rimidev.formats;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A bean used to calculate the BMI result using the Metric method which 
 * includes meters.
 * @author Maxime Lacasse
 * @version 1.1
 */
public class Metric {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
    //Metric fields
    private final DoubleProperty height;
    private final DoubleProperty weight;
    private final DoubleProperty result;
    
    //Default contructor setting to nothing
    public Metric() {
        this.height = new SimpleDoubleProperty(0);
        this.weight = new SimpleDoubleProperty(0);
        this.result = new SimpleDoubleProperty(0);
    }
    
    //Constructor initializing variables to the parameters
    public Metric(Double height, Double weight){
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.result = new SimpleDoubleProperty(0);
    }
    
    //Getters
    public final double getWeight() {
        return weight.get();
    }
    public final double getHeight() {
        return height.get();
    }
    public final double getResult(){
        return result.get();
    }
    
    //Setters
    public void setWeight(final double weight){
        this.weight.set(weight);
    }
    public void setHeight(final double height){
        this.height.set(height);
    }
    public void setResult(final double result){
        this.result.set(result);
    }
    
    //Binding
    public final DoubleProperty weightProperty(){
        return weight;
    }
    public final DoubleProperty heightProperty(){
        return height;    
    }
    public final DoubleProperty resultProperty(){
        return result;
    }
    
    /**
     * Validates if the height and weight of the metric values are suitable
     * for the body-mass-index calculation.
     * @return 
     */
    public boolean validateHeightWeight(){
        double inputWeight = this.weight.getValue();
        double inputHeight = this.height.getValue();
        
        if (inputHeight < 0.914 || inputHeight > 2.108) {
            return false;
        }
        return !(inputWeight < 50 || inputWeight > 500);
    }
    
    public Double calculateBMI(){
        Double weightInKG = (this.weight.getValue() / 2.2048226218); //Convert pounds to kg
        this.result.set((weightInKG / Math.pow(this.height.getValue(),2)));
        this.result.set(Math.round(this.result.get() * 100));
        return result.get()/100;
    }

    //Hash
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.height != null ? this.height.hashCode() : 0);
        hash = 29 * hash + (this.weight != null ? this.weight.hashCode() : 0);
        return hash;
    }

    //Equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Metric other = (Metric) obj;
        if (this.height != other.height && (this.height == null || !this.height.equals(other.height))) {
            return false;
        }
        if (this.weight != other.weight && (this.weight == null || !this.weight.equals(other.weight))) {
            return false;
        }
        return true;
    }

    //toString
    @Override
    public String toString() {
        return "Metric{" + "height=" + height + ", weight=" + weight + '}';
    }
 
} // End of Metric class
