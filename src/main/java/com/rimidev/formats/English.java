package com.rimidev.formats;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean used to calculate the BMI result using the English method which 
 * includes feet and inches.
 * @author Maxime Lacasse
 * @version 1.1
 */
public class English {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
     //English fields
    private final StringProperty height; // 5'11" -> 5 feet, 11 inches
    private final DoubleProperty weight; // pounds.
    private final DoubleProperty result; //
    
    
    //Default contructor setting to nothing
    public English() {
        this.height = new SimpleStringProperty("");
        this.weight = new SimpleDoubleProperty(0);
        this.result = new SimpleDoubleProperty(0);
    }
    
    //Constructor initializing variables to the parameters
    public English(String height, Double weight){
        this.height = new SimpleStringProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.result = new SimpleDoubleProperty(0);
    }
    
    //Getters
    public final double getWeight() {
        return weight.get();
    }
    public final double getWeightForForumla(){
        double weightTimes703 = weight.get()*703;
        return weightTimes703;
    }
    public final String getHeightString() {
        return height.get();
    }
    public final double getHeightTotalValue() {
        String feet = height.getValue().substring(0, 1);
        String inches = "";
        if (height.getValue().length() == 4) { //ex: 5'1"
            inches = height.getValue().substring(2, 3);
        } else {
            inches = height.getValue().substring(2, 4);
        }
        Double heightValue = (Double.valueOf(feet) * 12) + Double.valueOf(inches);
        return heightValue;
    }        
    public final double getResult(){
        return result.get();
    }
    
    //Setters
    public void setWeight(final double weight){
        this.weight.set(weight);
    }
    public void setHeight(final String height){
        this.height.set(height);
    }
    public void setResult(final double result){
        this.result.set(result);
    }
    
    //Binding
    public final DoubleProperty weightProperty(){
        return weight;
    }
    public final StringProperty heightProperty(){
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
        double inputHeight = this.getHeightTotalValue();
        
        //Minimum height: 3ft = 36.
        //Maximum height: 6'11" = (6*12)+11=83     
        if (inputHeight < 36 || inputHeight > 83) {
            return false;
        }
        return !(inputWeight < 50 || inputWeight > 500);
    }
      
    public Double calculateBMI(){   
        this.result.set((this.getWeightForForumla()) / Math.pow(this.getHeightTotalValue(),2));
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
        final English other = (English) obj;
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
        return "English{" + "height=" + height + ", weight=" + weight + '}';
    }
 
} // End of English class
