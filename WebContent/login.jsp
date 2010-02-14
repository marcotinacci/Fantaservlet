<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="main.Login"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : login</title>
</head>
<body>
<% 
String nome = request.getParameter("nome");
String password = request.getParameter("password");
if(nome != null && password != null && (!nome.trim().equals("") &&
	!password.trim().equals("")))
{
	Login log = new Login(nome,password);
	if(log.isLogged()){
		if(log.isAdmin()){
			response.sendRedirect("admin.html");
		}else{
			response.sendRedirect("player.html");
		}
	}
}

%>
<h1>Login</h1>
<form name="login_form" action="login.jsp" method="POST" >
User: <input type="text" name="nome"><br>
Password: <input type="password" name="password"><br>
<input type="submit" value="login">
</form>

</body>
</html>