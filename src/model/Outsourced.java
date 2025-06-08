
package model;

import java.io.Serializable;

public class Outsourced extends Part implements Serializable {
	private String companyName;

	// Constructor
	public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
		super(id, name, price, stock, min, max);
		this.companyName = companyName;
	}

	// Setter method for companyName
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	// Getter method for companyName
	public String getCompanyName() {
		return companyName;
	}
}
