package com.reader.bean;

public class UserInfo {
	int id;
	String username;
	String pswd;
	double money;
	boolean userload;//登录状态，1是登录，0是未登录  构造函数无此项
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public boolean isUserload() {
		return userload;
	}
	public void setUserload(boolean userload) {
		this.userload = userload;
	}
	public UserInfo(int id, String username, String pswd, double money
			) {
		super();
		this.id = id;
		this.username = username;
		this.pswd = pswd;
		this.money = money;
		
	}
	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
