<%@page import="java.sql.SQLException"%>
<%@page import="com.sun.org.apache.xpath.internal.operations.Bool"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.tomcat.util.http.fileupload.FileItemFactory"%>
<%@page import="org.apache.tomcat.util.http.fileupload.FileItem"%>
<%@page import="org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.util.ArrayList"%>
<%@page import="view.Style"%>
<%@page import="entities.ReportEntity"%>
<%@page import="dataimport.ReadXLS"%>
<%@page import="dataimport.IReadFile"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="java.util.List"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.DayEntity"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : importa voti</title>
</head>
<body>
<h1>Importa voti</h1>
<%
//connessione al database (viene fatta anche se non si riceve un file per la stampa del form)
MySQLConnection dbc = new MySQLConnection();
dbc.init();
// controlla se la richiesta è un formato multipart 
if (ServletFileUpload.isMultipartContent(request))
{
	// TODO problemi inizializzazione
	List<ReportEntity> lr = null;
	// TODO problemi inizializzazione	
	Integer did = -1;
	ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
	List<FileItem> items=upload.parseRequest(request);
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
				dbc.InsertReport(rep);
				out.println(Style.successMessage("Voto "+rep+" inserito."));
			}catch(SQLException sqle){
				out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
			}
		}else{
			out.println(Style.alertMessage("Voto "+rep+" non inserito."));				
		}

	}
}
%>
<form name="importreports_form" action="importreports" method="POST" 
	enctype="multipart/form-data">
Giornata: <select name="day" id="day">
<%
// lista dei campionati
List<ChampionshipEntity> lc = dbc.getChampionships();
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
%>
</select><br>
File: <input type="file" name="file"><br>
<input type="submit" value="importa">
</form>
<a href="adminmenu.jsp">Torna al menu</a>
<% dbc.destroy(); %>
</body>
</html>