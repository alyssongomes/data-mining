package br.ufc.mineracao.logic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufc.mineracao.dao.PointDAO;
import br.ufc.mineracao.model.Point;

public class ClusteringV3 {
	
	private List<Point> points = null;
	
	public void dbSCAN(int minPoints, double eps){
		PointDAO pdao = new PointDAO();
		points = pdao.queryPointByHour("09:00:00", "10:00:00", "2008-02-04");
		int clusterId = 0;
		double qtd = 0;
		
		System.out.println("Tamanho: "+pdao.LENGTH);
		
		for (Point point : points) {
			if(point.visited == false){
				point.visited = true;
				ArrayList<Point> neighbord = new ArrayList<Point>(); 
				int count = neighborsPoint(point, eps, neighbord);
				//System.out.println("Quantidade de taxisitas diferentes: "+count);
				qtd += neighbord.size();
				if(count < minPoints){
					point.cluster = Point.OUTLIER;
				}else{
					//System.out.println("Id do cluester: "+clusterId);
					clusterId++;
					point.cluster = clusterId;
					point.type = Point.CORE_POINT;
					expandCluster(neighbord, clusterId, minPoints, eps);
				}
			}
		}
			
		System.out.println(clusterId);
		System.out.println("Média: "+qtd/pdao.LENGTH);
		
	}
	
	private void expandCluster(ArrayList<Point> neighbors, int clusterId, int minPoint, double eps){
		int qtd = 0;
		for (int i=0; i < neighbors.size();i++) {
			//System.out.println("Qtd atual: "+neighbors.size());
			Point pn = neighbors.get(i);
			if (pn.visited == false) {
				pn.visited = true;
				ArrayList<Point> neighborsOfPoint = new ArrayList<Point>(); 
				int count = neighborsPoint(pn, eps, neighborsOfPoint);
				qtd += count;
				if(count >= minPoint){
					pn.type = Point.CORE_POINT;
					for (Point point : neighborsOfPoint) {
						neighbors.add(point);
					}
					//System.out.println("Quantidade de vizinhos distintos: "+count);
					//System.out.println("Quantidade de vizinhos sem distinção: "+neighborsOfPoint.size());
					//System.out.println("Qtd com os vizinhos: "+neighbors.size());
				}else{
					pn.type = Point.BORDER_POINT;
				}
				
				if(pn.cluster == Point.OUTLIER){
					pn.cluster = clusterId;
				}
			}
		}
		System.out.println("Quantidade: "+qtd);
	}
	
	
	
	//Visinhos de 'p'
	private int neighborsPoint(Point p, double eps, ArrayList<Point> neighbors){
		for (Point point : points) {
			//if(p.idTaxiDriver != point.idTaxiDriver)
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
		return distincts.size(); //qtd de taxistas ao redor;
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
		ClusteringV3 c = new ClusteringV3();
		long start = System.currentTimeMillis();
		c.dbSCAN(40,0.005);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed/1000+" segundos");
		System.out.println("Gravando csv...");
		c.writeCSV();
		System.out.println("CSV gravado!");
	}
	
}
