package com.reader.login.dao;

import com.reader.bean.UserInfo;

public interface LoginDao {
	public UserInfo search(String username);
	public void add(String username,String password,double money);
	public void updatamoney(String username,double money);
	public UserInfo showuserinfo(String username);
}
