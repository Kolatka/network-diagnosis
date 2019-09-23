package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SyndromeWindowController implements Initializable {

	@FXML
	private Button closeButton;
	@FXML 
	private BorderPane mainPane;
	@FXML
	private TextArea syndromeArea;
	
	static String text = "";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater( () ->syndromeArea.appendText(text));
	}
	
	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException{
		Stage stage = (Stage) mainPane.getScene().getWindow();
		if(event.getSource()==closeButton){ 
			stage.close();
	    }
	}

	@FXML
	public void showStage(String message, String title) throws IOException{
		this.text = message;
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/SyndromeWindow.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
