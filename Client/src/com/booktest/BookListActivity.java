package com.booktest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.booktest.View.ArcMenu;
import com.booktest.adapter.LocAdapter;
import com.booktest.androidreader.BookShopActivity;
import com.booktest.androidreader.MainActivity;
import com.booktest.androidreader.NetUtillogin;
import com.booktest.helper.LocalBook;
import com.booktest.helper.MarkHelper;
import com.booktest.util.PinyinListComparator;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.kaixinbook.R;

/**
 * 程序界面
 */

public class BookListActivity extends Activity {
	private long mExitTime=0;
	private com.booktest.View.ArcMenu mArcMenu;
	class AsyncSetApprove extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			if (!isInit) {
				File path = getFilesDir();
				String[] strings = getResources().getStringArray(R.array.bookid);// 获取assets目录下的文件列表
				for (int i = 0; i < strings.length; i++) {
					try {
						FileOutputStream out = new FileOutputStream(path + "/" + strings[i]);
						BufferedInputStream bufferedIn = new BufferedInputStream(getResources().openRawResource(R.raw.book0 + i));
						BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
						byte[] data = new byte[2048];
						int length = 0;
						while ((length = bufferedIn.read(data)) != -1) {
							bufferedOut.write(data, 0, length);
						}
						// 将缓冲区中的数据全部写出
						bufferedOut.flush();
						// 关闭流
						bufferedIn.close();
						bufferedOut.close();
						sp.edit().putBoolean("isInit", true).commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				ArrayList<HashMap<String, String>> insertList = new ArrayList<HashMap<String, String>>();
				File[] f1 = path.listFiles();
				int len = f1.length;
				for (int i = 0; i < len; i++) {
					if (f1[i].isFile()) {
						if (f1[i].toString().contains(".txt")) {
							HashMap<String, String> insertMap = new HashMap<String, String>();
							insertMap.put("parent", f1[i].getParent());
							insertMap.put("path", f1[i].toString());
							insertList.add(insertMap);
						}
					}
				}
				SQLiteDatabase db = localbook.getWritableDatabase();
				db.delete("localbook", "type='" + 2 + "'", null);     //type=2

				for (int i = 0; i < insertList.size(); i++) {
					try {
						if (insertList.get(i) != null) {
							String s = insertList.get(i).get("parent");
							String s1 = insertList.get(i).get("path");
							String sql1 = "insert into " + "localbook" + " (parent,path" + ", type" + ",now,ready) values('" + s + "','" + s1 + "',2,0,null" + ");";
							db.execSQL(sql1);
						}
					} catch (SQLException e) {
						Log.e(TAG, "setApprove SQLException", e);
					} catch (Exception e) {
						Log.e(TAG, "setApprove Exception", e);
					}
				}
				db.close();
			}
			isInit = true;
			sp.edit().putBoolean("isInit", true).commit();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			closeProgressDialog();
			local();
			super.onPostExecute(result);
		}
	}

	private static final String TAG = "MainActivity";
	// private Boolean a = true, b = false, c = false;
	private SharedPreferences.Editor editor;
	private boolean isInit = false;
	private Intent it;
	private ListView list;
	private ArrayList<HashMap<String, Object>> listItem = null;
	private SimpleAdapter listItemAdapter = null;
	ListView listView;
	private LocAdapter locAdapter = null;
	private LocalBook localbook;
	private HashMap<String, Object> map = null;
	private Map<String, Integer[]> map2;// 存放本地推荐目录的小封面图片引用
	private MarkHelper markhelper;
	protected ProgressDialog mDialog = null;
	private int[] menu_toolbar_image_array = { R.drawable.reading, R.drawable.local, R.drawable.favourite };
	private String[] menu_toolbar_name_array = { "最近阅读", "本地书库", "我的收藏" };
	private AdapterView.AdapterContextMenuInfo menuInfo;
	View menuView;
	private ProgressDialog mpDialog;

	private SharedPreferences sp;

	private TextView titletext, titletext1;

    private LocationClient mLocationClient;       //创建的定位端
	private MyLocationListener mylocation;        //自定义定位监控
	private StringBuffer sb;                      //存储查询后短信数据
	private String location_detail;
	private double location_latitude;
	private double location_longitude;
	SpeechSynthesizer mTts;
	private GridView toolbarGrid;
	private boolean flash_state=false;
	private MediaPlayer mp;
/*	Camera  camera = Camera.open();  
	Parameters parameter = camera.getParameters(); */

