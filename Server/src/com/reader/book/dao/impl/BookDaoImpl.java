package com.reader.book.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.reader.dao.util.DBUtil;
import com.reader.bean.BookInfo;

import com.reader.book.dao.BookDao;


public class BookDaoImpl implements BookDao {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	JSONArray ja=null;
	public JSONArray GetBooks() {
		// TODO Auto-generated method stub
		conn=DBUtil.getConnection();
		ArrayList<BookInfo> bis = new ArrayList<BookInfo>();
		
		String sql="select * from bookinfo";
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				BookInfo b = new BookInfo();
				int b_id=rs.getInt(1);
				String b_name=rs.getString(2);
				double b_price=rs.getDouble(3);
				String b_author=rs.getString(4);
				String b_img = rs.getString(5);
				String b_content = rs.getString(6);
				String b_read = rs.getString(7);
				b.setBook_id(b_id);
				b.setBook_bookname(b_name);
				b.setBook_price(b_price);
				b.setBook_author(b_author);
				b.setBook_img(b_img);
				b.setBook_content(b_content);
				b.setBook_read(b_read);
System.out.println(b_id);
System.out.println(b_name);
System.out.println(b_price);
System.out.println(b_author);
System.out.println(b_img);
System.out.println(b_content);
System.out.println(b_read);
				bis.add(b);
				
				 ja = JSONArray.fromObject(bis);  //将数组转化成JSON数组
				
		          
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return ja;
	}
	
	//通过书名找书
	
	public BookInfo search(int book_id) {
		// TODO Auto-generated method stub
		Boolean login=false;
		BookInfo bi = null;
		conn=DBUtil.getConnection();
		String sql="select * from bookinfo where book_id=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1,book_id);
			rs=ps.executeQuery();
			while(rs.next()){
				
				bi=new BookInfo();
				int uid=rs.getInt(1);
				String bname=rs.getString(2);
				double bprice=rs.getDouble(3);
				String bauthor=rs.getString(4);
				String bimg=rs.getString(5);
				String bcontent=rs.getString(6);
				String bread=rs.getString(7);
				bi.setBook_id(uid);
				bi.setBook_bookname(bname);
				bi.setBook_author(bauthor);
				bi.setBook_price(bprice);
				bi.setBook_img(bimg);
				bi.setBook_content(bcontent);
				bi.setBook_read(bread);
				
//				if(upass == password)
//				{
//					login = true;
//				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		
		
		
		return bi;
	}
}
