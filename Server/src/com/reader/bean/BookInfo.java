package com.reader.bean;

public class BookInfo {

	int book_id;
	String book_bookname;
	double book_price;
	String book_author;
	String book_img;
	String book_content;
	String book_read;
	public String getBook_read() {
		return book_read;
	}
	public void setBook_read(String book_read) {
		this.book_read = book_read;
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getBook_bookname() {
		return book_bookname;
	}
	public void setBook_bookname(String book_bookname) {
		this.book_bookname = book_bookname;
	}
	public double getBook_price() {
		return book_price;
	}
	public void setBook_price(double book_price) {
		this.book_price = book_price;
	}
	public String getBook_author() {
		return book_author;
	}
	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}
	public String getBook_img() {
		return book_img;
	}
	public void setBook_img(String book_img) {
		this.book_img = book_img;
	}
	public String getBook_content() {
		return book_content;
	}
	public void setBook_content(String book_content) {
		this.book_content = book_content;
	}
	public BookInfo(String book_bookname, double book_price,
			String book_author, String book_img, String book_content,String book_read) {
		super();
		this.book_bookname = book_bookname;
		this.book_price = book_price;
		this.book_author = book_author;
		this.book_img = book_img;
		this.book_content = book_content;
		this.book_read = book_read;
	}
	public BookInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
