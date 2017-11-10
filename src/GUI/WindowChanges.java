package GUI;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class WindowChanges implements Runnable {
	
	TextArea area;
	String data;
	
	public WindowChanges(TextArea area, String data) {
		this.area = area;
		this.data = data;
	}
	
	@FXML
	public void run() {
		SimpleDateFormat myDate = new SimpleDateFormat("hh.mm.ss");
		Date date = new Date();
		if(data.contains("This message is bad")) {
			area.setText(data + "\n");
			return;
		}
		area.setText("Some (" + myDate.format(date) + "):  " + data + "\n");
	}
}
