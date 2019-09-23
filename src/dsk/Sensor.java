package dsk;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Sensor implements Runnable{
	
	private int id;
	private boolean broken;
	public String state;
	private int received;
	private int time;
	boolean run = true;
	private int label[][];
	private ArrayList<Sensor> sensorsIn = new ArrayList<Sensor>();
	private ArrayList<Sensor> sensorsOut = new ArrayList<Sensor>();
	private int results[][];
	private int syndrome[];
	public enum ConnectionType {
		IN, OUT
	}
	private int FAILURE_PARAMETER = 20; 
	
	Sensor(int id){
		this.id = id;
		broken = false;
		state = "work";
	}

	public void addConnectionOut(Sensor sensor){
		sensorsOut.add(sensor);
	}
	public void addConnectionIn(Sensor sensor){
		sensorsIn.add(sensor);
}
	
	public void prepare(int label[][]){
		int n = label.length;
		this.label = label;
		syndrome = new int[n]; 
		for(int i=0;i<n;i++){
			syndrome[i] = -1;
			
		}
		results = new int[sensorsIn.size()][2];
		int i=0;
		for(Sensor sensor : sensorsIn){
			results[i][0] = sensorsIn.get(i).getId();
			results[i][1] = -1;
			i++;
		}
	}
	
	private void updateSensor(){
		Random rnd = new Random();
		int  n = rnd.nextInt(FAILURE_PARAMETER);
		if(n==0) setBroken(true);
	}
	
	public void setBroken(boolean broken){
		this.broken = broken;
	}
	
	
	public int getResult(){
		if(broken) return 1;
		else return 0;
	}
	
	void sendResults(){
		int size = sensorsOut.size();
		if(size>0){
			for(int i=0;i<size;i++){
				sensorsOut.get(i).receiveResults(getResult(), id);
			}
		}
	}

	public void receiveResults(int result, int id){
		int i=0;
		boolean found = false;
		while(!found && i<results.length){
			if(results[i][0]==id){
				results[i][1] = result;
				received++;
				found = true;
			}
			i++;
		}
	}

	void sendSyndrome(){
		int size = sensorsOut.size();
		if(size>0){
			for(int i=0;i<size;i++){
				sensorsOut.get(i).receiveSyndrome(syndrome);
			}
		}
	}

	public void receiveSyndrome(int[] receivedSyndrome){
		for(int i=0;i<receivedSyndrome.length;i++){
			if(receivedSyndrome[i]!=-1){
				syndrome[i] = receivedSyndrome[i];
			}
		}
	}

	public boolean checkSyndrome(){
		int i=0;
		boolean result = false;
		while(i<syndrome.length && syndrome[i]!=-1){
			i++;
		}
		if(syndrome.length==i)result = true;

		return result;
	}
	
	
	public void updateSyndrome(){
		for(int i=0;i<syndrome.length;i++){
			if(label[i][0]==id){
				int sensor1=-1, sensor2=-1;
				for(int j=0;j<results.length;j++){
					
					if(label[i][1]==results[j][0]){
						sensor1 = results[j][1];
					}
					if(label[i][2]==results[j][0]){
						sensor2 = results[j][1];
					}
				}
				if(sensor1!=-1 && sensor2!=-1){
					if(sensor1==0 && sensor2 ==0) syndrome[i]=0;
					else syndrome[i]=1;
				}
			}
		}
	}
	
	public void printSyndrome(){
		String s ="Syndrom sensora " + id + ": ";
		for(int i=0;i<syndrome.length;i++){
			if(syndrome[i]>-1)s += "{" + label[i][0] + "," + label[i][1] + "," + label[i][2] + "} = " + syndrome[i] + "; ";
		}
		System.out.println(s + "\n");
	}
	
	public void printJoinedSyndrome(){
		String s ="Syndrom sensora " + id + ": ";
		for(int i=0;i<syndrome.length;i++){
			s += syndrome[i] + " ";
		}
		System.out.println(s + "\n");
	}
	
	public void printResults(){
		String s ="Wyniki otrzymane przez sensor " + id + ": ";
		for(int i=0;i<results.length;i++){
			if(results[i][1]>-1)s += "p" + results[i][0] + " = " + results[i][1] + ", ";
		}
		System.out.println(s + "\n");
	}
	
	public void clearSyndrome(){
		for(int i=0;i<syndrome.length;i++){
			syndrome[i] = -1;
		}
		for(int i=0;i<results.length;i++){
			results[i][1] = -1;
		}
	}
	
	
	public void stop(){
		run = false;
	}
	
	@Override
	public void run() {
		time=0;
		received = 0;
		while(run){
			if(state.equals("work")){
				clearSyndrome();
				System.out.println("Sensor " + id + " pracuje " + time + " sekund.");
				sleep(1000);
				time++;
			}
			if(state.equals("diagnosis")){
				int i=0;
				while(received < sensorsIn.size() || i<2){
					sendResults();
					sleep(100);
					i++;
				}
				updateSyndrome();
				received = 0;
				state = "sleep";
			}
			if(state.equals("sleep")){
				sleep(1000);
			}
		}
	}
	
	private void sleep(int time){
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public void joinSyndromes(){
		
		try {
			TimeUnit.MILLISECONDS.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int i=0;
		while(i<100){
			sendSyndrome();
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	public int getId() {
		return id;
	}
	public ArrayList<Sensor> getSensorsIn() {
		return sensorsIn;
	}
	public void setSensorsIn(ArrayList<Sensor> sensorsIn) {
		this.sensorsIn = sensorsIn;
	}
	public ArrayList<Sensor> getSensorsOut() {
		return sensorsOut;
	}
	public void setSensorsOut(ArrayList<Sensor> sensorsOut) {
		this.sensorsOut = sensorsOut;
	}
	public int[] getSyndrome() {
		return syndrome;
	}
	public boolean isBroken() {
		return broken;
	}
	
	
	

}
