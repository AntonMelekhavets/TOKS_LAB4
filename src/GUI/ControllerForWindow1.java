package GUI;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import PortInteraction.ComPort;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jssc.SerialPortException;

public class ControllerForWindow1 {
	
	@FXML
	private Button sendButton;
	@FXML
	private ChoiceBox<String> setSpeed;
	@FXML
	private TextField messageField;
	@FXML
	public TextArea viewField;

	@FXML
	public void initialize() throws SerialPortException {
		setSpeed.setItems(FXCollections.observableArrayList("50", "75", "150", "300", "600", "1200",
				"2400", "4800", "9600", "19200", "38400", "57600", "115200"));
		ComPort.initializePorts();
		ComPort.setStartParams(1, viewField);
	}
	
	@FXML
	public TextArea getField() {
		return viewField;
	}
	
	@FXML
	public void sendMessage() throws SerialPortException, UnsupportedEncodingException, InterruptedException {
		SimpleDateFormat myDate = new SimpleDateFormat("hh.mm.ss");
		Date date = new Date();
		viewField.setText(viewField.getText() + "I (" + myDate.format(date) + "):     " + messageField.getText() + "\n");
		changeSpeed();
		ComPort.writeData(1, messageField.getText());
	}
	
	@FXML
	public void changeSpeed() throws NumberFormatException, SerialPortException {
		String speed = setSpeed.getSelectionModel().getSelectedItem();
		ComPort.setParams(Integer.parseInt(speed), 1);
	}
}
