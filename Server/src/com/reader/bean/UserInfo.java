package com.reader.bean;

public class UserInfo {
	int id;
	String username;
	String pswd;
	double money;
	boolean userload;//��¼״̬��1�ǵ�¼��0��δ��¼  ���캯���޴���
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
