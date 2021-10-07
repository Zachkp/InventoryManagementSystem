package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
 * This class controlls the modify part form
 * @author Zachariah Kordas-Potter
 */
public class modifyPartController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static Part partToBeModified = null;

    /**
     * This method switches to the main form
     * @param event the event that launches the method
     * @throws IOException allows the use of .load()
     */
    public void switchToMainForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method initializes the text fields with the data to be modified
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partNameuserInputField.setText(partToBeModified.getName());
        invUserInputField.setText(Integer.toString(partToBeModified.getStock()));
        priceUserInputField.setText(Double.toString(partToBeModified.getPrice()));
        maxUserInputField.setText(Integer.toString(partToBeModified.getMax()));
        minUserInputField.setText(Integer.toString(partToBeModified.getMin()));

        if (partToBeModified instanceof InHouse) {
            inHouseButton.setSelected(true);
            machineAndCompanyInputField.setText(Integer.toString((((InHouse) partToBeModified).getMachineId())));
        }
        if (partToBeModified instanceof Outsourced) {
            outsourceButton.setSelected(true);
            machineAndCompanyInputField.setText(((Outsourced) partToBeModified).getCompanyName());
        }
    }

     // LINES BELOW ARE FOR THE RADIO BUTTON CONTROLS IN ADD-PART FORM
    @FXML private RadioButton inHouseButton, outsourceButton;
    @FXML private Label companyName, machineId;
    @FXML private TextField companyNameTextField, machineIdTextField;

    /**
     * This method controls the radio buttons in the form
     * @param event the event launches the method
     */
    public void modifyPartFormButtons(ActionEvent event) {
            
        if(inHouseButton.isSelected()) {
            companyName.setVisible(false);
            machineId.setVisible(true);
        }
        else if(outsourceButton.isSelected()) {
            companyName.setVisible(true);
            machineId.setVisible(false);
        }
        else {
            companyName.setVisible(false);
            machineId.setVisible(true);
        }
    }

    @FXML private TextField partNameuserInputField, invUserInputField, priceUserInputField,
            maxUserInputField, minUserInputField, machineAndCompanyInputField;

    /**
     * This method controls the save button. It gets all the user entered data then calls the checkForErrors() method
     * and updates the part that the user wants to modify
     * @param e the event that launches the method
     * @throws IOException allows for the use of the try catch block
     */
    public void saveButton(ActionEvent e) throws IOException {
        System.out.println("~~~~SAVE BUTTON PRESSED~~~~~~");

        String partName, partCompanyName;
        String machineAndCompany = "";
        int partStock, partMin, partMax, partMachineId;
        double partPrice;

        try {
            partName = partNameuserInputField.getText();
            partPrice = Double.parseDouble(priceUserInputField.getText());
            partStock = Integer.parseInt(invUserInputField.getText());
            partMin = Integer.parseInt(minUserInputField.getText());
            partMax = Integer.parseInt(maxUserInputField.getText());

            machineAndCompany = machineAndCompanyInputField.getText();
            partMachineId = Integer.parseInt(machineAndCompany);

            if (checkForErrors(partName, partPrice, partStock, partMin, partMax, partMachineId, machineAndCompany) == true) {
                if (inHouseButton.isSelected()) {
                    Part newInHousePart = new InHouse(partToBeModified.getId(), partName, partPrice, partStock, partMin, partMax, partMachineId);
                    Inventory.updatePart(getPartIndex(partToBeModified), newInHousePart);
                } else {
                    partCompanyName = machineAndCompany;
                    Part newOutsourcedPart = new Outsourced(partToBeModified.getId(), partName, partPrice, partStock, partMin, partMax, partCompanyName);
                    Inventory.updatePart(getPartIndex(partToBeModified), newOutsourcedPart);
                }
                switchToMainForm(e);
            }
        } catch (NumberFormatException exception) {
            System.out.println("CATCH WORKS");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR ERROR ERROR");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }
    }

    /**
     * This method checks all the user entered data for errors
     * @param name the name to be checked
     * @param price the price to be checked
     * @param stock the stock the be checked
     * @param min the min to be checked
     * @param max the max to be checked
     * @param machineId the machineId to be checked
     * @param companyName the companyName to be checked
     * @return true or false depending if the user data has errors or not
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
        } else if (stock != (int) stock || stock > max) {
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

    /**
     * This method gets the index of the part.
     * @param partToBeFound the partToBeFound is the part that needs to have its index found
     * @return the index of partToBeFound
     */
    private int getPartIndex(Part partToBeFound) {
        ObservableList<Part> allParts = FXCollections.observableArrayList();
        allParts = Inventory.getAllParts();
        return allParts.indexOf(partToBeFound);
    }

    /**
     * This method holds the selected part from the main form
     * @param selectedPart the selected part that needs to be modified
     */
    public static void holdPart(Part selectedPart){
        partToBeModified = selectedPart;
    }
}
