package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataconnection.MySQLConnection;

import entities.DayEntity;
import entities.PlayerEntity;
import entities.TeamEntity;

import login.Logger;

public class GenericUtilities {
	public static boolean hasValue(String str){
		return((str != null) && (!str.equals("")));
	}
	
	public static boolean hasValue(Character c){
		return((c != null) && (!c.equals("")));
	}
	
	public static boolean hasValue(Integer i){
		return(i != null);
	}	
	
	public static boolean hasValue(List<?> l){
		return(l != null);
	}
	
	public static boolean hasValue(Long l) {
		return(l != null);
	}

	public static Boolean hasValue(Double d) {
		return (d != null);
	}	
	
	public static Logger checkLoggedIn(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		Logger log = new Logger(req.getSession());
		if(!log.isLogged()){
			resp.sendRedirect("Login");
		}
		return log;
	}	
	
	public static Logger checkLoggedIn(HttpServletRequest req, HttpServletResponse resp, Boolean isAdmin)
		throws IOException
	{
		Logger log = new Logger(req.getSession());
		if(!log.isLogged()){
			resp.sendRedirect("Login");
		}else if(log.getUser().isAdmin() && !isAdmin){
			resp.sendRedirect("AdminMenu");
		}else if(!log.getUser().isAdmin() && isAdmin){
			resp.sendRedirect("UserMenu");
		}
		return log;
	}
	
	public static String getAbsolutePath(HttpServletRequest req){
		String file = req.getRequestURI();
		if (req.getQueryString() != null) {
		   file += '?' + req.getQueryString();
		}
		URL reconstructedURL = null;
		try{
			reconstructedURL = new URL(req.getScheme(),
                    req.getServerName(),
                    req.getServerPort(),
                    file);			
		}catch(MalformedURLException mue){
			mue.printStackTrace();
		}

		return reconstructedURL.toString();
	}
	
	public static <E> List<E> rotate(List<E> list, int begin, int end){
		E element = list.remove(begin);
		list.add(end, element);
		return null;
	}

	public static boolean hasValue(Integer[] a) {
		return (a != null);
	}
	
	/**
	 * metodo che ritorna una sottolista dei calciatori di un ruolo specificato
	 * @param players lista di calciatori
	 * @param rule ruolo specificato
	 * @return sottolista di players di tutti e soli i calciatori di ruolo rule
	 */
	public static List<PlayerEntity> getPlayersListByRule(List<PlayerEntity> players, Character rule){
		// TODO questo metodo non di presta bene a successive modifiche per la mancanza di Predicate in Java
		List<PlayerEntity> sublist = new ArrayList<PlayerEntity>();
		for(Iterator<PlayerEntity> it = players.iterator(); it.hasNext();){
			PlayerEntity player = it.next();
			if(player.getRule().equals(rule)){
				sublist.add(player);
			}
		}
		return sublist;
	}
	
