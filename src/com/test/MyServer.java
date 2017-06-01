package com.test;  
  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.sql.ResultSet;  
  
  
public class MyServer {  
    public static void main(String[] args){  
        try{  
            ServerSocket server=new ServerSocket(6666);  
            while(true){  
                System.out.println("服务器回归原点");//调试信息  
                Socket s1 = server.accept();  
                System.out.println("接到客户端socket请求");//调试信息  
                InputStream is1 = s1.getInputStream();  
                DataInputStream dis1=new DataInputStream(is1);  
                String []getStr = dis1.readUTF().split(" ");;//用空格把账号和密码分开存储  
                System.out.println(getStr[0]+" "+getStr[1]+" "+getStr[2]+""+getStr[3]);//调试信息  
                  
                //如果发来注册的信息就做如下操作  
                if(getStr[3].equals("Register")){  
                    System.out.println("等待下一步指令");  
                    Socket s2 = server.accept();  
                    InputStream is2 = s2.getInputStream();  
                    DataInputStream dis2 = new DataInputStream(is2);  
                    String []getStrSecond = dis2.readUTF().split(" ");//用空格把账号和密码分开存储  
                    //这个处理是针对有人注册没注册完就退出了程序然后另外有人开始登陆或者重新注册  
                    System.out.println(getStrSecond[0] + " " + getStrSecond[1]  
                            + " " + getStrSecond[2]+""+getStr[3]);// 调试信息  
                    if(getStrSecond[2].equals("Login")){  
                        /////////////////判断是否可以登录//////////////  
                        login(s2,getStrSecond);  
                    }  
                    else if(getStrSecond[2].equals("Register")){  
                        //不需要处理  
                    }  
                    else if(getStrSecond[2].equals("Registered")){  
                        ///////////////数据库插入操作////////////////  
                        insertMasterDB(getStrSecond);  
                        ///////////////数据库插入操作////////////////  
                    }  
                    is2.close();  
                    s2.close();  
                    dis2.close();  
                }  
                //如果发来登录账号+密码+login做如下操作  
                else if(getStr[2].equals("Login")){  
                    System.out.println("进入登陆判断");//调试信息  
                    /////////////////判断是否可以登录//////////////  
                    login(s1,getStr);  
                    //System.out.println(getStr[0]+" "+getStr[1]+" "+getStr[2]);//调试信息  
                }  
                //这里是针对进入注册界面但是没有完成注册就退出且再次进入注册界面情况的处理  
                else if(getStr[3].equals("Registered")){  
                    ///////////////数据库插入操作////////////////  
                    insertMasterDB(getStr);  
                    ///////////////数据库插入操作////////////////  
                }  
                dis1.close();  
                s1.close();  
            }  
        }catch(IOException e){  
            e.printStackTrace();  
        }     
    }  
      
    /** 
     * 该函数为是否允许用户登录函数 
     * @param s1 建立连接的socket变量 
     * @param getStr 要查找的id和password都在里面 
     */  
    public static void login(Socket s1,String []getStr){  
        //如果账号和密码都对的话则返回允许登录命令  
        /////////////////查找用户名和密码是否一致//////////////  
        ServerDatabase masterDB = new ServerDatabase();  
        masterDB.connSQL();  
        String select = "select * from user where id = '" + getStr[0]  
                + "' and password = '" + getStr[1] + "';";  
        ResultSet resultSet = masterDB.selectSQL(select);  
        // ///////////////查找用户名和密码是否一致//////////////  
        try {  
            // 用户名和密码不一致  
            if (resultSet.next() == false) {  
                // 禁止登录命令  
                OutputStream os=s1.getOutputStream();  
                DataOutputStream dos=new DataOutputStream(os);  
                dos.writeUTF("NO");  
                dos.close();  
                System.out.println("用户密码错误");//调试信息  
            }  
            // 用户名和密码一致  
            else {  
                // 允许登录命令  
                OutputStream os=s1.getOutputStream();  
                DataOutputStream dos=new DataOutputStream(os);  
                dos.writeUTF("YES");  
                dos.close();  
                System.out.println("用户密码正确");//调试信息  
            }  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }  
        //masterDB.deconnSQL();// 关闭连接  
    }  
      
    /** 
     * 该函数为插入master数据库命令 
     * @param getStr 插入数据库的id password都在里面 
     */  
    public static void insertMasterDB(String []getStr){  
        ServerDatabase masterDB = new ServerDatabase();  
        masterDB.connSQL();  
        String s = "select * from user";//调试信息  
        String insert = "insert into user(id,password,email) " +  
                "values('"+getStr[0]+"','"+getStr[1]+"','"+getStr[2]+"')";  
        if (masterDB.insertSQL(insert) == true) {  
            System.out.println("insert successfully");  
            ResultSet resultSet = masterDB.selectSQL(s);//调试信息  
            masterDB.layoutStyle2(resultSet);//调试信息  
        }  
        masterDB.deconnSQL();//关闭连接  
    }  
}  
