
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit.Task;

import dsk.MmModel;
import dsk.SensorManager;
import graph.ExampleGraph;
import graph.ExampleGraphData;
import graph.GraphManager;
import graph.GraphManager.NodeColor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scala.util.grammar.EmptyHedgeRHS;

/**
 * @author �ukasz Ko�aci�ski
 *
 */
public class MainWindowController implements Initializable {

	@FXML
    private Button startButton;
	@FXML
	private Button diagnoseButton;
	@FXML
	private Button breakButton;
	@FXML
	private Button stopButton;
	@FXML
	private Button syndromeButton;
	@FXML
	private Button checkButton;
	@FXML
	private Button fixSensorButton;
	@FXML
	private Button breakSensorButton;
	@FXML 
	private BorderPane mainPane;
	@FXML 
	private StackPane stackPane;
	@FXML
	private ChoiceBox graphChoiceBox;
	@FXML
	private TextField mField;
	@FXML
	private TextField sensorField;
	
	Stage stage;
	GraphManager graphManager;
	SensorManager sensorManager;
	MmModel mmModel;
	ExampleGraph exampleGraph;
	ExampleGraphData exampleGraphData;
	private Viewer viewer;
    private ViewPanel defaultView;
    boolean running;
    int current;

	public void initialize (URL location, ResourceBundle resources) {
		running = false;
		graphManager= new GraphManager("Graph",1);
		exampleGraph = new ExampleGraph();
		exampleGraphData = exampleGraph.getData(1);
		
		diagnoseButton.setDisable(true);
		breakButton.setDisable(true);
		stopButton.setDisable(true);
		syndromeButton.setDisable(true);
		checkButton.setDisable(true);

		ObservableList<String> list = FXCollections.observableArrayList(exampleGraph.getNamesList());
		graphChoiceBox.setItems(list);
		graphChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov,Number value, Number new_value) {
				startButton.setDisable(false);
				syndromeButton.setDisable(false);
				checkButton.setDisable(false);
				if(running)sensorManager.stopSensors();
				running = false;
				sensorManager = new SensorManager(graphManager,1);
				current = (Integer)ov.getValue()+1;
				sensorManager.createSensorNetwork(exampleGraph.getData((Integer)ov.getValue()+1));
	        }
	    });

		 SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                viewer = new Viewer(graphManager.graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	                defaultView = viewer.addDefaultView(false);
	                viewer.enableAutoLayout();
	                javafx.application.Platform.runLater(new Runnable() {
	                    @Override public void run() {
	                        SwingNode swingNode = new SwingNode();
	                        swingNode.setContent(defaultView);
	                        stackPane.getChildren().add(swingNode);
	                        stackPane.requestLayout();
	                    }
	                });
	            }
	        });
	}

	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException{
		SyndromeWindowController swc = new SyndromeWindowController();
		AlertWindowController awc = new AlertWindowController();
		stage = (Stage) mainPane.getScene().getWindow();
	    Parent root;
		if(event.getSource()==startButton){   
			log("Rozpocz�to prace sensor�w");
			if(!running){
				sensorManager.runSensors(); 
				running=true;
			}else sensorManager.work();
			
			startButton.setDisable(true);
			diagnoseButton.setDisable(false);
			breakButton.setDisable(false);
			stopButton.setDisable(false);
			
			
	    }else if(event.getSource()==diagnoseButton){
	    	log("Rozpocz�to diagnoze sensor�w");
	    	sensorManager.diagnosis();
	    	sensorManager.joinAllSensorsSyndromes();
	    	sensorManager.printSyndrome();
	    	
	    	mmModel = new MmModel(graphManager,sensorManager);
	    	

	    	awc.showStage(mmModel.findBrokenSensors(sensorManager.getSyndrome()),"Wynik");
	    	startButton.setDisable(false);
			breakButton.setDisable(false);
	    }else if(event.getSource()==breakButton){
	    	log("Rozpocz�to przerw� sensor�w");
	    	sensorManager.sleep();
	    	
	    	startButton.setDisable(false);
			diagnoseButton.setDisable(false);
			breakButton.setDisable(true);
	    }else if(event.getSource()==stopButton){
	    	log("Zako�czono prac� sensor�w");
	    	sensorManager.stopSensors();
	    	running=false;
	    	
	    	stopButton.setDisable(true);
	    	startButton.setDisable(false);
	    	diagnoseButton.setDisable(true);
			breakButton.setDisable(true);
	    }else if(event.getSource()==syndromeButton){
	    	log("Rozpocz�to generowanie wzorca syndromu");
	    	
	    	mmModel = new MmModel(graphManager,sensorManager);
	    	int m = Integer.parseInt(mField.getText());
	    	if(m<1 || m>5) m=1;
			mmModel.generatePatternSyndrome(m);

    		if(mmModel.getRows()<700)
    			swc.showStage(mmModel.getSyndromeString(),"Syndrom globalny dla m = " + m + ".");
    		else swc.showStage("Zbyt du�o danych �eby wy�wietli�","Syndrom globalny dla " + m + " popsutych sensor�w");
    		
	    	log("Zako�czono generowanie wzorca syndromu");
	    }else if(event.getSource()==checkButton){
	    	mmModel = new MmModel(graphManager,sensorManager);
	    	int m = getFieldValue(mField);
	    	if (m>=1 && m<=5){
	    		log("Rozpocz�to sprawdzanie " + m + "-diagnozowalno�ci");
	    		mmModel.generatePatternSyndrome(m);
	    		if(mmModel.checkDiagnosisLevel()){
	    			log("Sie� jest " + m +"-diagnozowalna");
	    			awc.showStage("Sie� JEST " + m +"-diagnozowalna","Wynik");
	    		}
	    		else{
	    			log("Sie� nie jest " + m +"-diagnozowalna");
	    			awc.showStage("Sie� NIE JEST " + m +"-diagnozowalna","Wynik");
	    		}
	    	}else log("Niepoprawna warto�� liczby m. Podaj warto�� z przedzia�u <1,5>");
	    	
	    	
	    	
	    }else if(event.getSource()==fixSensorButton){
	    	int id = getFieldValue(sensorField);
	    	if(id!=-1){
	    		sensorManager.fixSensor(id);
	    		log("Naprawiono sensor: " + id);
	    		sensorField.setText("");
	    	}else  log("Niepoprawna warto�� id sensora. Podaj warto�� z przedzia�u <1," + sensorManager.getSensors().size() +">");
	    }else if(event.getSource()==breakSensorButton){
	    	int id = getFieldValue(sensorField);
	    	if(id!=-1){
	    		sensorManager.breakSensor(id);
	    		log("Popsuto sensor: " + id);
	    		sensorField.setText("");
	    	}else log("Niepoprawna warto�� id sensora. Podaj warto�� z przedzia�u <1," + sensorManager.getSensors().size() +">");
	    }
	}
	
	public int getFieldValue(TextField tf){
		if(checkFields(tf)){
			return Integer.parseInt(tf.getText());
		}else{
			return -1;
		}
	}

	public boolean checkFields(TextField tf){
		if(!tf.getText().isEmpty()){
			if(isInteger(tf.getText())){
				return true;
			}else return false;
		}else return false;	
	}
	
	public boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public void showStage(Parent root, Stage stage){
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	}

	
	public void log(String s){
		System.out.println(">>> " + s + " <<<");
	}

	
}