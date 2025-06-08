

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

public class ModifyProductController implements Initializable {

	@FXML
	private Button mAddBtn;

	@FXML
	private TableColumn<Part, Integer> mAsoPartIdCol;

	@FXML
	private TableColumn<Part, Integer> mAsoPartInvCol;

	@FXML
	private TableColumn<Part, String> mAsoPartNameCol;

	@FXML
	private TableColumn<Part, Double> mAsoPartPriceCol;

	@FXML
	private Button mAsoRemoveBtn;

	@FXML
	private TableView<Part> mAsoTableV;

	@FXML
	private TableColumn<Part, Integer> mPartIdCol;

	@FXML
	private TableColumn<Part, Integer> mPartInvCol;

	@FXML
	private TableColumn<Part, String> mPartNameCol;

	@FXML
	private TableColumn<Part, Double> mPartPriceCol;

	@FXML
	private TableView<Part> mPartTableV;

	@FXML
	private TextField mProductIdTF;

	@FXML
	private TextField mProductInvTF;

	@FXML
	private TextField mProductMaxTF;

	@FXML
	private TextField mProductMinTF;

	@FXML
	private TextField mProductNameTF;

	@FXML
	private TextField mProductPriceTF;

	@FXML
	private TextField mSearchTF;

	private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
	private Product oRow = MainMenuController.selectedProductRow;

	@FXML
	void handleCancelBtn(ActionEvent event) throws IOException {
		Parent main = FXMLLoader.load(new Main().getClass().getResource("/views/MainMenu.fxml"));
		Scene scene = new Scene(main);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
	}

	@FXML
	void handleMAddBtn(ActionEvent event) {

		Part selectedPart = mPartTableV.getSelectionModel().getSelectedItem();
		if (selectedPart == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Please select a row");
			MainMenuController.infoAlert.showAndWait();
		} else {
			associatedParts.add(selectedPart);
			mAsoTableV.setItems(associatedParts);
		}

	}

	@FXML
	void handleMRemoveBtn(ActionEvent event) {
		Part selectedAsoPart = mAsoTableV.getSelectionModel().getSelectedItem();
		if (selectedAsoPart == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Please select a row");
			MainMenuController.infoAlert.showAndWait();
		} else {
			int id = selectedAsoPart.getId();
			if (associatedParts == null) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Error Occured");
				MainMenuController.errorAlert.setContentText("No Associated Part Found");
				MainMenuController.errorAlert.showAndWait();

			} else {
				for (int i = 0; i < associatedParts.size(); i++) {
					if (associatedParts.get(i).getId() == id) {
						associatedParts.remove(associatedParts.get(i));
					}
				}
			}
		}
	}

	// Validates Input
	private boolean checkInput() {
		return CheckTF.isInteger(mProductIdTF.getText()) && CheckTF.isDouble(mProductPriceTF.getText())
				&& CheckTF.isInteger(mProductMaxTF.getText()) && CheckTF.isInteger(mProductMinTF.getText())
				&& !CheckTF.isEmpty(mProductNameTF.getText());
	}

	@FXML
	void handleSaveBtn(ActionEvent event) throws IOException {

		String name = "";
		int inv = 0;
		int max = 0;
		int min = 0;
		double price = 0;
		int id = oRow.getId();

		if (!checkInput()) {
			MainMenuController.errorAlert.setTitle("Error");
			MainMenuController.errorAlert.setHeaderText("Incorrect Input");
			MainMenuController.errorAlert.setContentText("Error: Please enter all field values");
			MainMenuController.errorAlert.showAndWait();
		} else {
			name = mProductNameTF.getText();
			price = Double.parseDouble(mProductPriceTF.getText());
			inv = Integer.parseInt(mProductInvTF.getText());
			min = Integer.parseInt(mProductMinTF.getText());
			max = Integer.parseInt(mProductMaxTF.getText());

			if (!(inv <= max && min <= max && inv >= min)) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Incorrect Input");
				MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
				MainMenuController.errorAlert.showAndWait();

			} else {
				Product product1 = new Product(id, name, price, inv, min, max);

				int index = -1;
				for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
					if (Inventory.getAllProducts().get(i).getId() == oRow.getId()) {
						index = i;
					}
				}

				for (Part part : associatedParts) {
					product1.addAssociatedPart(part);
				}
				Inventory.updateProduct(index, product1);
				handleCancelBtn(event);
			}
		}

	}

	@FXML
	void handleSearchTF(KeyEvent event) {

		if (isEntered(event) && isNumber()) {
			searchPartById();
		} else if (isEntered(event) && isString()) {
			searchPartByName();
		} else {
			mPartTableV.setItems(Inventory.getAllParts());
		}

	}

	private boolean isEntered(KeyEvent event) {
		return event.getCode().equals(KeyCode.ENTER);
	}

	private void searchPartByName() {
		ObservableList<Part> result = Inventory.searchPartByName(mSearchTF.getText());
		if (result.size() > 0) {
			mPartTableV.setItems(result);
		} else
			MainMenuController.infoAlert.setTitle("Information");
		MainMenuController.infoAlert.setHeaderText("Part is not found");
		MainMenuController.infoAlert.showAndWait();
	}

	private void searchPartById() throws NumberFormatException {
		var part = Inventory.searchPartByID(Integer.parseInt(mSearchTF.getText()));
		if (part == null) {
			MainMenuController.infoAlert.setTitle("Information");
			MainMenuController.infoAlert.setHeaderText("Part is not found");
			MainMenuController.infoAlert.showAndWait();
		} else {
			ObservableList<Part> result = FXCollections.observableArrayList();
			result.add(part);
			mPartTableV.setItems(result);
		}
	}

	private boolean isString() {
		return mSearchTF.getText() != null && mSearchTF.getText().matches("^[a-zA-Z\\s]*$");
	}

	private boolean isNumber() {
		return mSearchTF != null && mSearchTF.getText().matches("^[0-9]*$");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		mProductIdTF.setText(String.valueOf(oRow.getId()));
		mProductNameTF.setText(oRow.getName());
		mProductPriceTF.setText(String.valueOf(oRow.getPrice()));
		mProductInvTF.setText(String.valueOf(oRow.getStock()));
		mProductMinTF.setText(String.valueOf(oRow.getMin()));
		mProductMaxTF.setText(String.valueOf(oRow.getMax()));

		mPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		mPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		mPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		mPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		mPartTableV.setItems(Inventory.getAllParts());

		mAsoPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		mAsoPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		mAsoPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		mAsoPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

		associatedParts = MainMenuController.selectedProductRow.getAllAssociatedParts();
		mAsoTableV.setItems(associatedParts);

	}

}
