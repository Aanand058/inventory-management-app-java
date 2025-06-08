
package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

public class AddPartController {

	@FXML
	private RadioButton inHouseRBTn;

	@FXML
	private RadioButton outsourcedRBtn;

	@FXML
	private Button partCloseBtn;

	@FXML
	private TextField partIdTextF;

	@FXML
	private TextField partInvTextF;

	@FXML
	private TextField partMachineIdTextF;

	@FXML
	private TextField partMaxTextF;

	@FXML
	private TextField partMinTextF;

	@FXML
	private TextField partNameTextF;

	@FXML
	private TextField partPriceTextF;

	@FXML
	private Button partSaveBtn;

	@FXML
	private Label machineIdOrCompanyName;

	// Validates Input
	private boolean checkInput(boolean partMachineIdTextF) {
		return CheckTF.isInteger(partInvTextF.getText()) && CheckTF.isDouble(partPriceTextF.getText())
				&& CheckTF.isInteger(partMaxTextF.getText()) && CheckTF.isInteger(partMinTextF.getText())
				&& !CheckTF.isEmpty(partNameTextF.getText()) && partMachineIdTextF;
	}

	@FXML
	void handleAddPartSaveBtn(ActionEvent event) throws IOException {

		String name = "";
		int inv = 0;
		int max = 0;
		int min = 0;
		double price = 0;

		if (machineIdOrCompanyName.getText().equalsIgnoreCase("MachineID")) {
			if (!checkInput(CheckTF.isInteger(partMachineIdTextF.getText()))) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Incorrect Input");
				MainMenuController.errorAlert.setContentText("Error: Please enter all field values");
				MainMenuController.errorAlert.showAndWait();
			} else {
				name = partNameTextF.getText();
				inv = Integer.parseInt(partInvTextF.getText());
				price = Double.parseDouble(partPriceTextF.getText());
				max = Integer.parseInt(partMaxTextF.getText());
				min = Integer.parseInt(partMinTextF.getText());

				if (!(inv <= max && min <= max && inv >= min)) {
					MainMenuController.errorAlert.setTitle("Error");
					MainMenuController.errorAlert.setHeaderText("Incorrect Input");
					MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
					MainMenuController.errorAlert.showAndWait();

				} else {
					Inventory.addPart(new InHouse(Main.getUniquePartId(), name, price, inv, min, max,
							Integer.parseInt(partMachineIdTextF.getText())));
					System.out.println("Succes INhouse");

					handleAddPartCancelBtn(event);
				}
			}
		}
		if (machineIdOrCompanyName.getText().equalsIgnoreCase("Company Name")) {
			if (!checkInput(!CheckTF.isEmpty(partMachineIdTextF.getText()))) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Incorrect Input");
				MainMenuController.errorAlert.setContentText("Error: Please enter all field values");
				MainMenuController.errorAlert.showAndWait();

			} else {
				name = partNameTextF.getText();
				inv = Integer.parseInt(partInvTextF.getText());
				price = Double.parseDouble(partPriceTextF.getText());
				max = Integer.parseInt(partMaxTextF.getText());
				min = Integer.parseInt(partMinTextF.getText());

				if (!(inv <= max && min <= max && inv >= min)) {
					MainMenuController.errorAlert.setTitle("Error");
					MainMenuController.errorAlert.setHeaderText("Incorrect Input");
					MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
					MainMenuController.errorAlert.showAndWait();

				} else {
					Inventory.addPart(new Outsourced(Main.getUniquePartId(), name, price, inv, min, max,
							partMachineIdTextF.getText()));

					System.out.println("Succes Outsourced");
					handleAddPartCancelBtn(event);
				}
			}
		}
	}

	@FXML
	void addInHouseType(ActionEvent actionEvent) {
		machineIdOrCompanyName.setText("MachineID");
	}

	@FXML
	void addOutsourcedType(ActionEvent actionEvent) {
		machineIdOrCompanyName.setText("Company Name");
	}

	@FXML
	void handleAddPartCancelBtn(ActionEvent event) throws IOException {
		Parent main = FXMLLoader.load(new Main().getClass().getResource("/views/MainMenu.fxml"));
		Scene scene = new Scene(main);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
	}

}
