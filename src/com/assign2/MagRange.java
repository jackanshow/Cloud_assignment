package com.assign2;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*UTA-CSE 6331
Xiangxiang Wang
ID: 1001681420*/


/**
 * Servlet implementation class MagRange
 */
@WebServlet("/MagRange")
public class MagRange extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse resp onse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//如果大于min判断是否小于max，如果是，则把id存到数组，在根据ID统一取出来
		String magStr1= request.getParameter("MagR1");
		String magStr2= request.getParameter("MagR2");
		double magNum1=Double.parseDouble(magStr1);
		double magNum2=Double.parseDouble(magStr2);
		System.out.println("没问题");
		long date1=Long.parseLong(request.getParameter("date1").replaceAll("-", ""));
		long date2=Long.parseLong(request.getParameter("date2").replaceAll("-", ""));
		System.out.println(date1+"  "+date2);
		System.out.println(magNum1);
		
		Connection conn = null;
		Statement stmt=null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");
		      //conn = DriverManager.getConnection("jdbc:sqlite:"+"resource/db/xxwDB.db");
		      conn.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = conn.createStatement();
		      String sql = "SELECT * FROM earth";
		      ResultSet rs = stmt.executeQuery(sql);
		   // 设置响应内容类型
	   	      response.setContentType("text/html");
	   		// 实际的逻辑是在这里
	   	      PrintWriter out = response.getWriter();
	   	      out.println("<table border=\"1\">");
	   	      out.println("<tr><th>"+"TIME"+"</th><th>"+"LATITUDE"+
		        			 "<th>"+"LONGITUDE"+"</th><th>"+"DEPTH"+"</th>"+
		        			 "<th>"+"MAG"+"</th><th>"+"MAGTYPE"+"</th>"+
		        			 "<th>"+"NST"+"</th><th>"+"GAP"+"</th>"+
		        			 "<th>"+"DMIN"+"</th><th>"+"RMS"+"</th>"+"<th>"+"NET"+"</th>"
		        			 +"</tr>");
		      while ( rs.next() ) {
		         long dateSql = Long.parseLong(rs.getString("TIME").substring(0, 10).replaceAll("-", ""));
		         Double magSql = Double.parseDouble("".equals(rs.getString("MAG"))?"0.0":rs.getString("MAG"));
		         if(dateSql>=date1&&dateSql<=date2  && magSql>=magNum1&&magSql<=magNum2) {
		

		        	 out.println("<tr><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+
		        			 "<td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td>"+
		        			 "<td>"+rs.getString(6)+"</td><td>"+rs.getString(7)+"</td>"+
		        			 "<td>"+rs.getString(8)+"</td><td>"+rs.getString(9)+"</td>"+
		        			 "<td>"+rs.getString(10)+"</td><td>"+rs.getString(11)+"</td>"+"<td>"+rs.getString(12)+"</td>"
		        			 +"</tr>");
		        	 
		        	 
		         }
		     
		      }
		      out.println("</table>"); 
		      out.flush();
		      out.close();
		      
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

}
