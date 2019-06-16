package com.assign2;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SpLoc
 */
@WebServlet("/SpLoc")
public class SpLoc extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String lati = request.getParameter("lat");
		String longi=request.getParameter("long");
		String nea=request.getParameter("near");
		double lat = Double.parseDouble(lati);
		double lon = Double.parseDouble(longi);
		double near = Double.parseDouble(nea);
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
		      Class.forName("org.sqlite.JDBC");
		      conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");
		      //conn = DriverManager.getConnection("jdbc:sqlite:"+"resource/db/xxwDB.db");
		      conn.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = conn.createStatement();
		      String sql = "SELECT * FROM earth";
		      ResultSet rs = stmt.executeQuery(sql);

		      while ( rs.next() ) {
		         
		    	  double latSql = Double.parseDouble("".equals(rs.getString("LATITUDE"))?"0.0":rs.getString("LATITUDE"));
		    	  double longSql = Double.parseDouble("".equals(rs.getString("LONGITUDE"))?"0.0":rs.getString("LONGITUDE"));
		    	  //System.out.println(distance(lon,lat,longSql,latSql));
		    	  if(distance(lon,lat,longSql,latSql)<=near) {
		    		  System.out.println(rs.getString(1)+" "+distance(lon,lat,longSql,latSql));
		    	  }
		    	  
		      }
		      rs.close();
		      stmt.close();
		      conn.close();
		    } 
		    catch(SQLException se) {
	            // 处理 JDBC 错误
	            se.printStackTrace();
	        }
		    catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		    finally{
	            // 最后是用于关闭资源的块
	            try{
	                if(stmt!=null)
	                stmt.close();
	            }catch(SQLException se2){
	            }
	            try{
	                if(conn!=null)
	                conn.close();
	            }catch(SQLException se){
	                se.printStackTrace();
	            }
	        }
		    System.out.println("Operation done successfully");
	}
	
	
	public static double distance(double lon1, double lat1, double lon2, double lat2) {
		double pi = 0.0174532925199432944; // PI / 180;
		double t5 = Math.sin(lat1 * pi) * Math.sin(lat2 * pi) + Math.cos(lat1 * pi) * Math.cos(lat2 * pi)
		* Math.cos((lon1 - lon2) * pi);
		return (Math.atan(-t5 / Math.sqrt(-t5 * t5 + 1)) + 2 * Math.atan(1)) * 3437.74677 * 1.1508 * 1.6093470878864446;    //kilometer
		}

}
