package com.booktest.mydialog;

import com.booktest.BookListActivity;
import com.kaixinbook.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class Fankui extends Activity {
	private Button button_one;
	private long mExitTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report);
		button_one=(Button) findViewById(R.id.buttonfankui);            //弹出对话框
		button_one.setOnClickListener(new View.OnClickListener() 
		{
		     public void onClick(View v)
		     {
		    	 Dialog dialog = new AlertDialog.Builder(Fankui.this) 
		    	 		 .setTitle("提交")
		    			 .setMessage("你确认提交吗？").
		    			 setNegativeButton("是",
		    			 new OnClickListener() 
		    			 {         
		    				 @Override      
		    				 public void onClick(DialogInterface dialog, int which)  //点击确定后页面跳转
		    				 {        
		    					 // TODO Auto-generated method stub        
		    					Intent intent = new Intent(Fankui.this,BookListActivity.class);
		    					Toast.makeText(getApplicationContext(), "已成功提交！", Toast.LENGTH_SHORT).show();
		    					startActivity(intent);
		    					finish();
		    				 }     
		    		     }).setPositiveButton("否",
		    		    		 new OnClickListener() 
		    			 {         
		    				 @Override      
		    				 public void onClick(DialogInterface dialog, int which) 
		    				 {        
		    					 dialog.dismiss();
		    				 }     
		    		     }).create();      
		    	 dialog.show(); 
		     }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    	
	    	switch(item.getItemId())
	    	{
	    		default:
	    			return super.onOptionsItemSelected(item);
	    	} 
	}

}
