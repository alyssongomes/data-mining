package br.ufc.mineracao.dao;

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
	
	public ArrayList<Point> findAllResults(){
		try {
			con = ConnectionFactory.getConnection();
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery("SELECT * FROM clusters");
			
			ArrayList<Point> points = new ArrayList<Point>();
			rs.beforeFirst();
			while(rs.next()){
				Point p = new Point(rs.getInt("id_taxista"), rs.getDouble("longitude"), rs.getDouble("latitude"));
				p.cluster = rs.getInt("cluster");
				p.weekday = rs.getInt("weekday");
				p.type = rs.getInt("iscore");
				points.add(p);
			}
			
			stm.close();
			rs.close();
			
			return points;
		} catch (Exception e) {
			
		}
		return null;
	}

}
