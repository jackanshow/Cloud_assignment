package com.assign4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DefaultCategoryDataset ds = new DefaultCategoryDataset();  
        /*ds.addValue(100, "group1", "texas");  
        ds.addValue(200, "group2", "aa");  
        ds.addValue(300, "group3", "bb");  
        ds.addValue(400, "group4", "cc");  
        ds.addValue(500, "group3", "dd");  
        ds.addValue(500, "group3", "ee");*/
		 ds.addValue(300, "bj", "apple");  
         ds.addValue(200, "sh", "apple");  
         ds.addValue(100, "gz", "apple");  
         ds.addValue(200, "bj", "lizi");  
         ds.addValue(200, "sh", "lizi");  
         ds.addValue(200, "gz", "lizi");  
         ds.addValue(300, "bj", "putao");  
         ds.addValue(300, "sh", "putao");  
         ds.addValue(300, "gz", "putao");  
         ds.addValue(400, "bj", "juzi");  
         ds.addValue(400, "sh", "juzi");  
         ds.addValue(400, "gz", "juzi");  
         ds.addValue(500, "bj", "banan");  
         ds.addValue(500, "sh", "banan");  
         ds.addValue(500, "gz", "banan");  
        //ds.addValue(500, "广州", "香蕉"); 
		JFreeChart chart = ChartFactory.createBarChart3D(  
                "total register", //图表标题  
                "group", //目录轴的显示标签  
                "register", //数值轴的显示标签  
                ds, //数据集  
                PlotOrientation.VERTICAL, //图表方向  
                true, //是否显示图例，对于简单的柱状图必须为false  
                false, //是否生成提示工具  
                false);         //是否生成url链接  
		ChartFrame frame = new ChartFrame("Bar chart", chart, true);
		   frame.pack();  
	      frame.setVisible(true);
	}

}
