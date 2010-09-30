<%@page import="view.Style"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="calendargenerator.CalendarGenerator"%>
<%@page import="entities.CalendarEntity"%>
<%@page import="utils.BeanUtilities"%>
<%@page import="calendargenerator.BadTeamsNumberException"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : compute calendar</title>
</head>
<body>
<h1>Genera calendario</h1>
<%
CalendarEntity cal = new CalendarEntity();
BeanUtilities.populateBean(cal, request);

if(cal.isComplete()){
	// genera calendario
	CalendarGenerator cg = new CalendarGenerator(cal);
	try{
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
			out.println(Style.alertMessage("Le squadre non sono nel range [6,12]: sono presenti "+ 
				+ btne.getNumTeams()+ " squadre."));
		}
	}
}

// connessione al database
MySQLConnection dbc = new MySQLConnection();
dbc.init();
// lista campionati
List<ChampionshipEntity> lc = dbc.getUndefinedChampionships();
// se ci sono campionati da definire
if(lc.size() > 0){
%>
<form name="computecalendar_form" action="computecalendar" method="POST" >
Campionato: <select name="idChampionship">
<%
// stampa i campionati
for(Iterator<ChampionshipEntity> it = lc.iterator();it.hasNext();){
	ChampionshipEntity c = it.next();
	out.println(Style.option(c.getId().toString(),c.getName()));
}
%>
</select><br>
<input type="hidden" name="startDate" value="<%=
	(Calendar.getInstance().getTimeInMillis()+1000*60*60*24*7)%>">
<input type="submit" value="crea">
</form>
<%
}else{
	out.println(Style.alertMessage("Non ci sono campionati da definire."));
}
dbc.destroy();
%>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>