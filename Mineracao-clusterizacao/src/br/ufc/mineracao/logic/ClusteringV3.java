package br.ufc.mineracao.logic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class ClusteringV3 {
	
	private List<Point> points = null;
	private int k = 0;
	
	
	public void dbSCAN(int minPoints, double eps){
		PointDAO pdao = new PointDAO();
		points = pdao.queryPointByHour("08:00:00", "09:00:00", "2008-02-06");
		int clusterId = 1;
		
		System.out.println("Tamanho: "+pdao.LENGTH);
		
		ArrayList<Point> neighbord = null;
		for (Point point : points) {
			if(point.classfield == false){
				k++;
				System.out.println(k);
				point.classfield = true;
				neighbord =neighborsPoint(point, eps);
				if(neighbord.size() < minPoints){
					point.cluster = -1;
				}else{
					point.cluster = clusterId;
					point.iscore = true;
					expandCluster(point, clusterId, minPoints, eps);
					clusterId++;
				}
			}
		}
			
		System.out.println(clusterId);
		
	}
	
	private void expandCluster(Point p, int clusterId, int minPoint, double eps){
		ArrayList<Point> neighbors = neighborsPoint(p, eps);
		Point pn = null;
		for (int i=0; i<neighbors.size();i++) {
			pn = neighbors.get(i);
			if (pn.classfield == false) {
				pn.classfield = true;
				ArrayList<Point> neighborsOfPoint = neighborsPoint(pn, eps);
				if(neighborsOfPoint.size() >= minPoint){
					pn.iscore = true;
					neighbors.add(pn);
				}else{
					pn.iscore = false;
				}
				
				if(pn.cluster == -1){
					pn.cluster = clusterId;
				}
				
				k++;
				System.out.println(k);
			}
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
		return Math.sqrt(Math.pow((source.longitude - target.longitude),2)+ Math.pow((source.latitude - target.latitude),2));
	}
	
	//Gerar arquivo de saída
	private void writeCSV(){
		try{
			PrintWriter writer = new PrintWriter("pointsV6.csv", "UTF-8");
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
		ClusteringV3 c = new ClusteringV3();
		long start = System.currentTimeMillis();
		c.dbSCAN(10,0.00301);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
		System.out.println("Gravando csv...");
		c.writeCSV();
		System.out.println("CSV gravado!");
	}
	
}
