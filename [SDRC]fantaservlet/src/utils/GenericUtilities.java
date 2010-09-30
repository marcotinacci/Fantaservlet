package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.Login;

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
	
	public static void checkAdminLogged
		(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException
	{
		Login log = new Login(req.getSession());
		if(!log.isLogged()){
			resp.sendRedirect("login");	
		}else if(!log.isAdmin()){
			resp.sendRedirect("player.html");	
		}		
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
}