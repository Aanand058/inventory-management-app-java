
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

public class ModifyPartController implements Initializable {

	@FXML
	private RadioButton inHouseRBtn;

	@FXML
	private TextField machineIdOrComapanyNameTF;

	@FXML
	private Label machineIdOrCompanyNameLabel;

	@FXML
	private TextField modifyPartInvTF;

	@FXML
	private TextField modifyPartMaxTF;

	@FXML
	private TextField modifyPartMinTf;

	@FXML
	private TextField modifyPartNameTF;

	@FXML
	private TextField modifyPartPriceTF;

	@FXML
	private RadioButton outsourcedRBtn;

	@FXML
	private TextField modifyPartIdTF;

	private Part partSelected;

	@FXML
	void handleModifyCancelBtn(ActionEvent event) throws IOException {
		Parent main = FXMLLoader.load(new Main().getClass().getResource("/views/MainMenu.fxml"));
		Scene scene = new Scene(main);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
	}

	@FXML
	void handleModifySaveBtn(ActionEvent event) throws IOException {

		String name = "";
		int inv = 0;
		int max = 0;
		int min = 0;
		double price = 0;

		if (machineIdOrCompanyNameLabel.getText().equalsIgnoreCase("MachineID")) {
			{
				name = modifyPartNameTF.getText();
				inv = Integer.parseInt(modifyPartInvTF.getText());
				price = Double.parseDouble(modifyPartPriceTF.getText());
				max = Integer.parseInt(modifyPartMaxTF.getText());
				min = Integer.parseInt(modifyPartMinTf.getText());

				if (!(inv <= max && min <= max && inv >= min)) {
					MainMenuController.errorAlert.setTitle("Error");
					MainMenuController.errorAlert.setHeaderText("Incorrect Input");
					MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
					MainMenuController.errorAlert.showAndWait();

				} else {
					InHouse part = new InHouse(partSelected.getId(), name, price, inv, min, max,
							Integer.parseInt(machineIdOrComapanyNameTF.getText()));
					int index = getIndex();
					Inventory.updatePart(index, part);
					handleModifyCancelBtn(event);
				}

			}
		}
		if (machineIdOrCompanyNameLabel.getText().equalsIgnoreCase("Company Name")) {

			name = modifyPartNameTF.getText();
			inv = Integer.parseInt(modifyPartInvTF.getText());
			price = Double.parseDouble(modifyPartPriceTF.getText());
			max = Integer.parseInt(modifyPartMaxTF.getText());
			min = Integer.parseInt(modifyPartMinTf.getText());

			if (!(inv <= max && min <= max && inv >= min)) {
				MainMenuController.errorAlert.setTitle("Error");
				MainMenuController.errorAlert.setHeaderText("Incorrect Input");
				MainMenuController.errorAlert.setContentText("Error: Inv value should be between Min and Man");
				MainMenuController.errorAlert.showAndWait();
			} else {
				Outsourced part = new Outsourced(partSelected.getId(), name, price, inv, min, max,
						machineIdOrComapanyNameTF.getText());
				int index = getIndex();
				Inventory.updatePart(index, part);
				handleModifyCancelBtn(event);
			}

		}
	}

	@FXML
	void modifyInHouseType(ActionEvent actionEvent) {
		machineIdOrCompanyNameLabel.setText("MachineID");
	}

	@FXML
	void modifyOutsourcedType(ActionEvent actionEvent) {
		machineIdOrCompanyNameLabel.setText("Company Name");
	}

	// Getting index
	private int getIndex() {
		for (int i = 0; i < Inventory.getAllParts().size(); i++) {
			if (Inventory.getAllParts().get(i).getId() == partSelected.getId()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		partSelected = MainMenuController.selectedPartRow;
		modifyPartIdTF.setText(String.valueOf(partSelected.getId()));
		modifyPartNameTF.setText(partSelected.getName());
		modifyPartInvTF.setText(String.valueOf(partSelected.getStock()));
		modifyPartPriceTF.setText(String.valueOf(partSelected.getPrice()));
		modifyPartMaxTF.setText(String.valueOf(partSelected.getMax()));
		modifyPartMinTf.setText(String.valueOf(partSelected.getMin()));

		if (partSelected instanceof InHouse) {
			inHouseRBtn.setSelected(true);
			machineIdOrCompanyNameLabel.setText("MachineID");
			machineIdOrComapanyNameTF.setText(String.valueOf(((InHouse) partSelected).getMachine()));

		}
		if (partSelected instanceof Outsourced) {
			outsourcedRBtn.setSelected(true);
			machineIdOrCompanyNameLabel.setText("Company Name");
			machineIdOrComapanyNameTF.setText(String.valueOf(((Outsourced) partSelected).getCompanyName()));

		}

	}
	
	
	
}
