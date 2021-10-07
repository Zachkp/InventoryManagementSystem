package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.*;
import model.Inventory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

/**
 * This class is the main controller for the main form
 * @author Zachariah Kordas-Potter
 */
public class Controller implements Initializable {
    private static boolean firstTime = true;

    Part p = new InHouse(123, "test", 12.30, 12, 1, 2, 3);
    Part p2 = new Outsourced(456, "test two", 4.56, 45, 4, 6, "test company");

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    //LINES BELOW ARE FOR THE TABLE VIEW
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partInventoryLevel;
    @FXML private TableColumn<Part, Double> partPrice;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productId;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Integer> productInventoryLevel;
    @FXML private TableColumn<Product, Double> productPrice;

    @FXML private TextField partSearchField, productSearchField;

    /**
     * This method switches to the Add Part form
     * @param event the event that launches the method
     * @throws IOException to enable the use of .load()
     */
    public void switchToAddPartForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/addPartForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method switches to the Modify Part form
     * @param event the event that launches the method
     * @throws IOException to enable the use of .load()
     */
    public void switchToModifyPartForm(ActionEvent event) throws IOException {
        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null){
            System.out.println("selectedPart == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR ERROR ERROR");
            alert.setContentText("Please select a valid PART from the table");
            alert.showAndWait();
            return;
        } else {
            System.out.println("selectedPart not null sent to be modified");
            modifyPartController.holdPart(selectedPart);

            root = FXMLLoader.load(getClass().getResource("/view/modifyPartForm.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method switches to the Add Product form
     * @param event the event that launches the method
     * @throws IOException to enable the use of .load()
     */
    public void switchToAddProductForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/addProductForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method switches to the Modify Product form
     * @param event the event that launches the method
     * @throws IOException to enable the use of .load()
     */
    public void switchToModifyProductForm(ActionEvent event) throws IOException {
        Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            System.out.println("selectedProduct == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR ERROR ERROR");
            alert.setContentText("Please select a valid PRODUCT from the table");
            alert.showAndWait();
            return;
        } else {
            modifyProductController.holdProduct(selectedProduct);
            root = FXMLLoader.load(getClass().getResource("/view/modifyProductForm.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method initializes the two table views in the main form with data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        productId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        partTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());
    }

    /**
     * This method is for the delete part button. It takes the selected part checks that it isnt null then deletes it after
     * confirmation
     * @param e the event that launches the method
     */
    public void delPartButton(ActionEvent e) {
        System.out.println("~~ Part Delete Button Pressed ~~");

        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            System.out.println("selectedPart == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
            return;
        }
        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to delete the Part?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
                System.out.println("selectedPart Deleted");
            }
        }
    }

    /**
     * This method controls the search bar. Calls the lookupPart methods in Inventory and checks that they were found then
     * displays them.
     * @param e the event that launches the method
     */
    public  void searchPartBar(ActionEvent e) {
        String query = partSearchField.getText();
        ObservableList<Part> foundParts = Inventory.lookupPart(query);
        try {
            if (foundParts.size() == 0) {
                int queryPartId = Integer.parseInt(query);
                Part foundPartById = Inventory.lookupPart(queryPartId);
                if (foundPartById != null) {
                    foundParts.add(foundPartById);
                }
            }
        }
        catch (NumberFormatException exc) {
            //ignore
        }

        partTable.setItems(foundParts);
        partSearchField.setText("");
    }

    /**
     * This method controls the search product bar. Calls the lookupProduct method in Inventory and checks that the
     * products were found then displays them
     * @param e the event that launches the method
     */
    public  void searchProductBar(ActionEvent e) {

        String query = productSearchField.getText();
        ObservableList<Product> foundProducts = Inventory.lookupProduct(query);
        try {
            if (foundProducts.size() == 0) {
                int queryProductId = Integer.parseInt(query);
                Product foundProductById = Inventory.lookupProduct(queryProductId);
                if (foundProductById != null) {
                    foundProducts.add(foundProductById);
                }
            }
        }
        catch (NumberFormatException exc) {
            //ignore
        }
        productTable.setItems(foundProducts);
        productSearchField.setText("");
    }

    /**
     * This method controls the delete product button. It takes the selected product checks if it is null and checks if
     * it has associated parts attached to it then deletes the seleceted product
     * @param e the event that launches the method
     */
    public void delProductButton(ActionEvent e) {
        System.out.println("~~ Product Delete Button Pressed ~~");
        Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("selectedProduct == null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();
            return;
        } else if (selectedProduct.getAllAssociatedParts().isEmpty() == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can not delete a product that has parts associated with it");
            alert.showAndWait();
            return;
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to delete the Product?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                System.out.println("selectedProduct Deleted");
            }
        }
    }

    /**
     * This method controls the exit button. It closes and ends the whole program
     * @param e the event that launches the method
     */
    public void exitButton(ActionEvent e){
        System.out.println("exit button Pressed");
        Platform.exit();
    }
}

