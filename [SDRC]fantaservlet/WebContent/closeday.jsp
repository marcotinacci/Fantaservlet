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
<h1>Chiudi giornata</h1>
<%
MySQLConnection dc = new MySQLConnection();
dc.init();
DayEntity day = new DayEntity();
BeanUtilities.populateBean(day,request);
if(day.getId() != null){
	// chiudi data selezionata
	dc.updateCloseDay(day.getId());	
	out.println(Style.successMessage("Data chiusa."));
}
List<ChampionshipEntity> lc = dc.getChampionships();
%>
<form name="closeday_form" action="closeday" method="post">
Giornata: <select name="id">
<%
// stampa le giornate aperte divise per campionato
for(Iterator<ChampionshipEntity> i = lc.iterator();i.hasNext();){
	// fissa un campionato c
	ChampionshipEntity c = i.next();
	List<DayEntity> ld = dc.getOpenDayOfChampionship(c.getId());
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
</select><br>
<input type="submit" value="chiudi">
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>