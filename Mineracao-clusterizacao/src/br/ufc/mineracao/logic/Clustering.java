package br.ufc.mineracao.logic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class Clustering {
	
	private List<Point> points = null;
	
	
	public void dbSCAN(int minPoints, double eps){
		PointDAO pdao = new PointDAO();
		points = pdao.queryPointByHour("09:00:00", "10:00:00", "2008-02-03");
		boolean expansion;
		int clusterId = 1;
		
		for(int i=0; i < pdao.LENGTH; i++){
			points.get(i).weekday = 2;// 1- sábado, 2- domingo, 3- segunda, 4-terça, 5-quarta 
			points.get(i).studentId = 369584;
			if(points.get(i).classfield == false){
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
			p.classfield = true;
			p.iscore = false;
			return false;
		}else{
			p.cluster = clusterId; //p é atribuído a um cluster
			p.classfield = true; // p é marcado como visitado
			p.iscore = true; // p é marcado como um core point
			for (Point point : neighbors) { //todos os vizinhos são atribuidos ao cluster com o id = clusterId
				point.cluster = clusterId;
			}
			neighbors.remove(p);
			
			for (int i = 0; i < neighbors.size(); i++) { //os vizinhos de p são testados
				Point point = neighbors.get(i);
				if(point.idTaxiDriver != p.idTaxiDriver ){
					point.classfield = true; //marcado como visitado
					ArrayList<Point> neigh = neighborsPoint(point,eps);// consultando os vizinhos dos vizinhos de p					
					if(neigh.size() >= minPoint){
						point.iscore = true; //marcado como core point
						for (Point point2: neigh) {
							if(point2.classfield == false  || point2.cluster == -1){//testa se ainda não foi vizitado ou se foi marcado como um outlier em outras interações
								if(point2.classfield == false){
									point2.classfield = true; //marco como visitado
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
	
	//Visinhos de 'p'
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
	
	private void writeCSV(){
		try{
			PrintWriter writer = new PrintWriter("points_dom_09_10.csv", "UTF-8");
			writer.println("student_id;id_taxista;weekday;latitude;longitude;cluster;iscore");
			for (Point p : points) {
				writer.println(p.studentId+";"+p.idTaxiDriver+";"+p.weekday+";"+p.latitude+";"+p.longitude+";"+p.cluster+";"+p.iscore);
			}
			writer.close();
		}catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
	}
	
	
	public static void main(String[] args) {
		Clustering c = new Clustering();
		long start = System.currentTimeMillis();
		c.dbSCAN(10,0.00301);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
		System.out.println("Gravando csv...");
		c.writeCSV();
		System.out.println("CSV gravado!");
	}
	
}
