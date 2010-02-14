package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbconnection.DBConnection;

public class ConnectionTest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ConnectionTest() {

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DBConnection dbc = new DBConnection();
		dbc.init();
		System.out.println("utenti: "+dbc.getUsers());
		dbc.destroy();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
