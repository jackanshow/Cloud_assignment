package com.assign4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ShowPieChart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//���ȸ�һ������n��������3��Ȼ��group����i+3�������еĸ��ʷֲ���������浽����s�Ȼ��s��i��==i+3�Ļ���count++��Ȼ������תstring�Ž�ȥ
		int i=8;
		String s = ""+i;
		DefaultPieDataset dataset = new DefaultPieDataset();  
	    dataset.setValue(s, 0.55);  
	    dataset.setValue("project manager", 0.1);  
	    dataset.setValue("data analysis", 0.1);  
	    dataset.setValue("software engineer", 0.1);  
	    dataset.setValue("other", 0.2);  
	    //ͨ������������JFreeChart����  
	    JFreeChart chart = ChartFactory.createPieChart3D("IT pipe chart",  
	            dataset, true, false, false);  
	  ChartFrame frame = new ChartFrame("pie chart", chart, true);
	   frame.pack();  
      frame.setVisible(true);
	}

}
