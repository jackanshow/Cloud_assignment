package com.assign5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<double[]> dataSet = new ArrayList<double[]>();
		 
        dataSet.add(new double[] { 1, 2, 3 });
        dataSet.add(new double[] { 3, 3, 3 });
        dataSet.add(new double[] { 3, 4, 4});
        dataSet.add(new double[] { 5, 6, 5});
        dataSet.add(new double[] { 8, 9, 6});
        dataSet.add(new double[] { 4, 5, 4});
        dataSet.add(new double[] { 6, 4, 2});
        dataSet.add(new double[] { 3, 9, 7});
        dataSet.add(new double[] { 5, 9, 8});
        dataSet.add(new double[] { 4, 2, 10});
        dataSet.add(new double[] { 1, 9, 12});
        dataSet.add(new double[] { 7, 8, 112.4});
        dataSet.add(new double[] { 7, 8, 4});
 
        KMeansRun kRun =new KMeansRun(3, dataSet);
 
        Set<Cluster> clusterSet = kRun.run();
        System.out.println("单次迭代运行次数："+kRun.getIterTimes());
        for (Cluster cluster : clusterSet) {
            //System.out.println(cluster);
        	for (Point point : cluster.getMembers()) {
                System.out.println("\n"+(point.getId()+1)+" "+point.getClusterid()+" "
        	+point.getDist()+" "+Arrays.toString(cluster.getCenter().getlocalArray()));
            }
        }
        
	}

}
