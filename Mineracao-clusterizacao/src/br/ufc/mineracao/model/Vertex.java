package br.ufc.mineracao.model;

public class Vertex implements Comparable<Vertex>{
	public int id;
	public double longitude;
	public double latitude;
	public double estimate;
	public Vertex ancestor;
	public boolean open = true;
	
	public Vertex(){}
	
	public Vertex(int id, double longitude, double latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	public int compareTo(Vertex o) {
		double result = this.estimate - o.estimate;
		if(result > 0)
			return 1;
		else if(result < 0)
			return -1;
		else
			return 0;
	}
	
	
}
