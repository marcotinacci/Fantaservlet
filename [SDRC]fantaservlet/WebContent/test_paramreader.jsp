<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Param reader</title>
</head>
<body>
<%
Enumeration<String> parNames = request.getParameterNames();
%><table><%
while(parNames.hasMoreElements()){
	String param = parNames.nextElement();
	%><tr><%
		%><td><%= param %></td><%
		%><td><%= request.getParameter(param) %></td><%
	%></tr><%
}
%></table><%
%>
</body>
</html>