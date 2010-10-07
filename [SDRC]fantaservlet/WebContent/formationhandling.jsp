<%@page import="utils.GenericUtilities"%>
<%@page import="java.util.ArrayList"%>
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
// TODO fare un secondo form con le sole giornate aperte per praticità
List<ChampionshipEntity> lc = dbc.getDefChampOfUser(1);
// stampa le giornate aperte divise per campionato
for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
	// fissa un campionato c
	ChampionshipEntity c = i.next(); 
	List<DayEntity> ld = dbc.getDayOfChampionship(c.getId());
	// --- List<DayEntity> lod = dbc.getOpenDayOfChampionship(c.getId()); ---
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
	// TODO insformation e modformation hanno parti da fattorizzare
	
	if(todoParam.equalsIgnoreCase("insformation")){
// --- inserimento della formazione ---
		FormationEntity fe = new FormationEntity();
		BeanUtilities.populateBean(fe,request);
		ChampionshipEntity c = dbc.getChampionshipOfTeam(fe.getTeam());
		List<DayEntity> ld = dbc.getOpenDayOfChampionship(c.getId());
		// lista degli id delle giornate aperte
		List<Integer> lid = new ArrayList<Integer>();
		for(Iterator<DayEntity> it = ld.iterator(); it.hasNext();){
			lid.add(((DayEntity)it.next()).getId());
		}
		// se la formazione ricevuta è corretta, non è già presente una formazione
		// e la giornata è ancora aperta a modifiche		
		if(fe.isCorrect() && 
			dbc.getFormation(fe.getTeam(), fe.getDay()).getPlayers().size() == 0 && 
			lid.contains(fe.getDay()))
		{
			// inserisci i dati della convocazione
			dbc.insertFormation(fe);
			// TODO gestire eccezione di fallimento SQL
			out.println(Style.successMessage("Formazione salvata"));
		}else{
			// dati inseriti scorretti
			// TODO leggere i numeri da file di configurazione
			out.println(Style.alertMessage("I dati inseriti non sono corretti: "+ 
				"le formazioni possibili sono 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2"));
		}
	}else if(todoParam.equalsIgnoreCase("modformation")){
// --- modifica della formazione ---
		FormationEntity fe = new FormationEntity();
		BeanUtilities.populateBean(fe,request);
		// se la formazione ricevuta è corretta e la giornata è ancora aperta a modifiche
		// (in questo caso la formazione DEVE essere già presente in quanto da modificare)
		ChampionshipEntity c = dbc.getChampionshipOfTeam(fe.getTeam());
		List<DayEntity> ld = dbc.getOpenDayOfChampionship(c.getId());
		// lista degli id delle giornate aperte
		List<Integer> lid = new ArrayList<Integer>();
		for(Iterator<DayEntity> it = ld.iterator(); it.hasNext();){
			lid.add(((DayEntity)it.next()).getId());
		}
		if(fe.isCorrect() && lid.contains(fe.getDay()))
		{		
			// aggiorna la formazione passando al metodo il nuovo e il vecchio schieramento
			dbc.updateFormation(fe,dbc.getFormation(fe.getTeam(), fe.getDay()));			
			// TODO gestire eccezione di fallimento SQL
			out.println(Style.successMessage("Formazione modificata"));
		}else{
			// dati inseriti scorretti
			// TODO leggere i numeri da file di configurazione
			out.println(Style.alertMessage("I dati inseriti non sono corretti: "+ 
				"le formazioni possibili sono 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2"));
		}		
	}
	// --- stampa lo stato della giornata selezionata ---
	// id della giornata selezionata
	Integer did = Integer.parseInt(request.getParameter("day"));
	// determina se la giornata è aperta alle modifiche
	List<DayEntity> openDays = dbc.getOpenDayOfChampionship(dbc.getChampionshipOfDay(did).getId());
	List<Integer> openDaysId = new ArrayList<Integer>();
	for(Iterator<DayEntity> it = openDays.iterator(); it.hasNext();){
		openDaysId.add(((DayEntity)it.next()).getId());
	}
	// flag true se la giornata è aperta alle modifiche
	Boolean isOpenDay = openDaysId.contains(did);
	// stampa messaggio giornata chiusa
	if(!isOpenDay)
		out.println(Style.infoMessage("La giornata non &egrave; aperta alle modifiche"));
	// TODO id dell'utente da variabili di sessione
	List<TeamEntity> lTeam = dbc.getTeamsOfUserInDay(1,did);
	if(lTeam.size() > 0){
		// per ogni squadra
		for(Iterator<TeamEntity> it = lTeam.iterator(); it.hasNext();){
%> 
<hr> 
<%
			TeamEntity team = it.next();
			// prendi le convocazioni della squadra da database
			List<PlayerEntity> hiredPlayers = dbc.getHiredPlayers(team.getId());
			//se sono stati convocati giocatori per questa squadra
			if(hiredPlayers.size() > 0){
				// recupera da database la formazione della squadra nel giorno
				FormationEntity formation = dbc.getFormation(team.getId(), did);
				// memorizzo la lista di id per evitare di ricalcolarla ogni volta
				List<Integer> formationList = formation.getPlayers();
				if(formationList.size() > 0){
					// se ci sono giocatori la formazione è già presente
					// --- formazione attuale ---
					// recupera i dati dei calciatori nella formazione
					List<PlayerEntity> players = dbc.getPlayersById(formationList);
%>
	<p>La formazione della squadra <b><%=team.getName()%></b> &egrave; la seguente:
	<ul>
		<li><b>Modulo:</b>
			<%=formation.getDef().length %> - 
			<%=formation.getCen().length %> -
			<%=formation.getAtt().length %>
		<li><b>Difensori:</b>
			<%= Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'D'),false) %>
		<li><b>Centrocampisti:</b>
			<%= Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'C'),false) %>		
		<li><b>Attaccanti:</b>
			<%= Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'A'),false) %>		
		<li><b>Portiere:</b>
			<%= Style.showPlayersList(GenericUtilities.getPlayersListByRule(players,'P'),false) %>		
	</ul>
	</p>
