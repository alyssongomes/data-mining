package br.ufc.mineracao.model;

public class Point {
	
	public static int CORE_POINT = 1;
	public static int BORDER_POINT = 0;
	public static int OUTLIER = -1;
	
	public int studentId;
	public int idTaxiDriver;
	public double longitude;
	public double latitude;
	public int cluster;
	public boolean visited = false;
	public int type;
	public int weekday;
	
	/*Map Matching*/
	public int idVertex;

	public Point(int idTaxiDriver, double longitude, double latitude){
		this.idTaxiDriver = idTaxiDriver;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}
