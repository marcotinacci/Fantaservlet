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

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;

import dataconnection.MySQLConnection;
import entities.ChampionshipEntity;
import entities.DayEntity;
import entities.GiudgeEntity;
import entities.PlayerEntity;
import entities.ReportEntity;
import entities.VoteEntity;

/**
 * Servlet implementation class AddReport
 */
@WebServlet("/AddReport")
public class AddReport extends HttpServlet {
	private static final String TITLE = "Assegna voti";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddReport() {
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
		
		// assegnamento voto
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		String todo = request.getParameter("todo");
		if(todo != null){
			// richiesta di inserimento azione
			if(todo.equals("insAction")){
				ReportEntity report = new ReportEntity();
				BeanUtilities.populateBean(report,request);
				// se è stato inserita una votazione
				if(report.isComplete()){
					try{
						// inserimento report
						dbc.insertReport(report);
						out.println(Style.successMessage("Valutazione inserita"));
					}catch(SQLException sqle){
						// in caso di errore SQL stampa l'alert
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}
				
			}else if(todo.equals("insGiudge")){
				GiudgeEntity giudge = new GiudgeEntity();
				BeanUtilities.populateBean(giudge,request);
				// se è stato inserito un giudizio
				if(giudge.isComplete()){
					try{
						// inserimento report
						dbc.insertGiudge(giudge);
						out.println(Style.successMessage("Giudizio inserito"));
					}catch(SQLException sqle){
						// in caso di errore SQL stampa l'alert
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}
				
			}
		}
		try {
			List<VoteEntity> lv = dbc.getVotes();
			List<ChampionshipEntity> lc = dbc.getChampionships();
			List<PlayerEntity> lp = dbc.getPlayers();
			
			out.println("<h2>Aggiungi azione</h2>");
			out.println("<form name=\"addreport\" method=\"POST\">");
			out.println("Azione: <select name=\"vote\">");
			
			// stampa i tipi di voti
			for(Iterator<VoteEntity> it = lv.iterator();it.hasNext();){
				VoteEntity v = it.next();	
				out.println(Style.option(v.getId().toString(),v.getAction()));
			}
			
			out.println("</select><br>");
			out.println("Giornata: <select name=\"day\">");
			
			// stampa le giornate divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator(); i.hasNext(); ){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = dbc.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					// stampa le giornate del campionato c
					out.println(Style.optionGroup(c.getName()));
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						out.println(Style.option(d.getId().toString(),d.getFormatDate()));		
					}
				}
			}
			
			out.println("</select><br>");
			out.println("Calciatore: <select name=\"player\">");
			
			// stampa i calciatori
			for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
				PlayerEntity p = it.next();	
				out.println(Style.option(p.getId().toString(),p.getName()));
			}
			
			out.println("</select><br>");
			out.println(Style.hidden("todo", "insAction"));			
			out.println("<input type=\"submit\" value=\"Inserisci azione\">");
			out.println("</form>");
			
			out.println("<h2>Aggiungi giudizio</h2>");	
			out.println("<form name=\"addgiudge\" method=\"POST\">");
			out.println("Giudizio: <select name=\"vote\">");
			
			// stampa i possibili giudizi
			for(Double i = 1.; i <= 10; i+=0.5){
				out.println(Style.option(i.toString(),i.toString()));
			}
			
			out.println("</select><br>");
			// TODO fattorizzare giornata
			out.println("Giornata: <select name=\"day\">");
			
			// stampa le giornate divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator(); i.hasNext(); ){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = dbc.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					// stampa le giornate del campionato c
					out.println(Style.optionGroup(c.getName()));
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						out.println(Style.option(d.getId().toString(),d.getFormatDate()));		
					}
				}
			}
			
			out.println("</select><br>");
			// TODO fattorizzare calciatore
			out.println("Calciatore: <select name=\"player\">");
			
			// stampa i calciatori
			for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
				PlayerEntity p = it.next();	
				out.println(Style.option(p.getId().toString(),p.getName()));
			}
			
			out.println("</select><br>");
			out.println(Style.hidden("todo", "insGiudge"));
			out.println("<input type=\"submit\" value=\"Inerisci giudizio\">");
			out.println("</form>");			
			
		} catch (SQLException sqle) {
			out.println(Style.alertMessage("Errore SQL: "+ sqle.getMessage()));
		}			

		out.println(Style.pageFooter());
		// chiudi la connessione al database
		dbc.destroy();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
