<%@page import="java.io.File"%>
<%@page import="com.itextpdf.text.Paragraph"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.itextpdf.text.pdf.PdfWriter"%>
<%@page import="com.itextpdf.text.Document"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : stampa PDF</title>
</head>
<body>
<h1>Stampa PDF</h1>
<form action="print" method="post" target="_blank">
<input type="checkbox"/ value="team">Dati squadre<br/>
<input type="checkbox" value="champ"/>Risultati campionato<br/>
<input type="submit"/>
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>