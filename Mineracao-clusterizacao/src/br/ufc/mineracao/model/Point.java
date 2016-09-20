package br.ufc.mineracao.model;

public class Point {
	public int StudentId;
	public int idTaxiDriver;
	public double longitude;
	public double latitude;
	public int cluster;
	public int classfield = -1; 
	public boolean iscore;
	public int weekday;

	public Point(int idTaxiDriver, double longitude, double latitude){
		this.idTaxiDriver = idTaxiDriver;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}
