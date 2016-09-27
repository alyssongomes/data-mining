package br.ufc.mineracao.model;

public class Point {
	public int studentId;
	public int idTaxiDriver;
	public double longitude;
	public double latitude;
	public int cluster;
	public boolean classfield = false; //foi visitado, por padrão -1, ou seja, não foi visitado
	public boolean iscore;
	public int weekday;

	public Point(int idTaxiDriver, double longitude, double latitude){
		this.idTaxiDriver = idTaxiDriver;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}
