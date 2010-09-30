<%@page import="org.apache.tomcat.util.http.fileupload.FileItem"%>
<%@page import="org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.sql.SQLException"%>
<%@page import="view.Style"%>
<%@page import="com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException"%>
<%@page import="org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.util.Iterator"%>
<%@page import="entities.PlayerEntity"%>
<%@page import="java.util.List"%>
<%@page import="dataconnection.MySQLConnection"%>
<%@page import="dataimport.ReadXLS"%>
<%@page import="dataimport.IReadFile"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fantaservlet : importa calciatori</title>
</head>
<body>
<h1>Importa calciatori</h1>

<%
// controlla se la richiesta è un formato multipart
if(ServletFileUpload.isMultipartContent(request)) {
	// importa i calciatori da file
	IReadFile xls = new ReadXLS();
	MySQLConnection dbc = new MySQLConnection();
	dbc.init();
	ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
	// interpreta l'unico parametro
	FileItem item = upload.parseRequest(request).get(0);
	InputStream in = item.getInputStream();
	List<PlayerEntity> lp = xls.getPlayers(in);
	in.close();
	// inserisci tutti i giocatori nel database
	for(Iterator<PlayerEntity> it = lp.iterator(); it.hasNext();){
		PlayerEntity p = (PlayerEntity)it.next();
		// inserisci calciatore
		try{
			dbc.InsertPlayer(p);
			// stampa calciatore
			out.println(Style.successMessage("Inserito calciatore "+p.toString()));			
		}catch(SQLException sqle){
			// stampa errore SQL
			out.println(Style.alertMessage("Errore SQL: "+sqle.getMessage()));
		}
	}
	dbc.destroy();
}
// TODO importplayers servlet, senza .jsp
%>

<form name="importplayers_form" action="importplayers.jsp" method="POST" enctype="multipart/form-data">
	File: <input type="file" name="file"><br>
	<input type="submit" value="importa">
</form>
<a href="adminmenu.jsp">Torna al menu</a>
</body>
</html>