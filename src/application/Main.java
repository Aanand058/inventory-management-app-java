
package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/MainMenu.fxml"));
			Scene scene = new Scene(root, 920, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// Sample Data
//		Inventory.addPart(new InHouse(getUniquePartId(), "Mouse", 250, 18, 1, 35, 11231));
//		Inventory.addPart(new InHouse(getUniquePartId(), "Wheel", 191.00, 90, 1, 244, 1312));
//		Inventory.addPart(new Outsourced(getUniquePartId(), "Tablet", 995.00, 10, 1, 10, "Apple"));
//		Inventory.addPart(new InHouse(getUniquePartId(), "KeyBoard", 250, 18, 1, 35, 11231));
//
//		Product Apple = new Product(getUniqueProdId(), "Apple", 3343.434, 34, 1, 10);
//		Apple.addAssociatedPart(Inventory.getAllParts().get(0));
//		Inventory.addProduct(Apple);
//		
//		
//		Product Car = new Product(getUniqueProdId(), "Car", 45000, 15, 1, 10);
//		Car.addAssociatedPart(Inventory.getAllParts().get(1));
//		Inventory.addProduct(Car);
//
//		
//
//		Product Samsung = new Product(getUniqueProdId(), "Samsung", 12000, 87, 4, 1220);
//		Samsung.addAssociatedPart(Inventory.getAllParts().get(2));
//		Samsung.addAssociatedPart(Inventory.getAllParts().get(3));
//		Inventory.addProduct(Samsung);

		launch(args);
	}

	
	//Auto-Generate Parts and Products Number 
	public static int partId = 1111;
	public static int productId = 5555;

	public static int getUniquePartId() {
		return partId++;
	}

	public static int getUniqueProdId() {
		return productId++;
	}

}
