<%@page import="utils.BeanUtilities"%>
<%@page import="entities.PlayerEntity"%>
<%@page import="entities.FormationEntity"%>
<%@page import="entities.TeamEntity"%>
<%@page import="view.Style"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="entities.DayEntity"%>
<%@page import="java.util.List"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : gestione formazioni</title>
</head>
<body>
<h1>Gestione formazioni</h1>
<form action="formationhandling.jsp" method="get">
Giornate aperte alle modifiche:
<select name="day">
<%
// connessione al database
MySQLConnection dbc = new MySQLConnection();
dbc.init();

// --- stampa le giornate ---
// TODO il codice dell'utente dovra' essere reperito dai dati della sessione
List<ChampionshipEntity> lc = dbc.getDefChampOfUser(1);
// stampa le giornate aperte divise per campionato
for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
	// fissa un campionato c
	ChampionshipEntity c = i.next();
	// TODO lasciare anche le giornate chiuse per vedere che formazione è stata usata? 
	List<DayEntity> ld = dbc.getOpenDayOfChampionship(c.getId());
	if(ld.size() > 0){
		// stampa le giornate del campionato c
		out.println(Style.optionGroup(c.getName()));	
		for(Iterator<DayEntity> j = ld.iterator(); j.hasNext(); ){
			DayEntity d = j.next();
			out.println(Style.option(d.getId().toString(),d.getFormatDate()));		
		}
	}
}
%>
</select>
<input type="hidden" name="todo" value="viewday">
<input type="submit" value="Visualizza formazione">
</form>
<%

String todoParam = request.getParameter("todo");
if(todoParam != null){
	if(todoParam.equalsIgnoreCase("insformation")){
		// --- inserimento della formazione ---
		FormationEntity fe = new FormationEntity();
		BeanUtilities.populateBean(fe,request);	
		//se la formazione ricevuta è corretta
		if(fe.isCorrect()){
			// inserisci i dati della convocazione
			dbc.insertFormation(fe);
			// TODO gestire eccezione di fallimento SQL
			out.println(Style.successMessage("Formazione salvata"));
		}else{
			// dati inseriti scorretti
			// TODO leggere i numeri da file di configurazione
			out.println(Style.alertMessage("I dati inseriti non sono corretti: "+ 
				"le formazioni possibili sono: 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2"));
		}
	}else if(todoParam.equalsIgnoreCase("modformation")){
		// TODO --- modifica della formazione ---
	}
	// --- stampa lo stato della giornata selezionata ---
	Integer did = Integer.parseInt(request.getParameter("day"));	
	// TODO id dell'utente da variabili di sessione
	List<TeamEntity> lTeam = dbc.getTeamsOfUserInDay(1,did);
	if(lTeam.size() > 0){
		// per ogni squadra
		for(Iterator<TeamEntity> it = lTeam.iterator(); it.hasNext();){
			TeamEntity team = it.next();
			// recupera da database la formazione della squadra nel giorno
			FormationEntity formation = dbc.getFormation(team.getId(), did);
			if(formation.getPlayers().size() > 0){
				// se ci sono giocatori la formazione è già presente
				// --- form modifica formazione ---
				// TODO stampa formazione attuale
				// TODO stampa form modifica, coi le convocazioni selezionate
%>
	<p>La formazione della squadra <b><%=team.getName()%></b> &egrave; la seguente:</p>
	<br/>
	formazione attuale...
	<form action="formationhandling.jsp" method="get">
		form modifica...
		<input type="hidden" name="todo" value="modformation">
		<input type="submit" value="Modifica formazione">
		<input type="reset">
	</form>
<%
			}else{
				// --- form inserimento formazione ---
				// prendi le convocazioni della squadra da database
				List<PlayerEntity> hiredPlayers = dbc.getHiredPlayers(team.getId());
%>
	<p>La formazione della squadra <b><%=team.getName()%></b> deve ancora essere inserita:</p>
	<br/>
	<form action="formationhandling.jsp" method="get">
		<table border="1">
		<tr>
			<td>Attaccanti</td>
			<td>Centrocampisti</td>
			<td>Difensori</td>
			<td>Portieri</td>
		</tr>
		<tr>
	<td valign="top">
	<select name="att" multiple="multiple">
	<%	
	// TODO segnalare se non ci sono giocatori convocati!
	// TODO fattorizzare le stampe!
	// TODO stampare nome e squadra originaria del calciatore per distinguere gli omonimi
	// stampa attaccanti
	Integer count = 0;
	for(Iterator<PlayerEntity> itPlayers = hiredPlayers.iterator();itPlayers.hasNext();){
		PlayerEntity p = itPlayers.next();
		if(p.isAtt()){
			out.println(Style.option(p.getId().toString(),p.getName()));
			count++;
		}
	}
	%>
	</select>
	</td>
	<td valign="top">
	<select name="cen" multiple="multiple">
	<%
	// stampa centrocampisti
	for(Iterator<PlayerEntity> itPlayers = hiredPlayers.iterator();itPlayers.hasNext();){
		PlayerEntity p = itPlayers.next();
		if(p.isCen()){
			out.println(Style.option(p.getId().toString(),p.getName()));	
			count++;			
		}
	}
	%>
	</select>
	</td>
	<td valign="top">
	<select name="def" multiple="multiple">
	<%	
	// stampa difensori
	for(Iterator<PlayerEntity> itPlayers = hiredPlayers.iterator();itPlayers.hasNext();){
		PlayerEntity p = itPlayers.next();
		if(p.isDef()){
			out.println(Style.option(p.getId().toString(),p.getName()));			
			count++;			
		}
	}
	%>
	</select>
	</td>
	<td valign="top">
	<select name="golkeep" multiple="multiple">
	<%	
	// stampa portieri
	for(Iterator<PlayerEntity> itPlayers = hiredPlayers.iterator();itPlayers.hasNext();){
		PlayerEntity p = itPlayers.next();
		if(p.isGoalKeep()){
			out.println(Style.option(p.getId().toString(),p.getName()));			
			count++;			
		}
	}
	%>
	</select>
	</td>
	</tr>
	</table>				
		<input type="hidden" name="todo" value="insformation">
		<input type="hidden" name="day" value="<%= did %>">
		<input type="hidden" name="team" value="<%= team.getId() %>">
		<input type="submit" value="Inserisci formazione">
		<input type="reset">
	</form>
<%				
			}
		}
	}else{
		
	}
	
}

// chiusura database
dbc.destroy();
%>

<a href="usermenu.jsp">Torna al menu</a>
</body>
</html>