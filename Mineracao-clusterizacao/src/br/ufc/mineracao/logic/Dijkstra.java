package br.ufc.mineracao.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import br.ufc.mineracao.dao.GraphDAO;
import br.ufc.mineracao.model.Edge;
import br.ufc.mineracao.model.Vertex;

public class Dijkstra {
	
	private GraphDAO gdao;
	private double INFINITY = Double.MAX_VALUE;
	private ArrayList<Vertex> verteces;
	private ArrayList<Edge> edges;
	private HashMap<Integer, Double> distances;

	public Dijkstra(){
		gdao = new GraphDAO();
		verteces = gdao.findAllVertices();
		edges = gdao.findRoads();
	}
	
	public double dijkstra(int idSouce, int idTarget){
		
		distances = new HashMap<>();
		for (Vertex vertex : verteces) {
			distances.put(vertex.id, INFINITY);
		}
		
		PriorityQueue<Vertex> queue = new PriorityQueue<>();
		
		
		Vertex ver = findVertex(idSouce);
		ver.id = idSouce;
		ver.estimate = 0.0;
		distances.put(ver.id, 0.0);
		queue.add(ver);
		
		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			if(u.open){
				u.open = false;
				double uest = distances.get(u.id);
				for(Edge e: findVertices(u.id)){
					Vertex v = findVertex(e.target);
					double vest = distances.get(v.id);
					if (vest > (uest + e.cost)) {
						v.estimate = (e.cost+uest);
						distances.put(v.id, (uest + e.cost));
						queue.add(v);
					}
				}
			}
		}
		
		return distances.get(idTarget);
	}
	
	public Vertex findVertex(int id){
		for (Vertex vertex : verteces) {
			if(vertex.id == id)
				return vertex;
		}
		return null;
	}
	
	private ArrayList<Edge> findVertices(int idVertex){
		ArrayList<Edge> egs = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if(edge.source == idVertex)
				egs.add(edge);
		}
		return egs;
	}
	
	
}
