package br.ufc.mineracao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufc.mineracao.factory.ConnectionFactory;
import br.ufc.mineracao.model.Point;

public class PointDAO {
	
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	public int LENGTH = 0;
	
	public List<Point> queryPointByHour(String hourBegin, String hourEnd, String data){
		try {
			con = ConnectionFactory.getConnection();
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery("SELECT * FROM tdrive WHERE data_hora BETWEEN '"+data+" "+hourBegin+"' AND '"+data+" "+hourEnd+"'");
			
			List<Point> points = new ArrayList<Point>();
			rs.beforeFirst();
			while(rs.next()){
				Point p = new Point(rs.getInt("id_taxista"), rs.getDouble("longitude"), rs.getDouble("latitude"));
				p.studentId = 369584;
				points.add(p);
				LENGTH++;
			}
			
			stm.close();
			rs.close();
			
			return points;
		} catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
		return null;
	}
}
