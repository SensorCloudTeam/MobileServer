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
                System.out.println("�������ع�ԭ��");//������Ϣ  
                Socket s1 = server.accept();  
                System.out.println("�ӵ��ͻ���socket����");//������Ϣ  
                InputStream is1 = s1.getInputStream();  
                DataInputStream dis1=new DataInputStream(is1);  
                String []getStr = dis1.readUTF().split(" ");;//�ÿո���˺ź�����ֿ��洢  
                System.out.println(getStr[0]+" "+getStr[1]+" "+getStr[2]+""+getStr[3]);//������Ϣ  
                  
                //�������ע�����Ϣ�������²���  
                if(getStr[3].equals("Register")){  
                    System.out.println("�ȴ���һ��ָ��");  
                    Socket s2 = server.accept();  
                    InputStream is2 = s2.getInputStream();  
                    DataInputStream dis2 = new DataInputStream(is2);  
                    String []getStrSecond = dis2.readUTF().split(" ");//�ÿո���˺ź�����ֿ��洢  
                    //����������������ע��ûע������˳��˳���Ȼ���������˿�ʼ��½��������ע��  
                    System.out.println(getStrSecond[0] + " " + getStrSecond[1]  
                            + " " + getStrSecond[2]+""+getStr[3]);// ������Ϣ  
                    if(getStrSecond[2].equals("Login")){  
                        /////////////////�ж��Ƿ���Ե�¼//////////////  
                        login(s2,getStrSecond);  
                    }  
                    else if(getStrSecond[2].equals("Register")){  
                        //����Ҫ����  
                    }  
                    else if(getStrSecond[2].equals("Registered")){  
                        ///////////////���ݿ�������////////////////  
                        insertMasterDB(getStrSecond);  
                        ///////////////���ݿ�������////////////////  
                    }  
                    is2.close();  
                    s2.close();  
                    dis2.close();  
                }  
                //���������¼�˺�+����+login�����²���  
                else if(getStr[2].equals("Login")){  
                    System.out.println("�����½�ж�");//������Ϣ  
                    /////////////////�ж��Ƿ���Ե�¼//////////////  
                    login(s1,getStr);  
                    //System.out.println(getStr[0]+" "+getStr[1]+" "+getStr[2]);//������Ϣ  
                }  
                //��������Խ���ע����浫��û�����ע����˳����ٴν���ע���������Ĵ���  
                else if(getStr[3].equals("Registered")){  
                    ///////////////���ݿ�������////////////////  
                    insertMasterDB(getStr);  
                    ///////////////���ݿ�������////////////////  
                }  
                dis1.close();  
                s1.close();  
            }  
        }catch(IOException e){  
            e.printStackTrace();  
        }     
    }  
      
    /** 
     * �ú���Ϊ�Ƿ������û���¼���� 
     * @param s1 �������ӵ�socket���� 
     * @param getStr Ҫ���ҵ�id��password�������� 
     */  
    public static void login(Socket s1,String []getStr){  
        //����˺ź����붼�ԵĻ��򷵻������¼����  
        /////////////////�����û����������Ƿ�һ��//////////////  
        ServerDatabase masterDB = new ServerDatabase();  
        masterDB.connSQL();  
        String select = "select * from user where id = '" + getStr[0]  
                + "' and password = '" + getStr[1] + "';";  
        ResultSet resultSet = masterDB.selectSQL(select);  
        // ///////////////�����û����������Ƿ�һ��//////////////  
        try {  
            // �û��������벻һ��  
            if (resultSet.next() == false) {  
                // ��ֹ��¼����  
                OutputStream os=s1.getOutputStream();  
                DataOutputStream dos=new DataOutputStream(os);  
                dos.writeUTF("NO");  
                dos.close();  
                System.out.println("�û��������");//������Ϣ  
            }  
            // �û���������һ��  
            else {  
                // �����¼����  
                OutputStream os=s1.getOutputStream();  
                DataOutputStream dos=new DataOutputStream(os);  
                dos.writeUTF("YES");  
                dos.close();  
                System.out.println("�û�������ȷ");//������Ϣ  
            }  
        } catch (Exception e) {  
            System.out.println("��ʾ����");  
            e.printStackTrace();  
        }  
        //masterDB.deconnSQL();// �ر�����  
    }  
      
    /** 
     * �ú���Ϊ����master���ݿ����� 
     * @param getStr �������ݿ��id password�������� 
     */  
    public static void insertMasterDB(String []getStr){  
        ServerDatabase masterDB = new ServerDatabase();  
        masterDB.connSQL();  
        String s = "select * from user";//������Ϣ  
        String insert = "insert into user(id,password,email) " +  
                "values('"+getStr[0]+"','"+getStr[1]+"','"+getStr[2]+"')";  
        if (masterDB.insertSQL(insert) == true) {  
            System.out.println("insert successfully");  
            ResultSet resultSet = masterDB.selectSQL(s);//������Ϣ  
            masterDB.layoutStyle2(resultSet);//������Ϣ  
        }  
        masterDB.deconnSQL();//�ر�����  
    }  
}  
