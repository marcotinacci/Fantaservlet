package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calendargenerator.CalendarGenerator;
import dataconnection.MySQLConnection;

import entities.CalendarEntity;
import entities.ChampionshipEntity;
import exceptions.BadTeamsNumberException;

import utils.BeanUtilities;
import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class ComputeCalendar
 */
@WebServlet("/ComputeCalendar")
public class ComputeCalendar extends HttpServlet {
	private static final String TITLE = "Genera Calendario";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComputeCalendar() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(Style.pageHeader(TITLE));
		GenericUtilities.checkLoggedIn(request, response, true);
		
		CalendarEntity cal = new CalendarEntity();
		BeanUtilities.populateBean(cal, request);

		if(cal.getIdChampionship() != null){
			String time = request.getParameter("startDate");
			if(time != null){			
				CalendarGenerator cg = new CalendarGenerator(cal,Long.parseLong(time));
				try{
					// genera calendario					
					cg.generate();
					out.println(Style.successMessage("Calendario generato"));		
				}catch(BadTeamsNumberException btne){
					// squadre dispari
					if(!btne.isEven()){
						out.println(Style.alertMessage("Le squadre assegnate sono un numero "+ 
							"dispari: sono presenti "+ btne.getNumTeams() + " squadre."));
					}
					// squadre fuori dal range
					if(!btne.isInRange()){
						out.println(Style.alertMessage(
							"Le squadre non sono nel range [6,12]: sono presenti "+ 
							+ btne.getNumTeams()+ " squadre."));
					}
				} catch (SQLException sqle) {
					out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));				
				}
			}else{
				out.println(Style.alertMessage("Errore: il parametro interno 'startDate' non è presente"));
			}
		}

		// connessione al database
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		// lista campionati
		List<ChampionshipEntity> lc;
		try {
			lc = dbc.getUndefinedChampionships();
			// se ci sono campionati da definire
			if(lc.size() > 0){
				out.println("<form name=\"computecalendar\" method=\"POST\" >");
				out.println("Campionato: <select name=\"idChampionship\">");
				// stampa i campionati
				for(Iterator<ChampionshipEntity> it = lc.iterator();it.hasNext();){
					ChampionshipEntity c = it.next();
					out.println(Style.option(c.getId().toString(),c.getName()));
				}
				out.println("</select><br>");
				out.println("<input type=\"hidden\" name=\"startDate\" value=\""+
					(Calendar.getInstance().getTimeInMillis()+1000*60*60*24*7)+"\">");
				out.println("<input type=\"submit\" value=\"crea\">");
				out.println("</form>");
			}else{
				out.println(Style.alertMessage("Non ci sono campionati da definire"));
			}
		} catch (SQLException sqle) {
			out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
		}finally{
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
