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
        //ds.addValue(500, "����", "�㽶"); 
		JFreeChart chart = ChartFactory.createBarChart3D(  
                "total register", //ͼ�����  
                "group", //Ŀ¼�����ʾ��ǩ  
                "register", //��ֵ�����ʾ��ǩ  
                ds, //���ݼ�  
                PlotOrientation.VERTICAL, //ͼ����  
                true, //�Ƿ���ʾͼ�������ڼ򵥵���״ͼ����Ϊfalse  
                false, //�Ƿ�������ʾ����  
                false);         //�Ƿ�����url����  
		ChartFrame frame = new ChartFrame("Bar chart", chart, true);
		   frame.pack();  
	      frame.setVisible(true);
	}

}
