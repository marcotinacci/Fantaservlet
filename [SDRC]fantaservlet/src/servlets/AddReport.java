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
import entities.JudgeEntity;
import entities.PlayerEntity;
import entities.ReportEntity;
import entities.VoteEntity;
import exceptions.BadFormException;

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
						MySQLConnection.insertReport(report);
						out.println(Style.successMessage("Valutazione inserita"));
					}catch(SQLException sqle){
						// in caso di errore SQL stampa l'alert
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}
				
			}else if(todo.equals("insGiudge")){
				JudgeEntity giudge = new JudgeEntity();
				BeanUtilities.populateBean(giudge,request);
				// se è stato inserito un giudizio
				if(giudge.isComplete()){
					try{
						// inserimento report
						MySQLConnection.insertGiudge(giudge);
						out.println(Style.successMessage("Giudizio inserito"));
					}catch(SQLException sqle){
						// in caso di errore SQL stampa l'alert
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}
				
			}
		}
		try {
			List<VoteEntity> lv = MySQLConnection.getVotes();
			List<ChampionshipEntity> lc = MySQLConnection.getChampionships();
			List<PlayerEntity> lp = MySQLConnection.getPlayers();
			
			if(lc.size() == 0) throw new BadFormException("Non ci sono campionati");
			if(lp.size() == 0) throw new BadFormException("Non ci sono calciatori");
		
			StringBuffer code = new StringBuffer();			
			code.append("<h2>Aggiungi azione</h2>");
			code.append("<form name=\"addreport\" method=\"POST\">");
			code.append("Azione: <select name=\"vote\">");
			
			// stampa i tipi di voti
			for(Iterator<VoteEntity> it = lv.iterator();it.hasNext();){
				VoteEntity v = it.next();	
				code.append(Style.option(v.getId().toString(),v.getAction()));
			}
			
			code.append("</select><br>");
			code.append("Giornata: <select name=\"day\">");
			
			Boolean noDays = true;
			// stampa le giornate divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator(); i.hasNext(); ){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = MySQLConnection.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					noDays = false;
					// stampa le giornate del campionato c
					code.append(Style.optionGroup(c.getName()));
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						code.append(Style.option(d.getId().toString(),d.getFormatDate()));		
					}
				}
			}
			
			if(noDays) throw new BadFormException("Non ci sono giornate");
			
			code.append("</select><br>");
			code.append("Calciatore: <select name=\"player\">");
			
			// stampa i calciatori
			for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
				PlayerEntity p = it.next();	
				code.append(Style.option(p.getId().toString(),p.getName()));
			}
			
			code.append("</select><br/><br/>");
			code.append(Style.hidden("todo", "insAction"));			
			code.append("<input type=\"submit\" value=\"Inserisci\">");
			code.append("</form>");
			
			code.append("<h2>Aggiungi giudizio</h2>");	
			code.append("<form name=\"addgiudge\" method=\"POST\">");
			code.append("Giudizio: <select name=\"vote\">");
			
			// stampa i possibili giudizi
			for(Double i = 1.; i <= 10; i+=0.5){
				code.append(Style.option(i.toString(),i.toString()));
			}
			
			code.append("</select><br>");
			// TODO fattorizzare giornata
			code.append("Giornata: <select name=\"day\">");
			
			noDays = true;
			// stampa le giornate divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator(); i.hasNext(); ){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = MySQLConnection.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					noDays = false;
					// stampa le giornate del campionato c
					code.append(Style.optionGroup(c.getName()));
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						code.append(Style.option(d.getId().toString(),d.getFormatDate()));		
					}
				}
			}
			
			// TODO controllo ridondante, da eliminare con la fattorizzazione
			if(noDays) throw new BadFormException("Non ci sono giornate");
			
			code.append("</select><br>");
			// TODO fattorizzare calciatore
			code.append("Calciatore: <select name=\"player\">");
			
			// stampa i calciatori
			for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
				PlayerEntity p = it.next();	
				code.append(Style.option(p.getId().toString(),p.getName()));
			}
			
			code.append("</select><br/><br/>");
			code.append(Style.hidden("todo", "insGiudge"));
			code.append("<input type=\"submit\" value=\"Inserisci\">");
			code.append("</form>");
			out.println(code);
		} catch (SQLException sqle) {
			out.println(Style.alertMessage("Errore SQL: "+ sqle.getMessage()));
		} catch (BadFormException bfe) {
			out.println(Style.alertMessage(bfe.getMessage()));
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
