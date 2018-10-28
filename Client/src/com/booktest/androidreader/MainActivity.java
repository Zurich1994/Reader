package com.booktest.androidreader;

import com.kaixinbook.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	ImageView mage;
	EditTextWithDelete username1;
    EditText password1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_main);
		mage  =(ImageView) findViewById(R.id.mage);
		username1 = (EditTextWithDelete) findViewById(R.id.username);
		password1 = (EditText) findViewById(R.id.password);	
	}
	//�����¼
	 public void doGet(View v){
	        final String username=username1.getText().toString();
	        final String password=password1.getText().toString();
	        new Thread(new Runnable() {
	             
	            @Override
	            public void run() {
	                //��������Ҫ�����߳���ʵ�֣�ʹ��getȡ����
	            	final String state;
	                final String s=NetUtillogin.loginOfGet(username, password);
	                 if(s.trim().equals("success")){
	                	 state = "success welcom "+ username;
	                 	Intent intent=new Intent();
	                 	intent.setClass(MainActivity.this, BookShopActivity.class);
	                 	startActivity(intent);
	                 	finish();
	                 }else{state = "wrong username or password";}
	                //ִ�������߳���
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        //���������߳��ϲ���,�������
	                        Toast.makeText(MainActivity.this, state, 0).show();
	                         
	                    }
	                });
	            }
	        }).start();
	         
	    }
	 //���ֱ��ע��
	 public void register(View v){
	        final String username2=username1.getText().toString();
	        final String password2=password1.getText().toString();
	        System.out.println(username2);
	        new Thread(new Runnable() {
	             
	            @Override
	            public void run() {
	                //��������Ҫ�����߳���ʵ�֣�ʹ��getȡ����
	                final String state=NetUtillRegister.registerOfGet(username2, password2);
	                 
	                //ִ�������߳���
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        //���������߳��ϲ���,�������
	                        Toast.makeText(MainActivity.this, state, 0).show();
	                         
	                    }
	                });
	            }
	        }).start();
	         
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
