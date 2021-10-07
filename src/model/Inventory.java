package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class holds all the functions that allow the addition, searching, editing, and deletion of both parts and products.
 * It also holds all their data
 * <p>
 * FUTURE ENHANCEMENT -- For a future enhancement we could set the lists allParts and allProducts with a database from a SQL file or
 * an excel sheet or something along those lines that way the added parts and products would be saved more permanently
 * and would allow for more use cases from the users
 * </p>
 * @author Zachariah Kordas-Potter
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method adds a newPart to the allParts observable list
     * @param newPart the newPart to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * This method adds a newProduct to the allProducts observable list
     * @param newProduct the newProduct to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This method searches for a part that is in allParts by using the partId
     * @param partId the partId being used to locate the Part
     * @return searchedPart
     */
    public static Part lookupPart(int partId) {
        for(Part searchedPart : allParts) {
            if(searchedPart.getId() == partId){
                return searchedPart;
            }
        }
        return null;
    }

    /**
     * This method searches for a product that is in allProducts by using the productId
     * @param productId the productId being used to locate the Product
     * @return searchedProduct
     */
    public static Product lookupProduct(int productId) {
        for(Product searchedProduct : allProducts) {
            if(searchedProduct.getId() == productId){
                return searchedProduct;
            }
        }
        return null;
    }

    /**
     * This method searches for a Part that is in allParts by using the partName
     * @param partName the partName being used to locate the Part
     * @return namedParts
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();

        for(Part searchedPart : allParts) {
            if (searchedPart.getName().contains(partName)) {
                namedParts.add(searchedPart);
            }
        }
        return namedParts;
    }

    /**
     * This method searches for a Product that is in allProducts by using the productName
     * @param productName the productName being used to locate the Product
     * @return namedProducts
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();

        for(Product searchedProduct : allProducts) {
            if (searchedProduct.getName().contains(productName)) {
                namedProducts.add(searchedProduct);
            }
        }
        return namedProducts;
    }

    /**
     * This method updates a Part by replacing the part at the given index with the newPart
     * @param index the index being used to locate the part being updated
     * @param newPart the newPart is the updated part
     */
    public static void updatePart(int index, Part newPart) {
        allParts.remove(index);
        allParts.add(index, newPart);
    }

    /**
     * This method updates a Product by replacing the product at the given index with the newProduct
     * @param index the index being used to locate the product being update
     * @param newProduct the newProduct is the updated product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.remove(index);
        allProducts.add(index, newProduct);
    }

    /**
     * This method removes a part from the allParts list
     * @param selectedPart the selectedPart that is being removed
     * @return true
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * This method removes a product the allProducts list
     * @param selectedProduct the selectedProduct that is being removed
     * @return true
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * This method gets all the parts in the allParts list
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method gets all the products in the allProducts list
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
