package servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.Logger;
import utils.GenericUtilities;
import utils.Match;
import utils.Pair;
import view.Style;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dataconnection.MySQLConnection;

import entities.ChampionshipEntity;
import entities.PlayerEntity;
import entities.TeamEntity;

/**
 * Servlet implementation class PrintPDF
 */
@WebServlet("/PrintPDF")
public class PrintPDF extends HttpServlet {
	
    private static final String TITLE = "Stampa PDF";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public PrintPDF() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		Logger logger = GenericUtilities.checkLoggedIn(request, response, false);		
		String todo = request.getParameter("todo");
		if(todo != null && todo.equals("printPDF")){
			// --- stampa PDF ---
			Document doc = new Document();
			File file = new File("filePDF_"+logger.getUser().getId()+".pdf");
			FileOutputStream os = new FileOutputStream(file);
			String str = file.getAbsolutePath();
			Boolean printTeams = request.getParameter("hire") != null;
			Boolean printMatches = request.getParameter("match") != null;
			Boolean printChampResults = request.getParameter("champ") != null;
			// apri la connessione al database
			MySQLConnection dbc = new MySQLConnection();
			dbc.init();	
			try {
				
				
				// campionati a cui l'utente partecipa
				List<ChampionshipEntity> cl = 
					dbc.getChampionshipOfUser(logger.getUser().getId());				
				// apri il documento PDF
				PdfWriter.getInstance(doc,os);				
				doc.open();
				doc.add(new Phrase(new Chunk("Fantaservlet: utente "+logger.getUser().getName(), 
						 FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD))));
				// per ogni campionato
				for(Iterator<ChampionshipEntity> it = cl.iterator(); it.hasNext(); ){
					ChampionshipEntity champ = it.next();
					printChampTitle(champ,doc);
					
					// --- stampa le rose di calciatori ---
					if(printTeams){
						// prendi la lista delle squadre
						List<TeamEntity> teams = dbc.getTeamsOfChampionship(champ.getId());						
						for(Iterator<TeamEntity> it2 = teams.iterator(); it2.hasNext();){
							TeamEntity team = it2.next();
							printHiring(
								GenericUtilities.getPlayersListByRule(dbc.getHiring(team.getId()), 'D'), 
								GenericUtilities.getPlayersListByRule(dbc.getHiring(team.getId()), 'C'),
								GenericUtilities.getPlayersListByRule(dbc.getHiring(team.getId()), 'A'),
								GenericUtilities.getPlayersListByRule(dbc.getHiring(team.getId()), 'P'),
								team, doc);
						}
					}
					
					if(printMatches || printChampResults){
						// prendi la lista degli scontri
						List<Match> lm = GenericUtilities.getListOfMatches(champ.getId());
						if(lm.size() > 0){
							// --- stampa le partite del campionato ---
							if(printMatches){
								printMatches(lm, GenericUtilities.getNumOfTeams(lm), doc);								
							}
							// --- stampa la classifica ---
							if(printChampResults){
								printRanking(GenericUtilities.getRanking(lm), 
										GenericUtilities.isConcluse(lm), doc);								
							}
						}
					}
				}			
			}catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (SQLException sqle) {
				// TODO errore SQL
				
			}finally{
				// chiudi la connessione al database
				dbc.destroy();
				// chiudi documento PDF
				doc.close();				
			}
			// stampa il pdf 
			doDownload(request, response, str, "filepdf");			
		}else{
			// --- stamp pagina e form ---
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(Style.pageHeader(TITLE));
			
			out.println("<form action=\"PrintPDF\" method=\"POST\">");
			out.println("<input type=\"checkbox\"/ name=\"hire\" checked>Dati squadre<br/>");
			out.println("<input type=\"checkbox\" name=\"match\"/ checked>Risultati partite<br/>");
			out.println("<input type=\"checkbox\" name=\"champ\"/ checked>Classifiche dei campionati<br/>");
			out.println(Style.hidden("todo", "printPDF"));
			out.println("<input type=\"submit\"/>");
			out.println("</form>");
			out.println(Style.pageFooter());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	/**
     *  Sends a file to the ServletResponse output stream.  Typically
     *  you want the browser to receive a different name than the
     *  name the file has been saved in your local database, since
     *  your local names need to be unique.
     *
     *  @param req The request
     *  @param resp The response
     *  @param filename The name of the file you want to download.
     *  @param original_filename The name the browser should receive.
     */
    private void doDownload( HttpServletRequest req, HttpServletResponse resp,
                             String filename, String original_filename )
        throws IOException
    {
        File f = new File(filename);
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType( filename );

        //  Set the response and go!
        resp.setContentType( (mimetype != null) ? mimetype : "application/pdf" );
        resp.setContentLength( (int)f.length() );
        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );

        //  Stream to the requester.
        byte[] bbuf = new byte[3000000];
        DataInputStream in = new DataInputStream(new FileInputStream(f));

        while ((in != null) && ((length = in.read(bbuf)) != -1))
        {
            op.write(bbuf,0,length);
        }

        in.close();
        op.flush();
        op.close();
    }
    
	/**
	 * metodo di stampa dei risultati delle partite di un campionato su pdf
	 * @param matches lista delle partite del campionato
	 * @param numTeams numero delle squadre che vi partecipano
	 * @param doc documento pdf su cui stampare
	 * @throws DocumentException sollevata quando si verificano errori di stampa su pdf
	 */
	private void printMatches(List<Match> matches, Integer numTeams, Document doc) 
			throws DocumentException{
		// numero delle partite
		Integer matchPerDay = numTeams / 2;
		// tabella pdf
		PdfPTable table = new PdfPTable(7);
		// cella pdf
		PdfPCell cell;
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {1,2,2,0.5f,0.5f,0.5f,0.5f});
		// stampa i titoli della tabella
		cell = new PdfPCell(new Phrase("Giornata"));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell.setPhrase(new Phrase("Partita"));
		cell.setColspan(2);
		table.addCell(cell);
		cell.setPhrase(new Phrase("Risultato"));		
		table.addCell(cell);
		cell.setPhrase(new Phrase("Punti"));		
		table.addCell(cell);
		
		Integer counter = 0;
		// per ogni partita
		for(Iterator<Match> it = matches.iterator(); it.hasNext(); 
				counter = (counter+1) % matchPerDay){
			Match match = it.next();
			if(counter == 0){
				// inserisci la data della giornata
				cell = new PdfPCell(new Phrase(match.getDay().getFormatDate()));
				cell.setRowspan(matchPerDay);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				table.addCell(cell);
			}
			// stampa i nomi delle squadre
			table.addCell(match.getTeam1().getName());
			table.addCell(match.getTeam2().getName());
			
			// stampa i gol segnati
			if(match.getDay().isEvaluated()){
				table.addCell(GenericUtilities.pointsToGol(match.getPointsTeam1()).toString());
				table.addCell(GenericUtilities.pointsToGol(match.getPointsTeam2()).toString());
				table.addCell(match.getPointsTeam1().toString());
				table.addCell(match.getPointsTeam2().toString());
			}else{
				table.addCell("NA");				
				table.addCell("NA");
				table.addCell("NA");				
				table.addCell("NA");				
			}			
		}

		// inserisci il titolo nel documento
		doc.add(new Phrase(new Chunk("\nRisultati delle partite", 
				 FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))));
		// inserisci la tabella nel documento
		doc.add(table);
	}

	/**
	 * metodo che stampa la classifica su pdf
	 * @param ranks lista di coppie (squadra, punteggio)
	 * @param isConcluse flag vero se la classifica è definitiva
	 * @param doc documento pdf su cui stampare 
	 * @throws DocumentException sollevata quando si verificano errori di stampa su pdf
	 */
	private void printRanking(List<Pair<TeamEntity,Integer>> ranks, Boolean isConcluse, Document doc) 
			throws DocumentException{
		// tabella pdf, 3 colonne
		PdfPTable table = new PdfPTable(3);
		// cella
		PdfPCell cell;
		// aggiungi le celle di intestazione
		table.setHeaderRows(1);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {0.2f,1,0.2f});		
		table.addCell("Posizione");
		table.addCell("Squadra");
		table.addCell("Punteggio");
		
		Integer position = 0;
		Integer oldPoints = Integer.MAX_VALUE;
		Integer newPoints;
		for(Iterator<Pair<TeamEntity,Integer>> it = ranks.iterator(); it.hasNext();){
			Pair<TeamEntity, Integer> coppia = it.next();
			newPoints = coppia.getSecond();
			// se non è un parimerito avanza di posizione
			if(newPoints < oldPoints){
				oldPoints = newPoints;
				position++;
			}
			// stampa la posizione
			table.addCell(position.toString());
			// stampa il nome della squadra
			cell = new PdfPCell(new Phrase(coppia.getFirst().getName()));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			// stampa il punteggio
			table.addCell(newPoints.toString());
		}
		
		// aggiungi il titolo al pdf
		doc.add(new Phrase(new Chunk("\nClassifica "+ (isConcluse ? "definitiva" : "provvisoria"), 
				 FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))));
		// aggiungi la tabella al pdf
		doc.add(table);
	}    
	
	private void printHiring(List<PlayerEntity> def, List<PlayerEntity> cen,
			List<PlayerEntity> att, List<PlayerEntity> golkeep, TeamEntity team, Document doc) 
			throws DocumentException{
		// lista
		com.itextpdf.text.List list;
		// elemento della lista
		ListItem item;
		// calciatore
		PlayerEntity player;
		// stampa nome squadra
		doc.add(new Phrase(new Chunk("\nSquadra "+team.getName(), 
				 FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))));		
		// aggiungi i difensori
		doc.add(new Phrase(new Chunk("\nDifensori", 
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));
		list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);		
		for(Iterator<PlayerEntity> it = def.iterator(); it.hasNext();){
			player = it.next();
			item = new ListItem(player.getName()+" - "+player.getTeam());
			list.add(item);
		}
		doc.add(list);
		// aggiungi i centrocampisti
		doc.add(new Phrase(new Chunk("Centrocampisti", 
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));
		list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);		
		for(Iterator<PlayerEntity> it = cen.iterator(); it.hasNext();){
			player = it.next();
			item = new ListItem(player.getName()+" - "+player.getTeam());
			list.add(item);
		}
		doc.add(list);		
		// aggiungi gli attaccanti
		doc.add(new Phrase(new Chunk("Attaccanti", 
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));
		list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);		
		for(Iterator<PlayerEntity> it = att.iterator(); it.hasNext();){
			player = it.next();
			item = new ListItem(player.getName()+" - "+player.getTeam());
			list.add(item);
		}
		doc.add(list);
		// aggiungi i portieri
		doc.add(new Phrase(new Chunk("Portieri", 
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));
		list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);		
		for(Iterator<PlayerEntity> it = golkeep.iterator(); it.hasNext();){
			player = it.next();
			item = new ListItem(player.getName()+" - "+player.getTeam());
			list.add(item);
		}
		doc.add(list);
	}
	
	/**
	 * metodo che stampa il titolo di un campionato
	 * @param champ campionato
	 * @param doc documento su cui stampare
	 * @throws DocumentException
	 */
	private void printChampTitle(ChampionshipEntity champ, Document doc) throws DocumentException{
		doc.add(new Phrase(new Chunk("\nCampionato "+champ.getName(), 
				FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD))));		
	}
}
