package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;
import dataconnection.MySQLConnection;
import entities.PlayerEntity;

/**
 * Servlet implementation class AddPlayer
 */
@WebServlet("/AddPlayer")
public class AddPlayer extends HttpServlet {
    private static final String TITLE = "Crea calciatore";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AddPlayer() {
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
		
		// inserimento calciatore
		Boolean nameAvailable = true;
		PlayerEntity player = new PlayerEntity();
		BeanUtilities.populateBean(player, request);
		if(player.isComplete()){
			nameAvailable = player.isNameAvailable();
			if(nameAvailable){
				try {
					MySQLConnection.insertPlayer(player);
					// stampa avvenuto inserimento
					out.println(Style.successMessage("Calciatore "+player.getName()+" creato."));					
				}catch (SQLException sqle){
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
				}
			}else{
				out.println(Style.alertMessage("Nome non disponibile"));
			}
		}
		// stampa form di inserimento giocatore
		out.println("<form name=\"addplayer\" method=\"POST\">");
		out.println("Nome: <input type=\"text\" name=\"name\"><br>");
		out.println("Ruolo:"); 
		out.println("<input type=\"radio\" name=\"rule\" value=\"A\"> attaccante");
		out.println("<input type=\"radio\" name=\"rule\" value=\"C\"> centrocampista");
		out.println("<input type=\"radio\" name=\"rule\" value=\"D\"> difensore");
		out.println("<input type=\"radio\" name=\"rule\" value=\"P\" checked=\"checked\"> portiere");
		out.println("<br>");
		out.println("Squadra di provenienza: <input type=\"text\" name=\"team\">"); 
		out.println("<input type=\"submit\" value=\"crea\">");
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
