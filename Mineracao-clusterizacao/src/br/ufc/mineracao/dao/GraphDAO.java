package br.ufc.mineracao.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufc.mineracao.factory.ConnectionFactory;
import br.ufc.mineracao.model.Edge;
import br.ufc.mineracao.model.Point;
import br.ufc.mineracao.model.Vertex;

public class GraphDAO {
	
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	
	public ArrayList<Edge> findRoads(){
		try {
			con = ConnectionFactory.getConnection();
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery("SELECT * FROM roads ");
			
			ArrayList<Edge> edges = new ArrayList<Edge>();
			rs.beforeFirst();
			while(rs.next()){
				edges.add(new Edge(rs.getInt("id_edge"), rs.getInt("id_source"), rs.getInt("id_target"), rs.getDouble("cost_d")));
			}
			
			stm.close();
			rs.close();
			
			return edges;
		} catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
		
		return null;
	}
	
	public ArrayList<Vertex> findAllVertices(){
		try {
			con = ConnectionFactory.getConnection();
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery("SELECT * FROM vertices");
			
			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			rs.beforeFirst();
			while(rs.next()){
				vertices.add(new Vertex(rs.getInt("id"), rs.getDouble("longitude"), rs.getDouble("latitude")));
			}
			
			stm.close();
			rs.close();
			
			return vertices;
		} catch (Exception e) {
			System.out.println("[ERROR]: "+e.toString());
		}
		
		return null;
	}

}
