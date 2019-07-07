package com.assign3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.assign2.SpLoc;

/*UTA-CSE 6331
Xiangxiang Wang
ID: 1001681420*/


/**
 * Servlet implementation class Queries
 */
@WebServlet("/Queries")
public class Queries extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("接收到get请求");
		String random=request.getParameter("randomLength");
		String lati=request.getParameter("lat");
		String longi=request.getParameter("long");
		String dis=request.getParameter("dis");
		Integer randomLength = Integer.parseInt(random);
		Double lat=Double.parseDouble(lati);
		Double longti=Double.parseDouble(longi);
		Double dist=Double.parseDouble(dis);
		System.out.println("succedd");
		
		Connection conn = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");
	      //conn = DriverManager.getConnection("jdbc:sqlite:"+"resource/db/xxwDB.db");
	      conn.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      
	      long timeBegin = Clock.systemDefaultZone().millis();
	      stmt = conn.createStatement();
	      String sql = "SELECT * FROM earth ORDER BY RANDOM() LIMIT "+randomLength;
	      ResultSet rs = stmt.executeQuery(sql);
	      
		   // 设置响应内容类型
	   	      response.setContentType("text/html");
	   		// 实际的逻辑是在这里
	   	      PrintWriter out = response.getWriter();
	   	      
	   	      out.println("<table border=\"1\">");
	   	      out.println("<tr><th>"+"ID"+"</th><th>"+"DISTANCE"+
		        			 "<th>"+"PLACE"+"</th>"
		        			 +"</tr>");

	      while ( rs.next() ) {
	         
	    	  //double magOut = Double.parseDouble("".equals(rs.getString("MAG"))?"0.0":rs.getString("MAG"));
	    	  double latSql = Double.parseDouble("".equals(rs.getString("LATITUDE"))?"0.0":rs.getString("LATITUDE"));
	    	  double longSql = Double.parseDouble("".equals(rs.getString("LONGITUDE"))?"0.0":rs.getString("LONGITUDE"));
	    	  double twoDistan = SpLoc.distance(longti,lat,longSql,latSql);
	    	  double twoDist=(double)Math.round(twoDistan*1000)/1000;
	    	  
	    	  if(twoDist<=dist) {
	    		  out.println("<tr><td>"+rs.getString(1)+"</td><td>"+twoDist+"&ensp;km"+
		        			 "<td>"+rs.getString("PLACE")+"</td>"+"</tr>");
	    	  }

	    	  
	      }
	      out.println("</table>"); 	    
	      
	      rs.close();
	      stmt.close();
	      conn.close();
	      long timeEnd = Clock.systemDefaultZone().millis();
	      out.println("<p>"+(timeEnd-timeBegin)+" milliseconds time expended to perform these queries.</p>");	     
	      out.close();
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
	
	/*//random number,doesn't repeat
	public static void randomNum(int total,int n) {
		int num[]=new int[n];
		for(int i=0;i<total;i++) {
			num[i]=i;
		}
		int index=0;
		Random r=new Random();
		for(int i=0;i<n;i++) {
			index = r.nextInt(n-i)+i;
			int t=num[i];
			num[i]=num[index];
			num[index]=t;
			
		}
		
	}*/
	

}
