<%@page import="java.sql.SQLException"%>
<%@page import="entities.TeamEntity"%>
<%@page import="utils.BeanUtilities"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="com.sun.corba.se.impl.protocol.giopmsgheaders.Message"%>
<%@page import="java.util.List"%>
<%@page import="entities.UserEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="utils.GenericUtilities"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="view.Style"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : crea squadra</title>
</head>
<body>
<h1>Crea squadra</h1>
<%
// crea connessione al database
MySQLConnection dbc = new MySQLConnection();
dbc.init();
// lista utenti giocanti
List<UserEntity> lpu = dbc.getPlayingUsers();
// lista campionati
List<ChampionshipEntity> lc = dbc.getUndefinedChampionships();

// controlla che esistano almeno un campionato e un utente giocatore
if(lpu == null || lc == null){
%>
<%=(lpu == null)?Style.alertMessage("Non esistono utenti giocanti."):""%>
<%=(lc == null)?Style.alertMessage("Non esistono campionati da definire disponibili."):""%>
<%
}else{
	try{
		// flag nome disponibile
		boolean nameAvailable = true;
		// flag sotto il limite massimo di squadre per campionato
		boolean underMaxLimit = true;
		TeamEntity team = new TeamEntity();
		BeanUtilities.populateBean(team, request);
		if(team.isComplete()){
			nameAvailable = team.isAvailableName();
			// TODO prendere il numero massimo di squadre per campionato da file di configurazione
			underMaxLimit = dbc.getTeamsOfChampionship(team.getChampionship()).size() < 12;
			if(nameAvailable && underMaxLimit){
				dbc.InsertTeam(team);
				// stampa avvenuto inserimento
				out.println(Style.successMessage("Squadra "+team.getName()+" creata."));
			}
			// stampa eventuali errori
			// se nome non disponibile
			if(!nameAvailable){
				out.println(Style.alertMessage("Nome non disponibile."));
			}
			// se il limite massimo è stato raggiunto
			if(!underMaxLimit){
				// TODO prendere il numero massimo di squadre per campionato da file di configurazione
				out.println(Style.alertMessage("Il campionato ha raggiunto il numero massimo di squadre [12]"));
			}
		}
	}catch(SQLException sqle){
		// notifica errore SQL
		out.println(Style.alertMessage("Errore SQL:"+sqle.getMessage()));
	}
}
%>
<form name="addteam_form" action="addteam" method="POST" >
Squadra: <input type="text" name="name"> <br>
Utente: <select name="user">
<%
// stampa gli utenti
for(Iterator<UserEntity> it = lpu.iterator();it.hasNext();){
	UserEntity u = it.next();
	out.println(Style.option(u.getId().toString(),u.getName()));
}
%>
</select><br>
Campionato: <select name="championship">
<%
// stampa i campionati
for(Iterator<ChampionshipEntity> it = lc.iterator();it.hasNext();){
	ChampionshipEntity c = it.next();
	out.println(Style.option(c.getId().toString(),c.getName()));
}
%>
</select><br>
<input type="submit" value="crea">
</form>
<%
//chiudi connessione al database
dbc.destroy();
%>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>