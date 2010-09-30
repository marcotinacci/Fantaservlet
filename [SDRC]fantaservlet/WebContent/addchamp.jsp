<%@page import="java.sql.SQLException"%>
<%@page import="view.Style"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="utils.BeanUtilities"%>
<%@page import="entities.ChampionshipEntity"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : crea campionato</title>
</head>
<body>
<h1>Crea campionato</h1>
<% 
// inserimento campionato
boolean nameAvailable = true;
ChampionshipEntity champ = new ChampionshipEntity();
BeanUtilities.populateBean(champ, request);

if(champ.isComplete()){
	try{
		nameAvailable = champ.isNameAvailable();
		if(nameAvailable){
			MySQLConnection dbc = new MySQLConnection();
			dbc.init();		
			dbc.InsertChampionship(champ);
			dbc.destroy();
			// stampa avvenuto inserimento
			out.println(Style.successMessage("Campionato "+champ.getName()+" creato."));
		}
		// stampa eventuali errori
		String alert = (nameAvailable?"":"Nome non disponibile");
		if(!alert.equals("")){
			out.println(Style.alertMessage(alert));
		}
	}catch(SQLException sqle){
		// stampa messaggio di errore
		out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
	}
}
%>
<form name="addchamp_form" action="addchamp.jsp" method="POST" >
Nome: <input type="text" name="name"/> 
<br/>
<input type="submit" value="crea"/>
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>