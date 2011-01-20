package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import utils.GenericUtilities;
import utils.Pair;
import view.Style;

import dataconnection.MySQLConnection;
import dataimport.IReadFile;
import dataimport.ReadXLS;
import entities.ChampionshipEntity;
import entities.DayEntity;
import entities.GiudgeEntity;
import entities.ReportEntity;
import exceptions.BadFormException;

/**
 * Servlet implementation class ImportReports
 */
@WebServlet("/ImportReports")
public class ImportReports extends HttpServlet {
	private static final String TITLE = "Importa Voti";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportReports() {
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
		
		// controlla se la richiesta è un formato multipart 
		if (ServletFileUpload.isMultipartContent(request))
		{
			try{
				// TODO problemi inizializzazione
				Pair<List<ReportEntity>,List<GiudgeEntity>> lists = null;
				// TODO problemi inizializzazione	
				Integer did = -1;
				ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
				List<FileItem> items;
				items = upload.parseRequest(request);
				for(Iterator<FileItem> it = items.iterator();it.hasNext();) {
				    FileItem item=it.next();
				    if(item.isFormField()) {
				        // leggi l'id del giorno
				        did = Integer.parseInt(item.getString());
				    } else {
				    	// leggi i voti da file
				        InputStream in = item.getInputStream();
				    	IReadFile xls = new ReadXLS();
				    	lists = xls.getReports(in);
				        in.close();
				    }
				}
				// per ogni report
				for(Iterator<ReportEntity> it = lists.getFirst().iterator(); it.hasNext();){
					ReportEntity rep = it.next();
					// aggiorna i dati con il codice della giornata
					rep.setDay(did);
					// se il report è completo inseriscilo nel database
					if(rep.isComplete()){
						try{
							MySQLConnection.insertReport(rep);
							out.println(Style.successMessage("Voto "+rep+" inserito."));
						}catch(SQLException sqle){
							out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
						}
					}else{
						out.println(Style.alertMessage("Voto "+rep+" non inserito."));				
					}
				}
				// per ogni giudge
				for(Iterator<GiudgeEntity> it = lists.getSecond().iterator(); it.hasNext();){
					GiudgeEntity rep = it.next();
					// aggiorna i dati con il codice della giornata
					rep.setDay(did);
					// se il giudge è completo inseriscilo nel database
					if(rep.isComplete()){
						try{
							MySQLConnection.insertGiudge(rep);
							out.println(Style.successMessage("Giudizio "+rep+" inserito."));
						}catch(SQLException sqle){
							out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
						}
					}else{
						out.println(Style.alertMessage("Giudizio "+rep+" non inserito."));				
					}
				}
				
			}catch (FileUploadException e) {
				out.println(Style.alertMessage("Errore FileUpload: "+e.getMessage()));
			}
		}

		try {
			StringBuffer code = new StringBuffer();
			// lista dei campionati
			List<ChampionshipEntity> lc;
			lc = MySQLConnection.getChampionships();
			if(lc.size() == 0){
				throw new BadFormException("Non ci sono campionati");
			}
			// flag prima giornata
			Boolean firstDay = true;
			// flag zero giornate
			Boolean noDays = true;
			// stampa il form di importazione dei voti
			code.append("<form name=\"importreports\" method=\"POST\" " +
				"enctype=\"multipart/form-data\">");
			code.append("Giornata: <select name=\"day\" id=\"day\">");			
			// stampa le giornate aperte divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = MySQLConnection.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					out.println(Style.optionGroup(c.getName()));	
					// stampa le giornate del campionato c
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						if(firstDay){
							// la prima giornata stampata deve essere selezionata
							code.append(Style.option(d.getId().toString(),d.getFormatDate(),true));
							firstDay = false;
						}else{
							code.append(Style.option(d.getId().toString(),d.getFormatDate()));				
						}
						noDays = false;
					}
				}
			}
			if(noDays){
				throw new BadFormException("Non ci sono giornate");
			}
			code.append("</select><br>");
			code.append("File: <input type=\"file\" name=\"file\"><br>");
			code.append("<input type=\"submit\" value=\"Importa\">");
			code.append("</form>");
			out.println(code);
		} catch (SQLException e) {
			out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
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
