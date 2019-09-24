package dsk;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.graphstream.graph.Node;
import graph.ExampleGraphData;
import graph.GraphManager;
import graph.GraphManager.NodeColor;


public class SensorManager {

	private ArrayList<Sensor> sensors;
	private int id;
	private GraphManager graphManager;
	private int syndrome[];
	
	public SensorManager(GraphManager gm, int id){
		this.id = id;
		sensors = new ArrayList<Sensor>();
		this.graphManager = gm;
	}
	
	public void addSensor(int sensorId){
		Sensor sensor = new Sensor(sensorId);
		graphManager.addNode(sensorId);
		sensors.add(sensor);
	}

	void connectSensors(Sensor sensor1, Sensor sensor2){
		graphManager.addEdge(sensor1.getId(), sensor2.getId());
		sensor1.addConnectionOut(sensor2);
		sensor2.addConnectionIn(sensor1);

	}

	public void connectSensors(int id1, int id2){
		graphManager.addEdge(id1, id2);
		getSensor(id1).addConnectionOut(getSensor(id2));
		getSensor(id2).addConnectionIn(getSensor(id1));

	}

	public void runSensors(){
		int[][] label = generateLabel(); 
		for(Sensor sensor : sensors){
			sensor.prepare(label);
			updateColors(NodeColor.GREEN);
		}
		for(Sensor sensor : sensors){
			Thread thread = new Thread(sensor);
			thread.start();
		}
	}
	
	public void stopSensors(){
		for(Sensor sensor : sensors){
			sensor.stop();
			updateColors(NodeColor.ORANGE);
		}
	}
	
	public void work(){
		for(Sensor sensor : sensors){
			updateColors(NodeColor.GREEN);
			sensor.state = "work";
		}
	}
	
	public void diagnosis(){
		for(Sensor sensor : sensors){
			updateColors(NodeColor.YELLOW);
			sensor.state = "diagnosis";
		}
	}
	
	public void sleep(){
		for(Sensor sensor : sensors){
			updateColors(NodeColor.GRAY);
			sensor.state = "sleep";
		}
	}
	//--------------------------------------------------------------------------------
	

	public void updateColors(NodeColor nc){
		for(Sensor sensor : sensors){
			if(sensor.isBroken()){
				graphManager.changeColor(sensor.getId(), NodeColor.RED);
			}else{
				graphManager.changeColor(sensor.getId(), nc);
			}
		}
	}
	

	public void breakSensor(int id){
		Sensor sensor = getSensor(id);
		sensor.setBroken(true);
		graphManager.changeColor(sensor.getId(), NodeColor.RED);
	}

	public void fixSensor(int id){
		Sensor sensor = getSensor(id);
		sensor.setBroken(false);
		graphManager.changeColor(sensor.getId(), NodeColor.GREEN);
	}
	
	public void createSensorNetwork(ExampleGraphData egd){
		graphManager.clearGraph();
		for(int i=1;i<egd.getNodes()+1;i++) addSensor(i);
		for(int i=0;i<egd.getEdges().length;i++){
			connectSensors(egd.getEdges()[i][0],egd.getEdges()[i][1]);
		}	
	}
	
	public int getLabelSize(){
		int size=0;
		for(Sensor sensor : getSensors()){
			size += newton(sensor.getSensorsIn().size(),2);
		}
		
		return size;
	}
	
	public int[][] generateLabel(){
		int[][] label = new int[getLabelSize()][3];
		int k = 0;
		for(Sensor sensor : getSensors()){
			ArrayList<Sensor> sensorsIn = sensor.getSensorsIn();
			for(int i=0;i<sensorsIn.size();i++){
				for(int j=i+1;j<sensorsIn.size();j++){
					label[k][0] = sensor.getId();
					label[k][1] = sensorsIn.get(i).getId();
					label[k][2] = sensorsIn.get(j).getId();
					k++;
				}
			}
			
		}
		return label;
	}
	
	public void printSyndrome(){
		String s ="";
		for(int i=0;i<getLabelSize();i++){
			s+= syndrome[i] + " ";
		}
		System.out.println("Syndrome: ");
		System.out.println(s);
	}
	
	public static int newton(int n, int k){
		int  result = 1;     
		for(int i=1; i<=k;i++) result = result*(n-i+1)/i; 
			return result;  
	}

	public void joinAllSensorsSyndromes(){
		int size = getLabelSize();
		syndrome = new int[size];
		for(Sensor sensor : getSensors()){
			while(sensor.state.equals("diagnosis")){
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for(int i=0;i<size;i++){
				if(sensor.getSyndrome()[i]!=-1){
					syndrome[i] = sensor.getSyndrome()[i];
				}
			}
		}
	}
		
	
	public int[] getSyndrome() {
		return syndrome;
	}
	
	Sensor getSensor(int id){
		for(int i=0;i<sensors.size();i++){
			if(sensors.get(i).getId()==id){
				return sensors.get(i);
			}
		}
		return null;
	}
	
	public int[] getSensorsSyndrome(){
		Sensor sensor = sensors.get(1);
		return sensor.getSyndrome();
	}
	
	public ArrayList<Sensor> getSensors(){
		return sensors;
	}
	
	
	
	
}
