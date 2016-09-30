package br.ufc.mineracao.logic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class ClusteringV2 {
	
	private List<Point> points = null;
	
	
	public void dbSCAN(int minPoints, double eps){
		PointDAO pdao = new PointDAO();
		points = pdao.queryPointByHour("08:00:00", "08:10:00", "2008-02-06");
		boolean expansion;
		int clusterId = 1;
		
		System.out.println("Tamanho: "+pdao.LENGTH);
		
		for(int i=0; i < pdao.LENGTH; i++){
			points.get(i).weekday = 3;// 1- sábado, 2- domingo, 3- segunda, 4-terça, 5-quarta 
			points.get(i).studentId = 369584;
			if(points.get(i).visited == false){
				expansion = expandCoreCluster(points.get(i),clusterId,minPoints,eps);
				if(expansion == true){
					clusterId++;
				}
			}
		}
		
		for(int j=0; j<pdao.LENGTH; j++){
			if(points.get(j).type == Point.BORDER_POINT){//para do ponto borda
				points.get(j).cluster = findCoreClosestPoint(points.get(j), neighborsPoint(points.get(j), eps));
			}
		}
			
		System.out.println(clusterId);
		
	}
	
	private boolean expandCoreCluster(Point p, int clusterId, int minPoint, double eps){
		ArrayList<Point> neighbors = neighborsPoint(p,eps);//vizinhos
		if(neighbors.size() < minPoint){
			p.cluster = -1;//Outlier
			p.visited = true;
			p.type = Point.BORDER_POINT;
			return false;
		}else{
			p.cluster = clusterId; //p é atribuído a um cluster
			p.visited = true; // p é marcado como visitado
			p.type = Point.CORE_POINT; // p é marcado como um core point
			
			for (int i = 0; i < neighbors.size(); i++) { //os vizinhos de p são testados
				Point point = neighbors.get(i);
				if(point.idTaxiDriver != p.idTaxiDriver ){
					point.visited = true; //marcado como visitado
					ArrayList<Point> neigh = neighborsPoint(point,eps);// consultando os vizinhos dos vizinhos de p					
					if(neigh.size() >= minPoint){
						point.type = Point.CORE_POINT; //marcado como core point
						point.cluster = clusterId;
						for (Point point2 : neigh) {
							if(point2.visited == false){
								neighbors.add(point2);
							}
							if(point2.visited == false && point2.cluster == -1){
								point2.type = Point.BORDER_POINT; //marcado como bordar
							}
						}
					}
				}
			}
			return true;
		}
	}
	
	private int findCoreClosestPoint(Point p, ArrayList<Point> neighborsP){
		ArrayList<Point> corePoints = new ArrayList<Point>();
		for (Point point : corePoints) {
			if(point.type == Point.CORE_POINT)
				corePoints.add(point);
		}
		if(corePoints.size() > 0){
			Point closer = corePoints.get(0);
			for (Point core : corePoints) {
				if(euclideanDistance(p, closer) > euclideanDistance(p, core))
					closer = core;
			}
			return closer.cluster; 
		}
		return -1;
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
	
	//Gerar arquivo de saída
	private void writeCSV(){
		try{
			PrintWriter writer = new PrintWriter("pointsV2.csv", "UTF-8");
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
		ClusteringV2 c = new ClusteringV2();
		long start = System.currentTimeMillis();
		c.dbSCAN(10,0.00301);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
		System.out.println("Gravando csv...");
		c.writeCSV();
		System.out.println("CSV gravado!");
	}
	
}
