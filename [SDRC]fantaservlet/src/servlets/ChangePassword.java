package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.Logger;

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;
import dataconnection.MySQLConnection;
import entities.UserEntity;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final String TITLE = "Cambia Password";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
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
		Logger logger = GenericUtilities.checkLoggedIn(request, response);

		// inserimento utente
		UserEntity user = new UserEntity();
		BeanUtilities.populateBean(user, request);
		if(request.getParameter("todo") != null){
			user.setName(logger.getUser().getName());
			String old = request.getParameter("oldpassword");
			if(user.isComplete() && old != null){
				// controlla che la vecchia password inserita sia corretta
				if(logger.getUser().getPassword().equalsIgnoreCase(old)){
					if(user.isConfirmed()){
						try{
							// aggiorna la password nel database
							MySQLConnection.updatePassword(logger.getUser().getId(), user.getPassword());
							// aggiorna la password nella sessione
							logger.getUser().setPassword(user.getPassword());
							out.println(Style.successMessage(
								"La password è stata modificata correttamente"));
						}catch(SQLException sqle){
							out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));							
						}
						
					}else{
						out.println(Style.alertMessage(
							"Le nuove password inserite non corrispondono"));
					}
				}else{
					out.println(Style.alertMessage("La vecchia password non è corretta"));
				}
			}else{
				out.println(Style.alertMessage("Inserire tutti i campi"));
			}
		}
		out.println("<form name=\"modPassword\" method=\"POST\">");
		out.println("Vecchia password: <input type=\"password\" name=\"oldpassword\"/><br/>");
		out.println("Nuova password: <input type=\"password\" name=\"password\"/><br/>");
		out.println("Conferma: <input type=\"password\" name=\"confirm\"/><br/><br/>");		
		out.println("<input type=\"hidden\" name=\"todo\" value=\"modPassword\"/>");		
		out.println("<input type=\"submit\" value=\"crea\"/>");
		out.println("</form>");
		
		out.println(Style.pageFooter());		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
