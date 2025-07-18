package com.coffeepdf.coffeepdf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the CoffeePDF JavaFX application.
 * Loads the main PDF viewer interface from FXML and displays it.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coffeepdf/coffeepdf/pdf_preview.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("CoffeePDF");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
