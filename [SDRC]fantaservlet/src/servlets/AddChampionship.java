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

import entities.ChampionshipEntity;

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class AddChampionship
 */
@WebServlet("/AddChampionship")
public class AddChampionship extends HttpServlet {
       
    private static final String TITLE = "Crea campionato";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AddChampionship() {
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
		
		// inserimento del campionato
		Boolean nameAvailable = true;
		ChampionshipEntity champ = new ChampionshipEntity();
		BeanUtilities.populateBean(champ, request);

		if(champ.isComplete()){
			try{
				nameAvailable = champ.isNameAvailable();
				if(nameAvailable){
					MySQLConnection dbc = new MySQLConnection();
					dbc.startup();		
					dbc.insertChampionship(champ);
					dbc.destroy();
					// stampa avvenuto inserimento
					out.println(Style.successMessage("Campionato "+champ.getName()+" creato."));
				}
				// stampa eventuali errori
				String alert = (nameAvailable?"":"Nome non disponibile");
				if(!alert.equals("")){
					out.println(Style.alertMessage(alert));
				}
			}catch(SQLException sqle){
				// stampa messaggio di errore
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			}
		}		
		// stampa il form di inserimento
		out.println("<form name=\"addchamp\" method=\"POST\">");
		out.println("Nome: <input type=\"text\" name=\"name\"/>"); 
		out.println("<br/>");
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
