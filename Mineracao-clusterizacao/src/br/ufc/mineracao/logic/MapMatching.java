package br.ufc.mineracao.logic;

import java.util.ArrayList;

import br.ufc.mineracao.dao.GraphDAO;
import br.ufc.mineracao.dao.ResultDAO;
import br.ufc.mineracao.model.Point;
import br.ufc.mineracao.model.Vertex;

public class MapMatching {
	
	private ArrayList<Vertex> verteces;
	
	public void map(ArrayList<Point> clusters){
		GraphDAO gdao = new GraphDAO();
		verteces = gdao.findAllVertices();
		for (Point point : clusters) {
			point.idVertex = findVertex(point);
		}
		
	}
	
	private int findVertex(Point p){
		Vertex v = verteces.get(0);
		for (Vertex vertex : verteces) {
			if(euclideanDistance(p, new Point(0, vertex.longitude, vertex.latitude)) < euclideanDistance(p, new Point(0, v.longitude, v.latitude)))
				v = vertex;
		}
		return v.id;
	}
	
	private double euclideanDistance(Point source, Point target){
		return Math.sqrt(Math.pow((source.longitude - target.longitude),2)+ Math.pow((source.latitude - target.latitude),2));
	}
	
	public static void main(String[] args) {
		ResultDAO rdao = new ResultDAO();
		MapMatching map = new MapMatching();
		Clustering c = new Clustering();
				
		ArrayList<Point> clusters = rdao.selectClustersByWeekday(3);
		System.out.println("mapeando...");
		map.map(clusters);
		c.writeCSV("teste_mapping.csv", clusters);
		System.out.println("Terminou...");
	}
	
	
}
