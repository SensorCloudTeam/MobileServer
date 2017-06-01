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
        String urle = "jdbc:mysql://localhost:3306/wsn";//port：3306 database:wsn  
        String username = "root";//user  
        String password = "123456";//password  
        try {   
            Class.forName("com.mysql.jdbc.Driver" );//加载驱动，连接数据库  
            conn = DriverManager.getConnection(urle,username, password );   
            }  
        //捕获加载驱动程序异常  
         catch ( ClassNotFoundException cnfex ) {  
             System.err.println(  
             "装载 JDBC/ODBC 驱动程序失败。" );  
             cnfex.printStackTrace();   
         }   
         //捕获连接数据库异常  
         catch ( SQLException sqlex ) {  
             System.err.println( "无法连接数据库" );  
             sqlex.printStackTrace();  
         }  
    }  
  
    // disconnect to MySQL  
    public void deconnSQL() {  
        try {  
            if (conn != null)  
                conn.close();  
        } catch (Exception e) {  
            System.out.println("关闭数据库问题 ：");  
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
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
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
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
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
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
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
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }  
    }  
  
    //这个是提供update和delete函数的用法，因为在MyServer.java中并没有调用
    /*public static void main(String args[]) { 
 
        ServerDatabase h = new ServerDatabase(); 
        h.connSQL(); 
        String select = "select * from userdata where _id=" 
                + "'w'" + " and password=" 
                + "'w'" + ";"; 
        ResultSet resultSet = h.selectSQL(select); 
        h.layoutStyle2(resultSet);//调试信息 
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
