package com.rimidev.bmicalculator;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BMIapp extends Application {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    private Stage primaryStage;
    private Parent rootPane;
    
    private final Locale currentLocale;
    
    //Default contructor
     public BMIapp() {
        super();
        currentLocale = new Locale("en", "CA");
    }

     /**
      * Dealing with the stage and displaying it.
      * @param primaryStage
      * @throws Exception 
      */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.primaryStage = primaryStage;
        
        //Bring stage foward
        initRootLayout();
        
        // Set the window title
        this.primaryStage.setTitle(ResourceBundle.getBundle("MessageBundle", currentLocale).getString("bmiCalculator"));
        // Raise the curtain on the Stage
        primaryStage.show(); 
    }
    
    /**
     * Creating the stage and bringing it forward
     * @throws IOException 
     */
    public void initRootLayout() throws IOException {
      
            // Instantiate a FXMLLoader object
            FXMLLoader loader = new FXMLLoader();

            // Configure the FXMLLoader with the i18n locale resource bundles
            loader.setResources(ResourceBundle.getBundle("MessageBundle", currentLocale));
            // Connect the FXMLLoader to the fxml file that is stored in the jar
            loader.setLocation(BMIapp.class
                    .getResource("/fxml/bmiCalculator.fxml"));

            // The load command returns a reference to the root pane of the fxml file
            rootPane = (VBox) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootPane);

            // Put the Scene on the Stage
            primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
