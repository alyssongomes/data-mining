package br.ufc.mineracao.logic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class Clustering {
	
	private List<Point> points = null;
	
	public void dbSCAN(int minPoints, double eps){
		PointDAO pdao = new PointDAO();
		points = pdao.queryPointByHour("09:00:00", "10:00:00", "2008-02-04");
		int clusterId = 0;
		
		System.out.println("Tamanho: "+pdao.LENGTH);
		
		for (Point point : points) {
			if(point.visited == false){
				point.visited = true;
				
				ArrayList<Point> neighbord = new ArrayList<Point>(); 
				int count = neighborsPoint(point, eps, neighbord);
				
				if(count < minPoints){
					point.cluster = Point.OUTLIER;
				}else{
					clusterId++;
					point.cluster = clusterId;
					point.type = Point.CORE_POINT;
					expandCluster(neighbord, clusterId, minPoints, eps);
				}
			}
		}
			
		System.out.println("Quantidade de clusters: "+clusterId);
		
	}
	
	private void expandCluster(ArrayList<Point> neighbors, int clusterId, int minPoint, double eps){
		System.out.println("Expandindo cluster "+clusterId+" ...");
		
		for (Point point : neighbors) {
			point.cluster = clusterId;
		}
		
		while(neighbors.size() > 0){
			Point p = neighbors.get(0);
			neighbors.remove(0);
			
			if(p.visited == false){
				p.visited = true;
				ArrayList<Point> neighborsOfPoint = new ArrayList<Point>(); 
				int count = neighborsPoint(p, eps, neighborsOfPoint);
				
				for (Point point : neighborsOfPoint) {
					point.cluster = clusterId;
				}
				
				if(count >= minPoint){
					p.type = Point.CORE_POINT;
					neighbors.addAll(neighborsOfPoint);
				}else{
					p.type = Point.BORDER_POINT;
				}	
			}	
		}
	}
	
	
	
	//Visinhos de 'p'
	private int neighborsPoint(Point p, double eps, ArrayList<Point> neighbors){
		for (Point point : points) {
			if(euclideanDistance(p, point) < eps){
				neighbors.add(point);
			}
		}
		
		ArrayList<Point> distincts = new ArrayList<Point>();
		for (Point point : neighbors) {
			if(!contains(distincts, point)){
				distincts.add(point);
			}
		}
		return distincts.size();
	}
	
	//Distancia euclidiana
	private double euclideanDistance(Point source, Point target){
		return Math.sqrt(Math.pow((source.longitude - target.longitude),2)+ Math.pow((source.latitude - target.latitude),2));
	}
	
	private boolean contains(ArrayList<Point> list, Point p){
		for (Point point : list) {
			if(point.idTaxiDriver == p.idTaxiDriver)
				return true;
		}
		return false;
	}
	
	//Gerar arquivo de saída
	private void writeCSV(){
		try{
			PrintWriter writer = new PrintWriter("points_dados_09_10(4).csv", "UTF-8");
			writer.println("student_id;id_taxista;weekday;latitude;longitude;cluster;iscore");
			for (Point p : points) {
				writer.println(p.studentId+";"+p.idTaxiDriver+";"+p.weekday+";"+p.latitude+";"+p.longitude+";"+p.cluster+";"+p.type);
			}
			writer.close();
		}catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
	}
	
	
	public static void main(String[] args) {
		Clustering c = new Clustering();
		long start = System.currentTimeMillis();
		c.dbSCAN(40,0.005);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
		System.out.println("Gravando csv...");
		c.writeCSV();
		System.out.println("CSV gravado!");
	}
	
}
