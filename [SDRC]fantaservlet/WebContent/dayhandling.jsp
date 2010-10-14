<%@page import="view.Style"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="java.util.List"%>
<%@page import="entities.ChampionshipEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.DayEntity"%>
<%@page import="utils.BeanUtilities"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : close day</title>
</head>
<body>
<h1>Chiudi la valutazione della giornata</h1>
<%
MySQLConnection dc = new MySQLConnection();
dc.init();
if(request.getParameter("closeday") != null){
	// chiudi data selezionata
	dc.updateCloseDay(Integer.parseInt(request.getParameter("closeday")));	
	out.println(Style.successMessage("Data chiusa"));
}else if(request.getParameter("openday") != null){
	// riapri la data selezionata
	dc.updateOpenDay(Integer.parseInt(request.getParameter("openday")));
	out.println(Style.successMessage("Data riaperta"));	
}else if(request.getParameter("evaluateday") != null){
	// valuta la data selezionata
	dc.updateEvaluateDay(Integer.parseInt(request.getParameter("evaluateday")));	
	out.println(Style.successMessage("Data valutata"));	
}
List<ChampionshipEntity> lc = dc.getChampionships();
// stampa le giornate aperte divise per campionato
for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
	// fissa un campionato c
	ChampionshipEntity c = i.next();
	out.println("<h2>Camptionato: "+c.getName()+"</h2>");
	out.println(Style.modDays(dc.getDayOfChampionship(c.getId())));
}
%>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>