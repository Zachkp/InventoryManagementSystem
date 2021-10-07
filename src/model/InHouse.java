package model;


/**
 *  This class houses a child of the Part class and allows for different types of parts
 * @author Zachariah Kordas-Potter
 */

public class InHouse extends Part {

    private int machineId;

    /**
     * This method allows for the creation of an InHouse type of Part
     * @param id the id to be set
     * @param name the name to be set
     * @param price the price to be set
     * @param stock the stock to be set
     * @param min the min to be set
     * @param max the max to be set
     * @param machineId the machineId to be set
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * This method sets the machineId that is passed to it
     *  @param machineId the machineId to be set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * This method gets the machine id
     *  @return the machineId
     */
    public int getMachineId(){
        return machineId;
    }
}
