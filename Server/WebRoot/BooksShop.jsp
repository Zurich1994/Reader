<%@page import="com.reader.bean.BookInfo"%>
<%@ page language="java" import="java.util.*,com.reader.book.dao.impl.BookDaoImpl" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'BooksShop.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <title>书城</title>
  <p align="center" style="font-size: 24px;color:red;">我的书城 </p>
    <% BookDaoImpl bdi = new BookDaoImpl();
    ArrayList<BookInfo> bis =new  ArrayList<BookInfo>();
    // bis = (ArrayList<BookInfo>)bdi.GetBooks();
    if(bis!=null){
     %>
     <table style="width: 500px;"   border="1">
<tr><th>书名</th><th>书价格</th><th>书作者</th><th>图片</th><th>操作</th></tr>

    <%
    System.out.println(bis.size());
    System.out.println("---------"+bis.get(0).getBook_img());
    for(int i=0;i<bis.size();i++){
    BookInfo b=bis.get(i);

     %>
     <tr>
     	
     	<td style="width: 75px"><%= b.getBook_bookname() %></td>
     	<td style="width: 75px"><%= b.getBook_price() %></td>
     	<td style="width: 75px"><%= b.getBook_author() %></td>
     	<td style="width: 200px"><img src="<%=bis.get(0).getBook_img() %>" ></td>
     	<td style="width: 75px"><a href="<%=basePath%>servlet/DownLoadBook?book_id=<%= b.getBook_id() %>">下载</a>
     	</td>
     </tr>
     <%} %>
    </table>
    <%
   }
   else{
   	     out.println("没有数据");
   } 
  %>
  
  </body>
</html>
