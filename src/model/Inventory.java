
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
	private static ObservableList<Part> allParts = FXCollections.observableArrayList();
	private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

	// Add methods
	public static void addPart(Part newPart) {
		allParts.add(newPart);
	}

	public static void addProduct(Product newProduct) {
		allProducts.add(newProduct);
	}

	// Search methods
	public static Part searchPartByID(int partId) {
		for (Part part : allParts) {
			if (part.getId() == partId) {
				return part;
			}
		}
		return null;
	}

	public static Product searchProductByID(int productId) {
		for (Product product : allProducts) {
			if (product.getId() == productId) {
				return product;
			}
		}
		return null;
	}

	public static ObservableList<Part> searchPartByName(String name) {
		ObservableList<Part> matchingParts = FXCollections.observableArrayList();
		for (Part part : allParts) {
			if (part.getName().toLowerCase().contains(name.toLowerCase())) {
				matchingParts.add(part);
			}
		}
		return matchingParts;
	}

	public static ObservableList<Product> searchProductByName(String name) {
		ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
		for (Product product : allProducts) {
			if (product.getName().toLowerCase().contains(name.toLowerCase())) {
				matchingProducts.add(product);
			}
		}
		return matchingProducts;
	}

	// Update methods
	public static void updatePart(int index, Part selectedPart) {
		allParts.set(index, selectedPart);
	}

	public static void updateProduct(int index, Product newProduct) {
		allProducts.set(index, newProduct);
	}

	public static boolean deletePart(Part selectedPart) {
		boolean isDeleted = false;
		if (allParts.contains(selectedPart)) {
			allParts.remove(selectedPart);
			isDeleted = true;
		}
		return isDeleted;
	}

	
	public static boolean deletedProduct(Product selectedProduct) {
		boolean isDeleted = false;
		if (allProducts.contains(selectedProduct)) {
			allProducts.remove(selectedProduct);
			isDeleted = true;
		}
		return isDeleted;
	}

	// Get methods
	public static ObservableList<Part> getAllParts() {
		return allParts;
	}

	public static ObservableList<Product> getAllProducts() {
		return allProducts;
	}
}
