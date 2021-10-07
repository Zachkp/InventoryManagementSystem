package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;
import model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class controls all of the actions on the add product form
 * @author Zachariah Kordas-Potter
 */
public class addProductController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMainForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //table view shiet
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partInventoryLevel;
    @FXML private TableColumn<Part, Double> partPrice;

    /**
     * This method initializes the two table views with data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        partTable.setItems(Inventory.getAllParts());

        associatedPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        associatedPartTable.setItems(getAllAssociatedParts());
    }

    //TEXT FIELD DECLARATIONS FOR SAVE BUTTON
    @FXML private TextField productNameUserInputField;
    @FXML private TextField invUserInputField; // I.E Stock
    @FXML private TextField priceUserInputField;
    @FXML private TextField maxUserInputField;
    @FXML private TextField minUserInputField;

    private static int autoGenId = 1;

    /**
     * This method controls the save button in the add procut form. It gets all the user entered data
     * and created a new product with that data.
     * @param e the event that activates the method
     */
    public void saveProductButton(ActionEvent e) {
        System.out.println("~~~~SAVE BUTTON PRESSED~~~~~~");
        String productName;
        double productPrice;
        int productStock;
        int productMin;
        int productMax;

        try {
            productName = productNameUserInputField.getText();
            productPrice = Double.parseDouble(priceUserInputField.getText());
            productStock = Integer.parseInt(invUserInputField.getText());
            productMin = Integer.parseInt(minUserInputField.getText());
            productMax = Integer.parseInt(maxUserInputField.getText());

            if (checkForErrors(productName, productPrice, productStock, productMin, productMax) == true) {
                Product newProduct = new Product(autoGenId, productName, productPrice, productStock, productMin, productMax);
                for (Part p : associatedPartsController) {
                    newProduct.addAssociatedPart(p);
                }

                Inventory.addProduct(newProduct);
                autoGenId++;

                switchToMainForm(e);
            }
        } catch (NumberFormatException | IOException exception) {
            System.out.println("CATCH WORKS");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR ERROR ERROR");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }
    }

    /**
     * This method checks all the user entered data for various errors
     * @param name the name to be checked
     * @param price the price to be checked
     * @param stock the stock to be checked
     * @param min the min to be checked
     * @param max the max to be checked
     * @return true if no errors and false if errors are present
     */
    public boolean checkForErrors (String name, double price, int stock, int min, int max) {
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
            alert.setContentText("Please enter an integer for the max field that is greater than the min and equal to or less the total stock");
            alert.showAndWait();
            return false;
        } else return true;
    }


    private static ObservableList<Part> associatedPartsController = FXCollections.observableArrayList();
    @FXML private TableView<Part> associatedPartTable;
    @FXML private TableColumn<Part, Integer> associatedPartId;
    @FXML private TableColumn<Part, String> associatedPartName;
    @FXML private TableColumn<Part, Integer> associatedPartInventoryLevel;
    @FXML private TableColumn<Part, Double> associatedPartPrice;

    /**
     * This method adds parts to an observable list so they can be added to the product
     * @param e the event that starts the method
     */
    public void addAssociatedProductButton(ActionEvent e) {
        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            System.out.println("selectedPart == null");
            return;
        }
        if (selectedPart != null) {
            associatedPartsController.add(selectedPart);
        }
    }

    /**
     * This method removes parts from the observable list so they do not get added to the product
     * @param e the event that starts the method
     */
    public void deleteAssociatedPartButton(ActionEvent e) {
        System.out.println("~~ Associated Part Delete Button Pressed ~~");
        Part selectedPart = (Part) associatedPartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            System.out.println("selectedPart == null");
            return;
        } else {
            System.out.println("selectedPart Deleted");
            associatedPartsController.remove(selectedPart);
        }
    }

    /**
     * This methed gets all the associated parts
     * @return the associatedPartsController
     */
    public static ObservableList<Part> getAllAssociatedParts() {
        return associatedPartsController;
    }
}
