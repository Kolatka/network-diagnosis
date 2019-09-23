package graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import dsk.SensorManager;


public class ExampleGraphData {
	int id;
	String name;
	int nodes;
	int edges[][];
	
	ExampleGraphData(int id, String name, int nodes, int edges[][]){
		this.id = id;
		this.name = name;
		this.nodes = nodes;
		this.edges = edges;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public int[][] getEdges() {
		return edges;
	}

	public void setEdges(int[][] edges) {
		this.edges = edges;
	}
	
	
	
}