<%
				}
				
				if(isOpenDay){
					// --- form inserimento formazione ---
					if(formationList.size() > 0){
						%>
		<p>&Egrave; ancora possibile modificare la formazione: </p>					
						<%
					}else{
						%>
		<p>La formazione della squadra <b><%=team.getName()%></b> deve ancora essere inserita:</p>					
						<%
					}
%>
	<form action="formationhandling.jsp" method="post">
		<table border="1">
		<tr>
			<td>Difensori</td>
			<td>Centrocampisti</td>
			<td>Attaccanti</td>
			<td>Portieri</td>
		</tr>
		<tr>
	<td valign="top">
	<%= Style.selectPlayers(GenericUtilities.getPlayersListByRule(hiredPlayers,'D'),
			"def",formationList) %>
	</td>
	<td valign="top">
	<%= Style.selectPlayers(GenericUtilities.getPlayersListByRule(hiredPlayers,'C'),
			"cen",formationList) %>
	</td>
	<td valign="top">
	<%= Style.selectPlayers(GenericUtilities.getPlayersListByRule(hiredPlayers,'A'),
			"att",formationList) %>
	</td>
	<td valign="top">
	<%= Style.selectPlayers(GenericUtilities.getPlayersListByRule(hiredPlayers,'P'),
			"golkeep",formationList) %>
	</td>
	</tr>
	</table>
	
	<input type="hidden" name="day" value="<%= did %>">
	<input type="hidden" name="team" value="<%= team.getId() %>">	
	<%
	if(formationList.size() > 0){
	%>
		<input type="hidden" name="todo" value="modformation">
		<input type="submit" value="Modifica formazione">
	<%
	}else{
	%>
		<input type="hidden" name="todo" value="insformation">
		<input type="submit" value="Inserisci formazione">	
	<%
	}
	%>
	<input type="reset">	
	</form>
<%
				}else if(formationList.size() == 0){
					out.println(Style.alertMessage("La giornata è chiusa senza che la squadra "+
						team.getName()+" abbia ricevuto una formazione."));
				}
			}else{
				// se non sono stati convocati giocatori
				out.println(Style.alertMessage(
					"Non sono ancora stati convocati giocatori per la squadra "+team.getName()));
			}
		}
	}else{
		out.println(Style.alertMessage("Non hai squadre che giocano in questa giornata di campionato"));
	}
	
}

// chiusura database
dbc.destroy();
%>

<a href="usermenu.jsp">Torna al menu</a>
</body>
</html>