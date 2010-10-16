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
import entities.DayEntity;
import entities.TeamEntity;

import login.Logger;
import utils.GenericUtilities;
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

		// connessione al database		
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();		

		// stampa la selezione del campionato
		try{
			out.println("<form name=\"showresults\" method=\"POST\">");
			out.println("Scegli il campionato:");
			out.println("<select name=\"champ\">");

			// prendi i campionati a cui partecipa l'utente
			List<ChampionshipEntity> lc = dbc.getChampionshipOfUser(logger.getUser().getId());
			for(Iterator<ChampionshipEntity> it = lc.iterator(); it.hasNext(); ){
				ChampionshipEntity c = it.next();
				out.println(Style.option(c.getId().toString(),c.getName()));
			}
			
			out.println("</select>");
			out.println("<input type=\"submit\" value=\"Visualizza risultati\">");
			out.println("</form>");			
		}catch (SQLException sqle) {
			out.println("Errore SQL: "+sqle.getMessage());
		}

		
		// se è stato scelto un campionato
		if(request.getParameter("champ") != null){
			try{
			// --- stampa le partite --- 
				Integer cid = Integer.parseInt(request.getParameter("champ"));
				// stampa titolo della sezione
				out.println("<h2>Risultati delle partite</h2>");
				// flag di chiusura campionato
				Boolean isChampClosed = true;
				// stringa di stampa delle giornate
				StringBuffer printDays = new StringBuffer();
				// recupera le giornate del campionato
				List<DayEntity> days = dbc.getDayOfChampionship(cid);
				// stampa l'inizio della tabella
				printDays.append("<table border=\"1\">\n<tr><th>Giornata</th><th>Partita</th><th>Risultato</th></tr>");	
				// per ogni giornata
				for(Iterator<DayEntity> it = days.iterator(); it.hasNext();){
					// giornata
					DayEntity day = it.next();
					// stampa la giornata
					printDays.append("<tr><td rowspan=3>"+day.getFormatDate()+"</td>");
					// recupera i dati delle partite della giornata
					List<Pair<TeamEntity,TeamEntity>> matches = dbc.getMatchesOfDay(day.getId());
					// per ogni partita
					for(Iterator<Pair<TeamEntity,TeamEntity>> it2 = matches.iterator(); it2.hasNext();){
						Pair<TeamEntity,TeamEntity> pair = it2.next();
						// stampa i nomi delle squadre
						printDays.append("<td>"+pair.getFirst().getName()+" - "+pair.getSecond().getName());
						// stampa i gol segnati
						if(day.isEvaluated()){
							printDays.append("</td><td>"+dbc.getGolOfTeamInDay(pair.getFirst().getId(),day.getId())+
								" - "+dbc.getGolOfTeamInDay(pair.getSecond().getId(),day.getId())+"</td></tr>");
						}else{
							printDays.append("</td><td>n.a.</td></tr>");
							isChampClosed = false;
						}
					}
				}
				printDays.append("</table>");
				out.println(printDays.toString());
				// --- stampa la classifica ---
				out.println("<h2>Classifica "+ (isChampClosed? "definitiva" : "provvisoria") +"</h2>");
				out.println(Style.showResults(dbc.getChampionshipResults(cid)));
			}catch (SQLException sqle) {
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			}
		}
		
		// chiudi connessione al database
		dbc.destroy();
		
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
