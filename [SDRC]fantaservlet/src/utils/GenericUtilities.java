package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.PlayerEntity;

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
}