package com.assign2;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BigNight {
	
	
	public static void main(String arg[]) {
		
	Connection conn = null;
	Statement stmt = null;
	
	int countNight=0;
	int countDaytime = 0;
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
	         Integer dateSql = Integer.parseInt((rs.getString("TIME").substring(11, 19).replaceAll(":", "")));
	         Double magSql = Double.parseDouble("".equals(rs.getString("MAG"))?"0.0":rs.getString("MAG"));
	         if(magSql>4.0) {
	        	 if((dateSql>190000&&dateSql<240000)||(dateSql>0&&dateSql<70000)) {
	        		 countNight++; 
	        	 }
	        	 else {
	        		 countDaytime++;
	        	 }
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
	    System.out.println("night: "+countNight+" day: "+countDaytime);
}
}
