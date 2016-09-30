package br.ufc.mineracao.logic;

import java.util.ArrayList;

import br.ufc.mineracao.dao.GraphDAO;
import br.ufc.mineracao.model.Edge;
import br.ufc.mineracao.model.Vertex;

public class Dijkstra {
	
	private int INFINITY = Integer.MAX_VALUE;
	private ArrayList<Vertex> verteces = null;
	private ArrayList<Vertex> minPath = new ArrayList<Vertex>();
	
	public void dijkstra(int idSouce, int idTarget){
		GraphDAO gdao = new GraphDAO();
		verteces = gdao.findAllVertices();
		
		for (Vertex vertex : verteces) {
			if(vertex.id != idSouce){
				vertex.estimate = INFINITY;
				vertex.ancestor = null;
			}else{
				vertex.estimate = 0;
			}
		}
		
		Vertex ver = findVertex(idSouce);
		ver.ancestor = ver;
		
		ArrayList<Vertex> open = verteces; 
		while (open.size() != 0) {
			Vertex u = extractMin(open);
			minPath.add(new Vertex(u.id, u.longitude, u.latitude));
			remove(open,u);
			
			for(Edge e: gdao.findVertices(u.id)){
				Vertex v = findVertex(e.target); 
				if (v.estimate > u.estimate + e.cost) {
					v.estimate = u.estimate +e.cost;
					v.ancestor = u;
				}
			}
		}
		
		
	}
	
	public Vertex extractMin(ArrayList<Vertex> verteces){
		Vertex v = verteces.get(0);
		for (Vertex vertex : verteces) {
			if(vertex.estimate < v.estimate)
				v = vertex;
		}
		return v;
	}
	
	public Vertex findVertex(int id){
		for (Vertex vertex : verteces) {
			if(vertex.id == id)
				return vertex;
		}
		return null;
	}
	
	public void remove(ArrayList<Vertex> verteces, Vertex v){
		for (int i =0; i< verteces.size(); i++) {
			if(verteces.get(i).id == v.id)
				verteces.remove(i);
		}
	}
	
	
}
