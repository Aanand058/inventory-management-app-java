package model;

import java.io.Serializable;

public class Example implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String breed;

	public Example(String name, String breed) {
		this.name = name;
		this.breed = breed;
	}

	@Override
	public String toString() {
		return "Example [name=" + name + ", breed=" + breed + "]";
	}
	
	
	
}
