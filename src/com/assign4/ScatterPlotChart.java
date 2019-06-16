package com.assign4;
import java.awt.BasicStroke;  
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.NumberAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;  
import org.jfree.data.xy.DefaultXYDataset;  

public class ScatterPlotChart {
     private static double[][] data;

    public ScatterPlotChart() throws Exception {
        this.showChart();
    }  
    
    public double[][] getData(){
    	double[][] data = new double[2][20000]; //第一行存lat，第二行存long

    	
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
	      int i=0;

	      while ( rs.next() ) {
	         
	    	  double lat = Double.parseDouble("".equals(rs.getString("LATITUDE"))?"0.0":rs.getString("LATITUDE"));
	    	  double longi = Double.parseDouble("".equals(rs.getString("LONGITUDE"))?"0.0":rs.getString("LONGITUDE"));
	    	  
	    	  data[0][i] = lat;
	    	  data[1][i] = longi;
	    	  i++;	    	  
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
    	
    	
    	return data;
    }



    public void showChart() throws Exception {  

        DefaultXYDataset xydataset = new DefaultXYDataset();  

        //根据类别建立数据集   
        //data = dataTransfer.getData();
        double[][] data = getData();
        //double[][] data= {{1,2,3,4,5,6,7,8,9},{2,3,4,5,6,7,8,9,1}};

        // x axis data - lung capacity(ml)  
        //data[0] = new double[]{34.2382,34.238166666666665};  

        // y axis data - time holding breath(s)  
        //data[1] = new double[]{108.91033333333333,108.91046666666666,};  

        xydataset.addSeries("magnitude position", data);  

        JFreeChart chart = ChartFactory.createScatterPlot("Earthquake", "latitude", "longitude",
                                               xydataset, 
                                               PlotOrientation.VERTICAL,
                                               true, 
                                               false,
                                               false);  
        ChartFrame frame = new ChartFrame("散点图", chart, true);  
        chart.setBackgroundPaint(Color.white);    
        chart.setBorderPaint(Color.GREEN);    
        chart.setBorderStroke(new BasicStroke(1.5f));    
        XYPlot xyplot = (XYPlot) chart.getPlot();    

        xyplot.setBackgroundPaint(new Color(255, 253, 246));    
        ValueAxis vaaxis = xyplot.getDomainAxis();    
        vaaxis.setAxisLineStroke(new BasicStroke(1.5f));    

        ValueAxis va = xyplot.getDomainAxis(0);    
        va.setAxisLineStroke(new BasicStroke(1.5f));    

        va.setAxisLineStroke(new BasicStroke(1.5f)); // 坐标轴粗细    
        va.setAxisLinePaint(new Color(215, 215, 215)); // 坐标轴颜色    
        xyplot.setOutlineStroke(new BasicStroke(1.5f)); // 边框粗细    
        va.setLabelPaint(new Color(10, 10, 10)); // 坐标轴标题颜色    
        va.setTickLabelPaint(new Color(102, 102, 102)); // 坐标轴标尺值颜色    
        ValueAxis axis = xyplot.getRangeAxis();    
        axis.setAxisLineStroke(new BasicStroke(1.5f));    

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot    
                .getRenderer();    
        xylineandshaperenderer.setSeriesOutlinePaint(0, Color.WHITE);    
        xylineandshaperenderer.setUseOutlinePaint(true);    
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();    
        numberaxis.setAutoRangeIncludesZero(false);    
        numberaxis.setTickMarkInsideLength(2.0F);    
        numberaxis.setTickMarkOutsideLength(0.0F);    
        numberaxis.setAxisLineStroke(new BasicStroke(1.5f));    

//        frame.pack();  
//        frame.setVisible(true); 
        OutputStream image=null;
        ChartUtilities.writeChartAsJPEG(image, chart, 400, 400);
        System.out.println(image);
         
    } 
    public static void main(String[] args) throws Exception {
        ScatterPlotChart my = new ScatterPlotChart();

    }

}  

