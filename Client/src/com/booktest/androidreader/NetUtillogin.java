package com.booktest.androidreader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.widget.EditText;

public class NetUtillogin {

	public static String loginOfGet(String username, String password) {
		// TODO Auto-generated method stub
		HttpURLConnection conn=null;
        try {
            String data="username="+username+"&password="+password;
            URL url=new URL("http://192.168.146.1:8080/AndroidReader/servlet/LoginServlet?"+data);
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
                //System.out.println(s);//Êä³öµÄÊÇsuccess
                
//                if(s.trim().equals("success")){
//                	state = "success welcom "+ username;
//                	Intent intent=new Intent();
//                	intent.setClass(, BookShopActivity.class);
//                	startActivity(intent);
//                }
//                else{
//                	state = "wrong username or password";
//                }
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
