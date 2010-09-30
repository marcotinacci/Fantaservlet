<%@page import="view.Style"%>
<%@page import="utils.BeanUtilities"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="entities.UserEntity"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : crea utente</title>
</head>
<body>
<h1>Crea utente</h1>
<% 
// inserimento utente
boolean nameAvailable = true;
boolean confirmPassword = true;
UserEntity user = new UserEntity();
BeanUtilities.populateBean(user, request);

if(user.isComplete()){
	nameAvailable = user.isAvailableName();
	confirmPassword = user.isConfirmed();
	if(confirmPassword){
		if(nameAvailable){
			MySQLConnection dbc = new MySQLConnection();
			dbc.init();		
			dbc.InsertUser(user);
			dbc.destroy();
			// stampa avvenuto inserimento
			out.println(Style.successMessage("Utente "+user.getName()+" creato."));
		}
	}
	// stampa eventuali errori
	String alert = (nameAvailable?"":"Nome non disponibile")+
		(confirmPassword?"":"La password non corrisponde alla conferma");
	if(!alert.equals("")){
		out.println(Style.alertMessage(alert));
	}	
}else{
	// TODO risolvere (input hidden?) riconoscimento invio dati
	//out.println(Style.alertMessage("Inserire tutti i campi"));
}
%>
<form name="adduser_form" action="adduser.jsp" method="POST" >
Nome utente: <input type="text" name="name"/><br/>
Password: <input type="password" name="password"/><br/>
Conferma: <input type="password" name="confirm"/><br/>
Admin <input type="checkbox" name="admin"/><br/>
<input type="submit" value="crea"/>
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>