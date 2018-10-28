package com.booktest.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.DownloadListener;

public class BookDownLoadListener implements DownloadListener {

	@Override
	public void onDownloadStart(String url, String userAgent,
			String contentDisposition, String mimetype, long contentLength) {
		// TODO Auto-generated method stub
		Log.i("tag", "url="+url);             
        Log.i("tag", "userAgent="+userAgent);  
        Log.i("tag", "contentDisposition="+contentDisposition);           
        Log.i("tag", "mimetype="+mimetype);  
        Log.i("tag", "contentLength="+contentLength);  
        Uri uri = Uri.parse(url);  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        //startActivity(intent);       
	}

}
