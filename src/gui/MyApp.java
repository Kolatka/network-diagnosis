package gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import dsk.SensorManager;
import graph.GraphManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MyApp extends Application {
	
	private Stage stage;
	private MainWindowController mwc;

	@Override
	public void start(Stage primaryStage) throws Exception {
		mwc = new MainWindowController();
		
		Parent root = FXMLLoader.load(getClass().getResource("/gui/MainWindow.fxml"));
		mwc.showStage(root, primaryStage);

		primaryStage.setTitle("DSK");
		primaryStage.setOnCloseRequest(e->{
			e.consume();	
			primaryStage.close();
			Platform.exit();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}