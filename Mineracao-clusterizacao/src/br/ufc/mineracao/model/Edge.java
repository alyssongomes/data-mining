package br.ufc.mineracao.model;

public class Edge {
	
	public int idEdge;
	public int source;
	public int target;
	public double cost;
	
	public Edge(int idEdge, int idSource, int idTarget, double cost){
		this.idEdge = idEdge;
		this.source = idSource;
		this.target = idTarget;
		this.cost = cost;
	}

}
