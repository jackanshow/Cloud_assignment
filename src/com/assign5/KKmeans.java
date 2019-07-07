package com.assign5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KKmeans {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("start");
		List<Double> cores = new ArrayList<>();
        cores.add(260.0);
        cores.add(600.0);
        // 表名, 列名, 质心列表
        new KKmeans("earth", "MAG,LATITUDE,LONGITUDE", cores).run();
	}
	
	// 源数据
    private List<Integer> origins = new ArrayList<>();

    // 分组数据
    private Map<Double, List<Integer>> grouped;

    // 初始质心列表
    private List<Double> cores;

    // 数据源
    private String tableName;
    private String colName;

    /**
     * 构造方法
     *
     * @param tableName 源数据表名称
     * @param colName   源数据列名称
     * @param cores     质心列表
     */
    private KKmeans(String tableName, String colName,List<Double> cores){
        this.cores = cores;
        this.tableName = tableName;
        this.colName = colName;
    }

    /**
     * 重新计算质心
     *
     * @return 新的质心列表
     */
    private List<Double> newCores(){
        List<Double> newCores = new ArrayList<>();

        for(List<Integer> v: grouped.values()){
            newCores.add(v.stream().reduce(0, (sum, num) -> sum + num) / (v.size() + 0.0));
        }

        Collections.sort(newCores);
        return newCores;
    }

    /**
     * 判断是否结束
     *
     * @return bool
     */
    private Boolean isOver(){
        List<Double> _cores = newCores();
        for(int i=0, len=cores.size(); i<len; i++){
            if(!cores.get(i).toString().equals(_cores.get(i).toString())){
                // 使用新质心
                cores = _cores;
                return false;
            }
        }
        return true;
    }

    /**
     * 数据分组
     */
    private void setGrouped(){
        grouped = new HashMap<>();

        Double core;
        for (Integer origin: origins) {
            core = getCore(origin);

            if (!grouped.containsKey(core)) {
                grouped.put(core, new ArrayList<>());
            }

            grouped.get(core).add(origin);
        }
    }

    /**
     * 选择质心
     *
     * @param num   要分组的数据
     * @return      质心
     */
    private Double getCore(Integer num){

        // 差 列表
        List<Double> diffs = new ArrayList<>();

        // 计算差
        for(Double core: cores){
            diffs.add(Math.abs(num - core));
        }

        // 最小差 -> 索引 -> 对应的质心
        return cores.get(diffs.indexOf(Collections.min(diffs)));
    }

    /**
     *  建立数据库连接
     * @return  connection
     */
    private Connection getConn(){
        try {
           /* // URL指向要访问的数据库名mydata
            String url = "jdbc:mysql://localhost:3306/data_analysis_dev";
            // MySQL配置时的用户名
            String user = "root";
            // MySQL配置时的密码
            String password = "root";*/

            // 加载驱动
            Class.forName("org.sqlite.JDBC");

            //声明Connection对象
            Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");

            if(conn.isClosed()){
                System.out.println("连接数据库失败!");
                return null;
            }
            System.out.println("连接数据库成功!1");
            
            return conn;

        } catch (Exception e) {
            System.out.println("连接数据库失败！");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn  连接
     */
    private void close(Connection conn){
        try {
            if(conn != null && !conn.isClosed()) conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取源数据
     */
    private void getOrigins(){

        Connection conn = null;
        try {
            conn = getConn();
            if(conn == null) return;

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(String.format("select %s from %s", colName, tableName));

            while(rs.next()){
                origins.add(rs.getInt(1));
            }
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
          close(conn);
        }
    }

    /**
     * 向新表中写数据
     */
    private void write(){

        Connection conn = null;
        try {
            conn = getConn();
            
            if(conn == null) return;
            
            // 创建表
            Statement statement = conn.createStatement();

            // 删除旧数据表
            statement.executeUpdate("drop table if exists KMEANS");
            
            // 创建新表
            statement.executeUpdate("CREATE TABLE KMEANS(ID INT PRIMARY KEY NOT NULL,CORE REAL, COL REAL)");
            
            // 禁止自动提交
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO KMEANS(CORE,COL) VALUES (?,?)");
            
            for(Map.Entry<Double, List<Integer>> entry: grouped.entrySet()){
                Double core = entry.getKey();
                System.out.println(core);
                for(Integer value: entry.getValue()){
                    ps.setDouble(2, core);
                    ps.setInt(3, value);
                    ps.addBatch();
                }
            }
            
            // 批量执行
            ps.executeBatch();
            	String sql = "SELECT * FROM KMEANS";
		      ResultSet rs = statement.executeQuery(sql);
		      while(rs.next()) {
		    	  String s = rs.getString("CORE");
		    	  System.out.println(s);
		      }

            // 提交事务
            conn.commit();

            // 关闭连接
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    /**
     * 处理数据
     */
    private void run(){
        System.out.println("获取源数据");
        // 获取源数据
        getOrigins();

        // 停止分组
        Boolean isOver = false;

        System.out.println("数据分组处理");
        while(!isOver) {
            // 数据分组
            setGrouped();
            // 判断是否停止分组
            isOver = isOver();
        }

        System.out.println("将处理好的数据写入数据库");
        // 将分组数据写入新表
        write();

        System.out.println("写数据完毕");
    }

}
