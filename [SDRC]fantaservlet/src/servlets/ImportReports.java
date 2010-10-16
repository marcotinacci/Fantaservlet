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
import view.Style;

import dataconnection.MySQLConnection;
import dataimport.IReadFile;
import dataimport.ReadXLS;
import entities.ChampionshipEntity;
import entities.DayEntity;
import entities.ReportEntity;

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
		
		//connessione al database (viene fatta anche se non si riceve un file per la stampa del form)
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		// controlla se la richiesta è un formato multipart 
		if (ServletFileUpload.isMultipartContent(request))
		{
			try{
				// TODO problemi inizializzazione
				List<ReportEntity> lr = null;
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
				    	lr = xls.getReports(in);
				        in.close();
				    }
				}
				// per ogni report
				for(Iterator<ReportEntity> it = lr.iterator(); it.hasNext();){
					ReportEntity rep = it.next();
					// aggiorna i dati con il codice della giornata
					rep.setDay(did);
					// se il report è completo inseriscilo nel database
					if(rep.isComplete()){
						try{
							dbc.insertReport(rep);
							out.println(Style.successMessage("Voto "+rep+" inserito."));
						}catch(SQLException sqle){
							out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
						}
					}else{
						out.println(Style.alertMessage("Voto "+rep+" non inserito."));				
					}
				}
			}catch (FileUploadException e) {
				out.println(Style.alertMessage("Errore FileUpload: "+e.getMessage()));
			}
		}

		try {
			// stampa il form di importazione dei voti
			out.println("<form name=\"importreports\" method=\"POST\" enctype=\"multipart/form-data\">");
			out.println("Giornata: <select name=\"day\" id=\"day\">");
			
			// lista dei campionati
			List<ChampionshipEntity> lc;
	
			lc = dbc.getChampionships();
			// flag prima giornata
			Boolean firstDay = true;
			// stampa le giornate aperte divise per campionato
			for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
				// fissa un campionato c
				ChampionshipEntity c = i.next();	
				List<DayEntity> ld = dbc.getDayOfChampionship(c.getId());
				if(ld.size() > 0){
					out.println(Style.optionGroup(c.getName()));	
					// stampa le giornate del campionato c
					for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
						DayEntity d = j.next();
						if(firstDay){
							// la prima giornata stampata deve essere selezionata
							out.println(Style.option(d.getId().toString(),d.getFormatDate(),true));
							firstDay = false;
						}else{
							out.println(Style.option(d.getId().toString(),d.getFormatDate()));				
						}
					}
				}
			}
		} catch (SQLException e) {
			out.println(Style.alertMessage("Errore SQL: "+e.getMessage()));
		}finally{
			dbc.destroy();
		}
		out.println("</select><br>");
		out.println("File: <input type=\"file\" name=\"file\"><br>");
		out.println("<input type=\"submit\" value=\"Importa\">");
		out.println("</form>");
		
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
