package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataconnection.MySQLConnection;

/**
 * Servlet implementation class ConnectionHelper
 */
@WebServlet("/ConnectionHelper")
public class ConnectionHelper extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		MySQLConnection.startup(getServletContext().getRealPath("/fantaservlet.properties"));
	}
	
	@Override
	public void destroy() {
		MySQLConnection.destroy();
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectionHelper() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
