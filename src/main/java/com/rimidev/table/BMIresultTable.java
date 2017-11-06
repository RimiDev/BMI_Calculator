package com.rimidev.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is a bean used to create the rows in the BMI result table
 * @author Maxime Lacasse
 * @version 1.1
 */
public class BMIresultTable {
    
    //Columns of the table
    private StringProperty category;
    private StringProperty bmi;
    private StringProperty risk;
    
    //Default contructor 
    public BMIresultTable() {
        this("0","0","0");
    }
    //Constructor with appropriate fields filled in
    public BMIresultTable(String category, String bmi, String risk) {
        this.category = new SimpleStringProperty(category);
        this.bmi = new SimpleStringProperty(bmi);
        this.risk = new SimpleStringProperty(risk);
    }
    
    //Setters
    public void setCategory(String categoryValue) {
        category.set(categoryValue);
    }
    public void setBmi(String bmiValue) {
        bmi.set(bmiValue);
    }
    public void setRisk(String riskValue) {
        risk.set(riskValue);
    }
    //Getters
    public String getCategory() {
        return bmi.get();
    }
    public String getBmi() {
        return bmi.get();
    }
    public String getRisk() {
        return risk.get();
    }
    //Binds
    public StringProperty categoryProperty() {
        return category;
    }
    public StringProperty bmiProperty() {
        return bmi;
    }
    public StringProperty riskProperty() {
        return risk;
    }
   
    //Hash
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 23 * hash + (this.bmi != null ? this.bmi.hashCode() : 0);
        hash = 23 * hash + (this.risk != null ? this.risk.hashCode() : 0);
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
        final BMIresultTable other = (BMIresultTable) obj;
        if (this.category != other.category && (this.category == null || !this.category.equals(other.category))) {
            return false;
        }
        if (this.bmi != other.bmi && (this.bmi == null || !this.bmi.equals(other.bmi))) {
            return false;
        }
        if (this.risk != other.risk && (this.risk == null || !this.risk.equals(other.risk))) {
            return false;
        }
        return true;
    }

    //toString
    @Override
    public String toString() {
        return "BMIresultTable{" + "category=" + category + ", bmi=" + bmi + ", risk=" + risk + '}';
    }  
    
} // end of BMIresultTable

 