	/**
	 * 加载listview 的adapter
	 */
	private void adapter() {
		// 生成适配器的Item和动态数组对应的元素
		listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.item,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "itemback", "ItemImage", "BookName", "ItemTitle", "ItemTitle1", "ItemTitle2", "ItemImage9", "LastImage" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.itemback, R.id.ItemImage, R.id.bookName, R.id.ItemTitle, R.id.ItemTitle1, R.id.ItemTitle2, R.id.ItemImage9, R.id.last });
		// 添加并且显示
		list.setAdapter(listItemAdapter);
	}

	public void closeProgressDialog() {
		if (mpDialog != null) {
			mpDialog.dismiss();
		}
	}

	/**
	 * 工具栏adapter的获取
	 * 
	 * @param menuNameArray
	 * @param imageResourceArray
	 * @return simperAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray, int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data, R.layout.item_menu, new String[] { "itemImage", "itemText" }, new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}


	/**
	 * 获取SD卡根目录
	 * 
	 * @return
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	/**
	 * 本地书库载入
	 */
	public void local() {
		SQLiteDatabase db = localbook.getReadableDatabase();
		String col[] = { "path" };
		Cursor cur = db.query("localbook", col, "type=1", null, null, null, null);
		Cursor cur1 = db.query("localbook", col, "type=2", null, null, null, null);
		Integer num = cur.getCount();
		Integer num1 = cur1.getCount();
		ArrayList<String> arraylist = new ArrayList<String>();
		while (cur1.moveToNext()) {
			String s = cur1.getString(cur1.getColumnIndex("path"));
			arraylist.add(s);
		}
		while (cur.moveToNext()) {
			String s = cur.getString(cur.getColumnIndex("path"));
			arraylist.add(s);
		}
		db.close();
		cur.close();
		cur1.close();
		if (listItem == null)
			listItem = new ArrayList<HashMap<String, Object>>();
		listItem.clear();
		String[] bookids = getResources().getStringArray(R.array.bookid);
		String[] booknames = getResources().getStringArray(R.array.bookname);
		String[] bookauthors = getResources().getStringArray(R.array.bookauthor);
		Map<String, String[]> maps = new HashMap<String, String[]>();
		for (int i = 0; i < bookids.length; i++) {
			String[] value = new String[2];
			value[0] = booknames[i];
			value[1] = bookauthors[i];
			maps.put(bookids[i], value);
		}
		for (int i = 0; i < num + num1; i++) {
			if (i < num1) {
				File file1 = new File(arraylist.get(i));
				String m = file1.getName().substring(0, file1.getName().length() - 4);
				if (m.length() > 8) {
					m = m.substring(0, 8) + "...";
				}
				String id = arraylist.get(i).substring(arraylist.get(i).lastIndexOf("/") + 1);
				String[] array = maps.get(id);
				String auther = array != null && array[1] == null ? "未知" : array[1];
				String name = array[0] == null ? m : array[0];
				map = new HashMap<String, Object>();

				if (i == 0) {
					map.put("itemback", R.drawable.itemback);
				} else if ((i % 2) == 0) {
					map.put("itemback", R.drawable.itemback);
				}
				map.put("ItemImage", map2 != null ? map2.get(file1.getName())[0] : R.drawable.cover);
				map.put("BookName", "");
				map.put("ItemTitle", name == null ? m : name);
				map.put("ItemTitle1", "作者：" + auther);
				map.put("LastImage", "推荐书目");
				map.put("path", file1.getPath());
				map.put("com", 0 + file1.getName());// 单独用于排序
				listItem.add(map);
			} else {
				map = new HashMap<String, Object>();

				File file1 = new File(arraylist.get(i));
				String m = file1.getName().substring(0, file1.getName().length() - 4);
				if (m.length() > 8) {
					m = m.substring(0, 8) + "...";
				}
				if (i == 0) {
					map.put("itemback", R.drawable.itemback);
				} else if ((i % 2) == 0) {
					map.put("itemback", R.drawable.itemback);
				}
				map.put("ItemImage", R.drawable.cover);
				map.put("BookName", m);
				map.put("ItemTitle", m);
				map.put("ItemTitle1", "作者：未知");
				map.put("LastImage", "本地导入");
				map.put("path", file1.getPath());
				map.put("com", "1");
				listItem.add(map);
			}
		}
		////////////////////////////////////////////
		////////////////////////////////////////////
		////////////////////////////////////////////
		int read = sp.getInt("reading", 0);
		if(read==1)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemback", R.drawable.itemback);
			map.put("ItemImage", R.drawable.ccnn);
			map.put("BookName", "匆匆那年");
			map.put("ItemTitle", "匆匆那年");
			map.put("ItemTitle1", "作者：九夜茴" );
			map.put("LastImage", "试读书目");
			map.put("path", "/data/data/com.kaixinbook/files/bookread/ccnn1.txt");
			map.put("com", "/data/data/com.kaixinbook/files/bookread/ccnn1.txt");// 单独用于排序
			listItem.add(map);
		}
		else if(read==2)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemback", R.drawable.itemback);
			map.put("ItemImage", R.drawable.ccnn);
			map.put("BookName", "匆匆那年");
			map.put("ItemTitle", "匆匆那年");
			map.put("ItemTitle1", "作者：九夜茴" );
			map.put("LastImage", "下载书目");
			map.put("path", "/data/data/com.kaixinbook/files/booktext/ccnn.txt");
			map.put("com", "/data/data/com.kaixinbook/files/bookread/ccnn.txt");// 单独用于排序
			listItem.add(map);
		}
		Collections.sort(listItem, new PinyinListComparator());
		if (locAdapter == null) {
			locAdapter = new LocAdapter(this, listItem, num1);
			list.setAdapter(locAdapter);
		}
		locAdapter.notifyDataSetChanged();
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:// 删除文件
				// 获取点击的是哪一个文件
			menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			HashMap<String, Object> imap = listItem.get(menuInfo.position);
			String path0 = (String) imap.get("path");
			SQLiteDatabase db = localbook.getWritableDatabase();
			try {
				ContentValues values = new ContentValues();
				values.put("now", 0);// key为字段名，value为值
				values.put("type", 0);
				values.putNull("ready");
				// db.update("localbook", values, "path=?", new String[] { s
				// });// 修改状态为图书被已被导入
				db.update("localbook", values, "path=? and type=1", new String[] { path0 });// 修改状态为图书被已被导入
				// 清空对本书的记录
				editor = sp.edit();
				editor.remove(path0 + "jumpPage");
				editor.remove(path0 + "count");
				editor.remove(path0 + "begin");
				editor.commit();
				markhelper = new MarkHelper(this);
				// 删除数据库书签记录
				SQLiteDatabase db2 = markhelper.getWritableDatabase();
				db2.delete("markhelper", "path='" + path0 + "'", null);
				db2.close();
			} catch (SQLException e) {
				Log.e(TAG, "onContextItemSelected-> SQLException error", e);
			} catch (Exception e) {
				Log.e(TAG, "onContextItemSelected-> Exception error", e);
			}
			db.close();
			// 重新载入页面
			local();
			break;

		case 1:// 创建快捷方式
				// 获取点击的是哪一个文件
			menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			// 通过position获取图书地址
			HashMap<String, Object> imap1 = listItem.get(menuInfo.position);
			String path1 = (String) imap1.get("path");

			Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon); // 获取快捷键的图标
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, imap1.get("ItemTitle").toString() + ".txt");// 快捷方式的标题

			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//
			// 快捷方式的图标
			ComponentName comp = new ComponentName(this.getPackageName(), "." + this.getLocalClassName());
			// 将关键字"bbb"放入intent 当重新快捷方式重新载入本页时 自动跳转到该书
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).putExtra("bbb", path1).setComponent(comp));//
			// 快捷方式的动作
			sendBroadcast(addIntent);// 发送广播

			Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
			shortcut.putExtra("duplicate", false); // 不允许重复创建
			// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer //注意:
			// ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序 // ComponentName comp
			// =
			// new ComponentName(this.getPackageName(),
			// "."+this.getLocalClassName()); //
			// shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
			// Intent(Intent.ACTION_MAIN).setComponent(comp));
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).putExtra("bbb", path1).setComponent(comp));
			// 快捷方式的图标
			ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			sendBroadcast(shortcut);
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mArcMenu =  (ArcMenu) findViewById(R.id.menu);
		mp = MediaPlayer.create(getBaseContext(), R.raw.sound_file_1);
		
		//语音识别部分
		SpeechUtility.createUtility(getBaseContext(), SpeechConstant.APPID +"=556fff92");  
		mTts= SpeechSynthesizer.createSynthesizer(getBaseContext(), null);  
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");//设置发音人  
		mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速  
		mTts.setParameter(SpeechConstant.VOLUME, "100");//设置音量，范围0~100  
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		mTts.startSpeaking("欢迎进入我的掌上阅读器！", mSynListener);
		
		
		mLocationClient = new LocationClient(this);         //定位客户端
		mylocation = new MyLocationListener();
		LocationClientOption option = new LocationClientOption();//定位设置
		mLocationClient.registerLocationListener(mylocation);//返回的定位结果是高精度
		option.setLocationMode(LocationMode.Hight_Accuracy); //返回的定位结果是高精度
		option.setScanSpan(10000);                           //设置发起定位请求的间隔时间为10s 
		option.setCoorType("bd09ll");						 //返回的定位结果是百度经纬度
		option.setIsNeedAddress(true);						 //返回的定位结果包含所处位置
		option.setOpenGps(true);							 //允许打开GPS定位
		mLocationClient.setLocOption(option);
		
		map2 = new HashMap<String, Integer[]>();
		String[] bookids = getResources().getStringArray(R.array.bookid);
		for (int i = 0; i < bookids.length; i++) {
			map2.put(bookids[i], new Integer[] { R.drawable.book0 + i });
		}

		// 载入工具条 创建底部菜单 Toolbar
		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		// 设置背景
		toolbarGrid.setBackgroundResource(R.drawable.channelgallery1);
		// 设置每行列数
		toolbarGrid.setNumColumns(3);
		// 位置居中
		toolbarGrid.setGravity(Gravity.CENTER);
		// 垂直间隔
		toolbarGrid.setVerticalSpacing(10);
		// 水平间隔
		toolbarGrid.setHorizontalSpacing(10);
		// 设置菜单Adapter
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array, menu_toolbar_image_array));
		// 读取名为"mark"的sharedpreferences
		sp = getSharedPreferences("mark", MODE_PRIVATE);
		isInit = sp.getBoolean("isInit", false);
		list = (ListView) findViewById(R.id.ListView01);
		titletext = (TextView) findViewById(R.id.titletext);
		titletext1 = (TextView) findViewById(R.id.titletext1);
		localbook = new LocalBook(this, "localbook");
		
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

				menu.add(Menu.NONE, 0, 0, "从阅读列表中删除");
				menu.add(Menu.NONE, 1, 0, "创建快捷方式");
			}

		});
	

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					showProgressDialog("正在加载电子书...");
					// 修改数据库中图书的最近阅读状态为1
					String s = (String) listItem.get(arg2).get("path");
					SQLiteDatabase db = localbook.getWritableDatabase();

					File f = new File(s);
					if (f.length() == 0) {
						Toast.makeText(BookListActivity.this, "该文件为空文件", Toast.LENGTH_SHORT).show();
						if (mpDialog != null) {
							mpDialog.dismiss();
						}
					} else {
						ContentValues values = new ContentValues();
						values.put("now", 1);// key为字段名，value为值
						db.update("localbook", values, "path=?", new String[] { s });// 修改状态为图书被已被导入
						db.close();
						String path = (String) listItem.get(arg2).get("path");
						it = new Intent();
						it.setClass(BookListActivity.this, Read.class);
						it.putExtra("aaa", path);
						startActivity(it);
					}
				} catch (SQLException e) {
					Log.e(TAG, "list.setOnItemClickListener-> SQLException error", e);
				} catch (Exception e) {
					Log.e(TAG, "list.setOnItemClickListener Exception", e);
				}
			}
		});
		// 工具栏的点击事件处理
		
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				/*
				 * case 0: toolbarGrid
				 * .setBackgroundResource(R.drawable.channelgallery1); near(); a
				 * = true; b = false; c = false;
				 * 
				 * break;
				 */
				case 1:

					toolbarGrid.setBackgroundResource(R.drawable.channelgallery2);
					local();
					break;
				case 2:
					titletext.setText("我的收藏");
					toolbarGrid.setBackgroundResource(R.drawable.channelgallery3);
					listItem = new ArrayList<HashMap<String, Object>>();
					adapter();
					titletext1.setText("(0)");
					break;
				}
			}
		});
		
		initEvent();
	}

	private void initEvent() {
		
		mArcMenu.setOnMenuItemClickListener(new com.booktest.View.ArcMenu.OnMenuItemClickListener()
		{
			@Override
			public void onClick(View view, int pos)
			{
				
				//Toast.makeText(BookListActivity.this, pos+":"+view.getTag(), Toast.LENGTH_SHORT).show();  //开始处理单个点击的事件
				if(pos==1)
				{
					if (mp.isPlaying())   
					 {
						Toast.makeText(BookListActivity.this,"暂停音乐", Toast.LENGTH_SHORT).show();
						mp.pause();  
					 }
					 else
					 {
						 Toast.makeText(BookListActivity.this,"播放音乐", Toast.LENGTH_SHORT).show();
						 mp.start();
					 }
				}
				if(pos==2)
				{
					Toast.makeText(BookListActivity.this,"手机定位", Toast.LENGTH_SHORT).show();
					String Location="你所处的位置："+location_detail;
    				Toast.makeText(getApplication(),Location, Toast.LENGTH_LONG).show();
				}
				if(pos==3)
				{ 
					if(!flash_state)
					{
						Toast.makeText(BookListActivity.this,"打开闪光灯", Toast.LENGTH_SHORT).show();
						/*parameter.setFlashMode(Parameters.FLASH_MODE_TORCH); 
						camera.setParameters(parameter);
						flash_state=true;*/
					}
					else
					{
						Toast.makeText(BookListActivity.this,"关闭闪光灯", Toast.LENGTH_SHORT).show();
						/*parameter.setFlashMode(Parameters.FLASH_MODE_OFF); 
						camera.setParameters(parameter);
						flash_state=false;*/
					}
				}
				if(pos==4)
				{
					Toast.makeText(BookListActivity.this,"浏览书城", Toast.LENGTH_SHORT).show();
					Intent intents = new Intent();  
	                intents.setClass(BookListActivity.this,BookShopActivity.class);   
	                startActivity(intents);  
				}
				if(pos==5)
				{
					Toast.makeText(BookListActivity.this,"用户登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();  
	                intent.setClass(BookListActivity.this,MainActivity.class);   
	                startActivity(intent);  
				}
			}
		});
	}

	/** 
	 * 调用系统menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onStart() {                   //进入页面打开定位
		super.onStart();
		if(!mLocationClient.isStarted())
		mLocationClient.start();
	}
	
	@Override
	protected void onDestroy() {
		closeProgressDialog();
		super.onDestroy();
		mLocationClient.stop();
		finish();
	}
	
	
	@Override
	protected void onStop() {           
		super.onStop();
		mLocationClient.stop();
	}

	/**
	 * 处理menu弹出后点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int it = item.getItemId();
		switch (it) {
		case R.id.it1:
			// 进入导入图书界面
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, InActivity.class);
			startActivity(intent);
			break;
		case R.id.it2:
			// 进入反馈界面
			Intent intent2 = new Intent();
			intent2.setClass(BookListActivity.this, com.booktest.mydialog.Fankui.class);
			startActivity(intent2);
			break;
		case R.id.it3:
			//进入关于界面
			Intent intent3 = new Intent();
			intent3.setClass(BookListActivity.this, com.booktest.mydialog.guanyu.class);
			startActivity(intent3);
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		closeProgressDialog();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent itt = getIntent();
		String ss = itt.getStringExtra("nol");
		String bb = itt.getStringExtra("bbb");
		Boolean bad = itt.getBooleanExtra("bad", false);
		if (bad) {
			Toast.makeText(BookListActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
		}
		// 判断是否由快捷方式进入的程序
		if (bb != null) {

			it = new Intent();
			it.setClass(BookListActivity.this, Read.class);
			it.putExtra("aaa", bb);
			it.putExtra("ccc", "ccc");
			startActivity(it);
			this.finish();
		}
		// 判断是从哪里回退进主界面的 并重新载入 以此实现同步数据
		if (ss != null) {
			if (ss.equals("n")) {
				local();
			} else {
				toolbarGrid.setBackgroundResource(R.drawable.channelgallery2);
				local();
			}
		} else {
			local();
		}
		if (!isInit) {
			new AsyncSetApprove().execute("");
			showProgressDialog("正在初始化,请稍后...");
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {               //连按两次退出的程序
         if (keyCode == KeyEvent.KEYCODE_BACK) 
         {
                 if ((System.currentTimeMillis() - mExitTime) > 2000) 
                 {
                         //Object mHelperUtils;
                         Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                         mExitTime = System.currentTimeMillis();
                 } 
                 else 
                 {
                         finish();
                 }
                 return true;
         }
         return super.onKeyDown(keyCode, event);
	}


	/**
	 * 设置默认书籍
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setApprove() {
		if (!isInit) {
			synchronized (this) {
				String[] strings = getResources().getStringArray(R.array.bookname);
				for (int i = 0; i < strings.length; i++) {
					try {
						FileOutputStream out = new FileOutputStream(getFilesDir() + "/" + strings[i]);
						BufferedInputStream bufferedIn = new BufferedInputStream(getResources().openRawResource(R.raw.book0 + i));
						BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
						byte[] data = new byte[2048];
						int length = 0;
						while ((length = bufferedIn.read(data)) != -1) {
							bufferedOut.write(data, 0, length);
						}
						// 将缓冲区中的数据全部写出
						bufferedOut.flush();
						// 关闭流
						bufferedIn.close();
						bufferedOut.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sp.edit().putBoolean("isInit", true).commit();
			}

			ArrayList<HashMap<String, String>> insertList = new ArrayList<HashMap<String, String>>();
			File[] f1 = getFilesDir().listFiles();
			int len = f1.length;
			for (int i = 0; i < len; i++) {
				if (f1[i].isFile()) {
					if (f1[i].toString().contains(".txt")) {
						HashMap insertMap = new HashMap<String, String>();
						insertMap.put("parent", f1[i].getParent());
						insertMap.put("path", f1[i].toString());
						insertList.add(insertMap);
					}
				}
			}

			SQLiteDatabase db = localbook.getWritableDatabase();
			db.delete("localbook", "type='" + 2 + "'", null);

			for (int i = 0; i < insertList.size(); i++) {
				try {
					if (insertList.get(i) != null) {
						String s = insertList.get(i).get("parent");
						String s1 = insertList.get(i).get("path");
						String sql1 = "insert into " + "localbook" + " (parent,path" + ", type" + ",now,ready) values('" + s + "','" + s1 + "',2,0,null" + ");";
						db.execSQL(sql1);
					}
				} catch (SQLException e) {
					Log.e(TAG, "setApprove SQLException", e);
				} catch (Exception e) {
					Log.e(TAG, "setApprove Exception", e);
				}
			}
			db.close();
			local();
		}
	}

	public void showProgressDialog(String msg) {
		mpDialog = new ProgressDialog(BookListActivity.this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mpDialog.setMessage(msg);
		mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
		mpDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		mpDialog.show();
	}
	
	private SynthesizerListener mSynListener = new SynthesizerListener(){  
	    //会话结束回调接口，没有错误时，error为null  
	    public void onCompleted(SpeechError error) {}  
	    //缓冲进度回调  
	    //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。  
	    public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}  
	    //开始播放  
	    public void onSpeakBegin() {}  
	    //暂停播放  
	    public void onSpeakPaused() {}  
	    //播放进度回调  
	    //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.  
	    public void onSpeakProgress(int percent, int beginPos, int endPos) {}  
	    //恢复播放回调接口  
	    public void onSpeakResumed() {}  
	//会话事件回调接口  
	    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}  };
	    
	    
	    private class MyLocationListener implements BDLocationListener         //定位监控
		{

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
		            return ;
				if (location.getLocType() == BDLocation.TypeNetWorkLocation){     //必须打开网络后获得位置信息
					location_detail = location.getAddrStr();
				} 
				location_latitude  = location.getLatitude();
				location_longitude = location.getLongitude();
				
			}
			
		}
}