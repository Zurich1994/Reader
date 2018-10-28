package com.booktest.mydialog;

import com.booktest.BookListActivity;
import com.kaixinbook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class guanyu extends Activity {
	private Button button_que;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guanyu);
		button_que=(Button) findViewById(R.id.buttonqueren);           
		button_que.setOnClickListener(new View.OnClickListener() 
		{
		     public void onClick(View v)
		     {
		    	 Intent intent = new Intent(guanyu.this,BookListActivity.class);
		    	 startActivity(intent);
				 finish();
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
