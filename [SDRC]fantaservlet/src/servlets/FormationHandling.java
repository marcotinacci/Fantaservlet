package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import entities.ChampionshipEntity;
import entities.DayEntity;
import entities.FormationEntity;
import entities.PlayerEntity;
import entities.TeamEntity;

/**
 * Servlet implementation class FormationHandling
 */
@WebServlet("/FormationHandling")
public class FormationHandling extends HttpServlet {
	private static final String TITLE = "Gestione delle Formazioni";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormationHandling() {
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

		// --- stampa le giornate ---
		try {			
			// codice del form da stampare
			StringBuffer code = new StringBuffer();
			code.append("<form action=\"formationhandling.jsp\" method=\"POST\">\n");
			code.append("Scegli la giornata:\n");
			code.append("<select name=\"day\">\n");

			// flag vero se esistono sono giornate
			Boolean anyDays = false; 
			// lista dei campionati dell'utente
			List<ChampionshipEntity> lc = dbc.getDefChampOfUser(logger.getUser().getId());
			// stampa le giornate aperte divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
				// fissa un campionato c
				ChampionshipEntity c = i.next(); 
				List<DayEntity> ld = dbc.getDayOfChampionship(c.getId());
				// TODO far scegliere checkbox se stampare solo le giornate modificabili
				// --- List<DayEntity> lod = dbc.getOpenDayOfChampionship(c.getId()); ---
				if(ld.size() > 0){
					// segnala che ci sono giornate da stampare
					anyDays = true;
					// stampa le giornate del campionato c
					code.append(Style.optionGroup(c.getName()));	
					for(Iterator<DayEntity> j = ld.iterator(); j.hasNext(); ){
						DayEntity d = j.next();
						code.append(Style.option(d.getId().toString(),d.getFormatDate()));		
					}
				}
			}
			code.append("</select>\n");
			code.append("<input type=\"hidden\" name=\"todo\" value=\"viewday\">\n");
			code.append("<input type=\"submit\" value=\"Visualizza formazione\">\n");
			code.append("</form>\n");
			if(anyDays){
				out.println(code.toString());
			}else{
				out.println(Style.infoMessage(
					"Non ci sono giornate disponibili per questo utente"));
			}
			
		} catch (SQLException e) {
			out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
		}
		
