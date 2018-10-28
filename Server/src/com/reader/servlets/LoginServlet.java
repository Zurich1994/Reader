package com.reader.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reader.bean.UserInfo;
import com.reader.login.dao.impl.LoginDaoImpl;;
public class LoginServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


//		response.setContentType("text/html;charset=GBK");
//		request.setCharacterEncoding("UTF-8");
//		String username = request.getParameter("username");
//		String password	= request.getParameter("password");
//		PrintWriter writer = response.getWriter();
//		writer.print("获得的姓名是"+ username +"获得的密码是"+password);
//		
		UserInfo ui;
		LoginDaoImpl ldi = new LoginDaoImpl();
		String username=request.getParameter("username");
        String password=request.getParameter("password");
        ui=ldi.search(username);
        System.out.println("username"+username+" password"+password);
        if(ui!=null){
        if(ui.getPswd().equals(password)){
            response.getWriter().println("success");
        }else{
            response.getWriter().println("wrong");
        }}else{
            response.getWriter().println("wrong");
        }
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request,response);

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
