package graph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphManager {
	public Graph graph;
	private Viewer viewer;
	private int id;
	private String name;
	
	public enum NodeColor{
		RED, GREEN, YELLOW, GRAY, ORANGE, DEFAULT
	}
	
	public GraphManager(String name, int id){
		this.id = id;
		this.name = name;
		graph = new SingleGraph(name + " " + id);
		setupGraph();
	}
	
	public void setupGraph(){
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	}
	
	public void clearGraph(){
		graph.clear();
		setupGraph();
	}
	
	public void addNode(int id){
		String name = Integer.toString(id);
		Node node = graph.addNode(name);
		node.addAttribute("ui.style", "shape:circle;fill-color: #5d6fe8;size: 40px; text-alignment: center;");
		node.addAttribute("ui.label", name);
	}
	
	public void changeColor(int id, NodeColor color){
		Node node = graph.getNode(id-1);
		switch(color){
			case RED:
				node.addAttribute("ui.style", "fill-color: #f25a52;");
				break;
			case YELLOW:
				node.addAttribute("ui.style", "fill-color: #f7f95c;");
				break;
			case GREEN:
				node.addAttribute("ui.style", "fill-color: #13ad1a;");
				break;
			case DEFAULT:
				node.addAttribute("ui.style", "fill-color: #5d6fe8;");
				break;
			case GRAY:
				node.addAttribute("ui.style", "fill-color: #c7c8c9;");
				break;
			case ORANGE:
				node.addAttribute("ui.style", "fill-color: #f2bc48;");
				break;
			default:
	            break;
		}
	}
	
	public void displayGraph(){
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		View view = viewer.addDefaultView(false);
		viewer.enableAutoLayout();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
		viewer = graph.display();
	}
	
	public void addEdge(int id1, int id2){
		String name1 = Integer.toString(id1);
		String name2 = Integer.toString(id2);
		graph.addEdge(name1+name2, name1, name2, true);
	}
	
	
}
