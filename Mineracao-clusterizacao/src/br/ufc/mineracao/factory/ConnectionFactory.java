package br.ufc.mineracao.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	
	private static Connection connection; 
	
	public static Connection getConnection(){
		try{
			connection = DriverManager.getConnection("jdbc:postgresql://localhost/db_taxis", "postgres", "postgres");
			return connection;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		ConnectionFactory.getConnection();

	}

}
