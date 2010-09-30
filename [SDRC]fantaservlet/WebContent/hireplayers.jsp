<%@page import="java.sql.SQLException"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="view.Style"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="java.util.List"%>
<%@page import="entities.TeamEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.PlayerEntity"%>
<%@page import="entities.GroupHireEntity"%>
<%@page import="utils.BeanUtilities"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : convoca calciatori</title>
</head>
<body>
<h1>Convoca calciatori</h1>
<%
// connessione al database
MySQLConnection dbc = new MySQLConnection();
dbc.init();
// acquisizione dati da request
ChampionshipEntity ce = new ChampionshipEntity();
BeanUtilities.populateBean(ce,request);
// flag di fallimento query nella pagina di stampa giocatori e squadre
Boolean firstPage = true;
// se il campionato è stato selezionato
if(ce.getId() != null){
	try{
	// recupera calciatori non assegnati nel campionato
	List<PlayerEntity> lp = dbc.getAvailablePlayers(ce.getId());	
	// scelta giocatori
	%>
	<form name="hireplayers_form" action="hireplayers" method="POST">
	Squadra: <select name="team">
	<%
		// stampa i tipi di voti
		List<TeamEntity> lt = dbc.getOpenTeamsOfChampionship(ce.getId());
		for(Iterator<TeamEntity> it = lt.iterator();it.hasNext();){
			TeamEntity t = it.next();
			out.println(Style.option(t.getId().toString(),t.getName()));
		}
	%>
	</select>	
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
	// TODO stampare nome e squadra originaria del calciatore per distinguere gli omonimi
	// stampa attaccanti
	Integer count = 0;
	for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
		PlayerEntity p = it.next();
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
	for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
		PlayerEntity p = it.next();
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
	for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
		PlayerEntity p = it.next();
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
	for(Iterator<PlayerEntity> it = lp.iterator();it.hasNext();){
		PlayerEntity p = it.next();
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
	<input type="submit" value="scegli">
	</form>	
	<%
		firstPage = false;
	}catch(SQLException sqle){
		// se c'è un errore segnalalo e stampa la pagina di selezione campionato
		firstPage = true;
		out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
	}	
}
// pagina scelta del campionato
if(firstPage){
	// TODO controllare se esistono campionati
	// acquisizione dati da request
	GroupHireEntity ghe = new GroupHireEntity();
	BeanUtilities.populateBean(ghe,request);	
	// TODO controllare se si seleziona un campionato con solo squadre già completate
	//se la rosa di calciatori è stata ricevuta
	if(ghe.isComplete()){
		// se la rosa è composta dal giusto numero di calciatori
		if(ghe.isCorrect()){
			// inserisci i dati della convocazione
			dbc.insertHireGroup(ghe);
			// TODO gestire eccezione di fallimento SQL
			out.println(Style.successMessage("Convocazione effettuata con successo"));
		}else{
			// dati inseriti scorretti
			// TODO leggere i numeri da file di configurazione
			out.println(Style.alertMessage("I dati inseriti non sono corretti: devono esserci 6 attaccanti,"+ 
				" 8 centrocampisti, 8 difensori e 3 portieri"));
		}
	}
	// prova a stampare i campionati aperti (se esistono)
	try{
%>
<form name="hireplayers_form" action="hireplayers" method="POST">
Campionato: <select name="id">
<%
	// 
	List<ChampionshipEntity> cel = dbc.getChampionships();
	// stampa squadre senza rosa di calciatori
	for(Iterator<ChampionshipEntity> it = cel.iterator();it.hasNext();){
		ChampionshipEntity c = it.next();
		out.println(Style.option(c.getId().toString(),c.getName()));
	}
%>
</select><br>
<input type="submit" value="scegli">
</form>	
<%
	}catch(SQLException sqle){
		// stampa messaggio di errore
		out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
	}
}
dbc.destroy(); 
%>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>