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
import entities.GroupHireEntity;
import entities.PlayerEntity;
import entities.TeamEntity;

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class HirePlayers
 */
@WebServlet("/HirePlayers")
public class HirePlayers extends HttpServlet {
	private static final String TITLE = "Convoca calciatori";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HirePlayers() {
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
		
		// connessione al database
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		// acquisizione dati da request
		ChampionshipEntity ce = new ChampionshipEntity();
		BeanUtilities.populateBean(ce,request);
		// flag di fallimento query nella pagina di stampa giocatori e squadre
		Boolean firstPage = true;
		// se il campionato è stato selezionato
		if(ce.getId() != null){
			try{
				// recupera calciatori non assegnati nel campionato
				List<PlayerEntity> lp = dbc.getAvailablePlayers(ce.getId());	
				// scelta giocatori
				out.println("<form name=\"hireplayers\" method=\"POST\">");
				out.println("Squadra: <select name=\"team\">");
				// stampa i tipi di voti
				List<TeamEntity> lt = dbc.getOpenTeamsOfChampionship(ce.getId());
				for(Iterator<TeamEntity> it = lt.iterator();it.hasNext();){
					TeamEntity t = it.next();
					out.println(Style.option(t.getId().toString(),t.getName()));
				}
				out.println("</select>");
				out.println("<table border=\"1\">");
				out.println("<tr>");
				out.println("<td>Attaccanti</td>");
				out.println("<td>Centrocampisti</td>");
				out.println("<td>Difensori</td>");
				out.println("<td>Portieri</td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td valign=\"top\">");
				
				out.println("<select name=\"att\" multiple=\"multiple\">");
				// stampa attaccanti
				Integer count = 0;
				for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
					
					PlayerEntity p = it.next();
					if(p.isAtt()){
						out.println(Style.option(p.getId().toString(),p.getName() + 
							" - " + p.getTeam()));
						count++;
					}
				}
				out.println("</select>");
				out.println("</td>");
				out.println("<td valign=\"top\">");
				out.println("<select name=\"cen\" multiple=\"multiple\">");
				// stampa centrocampisti
				for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
					PlayerEntity p = it.next();
					if(p.isCen()){
						out.println(Style.option(p.getId().toString(),p.getName()));	
						count++;			
					}
				}
				out.println("</select>");
				out.println("</td>");
				out.println("<td valign=\"top\">");
				out.println("<select name=\"def\" multiple=\"multiple\">");
				// stampa difensori
				for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
					PlayerEntity p = it.next();
					if(p.isDef()){
						out.println(Style.option(p.getId().toString(),p.getName() +
							" - " + p.getTeam()));			
						count++;			
					}
				}
				out.println("</select>");
				out.println("</td>");
				out.println("<td valign=\"top\">");
				out.println("<select name=\"golkeep\" multiple=\"multiple\">");
				// stampa portieri
				for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
					PlayerEntity p = it.next();
					if(p.isGoalKeep()){
						out.println(Style.option(p.getId().toString(),p.getName() + 
							" - " + p.getTeam()));			
						count++;			
					}
				}
				out.println("</select>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<input type=\"submit\" value=\"scegli\">");
				out.println("</form>");
				firstPage = false;
			}catch(SQLException sqle){
				// se c'è un errore segnalalo e stampa la pagina di selezione campionato
				firstPage = true;
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			}	
		}
		// pagina scelta del campionato
		if(firstPage){
			// TODO controllare se esistono campionati
			// acquisizione dati da request
			GroupHireEntity ghe = new GroupHireEntity();
			BeanUtilities.populateBean(ghe,request);	
			// TODO controllare se si seleziona un campionato con solo squadre già completate
			//se la rosa di calciatori è stata ricevuta
			if(ghe.isComplete()){
				// se la rosa è composta dal giusto numero di calciatori
				if(ghe.isCorrect()){
					// inserisci i dati della convocazione
					try {
						dbc.insertHireGroup(ghe);
						out.println(Style.successMessage("Convocazione effettuata con successo"));						
					} catch (SQLException sqle) {
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}else{
					// dati inseriti scorretti
					// TODO leggere i numeri da file di configurazione
					out.println(Style.alertMessage(
						"I dati inseriti non sono corretti: devono esserci 6 attaccanti,"+ 
						" 8 centrocampisti, 8 difensori e 3 portieri"));
				}
			}
			try{
				List<ChampionshipEntity> cel = dbc.getHireableChampionships();
				if(cel.size() == 0){
					// avvisa che non esistono campionati
					out.println(Style.alertMessage(
						"Non esistono campionati con squadre abilitate alla convocazione"));
				}else{
					out.println("<form name=\"hireplayers\" method=\"POST\">\n");
					out.println("Campionato: <select name=\"id\">\n");					
					// stampa squadre senza rosa di calciatori
					for(Iterator<ChampionshipEntity> it = cel.iterator();it.hasNext();){
						ChampionshipEntity c = it.next();
						out.println(Style.option(c.getId().toString(),c.getName()));
					}
					out.println("</select><br>\n");
					out.println("<input type=\"submit\" value=\"scegli\">\n");
					out.println("</form>\n");					
				}
			}catch(SQLException sqle){
				// stampa messaggio di errore
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			}
		}
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
