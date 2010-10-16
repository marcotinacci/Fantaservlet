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

import dataconnection.MySQLConnection;
import dataimport.IReadFile;
import dataimport.ReadXLS;
import entities.PlayerEntity;

import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class ImportPlayers
 */
@WebServlet("/ImportPlayers")
public class ImportPlayers extends HttpServlet {
	private static final String TITLE = "Importa Calciatori";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportPlayers() {
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
		if(ServletFileUpload.isMultipartContent(request)) {
			// importa i calciatori da file
			IReadFile xls = new ReadXLS();
			MySQLConnection dbc = new MySQLConnection();
			dbc.init();
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
			// interpreta l'unico parametro
			FileItem item;
			try {
				item = upload.parseRequest(request).get(0);
	
				InputStream in = item.getInputStream();
				List<PlayerEntity> lp = xls.getPlayers(in);
				in.close();
				// inserisci tutti i giocatori nel database
				for(Iterator<PlayerEntity> it = lp.iterator(); it.hasNext();){
					PlayerEntity p = (PlayerEntity)it.next();
					// inserisci calciatore
					try{
						dbc.insertPlayer(p);
						// stampa calciatore
						out.println(Style.successMessage("Inserito calciatore "+p.toString()));			
					}catch(SQLException sqle){
						// stampa errore SQL
						out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
					}
				}
			} catch (FileUploadException e) {
				out.println(Style.alertMessage("Errore FileUpload: "+e.getMessage()));
			}finally{
				dbc.destroy();
			}
		}
		// stampa il form di importazione
		out.println("<form name=\"importplayers\" method=\"POST\" enctype=\"multipart/form-data\">");
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
