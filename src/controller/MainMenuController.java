
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import database.DatabaseAccess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Example;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainMenuController implements Initializable {

	private DatabaseAccess da;

	public MainMenuController() {
		da = new DatabaseAccess();
	}

	@FXML
	private Button addPartBtn;

	@FXML
	private Button addProductBtn;

	@FXML
	private Button deletePartBtn;

	@FXML
	private Button deleteProductBtn;

	@FXML
	private Button exitBtn;

	@FXML
	private Button modifyPartBtn;

	@FXML
	private Button modifyProductBtn;

	@FXML
	private TableColumn<Part, Integer> partIdCol;

	@FXML
	private TableColumn<Part, Integer> partInvCol;

	@FXML
	private TableColumn<Part, String> partNameCol;

	@FXML
	private TableColumn<Part, Double> partPriceCol;

	@FXML
	private TableView<Part> partTableV;

	@FXML
	private TableColumn<Product, Integer> productIdCol;

	@FXML
	private TableColumn<Product, Integer> productInvCol;

	@FXML
	private TableColumn<Product, String> productNameCol;

	@FXML
	private TableColumn<Product, Double> productPriceCol;

	@FXML
	private TableView<Product> productTableV;

	@FXML
	private TextField searchPart;

	@FXML
	private TextField searchProduct;

	// Rows Selection
	public static Part selectedPartRow;
	public static Product selectedProductRow;

	static Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
	public static Optional<ButtonType> confirmResult;
	private static Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	static Alert errorAlert = new Alert(Alert.AlertType.ERROR);

	@FXML
	void exitMainMenu(ActionEvent event) {
		Platform.exit();
	}

	// Search Part
	@FXML
	void handleSearchPart(KeyEvent event) {
		if (isEntered(event) && isNumber()) {
			searchPartById();
		} else if (isEntered(event) && isString()) {
			searchPartByName();
		} else {
			partTableV.setItems(Inventory.getAllParts());
		}
	}

	// Enter Key Pressed by the Keyboard
	private boolean isEntered(KeyEvent event) {
		return event.getCode().equals(KeyCode.ENTER);
	}

	private void searchPartByName() {
		ObservableList<Part> result = Inventory.searchPartByName(searchPart.getText());
		if (result.size() > 0) {
			partTableV.setItems(result);
		} else {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Part is not found");
			infoAlert.showAndWait();
		}
	}

	private void searchPartById() throws NumberFormatException {
		var part = Inventory.searchPartByID(Integer.parseInt(searchPart.getText()));
		if (part == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Part is not found");
			infoAlert.showAndWait();
		} else {
			ObservableList<Part> result = FXCollections.observableArrayList();
			result.add(part);
			partTableV.setItems(result);
		}
	}

	// This regex matches strings containing only alphabetical characters (both
	// uppercase and lowercase) and spaces.
	private boolean isString() {
		return searchPart.getText() != null && searchPart.getText().matches("^[a-zA-Z\\s]*$");
	}

	// This regex matches strings containing only numeric digits (0-9).
	private boolean isNumber() {
		return searchPart != null && searchPart.getText().matches("^[0-9]*$");
	}

	@FXML
	void handleSearchProduct(KeyEvent event) {
		if (isEntered(event) && isProdNumber()) {
			searchProdById();
		} else if (isEntered(event) && isProdString()) {
			searchProdByName();
		} else {
			productTableV.setItems(Inventory.getAllProducts());
		}

	}

	private void searchProdByName() {
		ObservableList<Product> result = Inventory.searchProductByName(searchProduct.getText());
		if (result.size() > 0) {
			productTableV.setItems(result);
		} else {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Product not found");
			infoAlert.showAndWait();
		}
	}

	private void searchProdById() throws NumberFormatException {
		var prod = Inventory.searchProductByID(Integer.parseInt(searchProduct.getText()));
		if (prod == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Product not found");
			infoAlert.showAndWait();
		} else {
			ObservableList<Product> result = FXCollections.observableArrayList();
			result.add(prod);
			productTableV.setItems(result);
		}
	}

	private boolean isProdString() {
		return searchProduct.getText() != null && searchProduct.getText().matches("^[a-zA-Z\\s]*$");
	}

	private boolean isProdNumber() {
		return searchProduct != null && searchProduct.getText().matches("^[0-9]*$");
	}

	// Add Part Btn
	@FXML
	void handleaddPartBtn(ActionEvent event) throws IOException {

		setScene(event, "/views/addPartForm.fxml");
	}

	// Add Product Btn
	@FXML
	void handleaddProductBtn(ActionEvent event) throws IOException {
		setScene(event, "/views/addProductForm.fxml");

	}

	// Delete Part Btn
	@FXML
	void handledeletePartBtn(ActionEvent event) {
		selectedPartRow = partTableV.getSelectionModel().getSelectedItem();
		if (selectedPartRow == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Please select a row");
			infoAlert.showAndWait();
		} else {
			confirmAlert.setTitle("Part");
			confirmAlert.setHeaderText("Delete");
			confirmAlert.setContentText("Are you sure you want to delete it?");
			confirmResult = confirmAlert.showAndWait();
			if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
				boolean isDeleted = Inventory.deletePart(selectedPartRow);
				if (!isDeleted) {
					errorAlert.setTitle("Error");
					errorAlert.setHeaderText("Error Occurred");
					errorAlert.setContentText("Error! Try Agaain");
				}
			}
		}

	}

	// Delete Product Btn
	@FXML
	void handledeleteProductBtn(ActionEvent event) {
		Product productSelectedRow = productTableV.getSelectionModel().getSelectedItem();
		Product prod = null;
		if (productSelectedRow == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Please select a row");
			infoAlert.showAndWait();

		} else {
			if (productSelectedRow.getAllAssociatedParts().size() > 0) {
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText("Product");
				errorAlert.setContentText("Product contains Parts, Delete parts first and proceed");
				errorAlert.showAndWait();
			} else {
				confirmAlert.setTitle("Part");
				confirmAlert.setHeaderText("Delete");
				confirmAlert.setContentText("Are you sure you want to delete it?");
				confirmResult = confirmAlert.showAndWait();
				if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
					for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
						if (Inventory.getAllProducts().get(i).getId() == productSelectedRow.getId()) {
							prod = Inventory.getAllProducts().get(i);
						}
					}
				}
				boolean isDeleted = Inventory.deletedProduct(prod);
				if (!isDeleted) {
					errorAlert.setTitle("Error");
					errorAlert.setHeaderText("Error Occurred");
					errorAlert.setContentText("Error! Try Agaain");
				}
			}
		}
	}

	// Modify Part Btn
	@FXML
	void handlemodifyPartBtn(ActionEvent event) throws IOException {
		selectedPartRow = partTableV.getSelectionModel().getSelectedItem();
		if (selectedPartRow == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Please select a row");
			infoAlert.showAndWait();
		} else
			setScene(event, "/views/modifyPartForm.fxml");
	}

	// MOdify Product Btn
	@FXML
	void handlemodifyProductBtn(ActionEvent event) throws IOException {
		selectedProductRow = productTableV.getSelectionModel().getSelectedItem();
		if (selectedProductRow == null) {
			infoAlert.setTitle("Information");
			infoAlert.setHeaderText("Please select a row");
			infoAlert.showAndWait();
		} else
			setScene(event, "/views/modifyProductForm.fxml");
	}

	// Setting Scene
	private void setScene(ActionEvent event, String s) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource(s));
		var scene = new Scene(parent);
		var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Showing table Data
		partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		partTableV.setItems(Inventory.getAllParts());

		productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		productTableV.setItems(Inventory.getAllProducts());

	}

	/// WORKSHOP 5&6
	
	@FXML
	void saveAsFile(ActionEvent event) throws IOException, ClassNotFoundException {
		System.out.println("Save Data In File\n");

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			Element rootElement = document.createElement("Inventory");
			document.appendChild(rootElement);

			// Getting Parts
			ObservableList<Part> allParts = Inventory.getAllParts();

			Element partsElement = document.createElement("Parts");

			for (Part part : allParts) {
				Element partElement = document.createElement("Part");
				partElement.setAttribute("type", part instanceof model.InHouse ? "InHouse" : "Outsourced");
				partElement.setAttribute("id", String.valueOf(part.getId()));
				partElement.setAttribute("name", part.getName());
				partElement.setAttribute("price", String.valueOf(part.getPrice()));
				partElement.setAttribute("stock", String.valueOf(part.getStock()));
				partElement.setAttribute("min", String.valueOf(part.getMin()));
				partElement.setAttribute("max", String.valueOf(part.getMax()));

				if (part instanceof model.InHouse) {
					partElement.setAttribute("machineId", String.valueOf(((model.InHouse) part).getMachine()));
				} else {
					partElement.setAttribute("companyName", ((model.Outsourced) part).getCompanyName());
				}
				partsElement.appendChild(partElement);
			}
			rootElement.appendChild(partsElement);

			// Getting Products
			ObservableList<Product> allProducts = Inventory.getAllProducts();

			Element productsElement = document.createElement("Products");

			for (Product product : allProducts) {
				Element productElement = document.createElement("Product");
				productElement.setAttribute("id", String.valueOf(product.getId()));
				productElement.setAttribute("name", product.getName());
				productElement.setAttribute("price", String.valueOf(product.getPrice()));
				productElement.setAttribute("stock", String.valueOf(product.getStock()));
				productElement.setAttribute("min", String.valueOf(product.getMin()));
				productElement.setAttribute("max", String.valueOf(product.getMax()));

				// Associated parts
				ObservableList<Part> associatedParts = product.getAllAssociatedParts();

				Element associatedPartsElement = document.createElement("AssociatedParts");

				for (Part part : associatedParts) {
					Element associatedPartElement = document.createElement("AssociatedPart");
					associatedPartElement.setAttribute("id", String.valueOf(part.getId()));
					associatedPartElement.setAttribute("name", part.getName());
					associatedPartElement.setAttribute("price", String.valueOf(part.getPrice()));
					associatedPartsElement.appendChild(associatedPartElement);
				}
				productElement.appendChild(associatedPartsElement);
				productsElement.appendChild(productElement);
			}
			rootElement.appendChild(productsElement);

			// Save XML file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new FileWriter("inventory.xml"));
			transformer.transform(source, result);

			System.out.println("Data written to inventory.xml");
		} catch (ParserConfigurationException | TransformerException | IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void loadFromFile(ActionEvent event) {
		System.out.println("Load Data From File\n");
		loadDataFromXML("inventory.xml");
	}

	private void loadDataFromXML(String fileName) {
		try {
			File inputFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// Load parts data
			NodeList partList = doc.getElementsByTagName("Part");
			ObservableList<Part> parts = FXCollections.observableArrayList();
			for (int i = 0; i < partList.getLength(); i++) {
				Element partElement = (Element) partList.item(i);
				Part part = createPartFromElement(partElement);
				parts.add(part);
				Inventory.addPart(part); // Add part to Inventory
			}
			partTableV.setItems(parts);

			// Load products data
			NodeList productList = doc.getElementsByTagName("Product");
			ObservableList<Product> products = FXCollections.observableArrayList();
			for (int i = 0; i < productList.getLength(); i++) {
				Element productElement = (Element) productList.item(i);
				Product product = createProductFromElement(productElement);
				products.add(product);
				Inventory.addProduct(product); // Add product to Inventory
			}
			productTableV.setItems(products);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Part createPartFromElement(Element partElement) {

		int id = Integer.parseInt(partElement.getAttribute("id"));
		String name = partElement.getAttribute("name");
		double price = Double.parseDouble(partElement.getAttribute("price"));
		int stock = Integer.parseInt(partElement.getAttribute("stock"));
		int min = Integer.parseInt(partElement.getAttribute("min"));
		int max = Integer.parseInt(partElement.getAttribute("max"));

		// Check if the part is InHouse or Outsourced
		Part part;
		if (partElement.getAttribute("type").equals("InHouse")) {
			int machineId = Integer.parseInt(partElement.getAttribute("machineId"));
			part = new InHouse(id, name, price, stock, min, max, machineId);
		} else {
			String companyName = partElement.getAttribute("companyName");
			part = new Outsourced(id, name, price, stock, min, max, companyName);
		}

		return part;
	}

	private Product createProductFromElement(Element productElement) {
		int id = Integer.parseInt(productElement.getAttribute("id"));
		String name = productElement.getAttribute("name");
		double price = Double.parseDouble(productElement.getAttribute("price"));
		int stock = Integer.parseInt(productElement.getAttribute("stock"));
		int min = Integer.parseInt(productElement.getAttribute("min"));
		int max = Integer.parseInt(productElement.getAttribute("max"));

		Product product = new Product(id, name, price, stock, min, max);

		// Associated parts
		NodeList associatedPartsList = productElement.getElementsByTagName("AssociatedPart");
		for (int i = 0; i < associatedPartsList.getLength(); i++) {
			Element associatedPartElement = (Element) associatedPartsList.item(i);
			int partId = Integer.parseInt(associatedPartElement.getAttribute("id"));
			String partName = associatedPartElement.getAttribute("name");
			double partPrice = Double.parseDouble(associatedPartElement.getAttribute("price"));

			// Check if the part is InHouse or Outsourced
			String type = associatedPartElement.getAttribute("type");
			int stockP = Integer.parseInt(productElement.getAttribute("stock"));
			int minP = Integer.parseInt(productElement.getAttribute("min"));
			int maxP = Integer.parseInt(productElement.getAttribute("max"));

			Part associatedPart;
			if (type.equals("InHouse")) {
				int machineId = Integer.parseInt(associatedPartElement.getAttribute("machineId"));
				associatedPart = new InHouse(partId, partName, partPrice, stockP, minP, maxP, machineId);
			} else {
				String companyName = associatedPartElement.getAttribute("companyName");
				associatedPart = new Outsourced(partId, partName, partPrice, stockP, minP, maxP, companyName);
			}

			product.addAssociatedPart(associatedPart);
		}

		return product;
	}

	

	@FXML
	void LoadFromDatabase(ActionEvent event) {
		System.out.println("Load Data From Database\n");

		da.loadInventoryFromDatabase();

	}

	@FXML
	void SaveInDatabase(ActionEvent event) {
		System.out.println("Save Data In Database\n");

		da.saveInventoryToDatabase();

	}

}
