package com.assign2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Mag5
 */
@WebServlet("/Mag5")
public class Mag5 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("接收到get请求");
		String magStr = request.getParameter("Mag");
		double mag = Double.parseDouble(magStr);		//将string转为double数值
		int count = 0;
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
		         
		    	  double magOut = Double.parseDouble("".equals(rs.getString("MAG"))?"0.0":rs.getString("MAG"));
		    	  if(magOut>=mag) {
		    		  count++;
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
		
		    
	      // 设置响应内容类型
	      response.setContentType("text/html");

	      // 实际的逻辑是在这里
	      PrintWriter out = response.getWriter();
	      out.println("<p>" + count + "</p>");
	}

}
