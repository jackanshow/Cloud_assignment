package com.assign5;

public class Point {
    private double[] localArray;
    private int id;
    private int clusterId;  // 标识属于哪个类中心。
    private double dist;     // 标识和所属类中心的距离。
 
    public Point(int id, double[] localArray) {
        this.id = id;
        this.localArray = localArray;
    }
 
    public Point(double[] localArray) {
        this.id = -10; //表示不属于任意一个类
        this.localArray = localArray;
    }
 
    public double[] getlocalArray() {
        return localArray;
    }
 
    public int getId() {
        return id;
    }
 
    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
 
    public int getClusterid() {
        return clusterId;
    }
 
    public double getDist() {
        return dist;
    }
 
    public void setDist(double dist) {
        this.dist = dist;
    }
 
    @Override
    public String toString() {
        String result = "Point_id=" + id + "  [";
        for (int i = 0; i < localArray.length; i++) {
            result += localArray[i] + " ";
        }
        return result.trim()+"] clusterId: "+clusterId+" dist: "+dist;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
 
        Point point = (Point) obj;
        if (point.localArray.length != localArray.length)
            return false;
 
        for (int i = 0; i < localArray.length; i++) {
            if (Double.compare(point.localArray[i], localArray[i]) != 0) {
                return false;
            }
        }
        return true;
    }
 
    @Override
    public int hashCode() {
        double x = localArray[0];
        double y = localArray[localArray.length - 1];
        long temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
