package controller;


import java.io.IOException;

import javafx.scene.control.Alert;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class controlls all of actions in the add part form.
 * <p>
 * RUNTIME ERROR --- When attempting to get the user data from the text fields if they for exampled entered a string
 * of letters for the inventory instead of integers the program would close and print a large stack trace
 * in order to fix this i created the checkForErrors() method to show dialog boxes instead of crashing
 * </p>
 * @author Zachariah Kordas-Potter
 */
public class addPartController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * This method switched to the main form
     * @param event the event that activates the method
     * @throws IOException if the input is incorrect
     */
    public void switchToMainForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // LINES BELOW ARE FOR THE RADIO BUTTON CONTROLS IN ADD-PART FORM
    @FXML
    private RadioButton inHouseButton, outsourceButton;
    @FXML
    private Label companyName, machineId;
    @FXML
    private TextField companyNameTextField, machineIdTextField;

    /**
     * This method controls the radio buttons in the add part form
     * @param event the event that activates the method
     */
    public void addPartFormButtons(ActionEvent event) {
        if (inHouseButton.isSelected()) {
            companyName.setVisible(false);
            machineId.setVisible(true);
        } else if (outsourceButton.isSelected()) {
            companyName.setVisible(true);
            machineId.setVisible(false);
        } else {
            companyName.setVisible(false);
            machineId.setVisible(true);
        }
    }

    //TEXT FIELD DECLARATIONS FOR SAVE BUTTON
    @FXML
    private TextField partNameuserInputField;
    @FXML
    private TextField invUserInputField; // I.E Stock
    @FXML
    private TextField priceUserInputField;
    @FXML
    private TextField maxUserInputField;
    @FXML
    private TextField minUserInputField;
    @FXML
    private TextField machineAndCompanyInputField;

    private static int autoGenId = 1;

    /**
     * This method controlls the save button in the add part form. It gets all the user enetered data
     * and uses it to create new parts
     * @param event the event that activates the method
     * @throws IOException if the input was incorrect
     */
    public void saveButton(ActionEvent event) throws IOException {
        System.out.println("~~~~SAVE BUTTON PRESSED~~~~~~");

        int partMachineId;
        String partCompanyName;

        try {
            String partName = partNameuserInputField.getText();
            double partPrice = Double.parseDouble(priceUserInputField.getText());
            int partStock = Integer.parseInt(invUserInputField.getText());
            int partMin = Integer.parseInt(minUserInputField.getText());
            int partMax = Integer.parseInt(maxUserInputField.getText());

            String machineAndCompany = machineAndCompanyInputField.getText();
            partMachineId = Integer.parseInt(machineAndCompany);

            if (checkForErrors(partName, partPrice, partStock, partMin, partMax, partMachineId, machineAndCompany) == true) {

              if (inHouseButton.isSelected()) {
                   Inventory.addPart(new InHouse(autoGenId, partName, partPrice, partStock, partMin, partMax, partMachineId));
              } else {
                  partCompanyName = machineAndCompany;
                  Inventory.addPart(new Outsourced(autoGenId, partName, partPrice, partStock, partMin, partMax, partCompanyName));
              }
              autoGenId++;
              switchToMainForm(event);
            }
        }
        catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Blank Text Field Error");
            alert.setContentText("Please do not leave any field blank");
            alert.showAndWait();
        }
    }

    /**
     * This method checks the user input for any errors that the user may have entered
     * @param name the name to be checked
     * @param price the price to be checked
     * @param stock the stock to be checked
     * @param min the min to be checked
     * @param max the max to be checked
     * @param machineId the machineId to be checked
     * @param companyName the companyName to be checked
     * @return true or false depending if the user entered data has errors
     */
    public boolean checkForErrors (String name, double price, int stock, int min, int max, int machineId, String companyName) {
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Name text field error");
            alert.setContentText("Please do not leave the name field blank");
            alert.showAndWait();
            return false;
        } else if (price != (double) price) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Price text field error");
            alert.setContentText("Please enter the price in dollars & cents");
            alert.showAndWait();
            return false;
        } else if (stock != (int) stock || stock < max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock text field error");
            alert.setContentText("Please enter a valid integer for the stock field");
            alert.showAndWait();
            return false;
        } else if (min != (int) min || min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Min text field error");
            alert.setContentText("Please enter an integer that is less than the max");
            alert.showAndWait();
            return false;
        } else if (max != (int) max || max < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Max text field error");
            alert.setContentText("Please enter an integer for the max field that is greater than the min and equal than the total stock");
            alert.showAndWait();
            return false;
        } else if(inHouseButton.isSelected()) {
            if (machineId != (int) machineId) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("machine ID text field error");
                alert.setContentText("Please enter an integer for the machineId field");
                alert.showAndWait();
                return false;
            } else return true;
        } else if (outsourceButton.isSelected()) {
            if (companyName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Company Name text field error");
                alert.setContentText("Please do not leave the company name field blank");
                alert.showAndWait();
                return false;
            } else return true;
        } else {
            return true;
        }
    }
}



