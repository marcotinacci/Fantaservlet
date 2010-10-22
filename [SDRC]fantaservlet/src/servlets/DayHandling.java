package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataconnection.MySQLConnection;
import entities.ChampionshipEntity;
import entities.PlayerEntity;

import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class DayHandling
 */
@WebServlet("/DayHandling")
public class DayHandling extends HttpServlet {
	private static final String TITLE = "Gestione giornate";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DayHandling() {
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
		
		MySQLConnection dc = new MySQLConnection();
		dc.init();
		if(request.getParameter("closeday") != null){
			// chiudi data selezionata
			try {
				dc.updateCloseDay(Integer.parseInt(request.getParameter("closeday")));
				out.println(Style.successMessage("Data chiusa"));				
			} catch (SQLException e) {
				out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
			}	
		}else if(request.getParameter("openday") != null){
			// riapri la data selezionata
			try {
				dc.updateOpenDay(Integer.parseInt(request.getParameter("openday")));
				out.println(Style.successMessage("Data riaperta"));					
			} catch (SQLException e) {
				out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
			}

		}else if(request.getParameter("evaluateday") != null){
			// valuta la data selezionata
			try {
				// controlla se sono stati assegnati i giudizi a ogni calciatore della giornata
				Integer evaluatedDay = Integer.parseInt(request.getParameter("evaluateday"));
				List<PlayerEntity> unevaluatedPlayers = dc.getUnevaluatedPlayersInDay(evaluatedDay);
				if(unevaluatedPlayers.size() == 0){
					dc.updateEvaluateDay(evaluatedDay);
					out.println(Style.successMessage("Data valutata"));					
				}else{
					// stampa i giocatori non valutati nel messaggio di errore
					StringBuffer alertMess = new StringBuffer("La giornata non pu&ograve; essere valutata " +
							"perch&eacute; i seguenti calciatori non sono stati valutati:\n");
					alertMess.append(Style.showPlayersList(unevaluatedPlayers, false));
					out.println(Style.alertMessage(alertMess.toString()));
				}
			} catch (SQLException e) {
				out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
			}
		}
		
		try{
			List<ChampionshipEntity> lc = dc.getChampionships();
			// stampa le giornate aperte divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
				// fissa un campionato c
				ChampionshipEntity c = i.next();
				out.println("<h2>Campionato: "+c.getName()+"</h2>");
				out.println(Style.modDays(dc.getDayOfChampionship(c.getId())));
			}
		}catch(SQLException sqle){
			out.println("Errore SQL: "+sqle.getMessage());
		}
		
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
