package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.DayEntity;
import entities.PlayerEntity;

public class Style {
	static public String successMessage(String msg){
		return 	"<p class=\"success\">"+msg+"</p>";
	}
	
	static public String alertMessage(String msg){
		return 	"<p class=\"alert\">"+msg+"</p>";
	}
	
	static public String infoMessage(String msg){
		return 	"<p class=\"info\">"+msg+"</p>";
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
		return pageFooter(true);
	}
	
	/**
	 * metodo di stampa del codice html di chiusura della pagina, il flag indica se inserire o meno
	 * il link al menu principale
	 * @param hasLink flag che indica la presenza del link al menu principale
	 * @return la stringa di chiusura
	 */
	static public String pageFooter(Boolean hasLink){
		if(hasLink)
			return "<a href=\"Login\">Torna al menu</a></body></html>";
		else
			return "</body></html>";
	}	
	
	/**
	 * metodo che ritorna il codice html di una lista che mostra gli elementi passati per parametro
	 * @param list elementi da stampare
	 * @param ordered se true indica che la lista è ordinata, altrimenti è non ordinata
	 * @return codice html della lista
	 */
	static public String showList(List<String> list, Boolean ordered){
		StringBuffer str = new StringBuffer(ordered ? "<ol>" : "<ul>");
		for(Iterator<String> it = list.iterator(); it.hasNext();){
			str.append("\n<li>"+it.next());
		}
		str.append(ordered ? "\n</ol>" : "\n</ul>");
		return str.toString();
	}
	
	/**
	 * metodo che sfrutta showList per ottenere il codice html di una lista che mostra i nomi dei
	 * calciatori passati per parametro
	 * @see view.Style#showList(List<String>,Boolean)
	 * @param players calciatori da stampare
	 * @param ordered se true indica che la lista è ordinata, altrimente è non ordinata
	 * @return codice html della lista di calciatori
	 */
	static public String showPlayersList(List<PlayerEntity> players, Boolean ordered){
		List<String> list = new ArrayList<String>();
		for(Iterator<PlayerEntity> it = players.iterator(); it.hasNext();){
			// stampa il nome del giocatore (e la squadra per evitare gli omonimi)
			PlayerEntity player = it.next();
			list.add(player.getName() + " - " + player.getTeam());
		}
		return showList(list, ordered);
	}
	
	/**
	 * metodo che restituisce il codice html di un input select che elenca i calciatori,
	 * selezionandone quelli specificati
	 * @param players lista dei calciatori nella select
	 * @param selectedPlayers lista dei giocatori selezionati
	 * @return codice html della select dei calciatori
	 */
	static public String selectPlayers(List<PlayerEntity> players, String name, 
			List<Integer> selectedPlayers){		
		StringBuffer code = new StringBuffer("<select name=\""+name+"\" multiple=\"multiple\">");
		for(Iterator<PlayerEntity> it = players.iterator(); it.hasNext();){
			PlayerEntity p = it.next();
			code.append("\n"+Style.option(p.getId().toString(),p.getName()+" - "+p.getTeam(),
				selectedPlayers.contains(p.getId())));
		}
		code.append("\n</select>");
		return code.toString();
	}
	
	/**
	 * metodo che restituisce il codice html di un input select che elenca i calciatori
	 * @see view.Style#selectPlayers(List<PlayerEntity>,String,List<PlayerEntity>)
	 * @param players lista dei calciatori nella select
	 * @return codice html della select dei calciatori
	 */
	static public String selectPlayers(List<PlayerEntity> players, String name){
		// riusa il metodo con la firma estesa passando la lista dei calciatori selezionati vuota
		return selectPlayers(players, name, new ArrayList<Integer>());
	}

	/**
	 * stampa il codice html di un radio button
	 * @param name nome
	 * @param value valore
	 * @param isChecked flag vero se il bottone è selezionato
	 * @return codice html del radio button
	 */
	static public String inputRadio(String name, String value, Boolean isChecked){
		return "<input type=\"radio\" name =\""+name+"\" value=\""+value+"\" "+
			(isChecked?"checked":"")+">";
	}
	
	/**
	 * metodo che restituisce il codice html del form di modifica delle giornate
	 * @param days lista delle giornate di un campionato
	 * @return codice html dei form
	 */
	static public String modDays(List<DayEntity> days){
		StringBuffer code = new StringBuffer();
		if(days.size() > 0){
			code.append("<table><tr><th>Giornata</th><th>Chiudi</th><th>Apri</th><th>Valuta</th></tr>");
			// stampa le giornate del campionato c
			for(Iterator<DayEntity> j = days.iterator(); j.hasNext(); ){
				DayEntity d = j.next();
				code.append("<tr><td>"+d.getFormatDate()+"</td><td>");
				code.append("<form name=\"closeday\" method=\"get\">");
				code.append("<input type=\"hidden\" name=\"closeday\" value=\""+d.getId()+"\">");
				code.append("<input type=\"submit\" value=\"Chiudi\" "+(d.isClose()?"disabled":"")+">");
				code.append("</form>");
				code.append("</td><td>");
				code.append("<form name=\"openday\" method=\"get\">");
				code.append("<input type=\"hidden\" name=\"openday\" value=\""+d.getId()+"\">");
				code.append("<input type=\"submit\" value=\"Apri\" "+
					(d.isEvaluated() || !d.isClose()? "disabled" : "") +">");	
				code.append("</form>");
				code.append("</td><td>");
				code.append("<form name=\"closeday\" method=\"get\">");
				code.append("<input type=\"hidden\" name=\"evaluateday\" value=\""+d.getId()+"\">");
				code.append("<input type=\"submit\" value=\"Valuta\" "+(d.isEvaluated()? "disabled" : "")+">");
				code.append("</form>");
				code.append("</td>");
				code.append("</tr>");
			}
			code.append("</table>");
		}else{
			code.append(Style.infoMessage("Il campionato non &egrave; stato definito"));
		}
		return code.toString();
	} 
	
}
