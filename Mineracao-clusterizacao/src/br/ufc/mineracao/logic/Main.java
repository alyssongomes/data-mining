package br.ufc.mineracao.logic;

import java.util.ArrayList;

import br.ufc.mineracao.dao.ResultDAO;
import br.ufc.mineracao.model.Point;

public class Main {
	public static void main(String[] args) {
		ResultDAO rdao = new ResultDAO();
		MapMatching m = new MapMatching();
		Clustering c = new Clustering();
		
		System.out.println("Consultando os dados...");
		ArrayList<Point> clusters = rdao.selectClustersByWeekday(3);
		System.out.println("Mapeando...");
		m.map(clusters);
		System.out.println("Reclusterizando...");
		c.dbSCAN(10, 0.1, clusters);// dbscan com distancia de rede
		c.writeCSV("nova_clusterizacao.csv", clusters);
	}
}
