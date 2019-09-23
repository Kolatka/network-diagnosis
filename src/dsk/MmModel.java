/**
 * 
 */
package dsk;

import java.util.ArrayList;

import graph.GraphManager;

public class MmModel {

	private GraphManager graphManager;
	private SensorManager sensorManager;
	private int patternSyndrome[][]; 
	private int label[][]; 
	private ArrayList<ArrayList<Integer>> fault; 
	private int m; 
	private int columns, rows; 
	
	public MmModel(GraphManager gm, SensorManager sm){
		this.graphManager = gm;
		this.sensorManager = sm;
	}
	
	public void generatePatternSyndrome(int m){
		label = sensorManager.generateLabel();
		this.m = m;
		int size = sensorManager.getSensors().size();
		rows = getRowNumber(m);
		columns = sensorManager.getLabelSize();
		patternSyndrome = new int[rows][columns];

		fault = getFaultArray(m,size);
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i=0;i<rows;i++){
			if(i>0) temp = fault.get(i-1);
			for(int j=0;j<columns;j++){
				int val = 0;
				if(i==0) val = 0;
				else val = check(label[j][0],label[j][1],label[j][2], temp);	
				patternSyndrome[i][j] = val;
			}
		}	
	}
	
	public int check(int i,int j, int k, ArrayList<Integer> temp){
		int val=0;
		for(int t : temp){
			if(t==i) val = 2;
			if((t==j || t==k) && val!=2) val = 1;
		}
		return val;
	}
	
	public boolean checkDiagnosisLevel(){
		boolean result = true;
		boolean diff = true;
		int i=0;
		while(result && i<rows){
			int j=i+1;
			while(result && j<rows){
				int k=0;
				diff = true;
				while(diff && k<columns){
					if(patternSyndrome[i][k]+patternSyndrome[j][k]==1) diff = false;
					k++;
				}
				if(k>=columns)result = false;
				j++;
			}
			i++;
		}
		
		return result;
	}

	public String findBrokenSensors(int [] syndrome){
		String s = "";
		boolean diff;
		int found=0;
		int i=0;
		
		int sum=0;
		for(int k=0;k<syndrome.length;k++) sum += syndrome[k];
		
		if(sum>0){
			findMaxM();
			if(m>0){
				while(i<rows){
					int j=0;
					diff = false;
					while(j<columns && !diff){
						if(syndrome[j]+patternSyndrome[i][j] == 1) diff= true;
						
						j++;
					}
					if(j==columns && diff==false){
						found++;
						s += getBrokenSensorsIds(i-1);
					}
					
					i++;
				}
			}else return "Ta sieæ nie jest diagnozowalna. Algorytm nie jest w stanie zidentyfikowac popsutych sensorów.";
		}else return "Ta sieæ nie ma popsutych sensorów.";

		if(found==1)
			return "Zidentyfikowano popsute sensory: " + s + ".";
		else
			return "Ta sieæ jest maksymalnie " + m + "-diagnozowalna. Algorytm nie jest w stanie zidentyfikowac popsutych sensorów.";
	}
	
	public void findMaxM(){
		boolean par = false;
		int m = 6;
		while(m>0 && !par){
			m--;
			generatePatternSyndrome(m);
			if(checkDiagnosisLevel()) par = true;
		}
		this.m = m;
	}
	
	//----------------------------------------------------------------------------------
	
	public String getSyndromeString(){
		String syndrome="";
		for(int j=0;j<4;j++){
			for(int l=0;l<=m;l++)
				syndrome +="\t";
			for(int i=0;i<columns;i++){
				if(j<3)syndrome+=label[i][j] + "\t";
			}
			syndrome+="\n";
		}
		for(int i=0;i<rows;i++){
			if(i>0)
				syndrome +=getRowLabel(i-1);
			else 
				for(int l=0;l<=m;l++)
					syndrome +="\t";
			for(int j=0;j<columns;j++){
				syndrome+=patternSyndrome[i][j] + "\t";
			}
			syndrome+="\n";
		}
		return syndrome;
	}
	
	public int getRowNumber(int m){
		int rows=0;
		for(int i=1;i<=m;i++){
			rows +=sensorManager.newton(sensorManager.getSensors().size(),i);
		}
		return rows+1;
	}
	
	public String getRowLabel(int i){
		String s="";
		ArrayList<Integer> temp = fault.get(i);
		int tabs=temp.size();
		for(int f : temp){
			s+=f + "\t";
		}
		for(int j=0;j<(m+1)-tabs;j++) s+="\t";
		return s;
	}

	public String getBrokenSensorsIds(int i){
		String s="";
		ArrayList<Integer> temp = fault.get(i);
		for(int f : temp){
			if(!s.equals(""))s+=", " + f;
			else s+= f;
		}
		return s;
	}
	public ArrayList<ArrayList<Integer>>getFaultArray(int m, int size){
		ArrayList<ArrayList<Integer>> fault = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp;
		for(int x=1;x<=m;x++)
		for(int i=0;i<size;i++){
			temp = new ArrayList<Integer>();
			if(x>1){
				for(int j=i+1;j<size;j++){
					temp = new ArrayList<Integer>();
					if(x>2){
						for(int l=j+1;l<size;l++){
							temp = new ArrayList<Integer>();
							if(x>3){
								for(int n=l+1;n<size;n++){
									temp = new ArrayList<Integer>();
									if(x>4){
										for(int o=n+1;o<size;o++){
											temp = new ArrayList<Integer>();
											temp.add(i+1);
											temp.add(j+1);
											temp.add(l+1);
											temp.add(n+1);
											temp.add(o+1);
											fault.add(temp);
										}
									}else{
										temp.add(i+1);
										temp.add(j+1);
										temp.add(l+1);
										temp.add(n+1);
										fault.add(temp);
									}
								}
							}else{
								temp.add(i+1);
								temp.add(j+1);
								temp.add(l+1);
								fault.add(temp);
							}
						}
					}else{
						temp.add(i+1);
						temp.add(j+1);
						fault.add(temp);
					}
				}
			}else{
				temp.add(i+1);
				fault.add(temp);
			}
		}
		
		return fault;
	}
	
	public int getRows() {
		return rows;
	}
	
	
}
