package model;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 *  This class creates an app that allows the user to add, modify, and remove parts and products from table views
 *  @author Zachariah Kordas-Potter
 */
public class Gui extends Application
{

    /**
     *  This is the start method that initializes the mainForm once the program is launched
     * @param primaryStage the primaryStage to be shown
     * @throws Exception that prints a stack trace
     */
   @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
            Scene sceneOne = new Scene(root);
            primaryStage.setScene(sceneOne);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
   }
    /**
     *  This is the main method it is the first method that gets called and it launches the java program
     *  @param args the arguments to be launched
     */
    public static void main(String[] args) {
        launch(args);
    }
}