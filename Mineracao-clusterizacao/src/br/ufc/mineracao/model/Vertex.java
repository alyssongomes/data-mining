package br.ufc.mineracao.model;

public class Vertex {
	public int id;
	public double longitude;
	public double latitude;
	public double estimate;
	public Vertex ancestor;
	public boolean open;
	
	public Vertex(int id, double longitude, double latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	
}
