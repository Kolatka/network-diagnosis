package graph;

import java.util.ArrayList;

public class ExampleGraph {
	
	ArrayList<ExampleGraphData> list = new ArrayList<ExampleGraphData>();

	public ExampleGraph(){
		populateList();
	}

	public ExampleGraphData getData(int id){
		for(ExampleGraphData edg : list){
			if(edg.id==id) return edg;
		}
		return new ExampleGraphData(-1,"null",0, new int[][]{});
	}
	
	public ExampleGraphData getData(String name){
		for(ExampleGraphData edg : list){
			if(edg.name.equals(name)) return edg;
		}
		return new ExampleGraphData(-1,"null",0, new int[][]{});
	}

	public ArrayList<ExampleGraphData> getList() {
		return list;
	}
	
	public ArrayList<String> getNamesList(){
		 ArrayList<String> nameList = new ArrayList<String>();
		for(ExampleGraphData edg : list){
			nameList.add(edg.getName());
		}
		return nameList;
	}

	private void populateList(){
		int exampleNodes = 16;
		int exampleEdges[][] = new int[][]{
			{1,2},{1,4},{1,5},{1,9},{2,1},{2,3},{2,6},{2,10},{3,2},{3,4},{3,7},{3,11},{4,1},{4,3},{4,8},{4,12},
			{5,1},{5,6},{5,8},{5,13},{6,2},{6,5},{6,7},{6,14},{7,3},{7,6},{7,8},{7,15},{8,4},{8,5},{8,7},{8,16},
			{9,10},{9,12},{9,13},{9,1},{10,9},{10,11},{10,14},{10,2},{11,10},{11,12},{11,15},{11,3},{12,9},{12,11},{12,16},{12,4},
			{13,9},{13,14},{13,16},{13,5},{14,10},{14,13},{14,15},{14,6},{15,11},{15,14},{15,16},{15,7},{16,12},{16,13},{16,15},{16,8}
		};

		ExampleGraphData egd = new ExampleGraphData(1,"H4",exampleNodes,exampleEdges);
		list.add(egd);
	
	
		int exampleNodes2 = 8;
		int exampleEdges2[][] = new int[][]{
			{1,2},{1,4},{1,5},{2,1},{2,3},{2,6},{3,2},{3,4},{3,7},{4,1},{4,3},{4,8},
			{5,1},{5,6},{5,8},{6,2},{6,5},{6,7},{7,3},{7,6},{7,8},{8,4},{8,5},{8,7}
		};
		egd = new ExampleGraphData(2,"H3",exampleNodes2,exampleEdges2);
		list.add(egd);
	
		
		int exampleNodes3 = 4;
		int exampleEdges3[][] = new int[][]{
			{1,2},{1,4},{2,1},{2,3},{3,2},{3,4},{4,1},{4,3}
		};
		egd = new ExampleGraphData(3,"H2",exampleNodes3,exampleEdges3);
		list.add(egd);
		
		int exampleNodes4 = 5;
		int exampleEdges4[][] = new int[][]{
			{1,2},{1,4},{1,5},{2,1},{2,3},{2,5},{3,2},{3,4},{3,5},{4,1},{4,3},{4,5},{5,1},{5,2},{5,3},{5,4}
		};
		egd = new ExampleGraphData(4,"example4",exampleNodes4,exampleEdges4);
		list.add(egd);
		
		int exampleNodes5 = 6;
		int exampleEdges5[][] = new int[][]{
			{1,2},{1,4},{1,6},{2,1},{2,3},{2,5},{3,2},{3,4},{3,6},{4,3},{4,5},{4,1},{5,2},{5,4},{5,6},{6,1},{6,3},{6,5}
		};
		egd = new ExampleGraphData(5,"example5",exampleNodes5,exampleEdges5);
		list.add(egd);
		
	
	}
	
	
}
