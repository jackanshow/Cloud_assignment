package com.assign5;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QClusTwo
 */
@WebServlet("/QClusTwo")
public class QClusTwo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("接收到get请求");
		Integer clusteee = Integer.parseInt(request.getParameter("clust"));
		String att1 = request.getParameter("att1").toUpperCase();
		String att2 = request.getParameter("att2").toUpperCase();
		System.out.println(att1+att2);
		
		
						
		long timeBegin = Clock.systemDefaultZone().millis();
		
		ArrayList<double[]> dataSet = new ArrayList<double[]>();
		
		Connection conn = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");
	      //conn = DriverManager.getConnection("jdbc:sqlite:"+"resource/db/xxwDB.db");
	      conn.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = conn.createStatement();
	      String sql = "SELECT * FROM TAITAN";
	      ResultSet rs = stmt.executeQuery(sql);
	      while ( rs.next() ) {
	         
	    	  double atte1 = Double.parseDouble("".equals(rs.getString(att1))?"0.0":rs.getString(att1));
	    	  double atte2 = Double.parseDouble("".equals(rs.getString(att2))?"0.0":rs.getString(att2));
	    	  	    	  	    	  	    	
	    	  dataSet.add(new double[] { atte1, atte2});
	    	   	  
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
 	      out.println("<table border=\"1\">");
 	      out.println("<tr><th>"+"centroids "+"</th><th>"+"number of points"+
	        			 "<th>"
	        			 +"</tr>");
	    
	    int[] countC = new int[clusteee];
	    
	    //kmeans algorithm
	    KMeansRun kRun =new KMeansRun(clusteee, dataSet);
	    
        Set<Cluster> clusterSet = kRun.run();
        System.out.println("单次迭代运行次数："+kRun.getIterTimes());
        System.out.println(dataSet.size());
        int[][] count = new int[clusteee][dataSet.size()];
        int index = 0;
        for (Cluster cluster : clusterSet) {
            //System.out.println(cluster);
        	for (Point point : cluster.getMembers()) {
               // System.out.println("\n"+(point.getId()+1)+" "+point.getClusterid()+" "
        	//+point.getDist()+" "+Arrays.toString(cluster.getCenter().getlocalArray()));
        		//count[point.getClusterid()][point.getId()] = 1;
        		//此处的point.getId()是表中的ID-1；
/*        		System.out.println(Arrays.toString(cluster.getCenter().getlocalArray()));
        		countC[point.getClusterid()]++;*/
        		countC[point.getClusterid()]++;
        		index = point.getClusterid();
        		//给一个attribute，找同类
        		/*if(point.getId()==0) {
        			point.getClusterid();
        		}*/
        		//找到culster id后，就用for(count[cid] ==1),output i; 再查表即可
        		

            }
        	out.println("<tr><th>"+Arrays.toString(cluster.getCenter().getlocalArray())+
    				"</th><th>"+countC[index]+
        			 "<th>"
        			 +"</tr>");
        }

          out.println("</table>"); 
	      out.flush();
	      out.close();
        
        
        //算一下三类中有没有没分类的
        
        
        
        /*int[]	cou= {0,0,0};
        for(int i=0;i<count[0].length;i++) {
        	if((count[0][i]==count[1][i])&&(count[1][i]==count[2][i])) {
        		cou[0]++;
        		System.out.println(cou[0]+" "+i);
        	}
        
        }
	    System.out.println(Arrays.toString(cou));
	    System.out.println(count[0].length+" "+count[1].length);
        
        	//显示出来第0-1310个数据归到了哪一类
        System.out.println(Arrays.toString(count[0]));
        System.out.println(Arrays.toString(count[1]));
        for(int i=0;i<clusteee;i++) {
        	int sum=0;
        	for(int j=0;j<count[i].length;j++) {
        		sum = sum+count[i][j];
        	}
        	System.out.println(sum);
        }*/
		
		
		
	}

}
