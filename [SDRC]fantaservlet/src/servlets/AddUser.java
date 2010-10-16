package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataconnection.MySQLConnection;
import entities.UserEntity;

import utils.BeanUtilities;
import utils.GenericUtilities;
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
		GenericUtilities.checkLoggedIn(request, response, true);

		// inserimento utente
		Boolean nameAvailable = true;
		Boolean confirmPassword = true;
		UserEntity user = new UserEntity();
		BeanUtilities.populateBean(user, request);
		if(request.getParameter("todo") != null){
			if(user.isComplete()){
				MySQLConnection dbc = new MySQLConnection();
				dbc.init();
				try {
					nameAvailable = user.isAvailableName();
					confirmPassword = user.isConfirmed();
					if(confirmPassword){
						if(nameAvailable){
							dbc.insertUser(user);
							// stampa avvenuto inserimento
							out.println(Style.successMessage("Utente "+user.getName()+" creato."));
						}
					}
					// stampa eventuali errori
					if(!nameAvailable){
						out.println("Nome non disponibile");
					}
					if(!confirmPassword){
						out.println("La password non corrisponde alla conferma");
					}
				} catch (SQLException sqle) {
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
				}finally{
					dbc.destroy();
				}
			}else{
				out.println(Style.alertMessage("Inserire tutti i campi"));
			}
		}
		out.println("<form name=\"adduser\" method=\"POST\">");
		out.println("Nome utente: <input type=\"text\" name=\"name\"/><br/>");
		out.println("Password: <input type=\"password\" name=\"password\"/><br/>");
		out.println("Conferma: <input type=\"password\" name=\"confirm\"/><br/>");
		out.println("Admin <input type=\"checkbox\" name=\"admin\"/><br/>");
		out.println("<input type=\"hidden\" name=\"todo\" value=\"insertUser\"/><br/>");		
		out.println("<input type=\"submit\" value=\"crea\"/>");
		out.println("</form>");		
		
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
