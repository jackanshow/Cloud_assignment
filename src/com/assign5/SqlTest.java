package com.assign5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class SqlTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
	         
	    	  double surviv = Double.parseDouble("".equals(rs.getString("SURVIVED"))?"0.0":rs.getString("SURVIVED"));
	    	  double age = Double.parseDouble("".equals(rs.getString("AGE"))?"0.0":rs.getString("AGE"));
	    	  double fare = Double.parseDouble("".equals(rs.getString("FARE"))?"0.0":rs.getString("FARE"));
	    	  
	    	  dataSet.add(new double[] { surviv, age, fare });
	    	  
	    	   	  
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
	    
	    
	    //kmeans algorithm
	    KMeansRun kRun =new KMeansRun(3, dataSet);
	    
        Set<Cluster> clusterSet = kRun.run();
        System.out.println("单次迭代运行次数："+kRun.getIterTimes());
        System.out.println(dataSet.size());
        int[][] count = new int[3][dataSet.size()];
        System.out.println("66");
        int countc=0;
        for (Cluster cluster : clusterSet) {
            //System.out.println(cluster);
        	for (Point point : cluster.getMembers()) {
               // System.out.println("\n"+(point.getId()+1)+" "+point.getClusterid()+" "
        	//+point.getDist()+" "+Arrays.toString(cluster.getCenter().getlocalArray()));
        		count[point.getClusterid()][point.getId()] = 1;
        		//此处的point.getId()是表中的ID-1；

            }
        }
        //算一下三类中有没有没分类的
        int[]	cou= {0,0,0};
        for(int i=0;i<count[0].length;i++) {
        	if((count[0][i]==count[1][i])&&(count[1][i]==count[2][i])) {
        		cou[0]++;
        		System.out.println(cou[0]+" "+i);
        	}
        
        }
	    System.out.println(Arrays.toString(cou));
	    System.out.println(count[0].length+" "+count[1].length+" "+count[2].length);
        
        	//显示出来第0-1310个数据归到了哪一类
        System.out.println(Arrays.toString(count[0]));
        System.out.println(Arrays.toString(count[1]));
        System.out.println(Arrays.toString(count[2]));
        for(int i=0;i<3;i++) {
        	int sum=0;
        	for(int j=0;j<count[i].length;j++) {
        		sum = sum+count[i][j];
        	}
        	System.out.println(sum);
        }
	    
	}

}
