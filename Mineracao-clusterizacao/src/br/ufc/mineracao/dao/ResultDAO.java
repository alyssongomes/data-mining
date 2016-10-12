package br.ufc.mineracao.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import br.ufc.mineracao.factory.ConnectionFactory;
import br.ufc.mineracao.model.Point;

public class ResultDAO {
	
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	
	public ArrayList<Point> selectClustersByWeekday(int weekday){
		try {
			con = ConnectionFactory.getConnection();
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery("SELECT * FROM clusters WHERE weekday = "+weekday);
			
			ArrayList<Point> points = new ArrayList<Point>();
			rs.beforeFirst();
			while(rs.next()){
				Point p = new Point(rs.getInt("id_taxista"), rs.getDouble("longitude"), rs.getDouble("latitude"));
				p.cluster = rs.getInt("cluster");
				p.weekday = rs.getInt("weekday");
				p.type = rs.getInt("iscore");
				p.studentId = rs.getInt("student_id");
				points.add(p);
			}
			
			stm.close();
			rs.close();
			
			return points;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	//Gerar arquivo de saída
	public void writeCSV(String fileName, ArrayList<Point> list){
		try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			System.out.println(fileName+" ...");
			writer.println("student_id;id_taxista;weekday;latitude;longitude;cluster;iscore");
			for (Point p : list) {
				writer.println(p.studentId+";"+p.idTaxiDriver+";"+p.weekday+";"+p.latitude+";"+p.longitude+";"+p.cluster+";"+p.type);
			}
			writer.close();
		}catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
	}
	
	public static void main(String[] args) {
		ResultDAO rdao = new ResultDAO();
		System.out.println("Gerando CSV ...");
		rdao.writeCSV("quinta.csv",rdao.selectClustersByWeekday(4));
		System.out.println("CSV gerado ...");
		System.out.println("Gerando CSV ...");
		rdao.writeCSV("sexta.csv",rdao.selectClustersByWeekday(5));
		System.out.println("CSV gerado ...");
	}

}