	/**
	 * metodo che elabora la lista (squadra, punteggio) partendo dagli scontri, il punteggio
	 * è quello ricavato dai gol effettuati dalle squadre nel campionato di fantacalcio
	 * (non i gol dei singoli calciatori reali)
	 * @param matches lista degli scontri
	 * @return lista dei punteggi di classifica (squadra, punteggio)
	 */
	static public List<Pair<TeamEntity,Integer>> getRanking(List<Match> matches){
		// TODO aggiungere al file di configurazione i punti da assegnare
		Integer pointsWin = 3;
		Integer pointsLose = 0;
		Integer pointsDraw = 1;
		// ricava il numero di squadre
		Integer numTeams = getNumOfTeams(matches);
		// lista della classifica
		List<Pair<TeamEntity, Integer>> ranks = new ArrayList<Pair<TeamEntity,Integer>>();
		// aggiungi alla lista tutte le squadre con punteggio a zero
		for(int i = 0; i < numTeams/2; i++){
			ranks.add(new Pair<TeamEntity, Integer>(matches.get(i).getTeam1(), 0));
			ranks.add(new Pair<TeamEntity, Integer>(matches.get(i).getTeam2(), 0));			
		}
		// aggiungi i punteggi a ogni squadra per pareggi e vittorie
		for(Iterator<Match> it = matches.iterator(); it.hasNext(); ){
			Match match = it.next();
			// se la giornata non è valutata non considerare i risultati
			if(!match.getDay().isEvaluated()) continue;
			// converti i punti delle squadre in gol
			Integer golTeam1 = pointsToGol(match.getPointsTeam1());
			Integer golTeam2 = pointsToGol(match.getPointsTeam2());
			if(golTeam1 > golTeam2){
				// vittoria team 1
				addPoints(ranks, match.getTeam1(), pointsWin);
				addPoints(ranks, match.getTeam2(), pointsLose);				
			}else if(golTeam1 < golTeam2){
				// vittoria team 2
				addPoints(ranks, match.getTeam1(), pointsLose);				
				addPoints(ranks, match.getTeam2(), pointsWin);
			}else{
				// pareggio
				addPoints(ranks, match.getTeam1(), pointsDraw);
				addPoints(ranks, match.getTeam2(), pointsDraw);				
			}
		}
		// ordina infine i risultati
		Collections.sort(ranks, 
			new Comparator<Pair<TeamEntity,Integer>>() {
				@Override
				public int compare(Pair<TeamEntity, Integer> o1, 
						Pair<TeamEntity, Integer> o2) {
					// ordina prima rispetto al punteggio
					if(o1.getSecond() < o2.getSecond())
						return 1;
					if(o1.getSecond() > o2.getSecond())
						return -1;
					// in caso di pareggio passa all'ordinamento lessicografico
					return 
						o1.getFirst().getName().compareTo(o2.getFirst().getName());
				}});
		return ranks;
	}	
	/**
	 * metodo che ricava il numero di squadre dalla lista degli scontri
	 * @param matches lista degli scontri
	 * @return numero di squadre
	 */
	static public Integer getNumOfTeams(List<Match> matches){
		Integer dayId = matches.get(0).getDay().getId();
		Integer counter;
		for(counter = 1; counter < matches.size(); counter ++){
			if(matches.get(counter).getDay().getId() != dayId) break;
		}
		return counter * 2;
	}
	
	/**
	 * metodo che aggiorna la lista di punteggi aggiungendo ad un certo team add punti
	 * @param ranks lista dei punteggi
	 * @param team squadra da aggiornare
	 * @param add punti da sommare
	 */
	static public void addPoints(List<Pair<TeamEntity, Integer>> ranks, TeamEntity team, Integer add){
		for(int i = 0; i < ranks.size(); i++){
			if(ranks.get(i).getFirst().getId().equals(team.getId())){
				// quando trovi la squadra, aggiorna il suo punteggio ed esci
				ranks.get(i).setSecond(ranks.get(i).getSecond() + add);
				break;
			}
		}
	}	
	
	/**
	 * metodo che converte il punteggio nei gol corrispondenti
	 * @param points punti
	 * @return numero di gol
	 */
	static public Integer pointsToGol(Double points){
		Integer gol = 0;
		for(points -= 66; points >= 0; points -= 6){
			gol++;
		}
		return gol;
	}
	
	/**
	 * metodo che restituisce tutti i dati di ogni partita, ordinati per giornata
	 * @param cid id del campionato
	 * @return lista degli scontri
	 * @throws SQLException sollevata quando le query al database falliscono
	 */
	static public List<Match> getListOfMatches(Integer cid) throws SQLException{
		// connessione al database		
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();			
		// composizione dei dati
		List<Match> versus = new ArrayList<Match>();
		// recupera le giornate del campionato
		List<DayEntity> days = dbc.getDayOfChampionship(cid);
		// per ogni giornata
		for(Iterator<DayEntity> it = days.iterator(); it.hasNext();){
			// giornata
			DayEntity day = it.next();
			// recupera i dati delle partite della giornata
			List<Pair<TeamEntity,TeamEntity>> matches = dbc.getMatchesOfDay(day.getId());
			// per ogni partita
			for(Iterator<Pair<TeamEntity,TeamEntity>> it2 = matches.iterator(); it2.hasNext();){
				Pair<TeamEntity,TeamEntity> pair = it2.next();
				versus.add(new Match(day, pair.getFirst(), pair.getSecond(), 
					dbc.getPointsOfTeamInDay(pair.getFirst().getId(),day.getId()), 
					dbc.getPointsOfTeamInDay(pair.getSecond().getId(),day.getId())));
			}
		}
		dbc.destroy();
		return versus;		
	}
	
	/**
	 * metodo che torna true se tutte le giornate sono state valutate quindi
	 * il campionato è concluso
	 * @param matches lista di partite del campionato
	 * @return true se il campionato è conluso, false altrimenti
	 */
	static public Boolean isConcluse(List<Match> matches) {
		for(Iterator<Match> it = matches.iterator(); it.hasNext();){
			if(!it.next().getDay().isEvaluated()){
				return false;
			}
		}
		return true;
	}	
}