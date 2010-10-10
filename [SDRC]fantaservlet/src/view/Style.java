package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.Pair;

import entities.PlayerEntity;
import entities.TeamEntity;

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
		return "<a href=\"index.jsp\">Torna alla pagina iniziale</a></body></html>";
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
	static public String selectPlayers(List<PlayerEntity> players, String name, List<Integer> selectedPlayers){		
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
	 * metodo che restituisce il codice html della classifica del campionato
	 * @param list lista di coppie (squadra, punteggio)
	 * @return codice html della classifica
	 */
	static public String showResults(List<Pair<TeamEntity,Double>> list){
		// TODO gestire pareggi
		// TODO posizione squadra
		StringBuffer code = 
			new StringBuffer("<table border=1><tr><th>Squadra</th><th>Punteggio</th></tr>");
		for(Iterator<Pair<TeamEntity,Double>> it = list.iterator(); it.hasNext();){
			Pair<TeamEntity, Double> coppia = it.next();
			code.append("<tr><td>"+coppia.getFirst().getName()+"</td>");
			code.append("<td>"+coppia.getSecond()+"</td></tr>");
		}
		code.append("</table>");
		return code.toString();
	}
}
