package com.mashen.common.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class ConnectionFactory {
	private static ThreadLocal<Connection> thlocal=new ThreadLocal<Connection>();
	public static void main(String[] args) {
		try {
			System.out.println(ConnectionFactory.getInstance().getConnection().getClass().getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private BasicDataSource dataSource;
	
	private static ConnectionFactory instance=new ConnectionFactory();
	public static ConnectionFactory getInstance(){
		return instance;
	}
	private ConnectionFactory(){
		Properties pro=new Properties();
		try {
			InputStream input=
					this.getClass().getResourceAsStream("/jdbc.properties");
			pro.load(input);
			input.close();
			dataSource=BasicDataSourceFactory.createDataSource(pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	public static Connection getLocalThreadConnection() throws SQLException{
		Connection conn=thlocal.get();
		if(conn==null){
			Connection localConn=instance.getConnection();
			thlocal.set(localConn);
			return localConn;
		}else{
			return conn;
		}
		
	}
	
	public  static void closeAll(Connection conn,Statement st,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
