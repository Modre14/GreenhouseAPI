/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guigreenhouseapi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Morten
 */
public class GUIGreenhouseAPI extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/lightprogress.css");
        scene.getStylesheets().add("/tempprogress.css");
        scene.getStylesheets().add("/waterprogress.css");
        scene.getStylesheets().add("/intensityprogress.css");
        
        stage.setScene(scene);  
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }
    
}
