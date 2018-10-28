package com.booktest.androidreader;

//
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.booktest.BookListActivity;
import com.booktest.Read;
import com.kaixinbook.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//
//

public class BookShopActivity extends Activity {
	JSONArray state = null;
	private List<BookInfo> bookshop = new ArrayList<BookInfo>();
	ArrayAdapter<JSONArray> adapter1 = null;
	int size;
	String a,b,c;
	String bookPath;
	String wholebookPath;
	private File cache;
	private File booktext;
	private File bookread;
	private SharedPreferences sp;
	
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookshop);
		sp = getSharedPreferences("mark", MODE_PRIVATE);

		// 创建缓存目录，系统一运行就得创建缓存目录的，getFliesDir沙盒
		cache = new File(getFilesDir(), "cache");
		if (!cache.exists()) {
			cache.mkdirs();
		}
		booktext = new File(getFilesDir(), "booktext");
		if (!booktext.exists()) {
			booktext.mkdirs();
		}
		bookread = new File(getFilesDir(), "bookread");
		if (!bookread.exists()) {
			bookread.mkdirs();
		}
		// final ListView list1 = (ListView) findViewById(R.id.list1);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 访问网络要在子线程中实现，使用get取数据
				state = NetUtillDownLoad.showbookshop();
				size = state.length();
				for (int i = 0; i < size; i++) {
					JSONObject jsonobj = state.getJSONObject(i);
					String book_bookname = jsonobj.getString("book_bookname");
					String book_img = jsonobj.getString("book_img");
					String book_author = jsonobj.getString("book_author");
					String book_content =jsonobj.getString("book_content");
					String book_read =jsonobj.getString("book_read");
					System.out.println(book_img);
					System.out.println("111111111111"+book_read);
					BookInfo bi = new BookInfo();
					bi.setBook_bookname(book_bookname);
					bi.setBook_author(book_author);
					bi.setBook_content(book_content);
					bi.setBook_read(book_read);
					try {
						a = NetUtillDownLoad.getImageURI(book_img, cache)
								.toString();
												System.out.println(a);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("img", "Exception = "+e.getMessage());
					}
					Log.e("img", "img a = " + a);
					bi.setBook_img(a);
					bookshop.add(bi);
				}

				// 执行在主线程上
				runOnUiThread(new Runnable() {
					public void run() {
						// 就是在主线程上操作,弹出结果
						// Toast.makeText(MainActivity.this, state, 0).show();
						BookAdapter adapter = new BookAdapter(
								BookShopActivity.this, R.layout.abook, bookshop);
						ListView listView = (ListView) findViewById(R.id.list1);
						Button Download = (Button) findViewById(R.id.book_download);
						Button read = (Button) findViewById(R.id.book_read);
						listView.setAdapter(adapter);
						
						
						
					}
				});
			}
		}).start();
		// adapter1 = new ArrayAdapter<JSONArray>(this, R.layout.abook);
		// list1.setAdapter(adapter1);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 清空缓存
		File[] files = cache.listFiles();
		for (File file : files) {
			file.delete();
		}
		cache.delete();
		File[] files1 = booktext.listFiles();
		for (File file : files) {
			file.delete();
		}
		booktext.delete();
		File[] files2 = bookread.listFiles();
		for (File file : files) {
			file.delete();
		}
		bookread.delete();
	}
	//
	
	class BookAdapter extends ArrayAdapter<BookInfo> {
		private int resourceId;



			public BookAdapter(Context context, int resource, List<BookInfo> objects) {
				super(context, resource, objects);
				resourceId = resource;
				// TODO Auto-generated constructor stub

			}

			public View getView(int position,View convertView,ViewGroup parent)
			{
				final BookInfo book = getItem(position);
				View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
				ImageView bookImage = (ImageView) view.findViewById(R.id.book_image);
				TextView bookname = (TextView) view.findViewById(R.id.book_name);
				TextView authorname = (TextView) view.findViewById(R.id.book_author);
				Button Download = (Button) view.findViewById(R.id.book_download);
				Button Read = (Button) view.findViewById(R.id.book_read);
				Download.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									System.out.println("book.book_content:"+book.book_content);
									b = NetUtillDownLoad.getBook(book.book_content, booktext)
											.toString();
									wholebookPath=b.substring(7);
									Log.e("wholebookPath",wholebookPath);
									sp.edit().putInt("reading", 2).commit();
									Intent its = new Intent();
									its.setClass(BookShopActivity.this, Read.class);
									its.putExtra("aaa",wholebookPath);
									startActivity(its);
									finish();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.e("bookcontent", "Exception = "+e.getMessage());
								}
							}
						}).start();
					}
				});
				Read.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									System.out.println("book.book_read:"+book.book_read);
									c = NetUtillDownLoad.getBook(book.book_read, bookread)
											.toString();
									bookPath=c.substring(7);
									Log.e("22222222222222222",bookPath);
									sp.edit().putInt("reading", 1).commit();
									Intent it = new Intent();
									it.setClass(BookShopActivity.this, Read.class);
									it.putExtra("aaa",bookPath);
									startActivity(it);
									finish();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.e("bookread", "Exception = "+e.getMessage());
								}
							}
						}).start();
						//////////////////////////////////////
						//////////////////////////////////////
						//////////////////////////////////////
					}
				});
				bookImage.setImageURI(Uri.parse(book.getBook_img()));
				Log.e("img",""+book.getBook_img());
				//Log.e("img",""+position+Uri.parse(book.getBook_img()));
				bookname.setText(book.getBook_bookname());
				authorname.setText(book.getBook_author());
				return view;
				
				
			}
		}

	
}
