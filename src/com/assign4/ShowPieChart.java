package com.assign4;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ShowPieChart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//我先给一个数字n，比如是3，然后group就是i+3；把所有的概率分布算出来，存到数组s里，然后s【i】==i+3的话，count++；然后数字转string放进去
		int i=8;
		String s = ""+i;
		DefaultPieDataset dataset = new DefaultPieDataset();  
	    dataset.setValue(s, 0.55);  
	    dataset.setValue("project manager", 0.1);  
	    dataset.setValue("data analysis", 0.1);  
	    dataset.setValue("software engineer", 0.1);  
	    dataset.setValue("other", 0.2);  
	    //通过工厂类生成JFreeChart对象  
	    JFreeChart chart = ChartFactory.createPieChart3D("IT pipe chart",  
	            dataset, true, false, false);  
	  ChartFrame frame = new ChartFrame("pie chart", chart, true);
	   frame.pack();  
      frame.setVisible(true);
	}

}
