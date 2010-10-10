<%@page import="entities.TeamEntity"%>
<%@page import="entities.MatchEntity"%>
<%@page import="utils.Pair"%>
<%@page import="entities.DayEntity"%>
<%@page import="view.Style"%>
<%@page import="java.util.List"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : visualizza risultati</title>
</head>
<body>
<h1>Visualizza risultati</h1>
<form action="showresults.jsp" method="get">
Scegli il campionato:
<select name="champ">
<%
// connessione al database
MySQLConnection dbc = new MySQLConnection();
dbc.init();
// prendi i campionati a cui partecipa l'utente
// recuperare id utente dalla sessione
List<ChampionshipEntity> lc = dbc.getChampionshipOfUser(1);
for(Iterator<ChampionshipEntity> it = lc.iterator(); it.hasNext(); ){
	ChampionshipEntity c = it.next();
	out.println(Style.option(c.getId().toString(),c.getName()));
}
%>
</select>
<input type="submit" value="Visualizza risultati">
</form>
<%
// se è stato scelto un campionato
if(request.getParameter("champ") != null){
// --- stampa le partite --- 
	Integer cid = Integer.parseInt(request.getParameter("champ"));
	// stampa titolo della sezione
	out.println("<h2>Risultati delle partite</h2>");
	// flag di chiusura campionato
	Boolean isChampClosed = true;
	// stringa di stampa delle giornate
	StringBuffer printDays = new StringBuffer();
	// recupera le giornate del campionato
	List<DayEntity> days = dbc.getDayOfChampionship(cid);
	// stampa l'inizio della tabella
	printDays.append("<table border=\"1\">\n<tr><th>Giornata</th><th>Partita</th><th>Risultato</th></tr>");	
	// per ogni giornata
	for(Iterator<DayEntity> it = days.iterator(); it.hasNext();){
		// giornata
		DayEntity day = it.next();
		// stampa la giornata
		printDays.append("<tr><td rowspan=3>"+day.getFormatDate()+"</td>");
		// recupera i dati delle partite della giornata
		List<Pair<TeamEntity,TeamEntity>> matches = dbc.getMatchesOfDay(day.getId());
		// per ogni partita
		for(Iterator<Pair<TeamEntity,TeamEntity>> it2 = matches.iterator(); it2.hasNext();){
			Pair<TeamEntity,TeamEntity> pair = it2.next();
			// stampa i nomi delle squadre
			printDays.append("<td>"+pair.getFirst().getName()+" - "+pair.getSecond().getName());
			// stampa i gol segnati
			if(day.isEvaluated()){
				printDays.append("</td><td>"+dbc.getGolOfTeamInDay(pair.getFirst().getId(),day.getId())+
					" - "+dbc.getGolOfTeamInDay(pair.getSecond().getId(),day.getId())+"</td></tr>");
			}else{
				printDays.append("</td><td>n.a.</td></tr>");
				isChampClosed = false;
			}
		}
	}
	printDays.append("</table>");
	out.println(printDays.toString());
// --- stampa la classifica ---
	out.println("<h2>Classifica "+ (isChampClosed? "definitiva" : "provvisoria") +"</h2>");
	out.println(Style.showResults(dbc.getChampionshipResults(cid)));
	
}

%>

<a href="usermenu.jsp">Torna al menu</a>
</body>
</html>