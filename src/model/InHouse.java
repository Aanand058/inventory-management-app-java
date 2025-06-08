
package model;

import java.io.Serializable;

public class InHouse extends Part implements Serializable {
    private int machineId;

    // Constructor
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    // Setter method for machineId
    public void setMachine(int machineId) {
        this.machineId = machineId;
    }

    // Getter method for machineId
    public int getMachine() {
        return machineId;
    }
}
