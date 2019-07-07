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
        // ����, ����, �����б�
        new KKmeans("earth", "MAG,LATITUDE,LONGITUDE", cores).run();
	}
	
	// Դ����
    private List<Integer> origins = new ArrayList<>();

    // ��������
    private Map<Double, List<Integer>> grouped;

    // ��ʼ�����б�
    private List<Double> cores;

    // ����Դ
    private String tableName;
    private String colName;

    /**
     * ���췽��
     *
     * @param tableName Դ���ݱ�����
     * @param colName   Դ����������
     * @param cores     �����б�
     */
    private KKmeans(String tableName, String colName,List<Double> cores){
        this.cores = cores;
        this.tableName = tableName;
        this.colName = colName;
    }

    /**
     * ���¼�������
     *
     * @return �µ������б�
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
     * �ж��Ƿ����
     *
     * @return bool
     */
    private Boolean isOver(){
        List<Double> _cores = newCores();
        for(int i=0, len=cores.size(); i<len; i++){
            if(!cores.get(i).toString().equals(_cores.get(i).toString())){
                // ʹ��������
                cores = _cores;
                return false;
            }
        }
        return true;
    }

    /**
     * ���ݷ���
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
     * ѡ������
     *
     * @param num   Ҫ���������
     * @return      ����
     */
    private Double getCore(Integer num){

        // �� �б�
        List<Double> diffs = new ArrayList<>();

        // �����
        for(Double core: cores){
            diffs.add(Math.abs(num - core));
        }

        // ��С�� -> ���� -> ��Ӧ������
        return cores.get(diffs.indexOf(Collections.min(diffs)));
    }

    /**
     *  �������ݿ�����
     * @return  connection
     */
    private Connection getConn(){
        try {
           /* // URLָ��Ҫ���ʵ����ݿ���mydata
            String url = "jdbc:mysql://localhost:3306/data_analysis_dev";
            // MySQL����ʱ���û���
            String user = "root";
            // MySQL����ʱ������
            String password = "root";*/

            // ��������
            Class.forName("org.sqlite.JDBC");

            //����Connection����
            Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:db/cloudDB.db");

            if(conn.isClosed()){
                System.out.println("�������ݿ�ʧ��!");
                return null;
            }
            System.out.println("�������ݿ�ɹ�!1");
            
            return conn;

        } catch (Exception e) {
            System.out.println("�������ݿ�ʧ�ܣ�");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * �ر����ݿ�����
     *
     * @param conn  ����
     */
    private void close(Connection conn){
        try {
            if(conn != null && !conn.isClosed()) conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ��ȡԴ����
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
     * ���±���д����
     */
    private void write(){

        Connection conn = null;
        try {
            conn = getConn();
            
            if(conn == null) return;
            
            // ������
            Statement statement = conn.createStatement();

            // ɾ�������ݱ�
            statement.executeUpdate("drop table if exists KMEANS");
            
            // �����±�
            statement.executeUpdate("CREATE TABLE KMEANS(ID INT PRIMARY KEY NOT NULL,CORE REAL, COL REAL)");
            
            // ��ֹ�Զ��ύ
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
            
            // ����ִ��
            ps.executeBatch();
            	String sql = "SELECT * FROM KMEANS";
		      ResultSet rs = statement.executeQuery(sql);
		      while(rs.next()) {
		    	  String s = rs.getString("CORE");
		    	  System.out.println(s);
		      }

            // �ύ����
            conn.commit();

            // �ر�����
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    /**
     * ��������
     */
    private void run(){
        System.out.println("��ȡԴ����");
        // ��ȡԴ����
        getOrigins();

        // ֹͣ����
        Boolean isOver = false;

        System.out.println("���ݷ��鴦��");
        while(!isOver) {
            // ���ݷ���
            setGrouped();
            // �ж��Ƿ�ֹͣ����
            isOver = isOver();
        }

        System.out.println("������õ�����д�����ݿ�");
        // ����������д���±�
        write();

        System.out.println("д�������");
    }

}
