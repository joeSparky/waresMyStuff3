package com.forms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.errorLogging.Internals;

//@WebServlet(urlPatterns = "/parts/login")
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -479510525280090016L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doBoth(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doBoth(request, response);
	}

	protected void doBoth(HttpServletRequest request, HttpServletResponse response) {
//		FormsArray ret = new FormsArray();
		response.setContentType("text/html");
//		HttpSession session = request.getSession();

		String userName = request.getParameter("username");

		if (userName == null) {
			// send the login form
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.html");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				Internals.dumpException(e);
			} catch (IOException e) {
				Internals.dumpException(e);
			}
		} else {
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				System.exit(-1);
			}
			out.println("fer shure");
			// send the login form
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.html");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				Internals.dumpException(e);
			} catch (IOException e) {
				Internals.dumpException(e);
			}
			out.close();
		}
	}
}
