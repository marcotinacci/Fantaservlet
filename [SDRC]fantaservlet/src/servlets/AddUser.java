package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dataconnection.MySQLConnection;

import entities.UserEntity;

import utils.BeanUtilities;
import view.Style;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final String TITLE = "Crea utente";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(Style.pageHeader(TITLE));
		
		HttpSession session = request.getSession();
		if(session.getAttribute("var") == null){
			out.println("Prima apertura pagina");
			session.setAttribute("var",1);
		}else{
			session.setAttribute("var",((Integer)session.getAttribute("var"))+1);
			out.println("Sei entrato in questa pagina "+(Integer)session.getAttribute("var")+ " volte");
		}		
		
		out.println(Style.pageFooter());		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

}
