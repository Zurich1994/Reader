package com.reader.login.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import com.reader.bean.UserInfo;
import com.reader.dao.util.DBUtil;
import com.reader.login.dao.LoginDao;

public class LoginDaoImpl implements LoginDao{
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	//登录 验证  返回用户对象
	
	public UserInfo search(String username) {
		// TODO Auto-generated method stub
		Boolean login=false;
		UserInfo ui = null;
		conn=DBUtil.getConnection();
		String sql="select * from userinfo where username=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			rs=ps.executeQuery();
			while(rs.next()){
				System.out.println(1);
				ui=new UserInfo();
				int uid=rs.getInt(1);
				String uname=rs.getString(2);
				String upass=rs.getString(3);
				ui.setId(uid);
				ui.setUsername(uname);
				ui.setPswd(upass);
System.out.println(upass);
				
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
		
		
		
		return ui;
	}
	
		//注册
	
	public void add(String username, String password, double money) {
		// TODO Auto-generated method stub
		conn=DBUtil.getConnection();
		String sql="insert into userinfo (username,pswd,money,userload)values(?,?,?,?)";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2,password);
			ps.setDouble(3,0);
			ps.setBoolean(4,false);
		
			int result = ps.executeUpdate();
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, null);
		}
	}
	
	//充值
	
	public void updatamoney(String username, double money) {
		// TODO Auto-generated method stub
		conn=DBUtil.getConnection();
		String sql="update userinfo set money=money+? where username=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setDouble(1, money);
			ps.setString(2, username);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		
	}
	
	//显示用户信息
	
	public UserInfo showuserinfo(String username) {
		// TODO Auto-generated method stub
		UserInfo ui=null;
		conn=DBUtil.getConnection();
		String sql="select * from userinfo where username=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			rs=ps.executeQuery();
			while(rs.next()){
				//ui=new UserInfo();
				int id = rs.getInt(1);
				String uname=rs.getString(2);
				String upass=rs.getString(3);
				double money = rs.getDouble(4);
				ui = new UserInfo(id,uname,upass,money);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return ui;
	}

}
