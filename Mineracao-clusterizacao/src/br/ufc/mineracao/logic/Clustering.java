package br.ufc.mineracao.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class Clustering {
	
	private List<Point> points = null;
	private int clusterId = 1;
	
	
	public void dbSCAN(int minPoints, double eps){
		points = PointDAO.queryPointByHour("09:00:00", "09:01:00", "2008-02-03");
		boolean expansion;
		
		for(int i=0; i<PointDAO.LENGTH; i++){
			//points.get(i).weekday = 1;
			if(points.get(i).classfield == -1){
				expansion = expandCluster(points.get(i),clusterId,minPoints,eps);
				if(expansion == true){
					clusterId++;
				}
			}
		}
		System.out.println(clusterId);
		
	}
	
	private boolean expandCluster(Point p, int clusterId, int minPoint, double eps){
		ArrayList<Point> neighbors = neighborsPoint(p,eps);//vizinhos
		if(neighbors.size() < minPoint){
			p.cluster = -1;//Outlier
			p.iscore = false;
			return false;
		}else{
			for (Point point : neighbors) {
				point.cluster = clusterId;
			}
			neighbors.remove(p);
			
			for (int i = 0; i < neighbors.size(); i++) {
				Point point = neighbors.get(i);
				if(point.idTaxiDriver != p.idTaxiDriver ){
					ArrayList<Point> neigh = neighborsPoint(point,eps);
					if(neigh.size() >= minPoint){
						point.iscore = true; //marco como centroide
						for (Point point2: neigh) {
							if(point2.classfield == -1  || point2.cluster == -1){
								if(point2.classfield == -1){
									neighbors.add(point2);
								}
								point2.cluster = clusterId;
								point2.iscore = false;
							}
						}
					}
				}
			}
			return true;
		}
	}
	
	//Vizinhos de 'p'
	private ArrayList<Point> neighborsPoint(Point p, double eps){
		ArrayList<Point> neighbors = new ArrayList<Point>();
		for (Point point : points) {
			if(euclideanDistance(p, point) <= eps){
				neighbors.add(point);
			}
		}
		return neighbors;
	}
	
	//Distancia euclidiana
	private double euclideanDistance(Point source, Point target){
		return Math.sqrt(Math.pow(source.longitude - target.longitude,2)+ Math.pow(source.latitude - target.latitude,2));
	}
	
	
	public static void main(String[] args) {
		Clustering c = new Clustering();
		long start = System.currentTimeMillis();
		//Clustering.dbSCAN(100,0.00333982674912137);
		c.dbSCAN(10,0.00301);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
	}
	
}
