package model;

/**
 *  This class is a child of the Part class and allows for different types of parts
 * @author Zachariah Kordas-Potter
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * This method allows for the creation of an Outsourced type of Part
     * @param id the id to be set
     * @param name the name to be set
     * @param price the price to be set
     * @param stock the stock to be set
     * @param min the min to be set
     * @param max the max to be set
     * @param companyName the companyName to be set
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }
    /**
     * This method sets the companyName that is passed to it
     * @param companyName the companyName to be set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * This method gets the company name
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
