package view;

public class Style {
	static public String successMessage(String msg){
		return 	"<p class=\"success\">"+msg+"</p>";
	}
	
	static public String alertMessage(String msg){
		return 	"<p class=\"alert\">"+msg+"</p>";
	}
	
	static public String option(String val, String msg){
		return "<option value=\""+val+"\">"+msg+"</option>";
	}

	static public String option(String val, String msg, Boolean selected){
		return "<option value=\""+val+"\""+(selected?" SELECTED ":"")+">"+msg+"</option>";
	}	
	
	static public String optionGroup(String label){
		return "<optgroup label=\""+label+"\">\n";
	}		
	
	static public String hidden(String name, String val){
		return "<input type=\"hidden\" name=\""+name+"\" value=\""+val+"\">";
	}
	
	/**
	 * metodo che fattorizza la stampa dell'header comune ad ogni pagina (codice html)
	 * @param title titolo da inserire nell'header
	 * @return la stringa di header
	 */
	static public String pageHeader(String title){
		return 
		"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" " +
		"\"http://www.w3.org/TR/html4/loose.dtd\">\n"+
		"<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
		"<title>Fantaservlet : "+title+"</title>\n</head>\n<body>\n<h1>"+title+"</h1>";
	}
	
	/**
	 * metodo di stampa del codice html di chiusura della pagina, compreso il link alla pagina iniziale
	 * @return la stringa di chiusura
	 */
	static public String pageFooter(){
		return "<a href=\"index.jsp\">Torna alla pagina iniziale</a></body></html>";
	}
}
