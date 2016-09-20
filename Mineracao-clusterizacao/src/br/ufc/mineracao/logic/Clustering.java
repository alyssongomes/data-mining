package br.ufc.mineracao.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class Clustering {
	
	private static List<Point> points = null;
	private static int clusterId = 1;
	
	
	public static void dbSCAN(int minPoints, double eps){
		points = PointDAO.queryPointByHour("17:00:00", "18:00:00", "2008-02-02");
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
	
	private static boolean expandCluster(Point p, int clusterId, int minPoint, double eps){
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
			
			for (Iterator<Point> po = neighbors.iterator();po.hasNext();) {
				Point point = po.next();
				ArrayList<Point> neigh = neighborsPoint(point,eps);
				if(neigh.size() >= minPoint){
					List<Point> pointsToBeAdd = new ArrayList<Point>();
					point.iscore = true; //marco como centroide
					for (Iterator<Point> po2 = neigh.iterator();po2.hasNext();) {
						Point point2 = po2.next();
						if(point2.classfield == -1  || point2.cluster == -1){
							if(point2.classfield == -1){
								//neighbors.add(point2);
								pointsToBeAdd.add(point2);
							}
							point2.cluster = clusterId;
							point2.iscore = false;
						}
					}
					neigh.addAll(pointsToBeAdd);
				}
				
			}
			return true;
		}
	}
	
	private static ArrayList<Point> neighborsPoint(Point p, double eps){
		ArrayList<Point> neighbors = new ArrayList<Point>();
		for (Point point : points) {
			if(euclideanDistance(p, point) <= eps){
				neighbors.add(point);
			}
		}
		return neighbors;
	}
	
	//Distancia euclidiana
	private static double euclideanDistance(Point source, Point target){
		return Math.sqrt(Math.pow(source.longitude - target.longitude,2)+ Math.pow(source.latitude - target.latitude,2));
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		//Clustering.dbSCAN(100,0.00333982674912137);
		Clustering.dbSCAN(100,0.005);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
	}
	
}
