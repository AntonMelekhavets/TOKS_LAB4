package GUI;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage baseStage, newStage;
	private BorderPane rootForFirstWindow, rootForSecondWindow;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage baseStage) throws Exception {
		this.baseStage = baseStage;
		initRootLayouts();
		showChatWindows();
	}
	
	public void initRootLayouts() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("RootLayout.fxml"));
		rootForFirstWindow = (BorderPane) loader.load();
		
		FXMLLoader newLoader = new FXMLLoader();
		newLoader.setLocation(Main.class.getResource("RootLayout.fxml"));
		rootForSecondWindow = (BorderPane) newLoader.load();
		
		Scene scene = new Scene(rootForFirstWindow);
		baseStage.setScene(scene);
		baseStage.show();
	
		newStage = new Stage();
		newStage.initModality(Modality.WINDOW_MODAL);
		newStage.initOwner(null);
		Scene newScene = new Scene(rootForSecondWindow);
		newStage.setScene(newScene);
		newStage.show();
	}
	
	public void showChatWindows() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("Window1.fxml"));
		AnchorPane chatOverwiew = (AnchorPane) loader.load();
		rootForFirstWindow.setCenter(chatOverwiew);
		ControllerForWindow1 controller = loader.getController();

		FXMLLoader newLoader = new FXMLLoader();
		newLoader.setLocation(Main.class.getResource("Window2.fxml"));
		AnchorPane chatWindow = (AnchorPane) newLoader.load();
		rootForSecondWindow.setCenter(chatWindow);
		ControllerForWindow2 newController = newLoader.getController();
	}
	
}
