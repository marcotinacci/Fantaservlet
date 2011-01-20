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
import entities.TeamEntity;
import exceptions.BadFormException;

import login.Logger;
import utils.GenericUtilities;
import utils.Match;
import utils.Pair;
import view.Style;

/**
 * Servlet implementation class ShowResults
 */
@WebServlet("/ShowResults")
public class ShowResults extends HttpServlet {
	private static final String TITLE = "Visualizza Risultati";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowResults() {
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
		Logger logger = GenericUtilities.checkLoggedIn(request, response, false);
		
		// stampa la selezione del campionato
		try{
			/* 
			 * TODO costruire il form della scelta del campionato in una funzione
			 * ed eliminare la connessione al db dalla funzione principale 
			 */
			
			// prendi i campionati a cui partecipa l'utente
			List<ChampionshipEntity> lc = MySQLConnection.getChampionshipOfUser(logger.getUser().getId());
			if(lc.size() == 0){
				out.println(Style.infoMessage("Non ci sono campionati"));
			}else{
				out.println("<form name=\"showresults\" method=\"POST\">");
				out.println("Scegli il campionato:");
				out.println("<select name=\"champ\">");				
				for(Iterator<ChampionshipEntity> it = lc.iterator(); it.hasNext(); ){
					ChampionshipEntity c = it.next();
					out.println(Style.option(c.getId().toString(),c.getName()));
				}
				out.println("</select>");
				out.println("<input type=\"submit\" value=\"Visualizza risultati\">");
				out.println("</form>");					
			}	
		}catch (SQLException sqle) {
			out.println("Errore SQL: "+sqle.getMessage());
		}

		// se è stato scelto un campionato
		if(request.getParameter("champ") != null){
			try{
				// id campionato
				Integer cid = Integer.parseInt(request.getParameter("champ"));
				// prendi la lista degli scontri
				List<Match> lm = GenericUtilities.getListOfMatches(cid);
				// --- stampa le partite ---
				Integer numTeams = GenericUtilities.getNumOfTeams(lm);
				if(numTeams == 0) throw new BadFormException("Non ci sono giornate");
				out.println(printMatches(lm, numTeams));
				// --- stampa la classifica ---
				out.println(printRanking(GenericUtilities.getRanking(lm),
						GenericUtilities.isConcluse(lm)));
			}catch (SQLException sqle) {
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			} catch (BadFormException bfe) {
				out.println(Style.infoMessage(bfe.getMessage()));
			}
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
	
	/**
	 * metodo di stampa dei risultati delle partite di un campionato
	 * @param matches lista delle partite del campionato
	 * @param numTeams numero delle squadre che vi partecipano
	 * @return risultati in codice html
	 */
	private String printMatches(List<Match> matches, Integer numTeams){
		Integer matchPerDay = numTeams/2;
		StringBuffer code = new StringBuffer();
		// stampa titolo della sezione
		code.append("<h2>Risultati delle partite</h2>");
		// stampa l'inizio della tabella
		code.append("<table border=\"1\">\n<tr><th>Giornata</th><th>Partita</th><th>Risultato</th><th>Punti</th></tr>");
		Integer counter = 0;
		for(Iterator<Match> it = matches.iterator();it.hasNext();counter = (counter+1) % matchPerDay){
			Match match = it.next();
			code.append("<tr>");
			if(counter == 0){
				code.append("<td rowspan="+matchPerDay.toString()+">"+match.getDay().getFormatDate()+"</td>");
			}
			// stampa i nomi delle squadre
			code.append("<td>"+match.getTeam1().getName()+" - "+match.getTeam2().getName()+"</td>");
			// stampa i gol segnati
			if(match.getDay().isEvaluated()){
				code.append("<td>"+
					GenericUtilities.pointsToGol(match.getPointsTeam1()) + " - " +
					GenericUtilities.pointsToGol(match.getPointsTeam2()) + "</td>");
				code.append("<td>" + match.getPointsTeam1() + " - " + match.getPointsTeam2() +"</td></tr>");
			}else{
				code.append("<td>NA</td><td>NA</td></tr>");
			}
		}
		code.append("</table>");		
		return code.toString();
	}

	/**
	 * metodo che stampa la classifica
	 * @param ranks lista di coppie (squadra, punteggio)
	 * @param isConcluse flag vero se la classifica è definitiva
	 * @return classifica in codice html
	 */
	private String printRanking(List<Pair<TeamEntity,Integer>> ranks, Boolean isConcluse){
		StringBuffer code = new StringBuffer();
		code.append("<h2>Classifica "+ (isConcluse ? "definitiva" : "provvisoria")+"</h2>");
		code.append("<table border=1><tr><th>Posizione</th><th>Squadra</th><th>Punteggio</th></tr>");
		Integer position = 0;
		Integer oldPoints = Integer.MAX_VALUE;
		Integer newPoints;
		for(Iterator<Pair<TeamEntity,Integer>> it = ranks.iterator(); it.hasNext();){
			Pair<TeamEntity, Integer> coppia = it.next();
			newPoints = coppia.getSecond();
			// se non è un parimerito avanza di posizione
			if(newPoints < oldPoints){
				oldPoints = newPoints;
				position++;
			}
			// stampa la posizione
			code.append("<tr><td>"+position+"</td>\n");
			// stampa il nome della squadra
			code.append("<td>"+coppia.getFirst().getName()+"</td>\n");
			// stampa il punteggio
			code.append("<td>"+newPoints+"</td></tr>\n");
		}
		code.append("</table>");
		return code.toString();		
	}

}
