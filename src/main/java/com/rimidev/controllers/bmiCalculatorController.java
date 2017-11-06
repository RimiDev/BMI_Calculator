package com.rimidev.controllers;

import com.rimidev.formats.English;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import com.rimidev.formats.Metric;
import com.rimidev.table.BMIresultTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the BMI calculator controller which controls all the activities that 
 * happen in the calculator, such as the calculations of users' input, the displaying
 * of the BMI result table, alert dialogs whenever user input is incorrect for
 * the system.
 * The application is bilangual -> EN/FR
 * @author Maxime Lacasse
 * @version 1.1
 */
public class bmiCalculatorController {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id="metric"
    private RadioButton metric; // Value injected by FXMLLoader

    @FXML // fx:id="calcFormat"
    private ToggleGroup calcFormat; // Value injected by FXMLLoader

    @FXML // fx:id="english"
    private RadioButton english; // Value injected by FXMLLoader

    @FXML // fx:id="pregnant"
    private RadioButton pregnant; // Value injected by FXMLLoader

    @FXML // fx:id="preg"
    private ToggleGroup preg; // Value injected by FXMLLoader

    @FXML // fx:id="notPregnant"
    private RadioButton notPregnant; // Value injected by FXMLLoader

    @FXML // fx:id="height"
    private TextField height; // Value injected by FXMLLoader

    @FXML // fx:id="weight"
    private TextField weight; // Value injected by FXMLLoader

    @FXML // fx:id="category"
    private Label category; // Value injected by FXMLLoader

    @FXML // fx:id="risk"
    private Label risk; // Value injected by FXMLLoader

    @FXML // fx:id="calculation"
    private Label calculation; // Value injected by FXMLLoader
    
    @FXML
    private Button calcButton;
    
    @FXML
    private TableView resultTable;
    
    @FXML
    private TableColumn<BMIresultTable, String> categoryLabel;
            
    @FXML
    private TableColumn<BMIresultTable, String> bmiLabel;
        
    @FXML
    private TableColumn<BMIresultTable, String> riskLabel;
   
    
    /**
     * Calculates the BMI result either with Metric or English
     * @param event 
     */
    @FXML
    void onCalculate(ActionEvent event) {
        //Checks if the format the user wants is metric or not
        if (isMetricFormat()) {
            //Metric format
           calculateMetric();
        } else {
            //English format
           calculateEnglish();
        }

    } // End of onCalculate

    /**
     * Exits the program.
     * @param event 
     */
    @FXML
    void onExit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Creating the BMI result table whenever the application begins.
     * Adding a listener to the Metric/English radio buttons to change the prompt text
     * within the text fields of the weight and height, to forewarn the user for the
     * desired inputs.
     * As well as adding a listener the pregnant/not pregnant radio buttons
     * to make sure we do not calculate values that include pregnant woman.
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        
        categoryLabel.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        bmiLabel.setCellValueFactory(cellData -> cellData.getValue().bmiProperty());
        riskLabel.setCellValueFactory(cellData -> cellData.getValue().riskProperty());
        
        createResultTable();
        
