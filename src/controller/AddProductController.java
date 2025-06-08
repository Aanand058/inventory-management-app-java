
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

public class AddProductController implements Initializable {

	@FXML
	private Button addProductAddBtn;

	@FXML
	private TableColumn<Part, Integer> addProductAsoInvCol;

	@FXML
	private TableColumn<Part, String> addProductAsoNameCol;

	@FXML
	private TableColumn<Part, Integer> addProductAsoPartCol;

	@FXML
	private TableColumn<Part, Double> addProductAsoPriceCol;

	@FXML
	private Button addProductCancelBtn;

	@FXML
	private TextField addProductIdTF;

	@FXML
	private TableColumn<Part, Integer> addProductInvCol;

	@FXML
	private TextField addProductInvTF;

	@FXML
	private TextField addProductMinTF;

	@FXML
	private TableColumn<Part, String> addProductNameCol;

	@FXML
	private TextField addProductNameTF;

	@FXML
	private TextField addProductPMaxTF;

	@FXML
	private TableColumn<Part, Integer> addProductPartCol;

	@FXML
	private TableView<Part> addProductPartTableV;

	@FXML
	private TableView<Part> addProductAsoTableV;

	@FXML
	private TableColumn<Part, Double> addProductPriceCol;

	@FXML
	private TextField addProductPriceTF;

	@FXML
	private Button addProductSaveBtn;

	@FXML
	private TextField addProductSearchTF;

	@FXML
	private Button removeAsoPartBtn;

	// Extra Associated Parts Variables;
	private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

	@FXML
	void handleAddProductAddBtn(ActionEvent event) {
		Part selectedPartRow = addProductPartTableV.getSelectionModel().getSelectedItem();
		if (selectedPartRow == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Please select a row");
			MainMenuController.infoAlert.showAndWait();
		} else {
			associatedParts.add(selectedPartRow);
			addProductAsoTableV.setItems(associatedParts);
		}
	}

	@FXML
	void handleAddProductCancelBtn(ActionEvent event) throws IOException {

		Parent main = FXMLLoader.load(new Main().getClass().getResource("/views/MainMenu.fxml"));
		Scene scene = new Scene(main);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);

	}

	// Validates Input
	private boolean checkInput() {
		return CheckTF.isInteger(addProductInvTF.getText()) && CheckTF.isDouble(addProductPriceTF.getText())
				&& CheckTF.isInteger(addProductPMaxTF.getText()) && CheckTF.isInteger(addProductMinTF.getText())
				&& !CheckTF.isEmpty(addProductNameTF.getText());
	}

	@FXML
	void handleAddProductSaveBtn(ActionEvent event) throws IOException {

		String name = "";
		int inv = 0;
		int max = 0;
		int min = 0;
		double price = 0;

		if (!checkInput()) {
			MainMenuController.errorAlert.setTitle("Error");
			MainMenuController.errorAlert.setHeaderText("Incorrect Input");
			MainMenuController.errorAlert.setContentText("Error: Please enter all field values");
			MainMenuController.errorAlert.showAndWait();

		} else {
			name = addProductNameTF.getText();
			inv = Integer.parseInt(addProductInvTF.getText());
			price = Double.parseDouble(addProductPriceTF.getText());
			min = Integer.parseInt(addProductMinTF.getText());
			max = Integer.parseInt(addProductPMaxTF.getText());

			if (!(inv <= max && min <= max && inv >= min)) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Incorrect Input");
				MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
				MainMenuController.errorAlert.showAndWait();

			} else {
				Product prod1 = new Product(Main.getUniqueProdId(), name, price, inv, min, max);
				for (Part part : associatedParts) {
					prod1.addAssociatedPart(part);
				}
				Inventory.addProduct(prod1);
				handleAddProductCancelBtn(event);
			}
		}

	}

	@FXML
	void handleRemoveAsoPartBtn(ActionEvent event) {
		Part selectedAsoPart = addProductAsoTableV.getSelectionModel().getSelectedItem();
		if (selectedAsoPart == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Please select a row");
			MainMenuController.infoAlert.showAndWait();
		} else {
			int id = selectedAsoPart.getId();
			for (int i = 0; i < associatedParts.size(); i++) {
				if (associatedParts.get(i).getId() == id) {
					associatedParts.remove(associatedParts.get(i));
				}
			}
		}
	}

	@FXML
	void handleSearchTF(KeyEvent event) {

		if (isEntered(event) && isNumber()) {
			searchedPartById();
		} else if (isEntered(event) && isString()) {
			searchedPartByName();
		} else {
			addProductPartTableV.setItems(Inventory.getAllParts());
		}

	}

	private boolean isEntered(KeyEvent event) {
		return event.getCode().equals(KeyCode.ENTER);
	}

	private void searchedPartByName() {
		ObservableList<Part> result = Inventory.searchPartByName(addProductSearchTF.getText());
		if (result.size() > 0) {
			addProductPartTableV.setItems(result);
		} else
			MainMenuController.infoAlert.setTitle("Information");
		MainMenuController.infoAlert.setHeaderText("Part is not found");
		MainMenuController.infoAlert.showAndWait();
	}

	private void searchedPartById() throws NumberFormatException {
		var part = Inventory.searchPartByID(Integer.parseInt(addProductSearchTF.getText()));
		if (part == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Part is not found");
			MainMenuController.infoAlert.showAndWait();
		} else {
			ObservableList<Part> result = FXCollections.observableArrayList();
			result.add(part);
			addProductPartTableV.setItems(result);
		}
	}

	private boolean isString() {
		return addProductSearchTF.getText() != null && addProductSearchTF.getText().matches("^[a-zA-Z\\s]*$");
	}

	private boolean isNumber() {
		return addProductSearchTF != null && addProductSearchTF.getText().matches("^[0-9]*$");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addProductPartCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		addProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		addProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		addProductPartTableV.setItems(Inventory.getAllParts());

		addProductAsoPartCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		addProductAsoNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		addProductAsoInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		addProductAsoPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

	}

}
