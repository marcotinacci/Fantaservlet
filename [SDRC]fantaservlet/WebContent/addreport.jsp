<%@page import="java.sql.SQLException"%>
<%@page import="view.Style"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="entities.VoteEntity"%>
<%@page import="java.util.List"%>
<%@page import="entities.DayEntity"%>
<%@page import="entities.PlayerEntity"%>
<%@page import="entities.ReportEntity"%>
<%@page import="utils.BeanUtilities"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : add report</title>
</head>
<body>
<h1>Assegna voti</h1>
<%
MySQLConnection dc = new MySQLConnection();
dc.init();

ReportEntity report = new ReportEntity();
BeanUtilities.populateBean(report,request);
// se è stato inserita una votazione
if(report.isComplete()){
	try{
		// inserimento report
		dc.InsertReport(report);
		out.println(Style.successMessage("Valutazione inserita"));
	}catch(SQLException sqle){
		// in caso di errore SQL stampa l'alert
		out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
	}
}
List<VoteEntity> lv = dc.getVotes();
List<ChampionshipEntity> lc = dc.getChampionships();
List<PlayerEntity> lp = dc.getPlayers();
%>
<form name="addreport_form" action="addreport" method="post">
Azione: <select name="vote">
<%
// stampa i tipi di voti
for(Iterator<VoteEntity> it = lv.iterator();it.hasNext();){
	VoteEntity v = it.next();	
	out.println(Style.option(v.getId().toString(),v.getAction()));
}
%>
</select><br>
Giornata: <select name="day">
<%
// stampa le giornate divise per campionato
for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
	// fissa un campionato c
	ChampionshipEntity c = i.next();	
	List<DayEntity> ld = dc.getDayOfChampionship(c.getId());
	if(ld.size() > 0){
		// stampa le giornate del campionato c
		out.println(Style.optionGroup(c.getName()));
		for(Iterator<DayEntity> j = ld.iterator();j.hasNext();){
			DayEntity d = j.next();
			out.println(Style.option(d.getId().toString(),d.getFormatDate()));		
		}
	}
}
%>
</select><br>
Calciatore: <select name="player">
<%
// stampa i calciatori
for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
	PlayerEntity p = it.next();	
	out.println(Style.option(p.getId().toString(),p.getName()));
}
%>
</select><br>
<input type="submit" value="crea">
</form>
<a href="adminmenu.jsp">Torna al menu</a>
<%
dc.destroy();
%>
</body>
</html>