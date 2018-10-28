package com.booktest.androidreader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtillRegister {


	public static String registerOfGet(String username, String password) {
		// TODO Auto-generated method stub
		HttpURLConnection conn=null;
        try {
            String data="username2="+username+"&password2="+password;
            URL url=new URL("http://192.168.146.1:8080/AndroidReader/servlet/RegisterServlet?"+data);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            //conn.connect();
            int code=conn.getResponseCode();
            System.out.println("code"+code);
            if(code==200){
                InputStream is=conn.getInputStream();
                String state=getStringFromInputStream(is);
                return state;
            }
             
             
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(conn!=null){
                conn.disconnect();
            }
        }
         
         
        return null;
	}

	private static String getStringFromInputStream(InputStream is) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buff=new byte[1024];
        int len=-1;
        while((len=is.read(buff))!=-1){
            baos.write(buff, 0, len);
        }
        is.close();
        String html=baos.toString();
        baos.close();
         
         
        return html;
	}



}
