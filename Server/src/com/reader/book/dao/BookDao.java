package com.reader.book.dao;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.reader.bean.BookInfo;

public interface BookDao {

	public JSONArray GetBooks();

}