		String todoParam = request.getParameter("todo");
		if(todoParam != null){
			// TODO insformation e modformation hanno parti da fattorizzare

			if(todoParam.equalsIgnoreCase("insformation")){
				// --- inserimento della formazione ---
				try{
					FormationEntity fe = new FormationEntity();
					BeanUtilities.populateBean(fe,request);
					ChampionshipEntity c = dbc.getChampionshipOfTeam(fe.getTeam());
					List<DayEntity> ld = dbc.getOpenDayOfChampionship(c.getId());
					// lista degli id delle giornate aperte
					List<Integer> lid = new ArrayList<Integer>();
					for(Iterator<DayEntity> it = ld.iterator(); it.hasNext();){
						lid.add(((DayEntity)it.next()).getId());
					}
					// se la formazione ricevuta è corretta, non è già presente una formazione
					// e la giornata è ancora aperta a modifiche		
					if(fe.isCorrect() && 
						dbc.getFormation(fe.getTeam(), fe.getDay()).getPlayers().size() == 0 && 
						lid.contains(fe.getDay()))
					{
						// inserisci i dati della convocazione
						dbc.insertFormation(fe);
						out.println(Style.successMessage("Formazione salvata"));
					}else{
						// dati inseriti scorretti
						// TODO leggere i numeri da file di configurazione
						out.println(Style.alertMessage("I dati inseriti non sono corretti: "+ 
							"le formazioni possibili sono 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2"));
					}
				}catch(SQLException sqle){
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
				}
			}else if(todoParam.equalsIgnoreCase("modformation")){
				// --- modifica della formazione ---
				try{
					FormationEntity fe = new FormationEntity();
					BeanUtilities.populateBean(fe,request);
					// se la formazione ricevuta è corretta e la giornata è ancora aperta a modifiche
					// (in questo caso la formazione DEVE essere già presente in quanto da modificare)
					ChampionshipEntity c = dbc.getChampionshipOfTeam(fe.getTeam());
					List<DayEntity> ld = dbc.getOpenDayOfChampionship(c.getId());
					// lista degli id delle giornate aperte
					List<Integer> lid = new ArrayList<Integer>();
					for(Iterator<DayEntity> it = ld.iterator(); it.hasNext();){
						lid.add(((DayEntity)it.next()).getId());
					}
					if(fe.isCorrect() && lid.contains(fe.getDay()))
					{		
						// aggiorna la formazione passando al metodo il nuovo e il vecchio schieramento
						dbc.updateFormation(fe,dbc.getFormation(fe.getTeam(), fe.getDay()));			
						out.println(Style.successMessage("Formazione modificata"));
					}else{
						// dati inseriti scorretti
						// TODO leggere i numeri da file di configurazione
						out.println(Style.alertMessage("I dati inseriti non sono corretti: "+ 
							"le formazioni possibili sono 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2"));
					}
				}catch(SQLException sqle){
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));					
				}
			}
			// --- stampa lo stato della giornata selezionata ---
			try{
				// id della giornata selezionata
				Integer did = Integer.parseInt(request.getParameter("day"));
				// determina se la giornata è aperta alle modifiche
				List<DayEntity> openDays = 
					dbc.getOpenDayOfChampionship(dbc.getChampionshipOfDay(did).getId());
				List<Integer> openDaysId = new ArrayList<Integer>();
				for(Iterator<DayEntity> it = openDays.iterator(); it.hasNext();){
					openDaysId.add(((DayEntity)it.next()).getId());
				}
				// flag true se la giornata è aperta alle modifiche
				Boolean isOpenDay = openDaysId.contains(did);
				// stampa messaggio giornata chiusa
				if(!isOpenDay)
					out.println(Style.infoMessage("La giornata non &egrave; aperta alle modifiche"));
				List<TeamEntity> lTeam = dbc.getTeamsOfUserInDay(logger.getUser().getId(),did);
				if(lTeam.size() > 0){
					// per ogni squadra
					for(Iterator<TeamEntity> it = lTeam.iterator(); it.hasNext();){
						out.println("<hr>"); 
						TeamEntity team = it.next();
						// prendi le convocazioni della squadra da database
						List<PlayerEntity> hiredPlayers = dbc.getHiredPlayers(team.getId());
						//se sono stati convocati giocatori per questa squadra
						if(hiredPlayers.size() > 0){
							// recupera da database la formazione della squadra nel giorno
							FormationEntity formation = dbc.getFormation(team.getId(), did);
							// memorizzo la lista di id per evitare di ricalcolarla ogni volta
							List<Integer> formationList = formation.getPlayers();
							if(formationList.size() > 0){
								// se ci sono giocatori la formazione è già presente
								// --- formazione attuale ---
								// recupera i dati dei calciatori nella formazione
								List<PlayerEntity> players = dbc.getPlayersById(formationList);
								out.println("<p>La formazione della squadra <b>"+team.getName()+
									"</b> &egrave; la seguente:</p>");
								out.println("<ul>");
								out.println("<li><b>Modulo:</b>"+
									formation.getDef().length+ " - "+
									formation.getCen().length+ " - "+
									formation.getAtt().length);
								out.println("<li><b>Difensori:</b>"+
									Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'D'),false));
								out.println("<li><b>Centrocampisti:</b>"+
									Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'C'),false));		
								out.println("<li><b>Attaccanti:</b>"+
									Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'A'),false));		
								out.println("<li><b>Portiere:</b>"+
									Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'P'),false));		
								out.println("</ul>");
							}
							
							if(isOpenDay){
								// --- form inserimento formazione ---
								if(formationList.size() > 0){
									out.println(
										"<p>&Egrave; ancora possibile modificare la formazione: </p>");
								}else{
									out.println("<p>La formazione della squadra <b>"+
										team.getName()+"</b> deve ancora essere inserita:</p>");
								}
								out.println("<form name=\"formationhandling\" method=\"POST\">");
								out.println("<table border=\"1\">");
								out.println("<tr>");
								out.println("<th>Difensori</th>");
								out.println("<th>Centrocampisti</th>");
								out.println("<th>Attaccanti</th>");
								out.println("<th>Portieri</th>");
								out.println("</tr>");
								out.println("<tr>");
								out.println("<td valign=\"top\">"+
									Style.selectPlayers(
									GenericUtilities.getPlayersListByRule(hiredPlayers,'D'),"def",
									formationList));
								out.println("</td><td valign=\"top\">"+
									Style.selectPlayers(
									GenericUtilities.getPlayersListByRule(hiredPlayers,'C'),"cen",
									formationList));
								out.println("</td><td valign=\"top\">"+
									Style.selectPlayers(
									GenericUtilities.getPlayersListByRule(hiredPlayers,'A'),"att",
									formationList));
								out.println("</td><td valign=\"top\">"+
									Style.selectPlayers(
									GenericUtilities.getPlayersListByRule(hiredPlayers,'P'),"golkeep",
									formationList));							
								out.println("</td></tr></table>");
								out.println(Style.hidden("day", did.toString()));
								out.println(Style.hidden("team", team.getId().toString()));
					
								if(formationList.size() > 0){
									out.println(Style.hidden("todo", "modformation"));								
									out.println("<input type=\"submit\" value=\"Modifica formazione\">");
								}else{
									out.println(Style.hidden("todo", "insformation"));
									out.println("<input type=\"submit\" value=\"Inserisci formazione\">");
								}
								out.println("<input type=\"reset\">");	
								out.println("</form>");
							}else if(formationList.size() == 0){
								out.println(Style.alertMessage("La giornata è chiusa senza che la squadra "+
									team.getName()+" abbia ricevuto una formazione."));
							}
						}else{
							// se non sono stati convocati giocatori
							out.println(Style.alertMessage(
								"Non sono ancora stati convocati giocatori per la squadra "+team.getName()));
						}
					}
				}else{
					out.println(Style.alertMessage(
						"Non hai squadre che giocano in questa giornata di campionato"));
				}
			}catch(SQLException sqle){
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));				
			}
		}

		// chiusura database
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
