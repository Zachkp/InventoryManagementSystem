package controller;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * This class controls the modify product controller
 * @author Zachariah Kordas-Potter
 */
public class modifyProductController implements Initializable {
    @FXML
    private TextField productNameUserInputField;
    @FXML
    private TextField invUserInputField; // I.E Stock
    @FXML
    private TextField priceUserInputField;
    @FXML
    private TextField maxUserInputField;
    @FXML
    private TextField minUserInputField;

    private static Product productToBeModified = null;

    /**
     * This method switches back to the main form screen
     * @param event the event that launches the method
     * @throws IOException allows for the use of .load()
     */
    public void switchToMainForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partInventoryLevel;
    @FXML
    private TableColumn<Part, Double> partPrice;


    /**
     * This method initializes the text fields and table views.
     * It initializes the text fields with the selected products data and it initializes the table views with the list of parts
     * and the list of associated parts that are connected with the product
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        associatedPartsController.setAll(productToBeModified.getAllAssociatedParts());

        productNameUserInputField.setText(productToBeModified.getName());
        invUserInputField.setText(Integer.toString(productToBeModified.getStock()));
        priceUserInputField.setText(Double.toString(productToBeModified.getPrice()));
        maxUserInputField.setText(Integer.toString(productToBeModified.getMax()));
        minUserInputField.setText(Integer.toString(productToBeModified.getMin()));

        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        partTable.setItems(Inventory.getAllParts());

        associatedPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        associatedPartTable.setItems(associatedPartsController);
    }


    private ObservableList<Part> associatedPartsController = FXCollections.observableArrayList();
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartId;
    @FXML
    private TableColumn<Part, String> associatedPartName;
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryLevel;
    @FXML
    private TableColumn<Part, Double> associatedPartPrice;

    /**
     * This method controlls the add associated part button. It checks if the selectedPart is null or not then adds it
     * to the product
     * @param e the event that launches the method
     */
    public void addAssociatedProductButton(ActionEvent e) {
        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            System.out.println("selectedPart == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a part to add");
            alert.showAndWait();
            return;
        } else {
            associatedPartsController.add(selectedPart);
            System.out.println("selectedPart added to Associated Parts");
        }
    }

    /**
     * This method controls the delete associated part button. It checks if the selected part is null or not then
     * gets confirmation to remove it from the product
     * @param e the event that launches the method
     */
    public void deleteAssociatedPartButton(ActionEvent e) {
        System.out.println("~~ Associated Part Delete Button Pressed ~~");
        Part selectedPart = (Part) associatedPartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            System.out.println("selectedPart == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
            return;
        } else {
            System.out.println("selectedPart Deleted");
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to delete the Part?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedPartsController.remove(selectedPart);
                System.out.println("selectedPart Deleted");
            }
        }
    }

    /**
     * This method holds the product that got passed from the main form
     * @param selectedProduct the selectedProduct to be held
     */
    public static void holdProduct(Product selectedProduct) {
        productToBeModified = selectedProduct;
    }

    /**
     * This method controls the save product button. It gets all the user data then calls the checkForErrors() method
     * and if there are no errors it adds updates the product
     * @param e the event that launches the method
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
                Product newProduct = new Product(productToBeModified.getId(), productName, productPrice, productStock, productMin, productMax);
                for (Part p : associatedPartsController) {
                    newProduct.addAssociatedPart(p);
                }
                Inventory.updateProduct(getProductIndex(productToBeModified), newProduct);
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
     * This method checks all the user entered data for errors.
     * @param name the name to be checked
     * @param price the price to be checked
     * @param stock the stock to be checked
     * @param min the min to be checked
     * @param max the max to be checked
     * @return true or false depending on if the user entered data has errors or not
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

    /**
     * This method gets the index of the product passed to it.
     * @param productToBeFound the productToBeFound that needs its index
     * @return the index of the passed product
     */
    private int getProductIndex(Product productToBeFound) {
        ObservableList<Product> allProducts = FXCollections.observableArrayList();
        allProducts = Inventory.getAllProducts();
        return allProducts.indexOf(productToBeFound);
    }
}
