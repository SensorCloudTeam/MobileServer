package com.test;  
  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
  
public class ServerDatabase {  
    private Connection conn = null;  
    private PreparedStatement statement = null;  
    // connect to MySQL  
    public void connSQL() {  
        String urle = "jdbc:mysql://localhost:3306/wsn";//port��3306 database:wsn  
        String username = "root";//user  
        String password = "123456";//password  
        try {   
            Class.forName("com.mysql.jdbc.Driver" );//�����������������ݿ�  
            conn = DriverManager.getConnection(urle,username, password );   
            }  
        //����������������쳣  
         catch ( ClassNotFoundException cnfex ) {  
             System.err.println(  
             "װ�� JDBC/ODBC ��������ʧ�ܡ�" );  
             cnfex.printStackTrace();   
         }   
         //�����������ݿ��쳣  
         catch ( SQLException sqlex ) {  
             System.err.println( "�޷��������ݿ�" );  
             sqlex.printStackTrace();  
         }  
    }  
  
    // disconnect to MySQL  
    public void deconnSQL() {  
        try {  
            if (conn != null)  
                conn.close();  
        } catch (Exception e) {  
            System.out.println("�ر����ݿ����� ��");  
            e.printStackTrace();  
        }  
    }  
  
    // execute selection language  
    public ResultSet selectSQL(String sql) {  
        ResultSet rs = null;  
        try {  
            statement = conn.prepareStatement(sql);  
            rs = statement.executeQuery(sql);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return rs;  
    }  
  
    // execute insertion language  
    public boolean insertSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("�������ݿ�ʱ����");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("����ʱ����");  
            e.printStackTrace();  
        }  
        return false;  
    }  
    //execute delete language  
    public boolean deleteSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("�������ݿ�ʱ����");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("����ʱ����");  
            e.printStackTrace();  
        }  
        return false;  
    }  
    //execute update language  
    public boolean updateSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("�������ݿ�ʱ����");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("����ʱ����");  
            e.printStackTrace();  
        }  
        return false;  
    }  
      
    // show data in ju_users  
    public void layoutStyle2(ResultSet rs) {  
        System.out.println("-----------------");  
        System.out.println("id" + "\t" + "password");  
        System.out.println("-----------------");  
        try {  
            while (rs.next()) {  
                System.out.println(rs.getString("id") + "\t"  
                        + rs.getString("password") + "\n"
                        + rs.getString("email" + "\n"));  
                //"\t" + rs.getInt("age") + "\t"+ rs.getString("work") + "\t" + rs.getString("others") +"\n");  
            }  
        } catch (SQLException e) {  
            System.out.println("��ʾʱ���ݿ����");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("��ʾ����");  
            e.printStackTrace();  
        }  
    }  
  
    //������ṩupdate��delete�������÷�����Ϊ��MyServer.java�в�û�е���
    /*public static void main(String args[]) { 
 
        ServerDatabase h = new ServerDatabase(); 
        h.connSQL(); 
        String select = "select * from userdata where _id=" 
                + "'w'" + " and password=" 
                + "'w'" + ";"; 
        ResultSet resultSet = h.selectSQL(select); 
        h.layoutStyle2(resultSet);//������Ϣ 
        String s = "select * from userdata"; 
 
        String insert = "insert into userdata(_id,password) " + 
                "values('aaron','102938475610')"; 
        String update = "update userdata set password ='123456789' where _id= 'aaron'"; 
        String delete = "delete from userdata where _id= 'aaron'"; 
 
        if (h.insertSQL(insert) == true) { 
            System.out.println("insert successfully"); 
            ResultSet resultSet = h.selectSQL(s); 
            h.layoutStyle2(resultSet); 
        } 
        if (h.updateSQL(update) == true) { 
            System.out.println("update successfully"); 
            ResultSet resultSet = h.selectSQL(s);    
            h.layoutStyle2(resultSet); 
        } 
        if (h.insertSQL(delete) == true) { 
            System.out.println("delete successfully"); 
            ResultSet resultSet = h.selectSQL(s); 
            h.layoutStyle2(resultSet); 
        } 
        h.deconnSQL(); 
    }*/  
}  
