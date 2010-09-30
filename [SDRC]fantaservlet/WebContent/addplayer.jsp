<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="view.Style"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="utils.BeanUtilities"%>
<%@page import="entities.PlayerEntity"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : crea calciatore</title>
</head>
<body>
<h1>Crea calciatore</h1>
<%
// inserimento calciatore
boolean nameAvailable = true;
PlayerEntity player = new PlayerEntity();
BeanUtilities.populateBean(player, request);
if(player.isComplete()){
	nameAvailable = player.isNameAvailable();
	if(nameAvailable){
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();		
		dbc.InsertPlayer(player);
		dbc.destroy();
		// stampa avvenuto inserimento
		out.println(Style.successMessage("Calciatore "+player.getName()+" creato."));
	}
	// stampa eventuali errori
	String alert = (nameAvailable?"":"Nome non disponibile");
	if(!alert.equals("")){
		out.println(Style.alertMessage(alert));
	}	
}
%>
<form name="addplayer_form" action="addplayer.jsp" method="POST" >
Nome: <input type="text" name="name"><br>
Ruolo: 
<input type="radio" name="rule" value="A"> attaccante
<input type="radio" name="rule" value="C"> centrocampista
<input type="radio" name="rule" value="D"> difensore
<input type="radio" name="rule" value="P" checked="checked"> portiere
<br>
Squadra di provenienza: <input type="text" name="team"> 
<input type="submit" value="crea">
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>