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
import entities.UserEntity;

import utils.BeanUtilities;
import view.Style;

/**
 * Servlet implementation class AddTeam
 */
@WebServlet("/AddTeam")
public class AddTeam extends HttpServlet {
    
	private static final String TITLE = "Crea squadra";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTeam() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(Style.pageHeader(TITLE));
		
		// crea connessione al database
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		List<UserEntity> lpu;
		try {
			// lista utenti giocanti
			lpu = dbc.getPlayingUsers();
			// lista campionati
			List<ChampionshipEntity> lc = dbc.getUndefinedChampionships();
	
			// controlla che esistano almeno un campionato e un utente giocatore
			if(lpu == null || lc == null){
				if(lpu == null){
					out.println(Style.alertMessage("Non esistono utenti giocanti."));
				}
				if(lc == null){
					out.println(Style.alertMessage("Non esistono campionati da definire disponibili."));
				}
			}else{
				try{
					// flag nome disponibile
					Boolean nameAvailable = true;
					// flag sotto il limite massimo di squadre per campionato
					Boolean underMaxLimit = true;
					TeamEntity team = new TeamEntity();
					BeanUtilities.populateBean(team, request);
					if(team.isComplete()){
						nameAvailable = team.isAvailableName();
						// TODO prendere il numero massimo di squadre per campionato da file di configurazione
						underMaxLimit = dbc.getTeamsOfChampionship(team.getChampionship()).size() < 12;
						if(nameAvailable && underMaxLimit){
							dbc.InsertTeam(team);
							// stampa avvenuto inserimento
							out.println(Style.successMessage("Squadra "+team.getName()+" creata."));
						}
						// stampa eventuali errori
						// se nome non disponibile
						if(!nameAvailable){
							out.println(Style.alertMessage("Nome non disponibile."));
						}
						// se il limite massimo  stato raggiunto
						if(!underMaxLimit){
							// TODO prendere il numero massimo di squadre per campionato da file di configurazione
							out.println(Style.alertMessage("Il campionato ha raggiunto il numero massimo di squadre [12]"));
						}
					}
				}catch(SQLException sqle){
					// notifica errore SQL
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
				}
	
				out.println("<form name=\"addteam\" method=\"POST\" >");
				out.println("Squadra: <input type=\"text\" name=\"name\"> <br>");
				out.println("Utente: <select name=\"user\">");
	
				// stampa gli utenti
				for(Iterator<UserEntity> it = lpu.iterator();it.hasNext();){
					UserEntity u = it.next();
					out.println(Style.option(u.getId().toString(),u.getName()));
				}
				
				out.println("</select><br>");
				out.println("Campionato: <select name=\"championship\">");
				
				// stampa i campionati
				for(Iterator<ChampionshipEntity> it = lc.iterator();it.hasNext();){
					ChampionshipEntity c = it.next();
					out.println(Style.option(c.getId().toString(),c.getName()));
				}
				
				out.println("</select><br>");
				out.println("<input type=\"submit\" value=\"crea\">");
				out.println("</form>");		
			}
		}catch (SQLException sqle) {
			out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
		}finally{
			//chiudi connessione al database
			dbc.destroy();			
		}					
		out.println(Style.pageFooter());		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