        calcFormat.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton format = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
                if (format.getText().equals(resources.getString("Metric"))) {
                    onClear();
                    height.setPromptText(resources.getString("metricFormat"));

                } else {
                    onClear();
                    height.setPromptText(resources.getString("englishFormat"));
                }
            }
        });
        
        preg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton preggo = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
                if (preggo.getText().equals(resources.getString("Pregnant"))) {
                    popUpWarnAlert(resources.getString("PregWarnTitle"), resources.getString("PregWarn"));
                    toggleCalculateButton(0);
                } else {
                    toggleCalculateButton(1);
                }
            }
        });     
        
    } // end of initialize
    
    /**
     * Clearing the height and weight text fields whenever the user switches
     * from English/Metric radio buttons so that the prompt text shows.
     */
    void onClear(){
        height.setText("");
        weight.setText("");
    }
    
    /**
     * Checks whether the user wants the format to be Metric or English
     * @return true == Metric || false == English
     */
    private boolean isMetricFormat(){
       RadioButton selectedFormat = (RadioButton) calcFormat.getSelectedToggle();
       String format = selectedFormat.getText();
       
        return format.equals(resources.getString("Metric"));      
    }
    
    /**
     * Validates the height/weight inputs through regex.
     * It checks if the text has numbers and/or a decimal inside.
     * @return true == valid || false == not valid
     * @param format 0 == Metric || 1 == English calculation formats.
     */
    private boolean validateInputs(int format) {
        boolean valid = false;
        if (format == 0) {
            if (height.getText().matches("[0-9]+\\.[0-9]+") || height.getText().matches("[0-9]+")) {
                if (weight.getText().matches("[0-9]+\\.[0-9]+") || weight.getText().matches("[0-9]+")) {
                    valid = true;
                }
            }
        } else {
            if (height.getText().matches("[0-9]'[0-9]+\"")) {
                if (weight.getText().matches("[0-9]+\\.[0-9]+") || weight.getText().matches("[0-9]+")) {
                    valid = true;
                }

            }
        }


        return valid;
    }
    
    /**
     * Sets the risk and category labels according to the resulting BMI
     * @param BMIresult 
     */
    private void setRiskAndCategory(double BMIresult){
        if (BMIresult < 18.5){
            category.setText(resources.getString("UnderweightCat"));
            risk.setText(resources.getString("UnderweightRisk"));
        }
        if (BMIresult > 18.5 && BMIresult < 24.9){
            category.setText(resources.getString("NormalweightCat"));
            risk.setText(resources.getString("NormalweightRisk"));        
        }
        if (BMIresult > 25.0 && BMIresult < 29.9){
            category.setText(resources.getString("OverweightCat"));
            risk.setText(resources.getString("OverweightRisk"));
        }
        if (BMIresult > 30.0 && BMIresult < 34.9){
            category.setText(resources.getString("Obese1Cat"));
            risk.setText(resources.getString("Obese1Risk"));
        }
        if (BMIresult > 35.0 && BMIresult < 39.9){
            category.setText(resources.getString("Obese2Cat"));
            risk.setText(resources.getString("Obese2Risk"));
        }
        if (BMIresult > 40){
            category.setText(resources.getString("Obese3Cat"));
            risk.setText(resources.getString("Obese3Risk"));
        }       
    }
    
    /**
     * Used to communicate with the user to warn about any invalidations.
     * @param title
     * @param content 
     */
    public void popUpWarnAlert(String title, String content){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Calculates the weight and height that the user entered with reference to 
     * the metric system.
     */
    public void calculateMetric(){
        //Checks with regex if height/weight are numeric
            if (validateInputs(0)) { // 0 --> metric
                //Metric format, create a Metric bean from the users' input
                Metric input = new Metric(Double.valueOf(height.getText()),
                        Double.valueOf(weight.getText()));
                //Validate user input
                if (input.validateHeightWeight()) {
                    //Calculates and setting the results
                    Double BMIresults = input.calculateBMI();
                    calculation.setText(String.valueOf(BMIresults));
                    setRiskAndCategory(BMIresults);

                } else {
                    //Invalid number range
                   popUpWarnAlert(resources.getString("invalidValueTitle") , resources.getString("invalidValue")); 
                }
            } else {
                //Invalid structure of text:                       
                popUpWarnAlert(resources.getString("invalidStructureTitle"),resources.getString("invalidStructureMetric"));
            }
    } // end of calcMetric
    
    /**
     * Calculates the weight and height that the user entered with reference to the
     * english system.
     */
    public void calculateEnglish(){
        //Checks with regex if height follows: 5'11" format, and weight is numeric.
            if (validateInputs(1)) { // 1 --> english
                English input = new English(height.getText(), Double.valueOf(weight.getText()));

                //Validate user input.
                if (input.validateHeightWeight()) {
                    //Calculates and setting the results
                    Double BMIresults = input.calculateBMI();
                    calculation.setText(String.valueOf(BMIresults));
                    setRiskAndCategory(BMIresults);
                } else {
                    //Invalid number range
                    popUpWarnAlert(resources.getString("invalidValueTitle") , resources.getString("invalidValue"));
                }
            } else {
                //Invalid structure of text:
                popUpWarnAlert(resources.getString("invalidStructureTitle"),resources.getString("invalidStructureEnglish"));
            }
            
        } // end of calcEnglish

    /**
     * Enables and disables the calcuation button depending on if the user is pregnant or not.
     * @param onoff 0 == disabled || 1 == enabled
     */
    public void toggleCalculateButton(int onoff){
       if (onoff == 0){
           calcButton.setDisable(true);
       } else {
           calcButton.setDisable(false);
       }
    }
    
    /**
     * Creates the BMI result table on the right of the stage to have all the 
     * information that it requires.
     */
    public void createResultTable(){
        ObservableList<BMIresultTable> rows = FXCollections.observableArrayList();
        rows.add(new BMIresultTable(resources.getString("UnderweightCat"),resources.getString("UnderweightBMI"), resources.getString("UnderweightRisk")));
        rows.add(new BMIresultTable(resources.getString("NormalweightCat"),resources.getString("NormalweightBMI"), resources.getString("NormalweightRisk")));
        rows.add(new BMIresultTable(resources.getString("OverweightCat"),resources.getString("OverweightBMI"), resources.getString("OverweightRisk")));
        rows.add(new BMIresultTable(resources.getString("Obese1Cat"),resources.getString("Obese1BMI"), resources.getString("Obese1Risk")));
        rows.add(new BMIresultTable(resources.getString("Obese2Cat"),resources.getString("Obese2BMI"), resources.getString("Obese2Risk")));
        rows.add(new BMIresultTable(resources.getString("Obese3Cat"),resources.getString("Obese3BMI"), resources.getString("Obese3Risk")));
        resultTable.setItems(rows);
    }

    
} // End of controller
